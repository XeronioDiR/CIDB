package net.xeroniodir.cidb.client;

import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.util.Identifier;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

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
    public static enum BarStyles {
        Horizontal,
        Vertical,
        TextPercent,
        TextDurability;

        public Text getDisplayName() {
            return Text.translatable(("cdib.barstyles." + name().toLowerCase()));
        }
    }

    @SerialEntry
    public static BarStyles barStyle = BarStyles.Horizontal;
    @SerialEntry
    public static List<Color> colorList = List.of(new Color(0x00FF00),new Color(0xFF0000));
    @SerialEntry
    public static boolean durabilityTwinkling = true;
    @SerialEntry
    public static boolean durabilityBarLengthOnCritical = true;
    @SerialEntry
    public static int durabilityProcent = 25;
    @SerialEntry
    public static double twinklingSpeed = 1;
    @SerialEntry
    public static Color twinklingDurabilityColor = new Color(0xFF9898);
    @SerialEntry
    public static Color bundlebarcolor = new Color(0x7087FF);
    @SerialEntry
    public static Color fullbundlebarcolor = new Color(0xFF5555);


    public void save() {  }

    public static Screen createGui(Screen parent) {
        ModConfig cfg = HANDLER.instance();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.literal("CIDB"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.literal("Options"))
                        .tooltip(Text.literal("CIDB Options"))
                        .option(ListOption.<Color>createBuilder()
                                .name(Text.translatable("cdib.config.colorlist.name"))
                                .description(OptionDescription.of(Text.translatable("cdib.config.colorlist.description")))
                                .binding(List.of(new Color(0x00FF00),new Color(0xFF0000)), () -> colorList, newVal -> colorList = newVal)
                                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(true))
                                .minimumNumberOfEntries(1)
                                .initial(new Color(0xFFFFFF))
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("cdib.config.twinklingdurabilitycolor.name"))
                                .description(OptionDescription.of(Text.translatable("cdib.config.twinklingdurabilitycolor.description")))
                                .binding(new Color(0xFF9898), () -> twinklingDurabilityColor, newVal -> twinklingDurabilityColor = newVal)
                                .controller(ColorControllerBuilder::create)
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("cdib.config.bundlebarcolor.name"))
                                .description(OptionDescription.of(Text.translatable("cdib.config.bundlebarcolor.description")))
                                .binding(new Color(0x7087FF), () -> bundlebarcolor, newVal -> bundlebarcolor = newVal)
                                .controller(ColorControllerBuilder::create)
                                .build())
                        .option(Option.<Color>createBuilder()
                                .name(Text.translatable("cdib.config.fullbundlebarcolor.name"))
                                .description(OptionDescription.of(Text.translatable("cdib.config.fullbundlebarcolor.description")))
                                .binding(new Color(0xFF5555), () -> fullbundlebarcolor, newVal -> fullbundlebarcolor = newVal)
                                .controller(ColorControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("cdib.config.durabilitytwinkling.name"))
                                .description(OptionDescription.of(Text.translatable("cdib.config.durabilitytwinkling.description")))
                                .binding(true, () -> durabilityTwinkling, newVal -> durabilityTwinkling = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.translatable("cdib.config.durabilitybarincreaseoncritical.name"))
                                .description(OptionDescription.of(Text.translatable("cdib.config.durabilitybarincreaseoncritical.description")))
                                .binding(true, () -> durabilityBarLengthOnCritical, newVal -> durabilityBarLengthOnCritical = newVal)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .option(Option.<Integer>createBuilder()
                                .name(Text.translatable("cdib.config.criticalpercent.name"))
                                .description(OptionDescription.of(Text.translatable("cdib.config.criticalpercent.description")))
                                .binding(25, () -> durabilityProcent, newVal -> durabilityProcent = newVal)
                                .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                        .range(1,100)
                                        .step(1))
                                .build())
                        .option(Option.<Double>createBuilder()
                                .name(Text.translatable("cdib.config.durabilitytwinklingspeed.name"))
                                .description(OptionDescription.of(Text.translatable("cdib.config.durabilitytwinklingspeed.description")))
                                .binding(1.0, () -> twinklingSpeed, newVal -> twinklingSpeed = newVal)
                                .controller(opt -> DoubleSliderControllerBuilder.create(opt)
                                        .range(0.1,20.0)
                                        .step(0.1)
                                )
                                .build())
                        .option(Option.<BarStyles>createBuilder()
                                .name(Text.translatable("cdib.config.durabilitybarstyle.name"))
                                .description(OptionDescription.of(Text.translatable("cdib.config.durabilitybarstyle.description")))
                                .binding(BarStyles.Horizontal, () -> barStyle, newVal -> barStyle = newVal)
                                .controller(opt -> EnumControllerBuilder.create(opt)
                                        .enumClass(BarStyles.class)
                                        .formatValue(BarStyles::getDisplayName))
                                .build())
                        .build())
                .save(HANDLER::save)
                .build()
                .generateScreen(parent);
    }


}
