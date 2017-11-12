package ru.noties.remodel.adapter;

import android.support.annotation.NonNull;

@SuppressWarnings("WeakerAccess")
public abstract class AdapterInfo<T> {

    /**
     * @param itemType type of the item
     * @return associated itemViewType
     */
    public abstract int assignedViewType(@NonNull Class<? extends T> itemType);

    /**
     * @param item to obtain assigned itemViewType
     * @return associated itemViewType
     */
    public abstract int assignedViewType(@NonNull T item);

    /**
     * @return current items count of Adapter
     */
    public abstract int count();

    /**
     * @param position to obtain item
     * @return item at specified position
     */
    public abstract T item(int position);

    /**
     * @param position to inspect
     * @return itemViewType at specified position
     */
    public abstract int itemViewType(int position);
}
