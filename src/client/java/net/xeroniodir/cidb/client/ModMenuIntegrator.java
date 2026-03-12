package net.xeroniodir.cidb.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.xeroniodir.cidb.client.config.ConfigManager;
import net.xeroniodir.cidb.client.config.ConfigScreen;
import net.xeroniodir.cidb.client.config.Option;
import net.xeroniodir.cidb.client.config.options.*;
import net.xeroniodir.cidb.client.enums.DurabilityBarStyleEnum;
import net.xeroniodir.cidb.client.enums.DurabilityColorStyleEnum;
import net.xeroniodir.cidb.client.enums.DurabilityTextStyleEnum;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModMenuIntegrator implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigManager.load();
            List<Option<?>> options = new ArrayList<>();
            options.addAll(List.of(
                    new ListOption<Integer>(
                            "cdib.config.colorlist.name",
                            List.of(0xFF00FF00,0xFFFF0000),
                            () -> ConfigManager.get().colorList,
                            v -> ConfigManager.get().colorList = v,
                            Color.green::getRGB,
                            (vals, setters, getters) -> new ColorOption(
                                    "",
                                    vals,
                                    false,
                                    getters,
                                    setters,
                                    "",null
                            ),
                            "cdib.config.colorlist.description",
                            1,
                            ConfigCategory.GENERAL
                    ),
                    new ColorOption(
                            "cdib.config.twinklingdurabilitycolor.name",
                            0xFFFF9898,
                            false,
                            () -> ConfigManager.get().twinklingDurabilityColor,
                            v -> ConfigManager.get().twinklingDurabilityColor = v,
                            "cdib.config.twinklingdurabilitycolor.description",
                            ConfigCategory.GENERAL
                    ),
                    new EnumOption<DurabilityColorStyleEnum>(
                            "cidb.config.durabilitycolorstyle.name",
                            DurabilityColorStyleEnum.VANILLA,
                            () -> ConfigManager.get().durabilityColorStyle,
                            v -> ConfigManager.get().durabilityColorStyle = v,
                            DurabilityColorStyleEnum.class,
                            "cidb.config.durabilitycolorstyle.description",
                            ConfigCategory.GENERAL
                    ),
                    new ColorOption(
                            "cdib.config.bundlebarcolor.name",
                            0xFF7087FF,
                            false,
                            () -> ConfigManager.get().bundleBarColor,
                            v -> ConfigManager.get().bundleBarColor = v,
                            "cdib.config.bundlebarcolor.description",
                            ConfigCategory.GENERAL
                    ),
                    new ColorOption(
                            "cdib.config.fullbundlebarcolor.name",
                            0xFFFF5555,
                            false,
                            () -> ConfigManager.get().fullBundleBarColor,
                            v -> ConfigManager.get().fullBundleBarColor = v,
                            "cdib.config.fullbundlebarcolor.description",
                            ConfigCategory.GENERAL
                    )));

            options.addAll(List.of(new TextOption(
                            Text.translatable("cidb.config.parameters.title"), ConfigCategory.GENERAL
                    ),
                    new BooleanOption(
                            "cdib.config.durabilitytwinkling.name",
                            true,
                            () -> ConfigManager.get().durabilityTwinkling,
                            v -> ConfigManager.get().durabilityTwinkling = v,
                            "cdib.config.durabilitytwinkling.description", ConfigCategory.GENERAL
                    ),
                    new BooleanOption(
                            "cidb.config.ignoremoditems.name",
                            true,
                            () -> ConfigManager.get().ignoreModItems,
                            v -> ConfigManager.get().ignoreModItems = v,
                            "cidb.config.ignoremoditems.description", ConfigCategory.GENERAL
                    ),
                    new BooleanOption(
                            "cdib.config.durabilitybarincreaseoncritical.name",
                            true,
                            () -> ConfigManager.get().durabilityBarLengthOnCritical,
                            v -> ConfigManager.get().durabilityBarLengthOnCritical = v,
                            "cdib.config.durabilitybarincreaseoncritical.description", ConfigCategory.GENERAL
                    ),
                    new EnumOption<DurabilityBarStyleEnum>(
                            "cdib.config.durabilitybarstyle.name",
                            DurabilityBarStyleEnum.HORIZONTAL,
                            () -> ConfigManager.get().durabilityBarStyle,
                            v -> ConfigManager.get().durabilityBarStyle = v,
                            DurabilityBarStyleEnum.class,
                            "cdib.config.durabilitybarstyle.description", ConfigCategory.GENERAL
                    ),
                    new EnumOption<DurabilityTextStyleEnum>(
                            "cidb.config.durabilitytextstyle.name",
                            DurabilityTextStyleEnum.NONE,
                            () -> ConfigManager.get().durabilityTextStyle,
                            v -> ConfigManager.get().durabilityTextStyle = v,
                            DurabilityTextStyleEnum.class,
                            "cidb.config.durabilitytextstyle.description",
                            ConfigCategory.GENERAL
                    ),
                    new DoubleOption(
                            "cdib.config.durabilitytwinklingspeed.name",
                            1,
                            0.1, 20.0,
                            () -> ConfigManager.get().twinklingSpeed,
                            v -> ConfigManager.get().twinklingSpeed = v,
                            "cdib.config.durabilitytwinklingspeed.description", ConfigCategory.GENERAL
                    ),
                    new IntegerOption(
                            "cdib.config.criticalpercent.name",
                            25,
                            0, 100,
                            () -> ConfigManager.get().durabiltiyProcent,
                            v -> ConfigManager.get().durabiltiyProcent = v,
                            "cdib.config.criticalpercent.description", ConfigCategory.GENERAL
                    )));

            options.addAll(List.of(
                    new ButtonOption(
                            "cidb.config.preset1.name",
                            button -> {
                                ConfigManager.loadPreset(1);
                            },
                            ConfigCategory.PRESETS
                    ),
                    new ButtonOption(
                            "cidb.config.preset2.name",
                            button -> {
                                ConfigManager.loadPreset(2);
                            },
                            ConfigCategory.PRESETS
                    )
            ));

            options.addAll(List.of(
                    new MapOption<Item, List<Integer>>(
                            "cidb.config.uniqueitemcolorlist.name",
                            Map.of(Items.DIAMOND_AXE, List.of(0xFF32E8C9,Color.red.getRGB()),
                                    Items.IRON_AXE, List.of(0xFFFCFCFC,Color.green.getRGB(),Color.red.getRGB())),
                            () -> ConfigManager.get().itemCustomDurabilityColor.entrySet().stream()
                                    .collect(Collectors.toMap(
                                            entry -> Registries.ITEM.get(Identifier.tryParse(entry.getKey())),
                                            Map.Entry::getValue,
                                            (oldVal, newVal) -> newVal,
                                            LinkedHashMap::new
                                    )),
                            (guiMap) -> {
                                ConfigManager.get().itemCustomDurabilityColor = guiMap.entrySet().stream()
                                        .collect(Collectors.toMap(
                                                entry -> Registries.ITEM.getId(entry.getKey()).toString(),
                                                Map.Entry::getValue,
                                                (oldVal, newVal) -> newVal,
                                                LinkedHashMap::new
                                        ));
                            },


                            () -> Items.DIAMOND_CHESTPLATE,
                            () -> List.of(0xFF32E8C9,Color.red.getRGB()),

                            (keyVal, keySetter, keyGetter) -> new ItemOption(
                                    "",
                                    keyVal,
                                    keyGetter,
                                    keySetter,
                                    "",
                                    null
                            ),
                            (valVal, valSetter, valGetter) -> new ListOption<Integer>(
                                    "",
                                    valVal,
                                    valGetter,
                                    valSetter,
                                    Color.blue::getRGB,
                                    (vals, setters, getters) -> new ColorOption(
                                            "",
                                            vals,
                                            false,
                                            getters,
                                            setters,
                                            "",
                                            null
                                    ),
                                    "",
                                    1,
                                    null
                            ),
                            "cidb.config.uniqueitemcolorlist.desc",
                            0,
                            ConfigCategory.EXCLUSIVE
                    )/*,
                    new MapOption<Item, DurabilityBarStyleEnum>(
                            "cidb.config.uniqueitemcolorlist.name",
                            Map.of(Items.DIAMOND_AXE, DurabilityBarStyleEnum.VERTICAL,
                                    Items.IRON_AXE, DurabilityBarStyleEnum.HORIZONTAL),
                            () -> ConfigManager.get().itemCustomDurabilityStyle.entrySet().stream()
                                    .map(entry -> Map.entry(
                                            Identifier.tryParse(entry.getKey()), // потенциально null
                                            entry.getValue()
                                    ))
                                    .filter(e -> e.getKey() != null) // убираем битые ключи
                                    .collect(Collectors.toMap(
                                            e -> Registries.ITEM.get(Identifier.tryParse(e.getKey().toString())),
                                            Map.Entry::getValue,
                                            (oldVal, newVal) -> newVal,
                                            LinkedHashMap::new
                                    )),
                            (guiMap) -> {
                                ConfigManager.get().itemCustomDurabilityStyle =
                                        guiMap.entrySet().stream()
                                                .filter(e -> e.getKey() != null) // хотя ключи от GUI почти всегда норм
                                                .collect(Collectors.toMap(
                                                        e -> Registries.ITEM.getId(e.getKey()).toString(),
                                                        Map.Entry::getValue,
                                                        (oldVal, newVal) -> newVal,
                                                        LinkedHashMap::new
                                                ));
                            },


                            () -> Items.DIAMOND_CHESTPLATE,
                            () -> DurabilityBarStyleEnum.VERTICAL,

                            (keyVal, keySetter, keyGetter) -> new ItemOption(
                                    "",
                                    keyVal,
                                    keyGetter,
                                    keySetter,
                                    "",
                                    null
                            ),
                            (valVal, valSetter, valGetter) -> new EnumOption<DurabilityBarStyleEnum>(
                                    "",
                                    valVal,
                                    valGetter,
                                    valSetter,
                                    DurabilityBarStyleEnum.class,
                                    "",
                                    null
                            ),
                            "cidb.config.uniqueitemcolorlist.desc",
                            0,
                            ConfigCategory.EXCLUSIVE
                    )*/)
            );

            return new ConfigScreen(parent, options);
        };
    }
}
