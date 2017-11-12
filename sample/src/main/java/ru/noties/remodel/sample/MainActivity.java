package ru.noties.remodel.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.noties.remodel.Remodel;
import ru.noties.remodel.adapter.AdapterInfo;
import ru.noties.remodel.sample.item.EmptyItem;
import ru.noties.remodel.sample.item.Item;
import ru.noties.remodel.sample.item.ListItem;
import ru.noties.remodel.sample.item.PhotoItem;
import ru.noties.remodel.sample.render.EmptyItemRenderer;
import ru.noties.remodel.sample.render.ListItemRenderer;
import ru.noties.remodel.sample.render.PhotoItemRenderer;
import ru.noties.remodel.sample.utils.MediaPicker;

public class MainActivity extends Activity implements MainService {

    private static final int PICK_REQUEST_CODE = 65;

    private final MediaPicker mediaPicker = MediaPicker.create();

    private Remodel<MainModel, Item> remodel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final View fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhotos();
            }
        });

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        initRecyclerSpacing(recyclerView);

        final MainModel model = ImmutableMainModel.builder().build();

        remodel = Remodel.builder(model, Item.class)
                .addRenderer(ListItem.class, new ListItemRenderer())
                .addRenderer(EmptyItem.class, new EmptyItemRenderer())
                .addRenderer(PhotoItem.class, new PhotoItemRenderer())
                .addService(MainService.class, this)
                .recyclerView(recyclerView)
                .reducer(new MainReducer())
                .build();

        recyclerView.setLayoutManager(createLayoutManager(this, remodel.adapterInfo()));
    }

    private void pickPhotos() {
        startActivityForResult(mediaPicker.createPickIntent(), PICK_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (PICK_REQUEST_CODE == requestCode) {
            if (RESULT_OK == resultCode) {
                processPickedPhotos(data);
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void processPickedPhotos(Intent data) {

        final List<Uri> picked = mediaPicker.extractPickedUris(data);
        final int size = picked != null
                ? picked.size()
                : 0;

        if (size > 0) {

            final MainModel model = remodel.getModel();

            final List<Uri> current = model.pickedPhotos();
            final int currentSize = current != null
                    ? current.size()
                    : 0;

            final List<Uri> newPhotos;
            if (currentSize == 0) {
                newPhotos = picked;
            } else {
                newPhotos = new ArrayList<>(size + currentSize);
                newPhotos.addAll(current);
                newPhotos.addAll(picked);
            }

            final MainModel out = ImmutableMainModel.copyOf(model)
                    .withPickedPhotos(newPhotos);

            remodel.setModel(out);
        }
    }

    private static void initRecyclerSpacing(@NonNull RecyclerView recyclerView) {
        final int spacing = recyclerView.getResources().getDimensionPixelSize(R.dimen.recycler_view_spacing);
        recyclerView.setPadding(spacing, spacing, spacing, spacing);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(spacing, spacing, spacing, spacing);
            }
        });
        recyclerView.setClipToPadding(false);
    }

    private static GridLayoutManager createLayoutManager(
            @NonNull Context context,
            @NonNull final AdapterInfo<Item> adapterInfo
    ) {

        // next: initialize gridLayoutManager
        // for the sake of brevity we will use hardcode 3 value for the span count
        final int spanCount = 3;

        final GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            final int listItemViewType = adapterInfo.assignedViewType(ListItem.class);

            @Override
            public int getSpanSize(int position) {
                return listItemViewType == adapterInfo.itemViewType(position)
                        ? 1
                        : spanCount;
            }
        });

        return layoutManager;
    }

    @Override
    public void displayPhoto(@NonNull Uri uri) {
        final MainModel out = ImmutableMainModel.copyOf(remodel.getModel())
                .withPhoto(uri);
        remodel.setModel(out);
    }

    @Override
    public void onBackPressed() {

        final MainModel model = remodel.getModel();
        if (model.photo() != null) {
            final MainModel out = ImmutableMainModel.copyOf(model)
                    .withPhoto(null);
            remodel.setModel(out);
            return;
        }

        super.onBackPressed();
    }
}
