package ru.noties.remodel.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.util.List;

@SuppressWarnings("WeakerAccess")
public class DataSetChangerNotify<T> implements DataSetChanger<T> {

    @Override
    public void changeDataSet(
            @NonNull DataSetHolder<T> dataSetHolder,
            @NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter,
            @Nullable List<T> currentDataSet,
            @Nullable List<T> newDataSet
    ) {
        dataSetHolder.updateDataSet(newDataSet);
        adapter.notifyDataSetChanged();
    }
}
