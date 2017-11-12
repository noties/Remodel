package ru.noties.remodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import ru.noties.remodel.renderer.Renderer;
import ru.noties.remodel.renderer.Holder;
import ru.noties.remodel.renderer.RendererViewPostProcessor;

class AdapterInfoItem {

    Class<?> itemType;
    Renderer<?, ? extends Holder> renderer;
    RendererViewPostProcessor<?> viewPostProcessor;

    AdapterInfoItem(
            @NonNull Class<?> itemType,
            @NonNull Renderer<?, ? extends Holder> renderer,
            @Nullable RendererViewPostProcessor<?> viewPostProcessor
    ) {
        this.itemType = itemType;
        this.renderer = renderer;
        this.viewPostProcessor = viewPostProcessor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdapterInfoItem infoItem = (AdapterInfoItem) o;

        return itemType.equals(infoItem.itemType);

    }

    @Override
    public int hashCode() {
        return itemType.hashCode();
    }
}
