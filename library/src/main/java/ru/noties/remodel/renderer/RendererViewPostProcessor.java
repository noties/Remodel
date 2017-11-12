package ru.noties.remodel.renderer;

import android.support.annotation.NonNull;
import android.view.View;

/**
 * @see OnClickViewPostProcessor
 * @see OnLongClickViewPostProcessor
 * @see OnTouchViewPostProcessor
 */
public interface RendererViewPostProcessor<T> {

    /**
     * Process supplied view after it was prepared by {@link Renderer}.
     *
     * @param view to post-process
     * @param item associated item with this view
     */
    void process(@NonNull View view, @NonNull T item);
}
