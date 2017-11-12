package ru.noties.remodel.sample.render;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import ru.noties.remodel.renderer.Holder;
import ru.noties.remodel.renderer.Renderer;
import ru.noties.remodel.sample.R;
import ru.noties.remodel.sample.item.EmptyItem;
import ru.noties.remodel.service.Services;

public class EmptyItemRenderer extends Renderer<EmptyItem, Holder> {

    @NonNull
    @Override
    public Holder createHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return new Holder(inflater.inflate(R.layout.render_empty, parent, false));
    }

    @Override
    public void render(@NonNull Services services, @NonNull Holder holder, @NonNull EmptyItem item) {
        // no op
    }
}
