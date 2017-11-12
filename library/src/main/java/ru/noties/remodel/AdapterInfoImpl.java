package ru.noties.remodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.noties.remodel.adapter.AdapterInfo;
import ru.noties.remodel.renderer.Renderer;
import ru.noties.remodel.renderer.RendererViewPostProcessor;

class AdapterInfoImpl<T> extends AdapterInfo<T> {

    interface AdapterSource<T> {

        int itemCount();

        T item(int position);
    }

    private final AdapterSource<T> adapterSource;

    private final int length;

    private final int[] typeHashCode;
    private final Renderer[] renderers;
    private final RendererViewPostProcessor[] viewPostProcessors;

    AdapterInfoImpl(@NonNull AdapterSource<T> adapterSource, @NonNull Collection<AdapterInfoItem> items) {

        this.adapterSource = adapterSource;

        final List<AdapterInfoItem> list = new ArrayList<>(items);

        final int size = list.size();

        final int[] typeHashCode = new int[size];
        final Renderer[] renderers = new Renderer[size];
        final RendererViewPostProcessor[] viewPostProcessors = new RendererViewPostProcessor[size];

        Collections.sort(list, new ItemComparator());

        AdapterInfoItem item;

        for (int i = 0; i < size; i++) {
            item = list.get(i);
            typeHashCode[i] = item.itemType.hashCode();
            renderers[i] = item.renderer;
            viewPostProcessors[i] = item.viewPostProcessor;
        }

        this.length = size;
        this.typeHashCode = typeHashCode;
        this.renderers = renderers;
        this.viewPostProcessors = viewPostProcessors;
    }

    @Override
    public int assignedViewType(@NonNull Class<? extends T> itemType) {
        final int out;
        final int index = Arrays.binarySearch(typeHashCode, itemType.hashCode());
        if (index < 0) {
            out = -1;
        } else {
            out = index;
        }
        return out;
    }

    @Override
    public int assignedViewType(@NonNull T item) {
        //noinspection unchecked
        return assignedViewType((Class<? extends T>) item.getClass());
    }

    @Override
    public int count() {
        return adapterSource.itemCount();
    }

    @Override
    public T item(int position) {
        return adapterSource.item(position);
    }

    @Override
    public int itemViewType(int position) {
        return assignedViewType(adapterSource.item(position));
    }

    @NonNull
    Renderer renderer(@NonNull Class<?> itemType) {

        //noinspection unchecked
        final int itemViewType = assignedViewType((Class<? extends T>) itemType);

        validateItemTypeRange(itemViewType);

        return renderers[itemViewType];
    }

    @NonNull
    Renderer renderer(int itemViewType) {
        validateItemTypeRange(itemViewType);
        return renderers[itemViewType];
    }

    @Nullable
    RendererViewPostProcessor viewPostProcessor(int itemViewType) {
        validateItemTypeRange(itemViewType);
        return viewPostProcessors[itemViewType];
    }

    private void validateItemTypeRange(int itemViewType) throws IllegalStateException {
        if (itemViewType < 0
                || itemViewType >= length) {
            throw new IllegalStateException("Requested itemViewType is not within generated types range. " +
                    "ItemViewType: " + itemViewType + ", total length: " + length);
        }
    }

    private static class ItemComparator implements Comparator<AdapterInfoItem> {

        @Override
        public int compare(AdapterInfoItem o1, AdapterInfoItem o2) {
            return compare(o1.itemType.hashCode(), o2.itemType.hashCode());
        }

        private static int compare(int lhs, int rhs) {
            return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
        }
    }
}
