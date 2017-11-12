package ru.noties.remodel.sample.render;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collection;

import ru.noties.remodel.renderer.Holder;
import ru.noties.remodel.renderer.Renderer;
import ru.noties.remodel.sample.MainService;
import ru.noties.remodel.sample.R;
import ru.noties.remodel.sample.item.ListItem;
import ru.noties.remodel.service.RemodelService;
import ru.noties.remodel.service.RequiredServices;
import ru.noties.remodel.service.Services;

public class ListItemRenderer extends Renderer<ListItem, ListItemRenderer.ListItemHolder> {

    private interface Action<V extends View> {
        void apply(@NonNull V v);
    }

    @NonNull
    @Override
    public ListItemHolder createHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new ListItemHolder(inflater.inflate(R.layout.render_list, parent, false));
    }

    @Override
    public void render(@NonNull final Services services, @NonNull final ListItemHolder holder, @NonNull final ListItem item) {

        whenReady(holder.image, new Action<ImageView>() {
            @Override
            public void apply(@NonNull ImageView imageView) {
                Picasso.with(imageView.getContext())
                        .load(item.uri())
                        .centerCrop()
                        .resize(imageView.getWidth(), imageView.getHeight())
                        .into(imageView);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MainService service = services.require(MainService.class);
                service.displayPhoto(item.uri());
            }
        });
    }

    private <V extends View> void whenReady(@NonNull final V v, @NonNull final Action<V> action) {
        if (v.getWidth() == 0
                || v.getHeight() == 0) {
            v.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    if (v.getWidth() != 0
                            && v.getHeight() != 0) {
                        v.getViewTreeObserver().removeOnPreDrawListener(this);
                        action.apply(v);
                        return true;
                    }
                    return false;
                }
            });
        } else {
            action.apply(v);
        }
    }

    @Nullable
    @Override
    public Collection<Class<? extends RemodelService>> requiredServices() {
        return RequiredServices.singleton(MainService.class);
    }

    static class ListItemHolder extends Holder {

        final ImageView image;

        ListItemHolder(@NonNull View itemView) {
            super(itemView);

            this.image = findView(R.id.image);
        }
    }
}
