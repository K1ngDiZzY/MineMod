package net.minemod.onepiecemod.client.navy;

import java.util.Arrays;
import java.util.Comparator;

public enum NavyVariant {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3);

    private final int id;
    private static final NavyVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator
            .comparingInt(NavyVariant::getId)).toArray(NavyVariant[]::new);

    NavyVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static NavyVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
