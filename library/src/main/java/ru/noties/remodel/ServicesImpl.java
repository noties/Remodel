package ru.noties.remodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collections;
import java.util.Map;

import ru.noties.remodel.service.RemodelService;
import ru.noties.remodel.service.Services;

class ServicesImpl implements Services {

    @NonNull
    static ServicesImpl create(@Nullable Map<Class<? extends RemodelService>, RemodelService> map) {
        if (map == null) {
            map = Collections.emptyMap();
        }
        return new ServicesImpl(map);
    }

    private final Map<Class<? extends RemodelService>, RemodelService> map;

    private ServicesImpl(@NonNull Map<Class<? extends RemodelService>, RemodelService> map) {
        this.map = map;
    }

    @NonNull
    @Override
    public <S extends RemodelService> S require(@NonNull Class<S> type) {
        final RemodelService service = map.get(type);
        if (service == null) {
            throw new IllegalStateException("Requested service: " + type.getName() + " was not " +
                    "registered with this Remodel instance. Use `Remodel.Builder.addService()` methods");
        }
        //noinspection unchecked
        return (S) service;
    }
}
