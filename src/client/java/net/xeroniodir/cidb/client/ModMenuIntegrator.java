package net.xeroniodir.cidb.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.xeroniodir.cidb.client.config.ConfigManager;
import net.xeroniodir.cidb.client.config.ConfigScreen;
import net.xeroniodir.cidb.client.config.options.*;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModMenuIntegrator implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigManager.load();

            return new ConfigScreen(parent, List.of(
                    new BooleanOption(
                            "cdib.config.durabilitytwinkling.name",
                            true,
                            () -> ConfigManager.get().durabilityTwinkling,
                            v -> ConfigManager.get().durabilityTwinkling = v,
                            "cdib.config.durabilitytwinkling.description"
                    ),
                    new BooleanOption(
                            "cdib.config.durabilitybarincreaseoncritical.name",
                            true,
                            () -> ConfigManager.get().durabilityBarLengthOnCritical,
                            v -> ConfigManager.get().durabilityBarLengthOnCritical = v,
                            "cdib.config.durabilitybarincreaseoncritical.description"
                    ),
                    new EnumOption<DurabilityBarStyleEnum>(
                            "cdib.config.durabilitybarstyle.name",
                            DurabilityBarStyleEnum.HORIZONTAL,
                            () -> ConfigManager.get().durabilityBarStyle,
                            v -> ConfigManager.get().durabilityBarStyle = v,
                            DurabilityBarStyleEnum.class,
                            "cdib.config.durabilitybarstyle.description"
                    ),
                    new DoubleOption(
                            "cdib.config.durabilitytwinklingspeed.name",
                            1,
                            0.1, 20.0,
                            () -> ConfigManager.get().twinklingSpeed,
                            v -> ConfigManager.get().twinklingSpeed = v,
                            "cdib.config.durabilitytwinklingspeed.description"
                    ),
                    new IntegerOption(
                            "cdib.config.criticalpercent.name",
                            25,
                            0, 100,
                            () -> ConfigManager.get().durabiltiyProcent,
                            v -> ConfigManager.get().durabiltiyProcent = v,
                            "cdib.config.criticalpercent.description"
                    ),
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
                                    ""
                            ),
                            "cdib.config.colorlist.description"
                    ),
                    new ColorOption(
                            "cdib.config.bundlebarcolor.name",
                            0xFF7087FF,
                            false,
                            () -> ConfigManager.get().bundleBarColor,
                            v -> ConfigManager.get().bundleBarColor = v,
                            "cdib.config.bundlebarcolor.description"
                    ),
                    new ColorOption(
                            "cdib.config.twinklingdurabilitycolor.name",
                            0xFFFF9898,
                            false,
                            () -> ConfigManager.get().twinklingDurabilityColor,
                            v -> ConfigManager.get().twinklingDurabilityColor = v,
                            "cdib.config.twinklingdurabilitycolor.description"
                    ),
                    new ColorOption(
                            "cdib.config.fullbundlebarcolor.name",
                            0xFFFF5555,
                            false,
                            () -> ConfigManager.get().fullBundleBarColor,
                            v -> ConfigManager.get().fullBundleBarColor = v,
                            "cdib.config.fullbundlebarcolor.description"
                    ),
                    new MapOption<Item, List<Integer>>(
                            "cidb.config.uniqueitemcolorlist.name",
                            Map.of(Items.DIAMOND_AXE, List.of(0xFF32E8C9,Color.red.getRGB()),
                                    Items.IRON_AXE, List.of(0xFFFCFCFC,Color.red.getRGB())),
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
                                    ""
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
                                            ""
                                    ),
                                    ""
                            ),
                            "cidb.config.uniqueitemcolorlist.desc"
                    )
            ));
        };
    }
}
