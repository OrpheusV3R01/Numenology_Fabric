package numenology.block.custom;

import net.minecraft.util.StringIdentifiable;

public enum ResearchTableState implements StringIdentifiable {

    EMPTY,
    TOOLS,
    TOOLS_PAPER,
    FULL;

    @Override
    public String asString() {
        return this.name().toLowerCase();
    }
}