package ru.noties.remodel.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public abstract class RequiredServices {

    @NonNull
    public static Collection<Class<? extends RemodelService>> singleton(@NonNull Class<? extends RemodelService> service) {
        //noinspection unchecked
        return (Collection<Class<? extends RemodelService>>) (Collection) Collections.singletonList(service);
    }

    @NonNull
    public static RequiredServices builder() {
        return new Impl();
    }

    @NonNull
    public abstract RequiredServices add(@NonNull Class<? extends RemodelService> service);

    @NonNull
    public abstract RequiredServices addAll(@NonNull Collection<Class<? extends RemodelService>> services);

    @NonNull
    public abstract Collection<Class<? extends RemodelService>> build();


    private static class Impl extends RequiredServices {

        private final List<Class<? extends RemodelService>> set = new ArrayList<>(3);

        @NonNull
        @Override
        public RequiredServices add(@NonNull Class<? extends RemodelService> service) {
            set.add(service);
            return this;
        }

        @NonNull
        @Override
        public RequiredServices addAll(@NonNull Collection<Class<? extends RemodelService>> services) {
            for (Class<? extends RemodelService> service : services) {
                RequiredServices.checkNonNull(service, "Cannot add null service");
                set.add(service);
            }
            return this;
        }

        @NonNull
        @Override
        public Collection<Class<? extends RemodelService>> build() {
            final Collection<Class<? extends RemodelService>> out;
            if (set.size() == 0) {
                out = Collections.emptyList();
            } else {
                out = Collections.unmodifiableList(set);
            }
            return out;
        }
    }

    private static void checkNonNull(@Nullable Object who, @NonNull String message) {
        if (who == null) {
            throw new IllegalStateException(message);
        }
    }
}
