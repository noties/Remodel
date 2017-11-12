package ru.noties.remodel.renderer;

import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;

@SuppressWarnings({"WeakerAccess", "unused"})
public abstract class OnTouchViewPostProcessor<T> implements RendererViewPostProcessor<T> {

    @Override
    public void process(@NonNull View view, @NonNull final T item) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return OnTouchViewPostProcessor.this.onTouch(v, event, item);
            }
        });
    }

    public abstract boolean onTouch(@NonNull View view, @NonNull MotionEvent event, T item);
}
