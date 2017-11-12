package ru.noties.remodel.result;

@SuppressWarnings("WeakerAccess")
public interface ResultSubscription {

    void unsubscribe();

    boolean hasSubscribers();
}
