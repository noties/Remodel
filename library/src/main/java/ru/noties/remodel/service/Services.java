package ru.noties.remodel.service;

import android.support.annotation.NonNull;

public interface Services {

    @NonNull
    <S extends RemodelService> S require(@NonNull Class<S> type);
}
