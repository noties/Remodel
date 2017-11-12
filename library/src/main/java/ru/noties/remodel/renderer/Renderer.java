package ru.noties.remodel.renderer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.Collection;

import ru.noties.remodel.service.RemodelService;
import ru.noties.remodel.service.Services;

@SuppressWarnings({"unused", "WeakerAccess"})
public abstract class Renderer<T, H extends Holder> {

    /**
     * @param inflater LayoutInflater to inflate
     * @param parent   ViewGroup to which created view will be attached
     * @return a holder
     */
    @NonNull
    public abstract H createHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    /**
     * @param services {@link Services} that holds registered {@link RemodelService}s
     * @param holder   to render to
     * @param item     item to render
     */
    public abstract void render(@NonNull Services services, @NonNull H holder, @NonNull T item);

    /**
     * Default implementation uses hashCode of supplied item. Override this method if you would require
     * different handling
     *
     * @param item to obtain id from
     * @return id of supplied item
     * @see android.support.v7.widget.RecyclerView#NO_ID
     */
    public long itemId(@NonNull T item) {
        return item.hashCode();
    }

    /**
     * @return a collection of services that this {@link Renderer} require
     * @see ru.noties.remodel.service.RequiredServices
     * @see ru.noties.remodel.service.RequiredServices#singleton(Class)
     * @see ru.noties.remodel.service.RequiredServices#builder()
     */
    @Nullable
    public Collection<Class<? extends RemodelService>> requiredServices() {
        return null;
    }

    /**
     * Helper method to obtain Context.
     *
     * @param holder from which to obtain Context
     * @return associated Context
     */
    @NonNull
    protected Context context(@NonNull Holder holder) {
        return holder.itemView.getContext();
    }
}
