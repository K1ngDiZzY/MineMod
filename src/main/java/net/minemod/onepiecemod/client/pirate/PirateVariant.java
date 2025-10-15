package net.minemod.onepiecemod.client.pirate;

import java.util.Arrays;
import java.util.Comparator;

public enum PirateVariant {
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4);

    private final int id;
    private static final PirateVariant[] BY_ID = Arrays.stream(values()).sorted(Comparator.
            comparingInt(PirateVariant::getId)).toArray(PirateVariant[]::new);

    PirateVariant(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static PirateVariant byId(int id) {
        return BY_ID[id % BY_ID.length];
    }
}
