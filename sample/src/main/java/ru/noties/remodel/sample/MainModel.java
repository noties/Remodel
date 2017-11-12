package ru.noties.remodel.sample;

import android.net.Uri;
import android.support.annotation.Nullable;

import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
abstract class MainModel {

    @Nullable
    abstract List<Uri> pickedPhotos();

    @Nullable
    abstract Uri photo();
}
