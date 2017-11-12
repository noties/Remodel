package ru.noties.remodel.renderer;

import android.support.annotation.NonNull;
import android.view.View;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class OnLongClickViewPostProcessor<T> implements RendererViewPostProcessor<T> {

    @Override
    public void process(@NonNull View view, @NonNull final T item) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return OnLongClickViewPostProcessor.this.onLongClick(v, item);
            }
        });
    }

    public abstract boolean onLongClick(@NonNull View view, @NonNull T item);
}
