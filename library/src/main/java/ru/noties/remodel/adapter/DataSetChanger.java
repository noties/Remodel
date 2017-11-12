package ru.noties.remodel.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

/**
 * @see DataSetChangerNotify
 * @see DataSetChangerDiffUtil
 */
@SuppressWarnings("WeakerAccess")
public interface DataSetChanger<T> {

    interface DataSetHolder<T> {
        void updateDataSet(@Nullable List<T> dataSet);
    }

    void changeDataSet(
            @NonNull DataSetHolder<T> dataSetHolder,
            @NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter,
            @Nullable List<T> currentDataSet,
            @Nullable List<T> newDataSet
    );
}
