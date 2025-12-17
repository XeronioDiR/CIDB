package net.xeroniodir.cidb.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.xeroniodir.cidb.client.config.ConfigManager;
import net.xeroniodir.cidb.client.config.ConfigScreen;
import net.xeroniodir.cidb.client.config.options.*;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModMenuIntegrator implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigManager.load();

            return new ConfigScreen(parent, List.of(
                    new BooleanOption(
                            "Тестовый Булеан",
                            true,
                            () -> ConfigManager.get().booleanTest,
                            v -> ConfigManager.get().booleanTest = v
                    ),
                    new IntegerOption(
                            "Слайдер (0-100)",
                            50,
                            0, 100,
                            () -> ConfigManager.get().intSliderTest,
                            v -> ConfigManager.get().intSliderTest = v
                    ),
                    new ItemOption(
                            "Выбранный предмет",
                            Items.OAK_LOG,
                            () -> {
                                Identifier id = Identifier.tryParse(ConfigManager.get().selectedItemId);
                                return Registries.ITEM.get(id);
                            },
                            (Item item) -> {
                                Identifier id = Registries.ITEM.getId(item);

                                ConfigManager.get().selectedItemId = id.toString();
                            }
                    ),
                    new ListOption<Integer>(
                            "Мой Список Чисел",
                            List.of(10, 20),
                            () -> ConfigManager.get().integerListTest,
                            v -> ConfigManager.get().integerListTest = v,

                            () -> 0,

                            (val, setter, getter) -> new IntegerOption(
                                    "",
                                    val,
                                    0, 100,
                                    getter,
                                    setter
                            )
                    ),
                    new ListOption<List<Integer>>(
                            "Мой Список Списков Чисел",
                            List.of(List.of(10, 20), List.of(10, 20)),
                            () -> ConfigManager.get().integerListList,
                            v -> ConfigManager.get().integerListList = v,

                            () -> List.of(0),

                            (val,setter, getter) -> new ListOption<Integer>(
                                    "",
                                    val,
                                    getter,
                                    setter,
                                    () -> 0,
                                    (vals, setters, getters) -> new IntegerOption(
                                            "",
                                            vals,
                                            0, 100,
                                            getters,
                                            setters
                                    )
                            )
                    ),
                    new ListOption<Boolean>(
                            "Список Булевых",
                            List.of(true, false),
                            () -> ConfigManager.get().booleanListTest,
                            v -> ConfigManager.get().booleanListTest = v,

                            () -> false,

                            (val, setter, getter) -> new BooleanOption("", val, getter, setter)
                    ),
                    new ColorOption(
                            "Тестовый цвет",
                            0xFFFFFFFF,
                            true,
                            () -> ConfigManager.get().colorTest,
                            v -> ConfigManager.get().colorTest = v
                    ),
                    new MapOption<Item, List<Integer>>(
                            "Цвета полоски прочности определённых предметов",
                            Map.of(Items.DIAMOND_AXE, List.of(Color.blue.getRGB(),Color.red.getRGB()),
                                    Items.IRON_AXE, List.of(Color.white.getRGB(),Color.red.getRGB())),
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
                            () -> List.of(Color.blue.getRGB(),Color.red.getRGB()),

                            (keyVal, keySetter, keyGetter) -> new ItemOption(
                                    "",
                                    keyVal,
                                    keyGetter,
                                    keySetter
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
                                            setters
                                    )
                            )
                    )
            ));
        };
    }
}