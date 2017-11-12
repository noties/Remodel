package ru.noties.remodel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import ru.noties.remodel.adapter.DataSetChanger;
import ru.noties.remodel.renderer.Holder;
import ru.noties.remodel.renderer.RendererViewPostProcessor;
import ru.noties.remodel.service.Services;

class RemodelAdapter<T> extends RecyclerView.Adapter<Holder>
        implements DataSetChanger.DataSetHolder<T>, AdapterInfoImpl.AdapterSource<T> {

    private final AdapterInfoImpl<T> adapterInfo;
    private final DataSetChanger dataSetChanger;
    private final Services services;
    private final LayoutInflater inflater;

    private List<T> items;

    RemodelAdapter(
            @NonNull Context context,
            @NonNull AdapterInfoImpl<T> adapterInfo,
            @NonNull DataSetChanger dataSetChanger,
            @NonNull Services services
    ) {
        this.adapterInfo = adapterInfo;
        this.dataSetChanger = dataSetChanger;
        this.services = services;
        this.inflater = LayoutInflater.from(context);
        setHasStableIds(true);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return adapterInfo.renderer(viewType).createHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(final Holder holder, int position) {

        final Object item = item(position);
        final int viewType = holder.getItemViewType();

        //noinspection unchecked
        adapterInfo.renderer(viewType).render(services, holder, item);

        final RendererViewPostProcessor viewPostProcessor = adapterInfo.viewPostProcessor(viewType);
        if (viewPostProcessor != null) {
            //noinspection unchecked
            viewPostProcessor.process(holder.itemView, item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return adapterInfo.assignedViewType(item(position));
    }

    @Override
    public int getItemCount() {
        return items != null
                ? items.size()
                : 0;
    }

    @Override
    public long getItemId(int position) {
        final Object item = item(position);
        //noinspection unchecked
        return adapterInfo.renderer(item.getClass()).itemId(item);
    }

    void setItems(@Nullable List<T> items) {
        //noinspection unchecked
        dataSetChanger.changeDataSet(this, this, this.items, items);
    }

    @Override
    public int itemCount() {
        return getItemCount();
    }

    @NonNull
    public T item(int position) {
        return items.get(position);
    }

    @Override
    public void updateDataSet(@Nullable List<T> dataSet) {
        this.items = dataSet;
    }
}
