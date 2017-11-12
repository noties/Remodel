package ru.noties.remodel;

import android.support.annotation.NonNull;

import ru.noties.remodel.result.ResultList;

public interface Reducer<M, T> {

    /**
     * @param model updated model
     * @return a {@link ResultList} reduced from supplied model
     * @see ru.noties.remodel.result.SyncResultList
     * @see ru.noties.remodel.result.AsyncResultList
     */
    @NonNull
    ResultList<T> reduce(@NonNull M model);
}
