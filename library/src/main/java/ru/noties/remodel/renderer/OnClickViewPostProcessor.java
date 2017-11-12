package ru.noties.remodel.renderer;

import android.support.annotation.NonNull;
import android.view.View;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class OnClickViewPostProcessor<T> implements RendererViewPostProcessor<T> {

    @Override
    public void process(@NonNull View view, @NonNull final T item) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnClickViewPostProcessor.this.onClick(v, item);
            }
        });
    }

    public abstract void onClick(@NonNull View view, @NonNull T item);
}
