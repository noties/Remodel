package ru.noties.remodel.sample.item;

import android.net.Uri;
import android.support.annotation.NonNull;

public class ListItem implements Item {

    private final Uri uri;

    public ListItem(@NonNull Uri uri) {
        this.uri = uri;
    }

    public Uri uri() {
        return uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ListItem listItem = (ListItem) o;

        return uri.equals(listItem.uri);

    }

    @Override
    public int hashCode() {
        return uri.hashCode();
    }
}
