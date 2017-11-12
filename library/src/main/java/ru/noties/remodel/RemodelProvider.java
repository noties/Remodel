package ru.noties.remodel;

import android.support.annotation.NonNull;

import ru.noties.remodel.adapter.AdapterInfo;

@SuppressWarnings({"WeakerAccess", "unused"})
public class RemodelProvider<M, T> {

    @NonNull
    public static <M, T> RemodelProvider<M, T> create() {
        return new RemodelProvider<>();
    }

    @NonNull
    public static <M, T> RemodelProvider<M, T> create(@NonNull Class<M> modelType, @NonNull Class<T> itemType) {
        return new RemodelProvider<>();
    }

    private final Delegate<M, T> delegate = new Delegate<>();

    private RemodelProvider() {
    }

    @NonNull
    public Remodel<M, T> get() throws IllegalStateException {
        return delegate;
    }


    void set(@NonNull Remodel<M, T> remodel) {
        this.delegate.set(remodel);
    }

    private static class Delegate<M, T> extends Remodel<M, T> {

        private Remodel<M, T> parent;

        @Override
        public void setModel(@NonNull M model) {
            validateInitialized();
            parent.setModel(model);
        }

        @NonNull
        @Override
        public M getModel() {
            validateInitialized();
            return parent.getModel();
        }

        @NonNull
        @Override
        public AdapterInfo<T> adapterInfo() {
            validateInitialized();
            return parent.adapterInfo();
        }

        @Override
        public void dispose() {
            validateInitialized();
            parent.dispose();
        }

        @Override
        public Subscription observe(@NonNull Action<M> action) {
            validateInitialized();
            return parent.observe(action);
        }

        void set(@NonNull Remodel<M, T> remodel) {
            this.parent = remodel;
        }

        private void validateInitialized() throws IllegalStateException {
            if (parent == null) {
                throw new IllegalStateException("RemodelProvider was not initialized properly. " +
                        "It cannot be used until Builder successfully returns from the `build` method.");
            }
        }
    }
}
