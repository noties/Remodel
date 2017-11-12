package ru.noties.remodel.result;

import android.support.annotation.NonNull;

import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public interface ResultAction<T> {

    void apply(@NonNull List<T> list);
}
