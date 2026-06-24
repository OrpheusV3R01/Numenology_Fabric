package numenology.block.custom;

import net.minecraft.util.StringIdentifiable;

public enum CrucibleState implements StringIdentifiable {

    EMPTY("empty"),
    WATER("water"),
    NUMEN("numen");

    private final String name;

    CrucibleState(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}