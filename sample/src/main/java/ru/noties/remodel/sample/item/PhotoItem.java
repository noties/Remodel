package ru.noties.remodel.sample.item;

import android.net.Uri;
import android.support.annotation.NonNull;

public class PhotoItem implements Item {

    private final Uri uri;

    public PhotoItem(@NonNull Uri uri) {
        this.uri = uri;
    }

    public Uri uri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhotoItem photoItem = (PhotoItem) o;

        return uri.equals(photoItem.uri);

    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
