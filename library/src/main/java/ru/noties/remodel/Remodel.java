package ru.noties.remodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.noties.remodel.adapter.AdapterInfo;
import ru.noties.remodel.adapter.DataSetChanger;
import ru.noties.remodel.adapter.DataSetChangerNotify;
import ru.noties.remodel.renderer.Holder;
import ru.noties.remodel.renderer.Renderer;
import ru.noties.remodel.renderer.RendererViewPostProcessor;
import ru.noties.remodel.result.ResultAction;
import ru.noties.remodel.service.RemodelService;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class Remodel<M, T> {

    /**
     * Factory method to obtain {@link Builder} instance
     *
     * @param model model instance
     * @param type  base type for the items (that will be created by {@link Reducer}
     * @return {@link Builder} instance
     */
    @NonNull
    public static <M, T> Builder<M, T> builder(@NonNull M model, @NonNull Class<T> type) {
        return new Builder<>(model, type);
    }

    public interface Subscription {
        void unsubscribe();
    }

    public interface Action<M> {
        void apply(@NonNull M model);
    }

    /**
     * Triggers model update. Please note, that if supplied model is the same as the previous one
     * (checked by calling `equals`) update won\'t be triggered
     *
     * @param model model to set
     */
    public abstract void setModel(@NonNull M model);

    /**
     * @return current model associated with this Remodel instance
     */
    @NonNull
    public abstract M getModel();

    /**
     * @return {@link AdapterInfo}
     */
    @NonNull
    public abstract AdapterInfo<T> adapterInfo();

    /**
     * Disposes current instance. Might be helpful if {@link Reducer} produces async {@link ru.noties.remodel.result.ResultList}
     * ({@link ru.noties.remodel.result.AsyncResultList})
     */
    public abstract void dispose();

    /**
     * Additional generic listener for model change (will be called only after reducer creates
     * a list of items)
     *
     * @param action {@link Action} to be notified when model changes
     * @return {@link Subscription}
     */
    public abstract Subscription observe(@NonNull Action<M> action);

    public static class Builder<M, T> {

        private final M model;

        private final Set<AdapterInfoItem> items = new HashSet<>(3);

        private Reducer<M, T> reducer;

        private RecyclerView recyclerView;

        private DataSetChanger<T> dataSetChanger;

        private RemodelProvider<M, T> provider;

        private Set<Class<? extends RemodelService>> requiredServices;

        private Map<Class<? extends RemodelService>, RemodelService> registeredServices;

        Builder(@NonNull M model, @NonNull Class<T> type) {
            this.model = model;
        }

        @NonNull
        public Builder<M, T> provider(@NonNull RemodelProvider<M, T> provider) {
            this.provider = provider;
            return this;
        }

        @NonNull
        public Builder<M, T> reducer(@NonNull Reducer<M, T> reducer) {
            this.reducer = reducer;
            return this;
        }

        @NonNull
        public <I extends T, H extends Holder> Builder<M, T> addRenderer(
                @NonNull Class<I> itemType,
                @NonNull Renderer<I, H> itemRenderer
        ) {
            return addRenderer(itemType, itemRenderer, null);
        }

        @NonNull
        public <I extends T, H extends Holder> Builder<M, T> addRenderer(
                @NonNull Class<I> itemType,
                @NonNull Renderer<I, H> itemRenderer,
                @Nullable RendererViewPostProcessor<I> viewPostProcessor
        ) {

            items.add(new AdapterInfoItem(itemType, itemRenderer, viewPostProcessor));

            // check if renderer requested services and add them to a collection for later validation
            final Collection<Class<? extends RemodelService>> services = itemRenderer.requiredServices();
            if (!CollectionUtils.isEmpty(services)) {
                if (requiredServices == null) {
                    requiredServices = new HashSet<>(3);
                }
                requiredServices.addAll(services);
            }

            return this;
        }

        @NonNull
        public <S extends RemodelService> Builder<M, T> addService(@NonNull S service) {
            //noinspection unchecked
            return addService((Class<S>) service.getClass(), service);
        }

        @NonNull
        public <S extends RemodelService, I extends S> Builder<M, T> addService(@NonNull Class<S> type, I service) {
            if (registeredServices == null) {
                registeredServices = new HashMap<>(3);
            }
            registeredServices.put(type, service);
            return this;
        }

        @NonNull
        public Builder<M, T> recyclerView(@NonNull RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        @NonNull
        public Builder<M, T> dataSetChanger(@NonNull DataSetChanger<T> dataSetChanger) {
            this.dataSetChanger = dataSetChanger;
            return this;
        }

        @NonNull
        public Remodel<M, T> build() throws IllegalStateException {

            validate();

            final AdapterSourceDelegate<T> delegate = new AdapterSourceDelegate<>();
            final AdapterInfoImpl<T> adapterInfo = new AdapterInfoImpl<>(delegate, items);

            if (dataSetChanger == null) {
                dataSetChanger = new DataSetChangerNotify<>();
            }

            final RemodelAdapter<T> adapter = new RemodelAdapter<>(
                    recyclerView.getContext(),
                    adapterInfo,
                    dataSetChanger,
                    ServicesImpl.create(registeredServices)
            );
            delegate.parent = adapter;

            final ResultAction<T> action = new ResultAction<T>() {
                @Override
                public void apply(@NonNull List<T> list) {
                    adapter.setItems(list);
                }
            };

            recyclerView.setAdapter(adapter);

            final Remodel<M, T> remodel = new RemodelImpl<>(reducer, action, adapterInfo);

            // if we have provider initialize it
            // strictly speaking we do not need this provider if we have remodel as a field
            // but it can be useful if we will need remodel instance in a service
            if (provider != null) {
                provider.set(remodel);
            }

            // please do not forget that we need items for initial state
            remodel.setModel(model);

            return remodel;
        }

        private void validate() throws IllegalStateException {

            // required: at least one renderer & reducer & recyclerView

            if (reducer == null) {
                throw new IllegalStateException("Reducer argument is required");
            }

            if (items.size() == 0) {
                throw new IllegalStateException("At least one Renderer is required");
            }

            if (recyclerView == null) {
                throw new IllegalStateException("RecyclerView argument is required");
            }

            // now, if we have required services -> we must validate that all of them are registered
            // via `addService` method, if not -> it's an error and we want to fail as soon as possible
            if (!CollectionUtils.isEmpty(requiredServices)) {

                final Collection<Class> mismatches;

                if (CollectionUtils.isEmpty(registeredServices)) {
                    //noinspection unchecked
                    mismatches = (Collection<Class>) (Set) requiredServices;
                } else {
                    mismatches = new ArrayList<>();
                    for (Class<? extends RemodelService> service : requiredServices) {
                        if (registeredServices.get(service) == null) {
                            mismatches.add(service);
                        }
                    }
                }

                if (!CollectionUtils.isEmpty(mismatches)) {

                    final String mismatchTypes;
                    {
                        final StringBuilder builder = new StringBuilder("[");
                        boolean isFirst = true;
                        for (Class<?> type : mismatches) {
                            if (isFirst) {
                                isFirst = false;
                            } else {
                                builder.append(", ");
                            }
                            builder.append(type.getName());
                        }
                        builder.append(']');
                        mismatchTypes = builder.toString();
                    }

                    throw new IllegalStateException("These services were requested by renderers, " +
                            "but were not supplied: " + mismatchTypes);
                }
            }
        }
    }

    private static class AdapterSourceDelegate<T> implements AdapterInfoImpl.AdapterSource<T> {

        AdapterInfoImpl.AdapterSource<T> parent;

        @Override
        public int itemCount() {
            validate();
            return parent.itemCount();
        }

        @Override
        public T item(int position) {
            validate();
            return parent.item(position);
        }

        private void validate() {
            if (parent == null) {
                throw new IllegalStateException();
            }
        }
    }
}
