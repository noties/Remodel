package ru.noties.remodel;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Map;

abstract class CollectionUtils {

    static boolean isEmpty(@Nullable Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    static boolean isEmpty(@Nullable Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    private CollectionUtils() {
    }
}
