package net.xeroniodir.cidb.client;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.List;

public class ModConfig {

    public static ConfigClassHandler<ModConfig> HANDLER = ConfigClassHandler.createBuilder(ModConfig.class)
            .id(Identifier.of("cidb", "cidb"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("cidb.json5"))
                    .appendGsonBuilder(GsonBuilder::setPrettyPrinting) // not needed, pretty print by default
                    .setJson5(true)
                    .build())
            .build();
    @SerialEntry
    public static List<Color> colorList = List.of(new Color(0x00FF00),new Color(0x00FF00));
    @SerialEntry
    public static boolean durabilityTwinkling = true;
    @SerialEntry
    public static boolean durabilityBarLengthOnCritical = true;
    @SerialEntry
    public static int durabilityProcent = 25;
    @SerialEntry
    public static double twinklingSpeed = 2;
    @SerialEntry
    public static Color basicDurabilityColor = new Color(0x00FF00);
    @SerialEntry
    public static Color twinklingDurabilityColor = new Color(0xFF9898);

    public void save() {  }

    public static Screen createGui(Screen parent) {
        ModConfig cfg = HANDLER.instance();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("CIDB"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Options"))
                        .tooltip(Text.literal("CIDB Options"))
                        .option(ListOption.<Color>createBuilder()
                                .name(Text.literal("Durability Bar Colors"))
                                .binding(List.of(new Color(0x00FF00),new Color(0xFF0000)), () -> colorList, newVal -> colorList = newVal)
                                .controller(ColorControllerBuilder::create)
                                .minimumNumberOfEntries(1)
                                .initial(new Color(0xFFFFFF))
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.literal("Item Durability"))
                                .description(OptionDescription.of(Text.literal("Item Durability Options")))
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Basic Durability Color"))
                                        .description(OptionDescription.of(Text.literal("Standard color of item durability")))
                                        .binding(new Color(0x00FF00), () -> basicDurabilityColor, newVal -> basicDurabilityColor = newVal)
                                        .controller(ColorControllerBuilder::create)
                                        .build())
                                .option(Option.<Color>createBuilder()
                                        .name(Text.literal("Twinkling Durability Color"))
                                        .description(OptionDescription.of(Text.literal("Twinkling color of item durability")))
                                        .binding(new Color(0xFF9898), () -> twinklingDurabilityColor, newVal -> twinklingDurabilityColor = newVal)
                                        .controller(ColorControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Durability Twinkling"))
                                        .description(OptionDescription.of(Text.literal("If enabled, durability will twinkle on critical amount")))
                                        .binding(true, () -> durabilityTwinkling, newVal -> durabilityTwinkling = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.literal("Durability Bar Increase on Critical"))
                                        .description(OptionDescription.of(Text.literal("If enabled, durability bar increases if durability is critical")))
                                        .binding(true, () -> durabilityBarLengthOnCritical, newVal -> durabilityBarLengthOnCritical = newVal)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.literal("Critical Durability Percent"))
                                        .description(OptionDescription.of(Text.literal("If item durability is lower then n%, it will be considered critical amount.")))
                                        .binding(25, () -> durabilityProcent, newVal -> durabilityProcent = newVal)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1,100)
                                                .step(1))
                                        .build())
                                .option(Option.<Double>createBuilder()
                                        .name(Text.literal("Durability Twinkling Speed"))
                                        .description(OptionDescription.of(Text.literal("Speed of durability twinkling")))
                                        .binding(1.0, () -> twinklingSpeed, newVal -> twinklingSpeed = newVal)
                                        .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                                .range(0.1,20.0)
                                                .step(0.1)
                                        )
                                        .build())
                                .build())
                        .build())
                .save(HANDLER::save)
                .build()
                .generateScreen(parent);
    }


}
