package ru.noties.remodel.sample.render;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import ru.noties.remodel.renderer.Holder;
import ru.noties.remodel.renderer.Renderer;
import ru.noties.remodel.sample.R;
import ru.noties.remodel.sample.item.PhotoItem;
import ru.noties.remodel.service.Services;

public class PhotoItemRenderer extends Renderer<PhotoItem, PhotoItemRenderer.PhotoItemHolder> {

    @NonNull
    @Override
    public PhotoItemHolder createHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new PhotoItemHolder(inflater.inflate(R.layout.render_photo, parent, false));
    }

    @Override
    public void render(@NonNull Services services, @NonNull PhotoItemHolder holder, @NonNull PhotoItem item) {
        Picasso.with(context(holder))
                .load(item.uri())
                .into(holder.image);
    }

    static class PhotoItemHolder extends Holder {

        final ImageView image;

        PhotoItemHolder(@NonNull View itemView) {
            super(itemView);

            this.image = findView(R.id.image);
        }
    }
}
