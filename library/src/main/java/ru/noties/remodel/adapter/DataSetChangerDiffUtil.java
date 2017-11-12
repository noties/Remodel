package ru.noties.remodel.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

@SuppressWarnings({"WeakerAccess", "unused"})
public class DataSetChangerDiffUtil<T> implements DataSetChanger<T> {

    private final boolean detectMoves;

    public DataSetChangerDiffUtil() {
        this(true);
    }

    public DataSetChangerDiffUtil(boolean detectMoves) {
        this.detectMoves = detectMoves;
    }

    @Override
    public void changeDataSet(
            @NonNull DataSetHolder<T> dataSetHolder,
            @NonNull RecyclerView.Adapter<? extends RecyclerView.ViewHolder> adapter,
            @Nullable List<T> currentDataSet, @Nullable List<T> newDataSet
    ) {

        final DiffUtil.DiffResult result = DiffUtil.calculateDiff(
                CallbackImpl.create(currentDataSet, newDataSet),
                detectMoves
        );

        dataSetHolder.updateDataSet(newDataSet);
        result.dispatchUpdatesTo(adapter);
    }

    private static class CallbackImpl<T> extends DiffUtil.Callback {

        static <T> CallbackImpl<T> create(@Nullable List<T> oldList, @Nullable List<T> newList) {
            if (oldList == null) {
                oldList = Collections.emptyList();
            }
            if (newList == null) {
                newList = Collections.emptyList();
            }
            return new CallbackImpl<>(oldList, newList);
        }

        private final List<T> oldList;
        private final List<T> newList;

        private CallbackImpl(@NonNull List<T> oldList, @NonNull List<T> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).equals(newItemPosition);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // as we rely on equals/hashCode to identify items, there is no need for additional
            // `equals` call here. This method is called only if `areItemsTheSame` returned `true
            return true;
        }
    }
}
