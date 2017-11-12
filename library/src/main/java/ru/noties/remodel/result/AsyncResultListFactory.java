package ru.noties.remodel.result;

import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class AsyncResultListFactory {

    private final ExecutorService executorService;
    private final Handler deliverHandler;

    public AsyncResultListFactory(@NonNull ExecutorService executorService, @NonNull Handler deliverHandler) {
        this.executorService = executorService;
        this.deliverHandler = deliverHandler;
    }

    public <T> AsyncResultList<T> create(@NonNull Callable<List<T>> callable) {
        return new AsyncResultList<>(executorService, deliverHandler, callable);
    }
}
