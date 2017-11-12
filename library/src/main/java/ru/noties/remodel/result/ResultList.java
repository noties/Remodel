package ru.noties.remodel.result;

import android.support.annotation.NonNull;

public interface ResultList<T> {

    @NonNull
    ResultSubscription subscribe(@NonNull ResultAction<T> action);
}
