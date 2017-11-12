package ru.noties.remodel.sample.utils;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MediaPicker {

    @NonNull
    public static MediaPicker create() {
        return new Impl();
    }

    @NonNull
    public abstract Intent createPickIntent();

    @Nullable
    public abstract List<Uri> extractPickedUris(@Nullable Intent data);


    private static class Impl extends MediaPicker {

        private static final boolean IS_ADULT = Build.VERSION.SDK_INT >= 18;

        @NonNull
        @Override
        public Intent createPickIntent() {
            final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            if (IS_ADULT) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            return intent;
        }

        @Nullable
        @Override
        public List<Uri> extractPickedUris(@Nullable Intent data) {

            final List<Uri> list;

            if (data == null) {

                list = null;

            } else {

                final ClipData clipData = data.getClipData();

                final int count = clipData != null
                        ? clipData.getItemCount()
                        : 0;

                if (count > 0) {
                    list = new ArrayList<>(count);
                    ClipData.Item item;
                    for (int i = 0; i < count; i++) {
                        item = clipData.getItemAt(i);
                        if (item.getUri() != null) {
                            list.add(item.getUri());
                        }
                    }
                } else {
                    final Uri single = data.getData();
                    if (single != null) {
                        list = Collections.singletonList(single);
                    } else {
                        list = null;
                    }
                }
            }

            return list;
        }
    }
}
