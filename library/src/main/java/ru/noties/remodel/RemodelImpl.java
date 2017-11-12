package ru.noties.remodel;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.noties.remodel.adapter.AdapterInfo;
import ru.noties.remodel.result.ResultAction;
import ru.noties.remodel.result.ResultSubscription;

class RemodelImpl<M, T> extends Remodel<M, T> {

    private M model;

    private final Reducer<M, T> reducer;

    private final ResultAction<T> resultAction;

    private final AdapterInfo<T> adapterInfo;

    private final List<Action<M>> observers = new ArrayList<>(3);

    private ResultSubscription subscription;

    RemodelImpl(
            @NonNull Reducer<M, T> reducer,
            @NonNull ResultAction<T> resultAction,
            @NonNull AdapterInfo<T> adapterInfo
    ) {
        this.reducer = reducer;
        this.resultAction = new ResultActionImpl(resultAction);
        this.adapterInfo = adapterInfo;
    }

    @Override
    public void setModel(@NonNull M model) {

        if (this.model != null
                && this.model.equals(model)) {
            // do nothing ignore this call
            return;
        }

        // cancel pending subscription if any
        if (subscription != null
                && subscription.hasSubscribers()) {
            subscription.unsubscribe();
        }

        this.model = model;
        this.subscription = reducer.reduce(model).subscribe(resultAction);
    }

    @NonNull
    @Override
    public M getModel() {
        return model;
    }

    @NonNull
    @Override
    public AdapterInfo<T> adapterInfo() {
        return adapterInfo;
    }

    @Override
    public void dispose() {
        // check if there is a subscription
        if (subscription != null
                && subscription.hasSubscribers()) {
            subscription.unsubscribe();
        }
        observers.clear();
    }

    @Override
    public Subscription observe(@NonNull Action<M> action) {
        return new SubscriptionImpl(action);
    }

    private class ResultActionImpl implements ResultAction<T> {

        private final ResultAction<T> parent;

        private ResultActionImpl(@NonNull ResultAction<T> parent) {
            this.parent = parent;
        }

        @Override
        public void apply(@NonNull List<T> list) {

            parent.apply(list);

            for (Action<M> action : observers) {
                action.apply(model);
            }
        }
    }

    private class SubscriptionImpl implements Subscription {

        private Action<M> action;

        private SubscriptionImpl(@NonNull Action<M> action) {
            this.action = action;
            observers.add(action);
        }

        @Override
        public void unsubscribe() {
            if (action != null) {
                observers.remove(action);
                action = null;
            }
        }
    }
}
