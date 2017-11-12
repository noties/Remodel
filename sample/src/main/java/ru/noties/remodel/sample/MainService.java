package ru.noties.remodel.sample;

import android.net.Uri;
import android.support.annotation.NonNull;

import ru.noties.remodel.service.RemodelService;

public interface MainService extends RemodelService {

    void displayPhoto(@NonNull Uri uri);

}
