package ru.noties.remodel.sample;

import android.net.Uri;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.noties.remodel.Reducer;
import ru.noties.remodel.result.ResultList;
import ru.noties.remodel.result.SyncResultList;
import ru.noties.remodel.sample.item.EmptyItem;
import ru.noties.remodel.sample.item.Item;
import ru.noties.remodel.sample.item.ListItem;
import ru.noties.remodel.sample.item.PhotoItem;

class MainReducer implements Reducer<MainModel, Item> {

    @NonNull
    @Override
    public ResultList<Item> reduce(@NonNull MainModel model) {

        final List<Item> list;

        final Uri uri = model.photo();
        if (uri != null) {
            list = Collections.singletonList((Item) new PhotoItem(uri));
        } else {
            final List<Uri> pickedPhotos = model.pickedPhotos();
            final int length = pickedPhotos != null
                    ? pickedPhotos.size()
                    : 0;
            if (length > 0) {
                list = new ArrayList<>(length);
                for (int i = 0; i < length; i++) {
                    list.add(new ListItem(pickedPhotos.get(i)));
                }
            } else {
                list = Collections.singletonList((Item) EmptyItem.create());
            }
        }

        return SyncResultList.create(list);
    }
}
