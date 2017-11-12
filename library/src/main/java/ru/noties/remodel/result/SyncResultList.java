package ru.noties.remodel.result;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.List;

public class SyncResultList<T> implements ResultList<T> {

    public static <T> SyncResultList<T> create(@Nullable List<T> list) {
        final List<T> result;
        if (list == null) {
            result = Collections.emptyList();
        } else {
            result = list;
        }
        return new SyncResultList<>(result);
    }

    private final List<T> result;

    private SyncResultList(@NonNull List<T> result) {
        this.result = result;
    }

    @NonNull
    @Override
    public ResultSubscription subscribe(@NonNull ResultAction<T> action) {
        action.apply(result);
        return NO_OP;
    }

    private static final ResultSubscription NO_OP = new ResultSubscription() {
        @Override
        public void unsubscribe() {

        }

        @Override
        public boolean hasSubscribers() {
            return false;
        }
    };
}
