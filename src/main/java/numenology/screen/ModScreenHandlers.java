package numenology.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import numenology.Numenology;

public class ModScreenHandlers {

    public static ScreenHandlerType<ResearchTableScreenHandler> RESEARCH_TABLE;
    public static ScreenHandlerType<NumenSmelteryScreenHandler> NUMEN_SMELTERY;
    public static ScreenHandlerType<DuffelBagScreenHandler> DUFFEL_BAG_SCREEN_HANDLER;
    public static ScreenHandlerType<DryingTableScreenHandler> DRYING_TABLE;

    public static void register() {
        RESEARCH_TABLE = ScreenHandlerRegistry.registerSimple(
                new Identifier(Numenology.MOD_ID, "research_table"),
                ResearchTableScreenHandler::new
        );

        NUMEN_SMELTERY = ScreenHandlerRegistry.registerSimple(
                new Identifier(Numenology.MOD_ID, "numen_smeltery"),
                NumenSmelteryScreenHandler::new
        );

        DRYING_TABLE = ScreenHandlerRegistry.registerSimple(
                new Identifier(Numenology.MOD_ID, "drying_table"),
                DryingTableScreenHandler::new
        );

        DUFFEL_BAG_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(
                new Identifier(Numenology.MOD_ID, "duffel_bag"),
                DuffelBagScreenHandler::new
        );
    }
}