package ru.noties.remodel.renderer;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

@SuppressWarnings("WeakerAccess")
public class Holder extends RecyclerView.ViewHolder {

    public Holder(@NonNull View itemView) {
        super(itemView);
    }

    /**
     * Helper method to automatically cast view to desired type. (Obsolete when targeting SDK >= 26)
     *
     * @param id id of a view
     * @return found view
     */
    public <V extends View> V findView(@IdRes int id) {
        //noinspection unchecked
        return (V) itemView.findViewById(id);
    }
}
