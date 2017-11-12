package ru.noties.remodel.result;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @see AsyncResultListFactory
 */
@SuppressWarnings("WeakerAccess")
public class AsyncResultList<T> implements ResultList<T> {

    private final ExecutorService service;
    private final Handler deliverHandler;
    private final Callable<List<T>> callable;

    public AsyncResultList(
            @NonNull ExecutorService service,
            @NonNull Handler deliverHandler,
            @NonNull Callable<List<T>> callable
    ) {
        this.service = service;
        this.deliverHandler = deliverHandler;
        this.callable = callable;
    }

    @NonNull
    @Override
    public ResultSubscription subscribe(@NonNull ResultAction<T> action) {
        return new SubscriptionImpl(service.submit(new ResultCallable<>(action, callable, deliverHandler)));
    }

    private static class ResultCallable<T> implements Callable<Object> {

        private static final Object RESULT = new Object();

        private final ResultAction<T> action;
        private final Callable<List<T>> callable;
        private final Handler deliverHandler;

        private ResultCallable(
                @NonNull ResultAction<T> action,
                @NonNull Callable<List<T>> callable,
                @NonNull Handler deliverHandler
        ) {
            this.action = action;
            this.callable = callable;
            this.deliverHandler = deliverHandler;
        }

        @Override
        public Object call() throws Exception {
            final List<T> list = callable.call();
            deliverHandler.post(new Runnable() {
                @Override
                public void run() {
                    action.apply(list);
                }
            });
            return RESULT;
        }
    }

    private static class SubscriptionImpl implements ResultSubscription {

        private final Future<?> future;

        SubscriptionImpl(@NonNull Future<?> future) {
            this.future = future;
        }

        @Override
        public void unsubscribe() {
            if (!future.isDone()) {
                future.cancel(true);
            }
        }

        @Override
        public boolean hasSubscribers() {
            return !future.isDone();
        }
    }
}
