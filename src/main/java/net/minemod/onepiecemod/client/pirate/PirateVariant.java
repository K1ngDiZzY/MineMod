package net.minemod.onepiecemod.client.pirate;

import java.util.Arrays;
import java.util.Comparator;

public enum PirateVariant {
    DEFAULT(0),
    FRANKY(1),
    SANJI(2),
    LUFFY(3),
    ZORO(4);

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
