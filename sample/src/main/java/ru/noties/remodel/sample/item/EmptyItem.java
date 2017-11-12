package ru.noties.remodel.sample.item;

public class EmptyItem implements Item {

    public static EmptyItem create() {
        return INSTANCE;
    }

    private static final EmptyItem INSTANCE = new EmptyItem();

    private EmptyItem() {
    }
}
