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

import java.util.List;
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
                    new FloatOption(
                            "Float Слайдер (0-100)",
                            75.655f,
                            0, 100,
                            () -> ConfigManager.get().floatSliderTest,
                            v -> ConfigManager.get().floatSliderTest = v
                    )
                    ,
                    new DoubleOption(
                            "Double Слайдер (0-100)",
                            25.5,
                            0, 100,
                            () -> ConfigManager.get().doubleSliderTest,
                            v -> ConfigManager.get().doubleSliderTest = v
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
                            List.of(10, 20), // Дефолт
                            () -> ConfigManager.get().integerListTest,
                            v -> ConfigManager.get().integerListTest = v,

                            // 1. Supplier: Какое значение создавать при нажатии "Добавить"?
                            () -> 0,

                            // 2. Factory: Как создавать Option для каждого элемента?
                            // val - текущее значение числа, setter - как сохранить изменение этого числа
                            (val, setter, getter) -> new IntegerOption(
                                    "",  // Имя не нужно внутри списка
                                    val,
                                    0, 100,
                                    getter, // ПЕРЕДАЕМ НОВЫЙ ГЕТТЕР
                                    setter
                            )
                    ),
                    new ListOption<List<Integer>>(
                            "Мой Список Списков Чисел",
                            List.of(List.of(10, 20), List.of(10, 20)), // Дефолт
                            () -> ConfigManager.get().integerListList,
                            v -> ConfigManager.get().integerListList = v,

                            // 1. Supplier: Какое значение создавать при нажатии "Добавить"?
                            () -> List.of(0),

                            // 2. Factory: Как создавать Option для каждого элемента?
                            // val - текущее значение числа, setter - как сохранить изменение этого числа
                            (val,setter, getter) -> new ListOption<Integer>(
                                    "",  // Имя не нужно внутри списка
                                    val,
                                    getter,
                                    setter,
                                    () -> 0,
                                    (vals, setters, getters) -> new IntegerOption(
                                            "",  // Имя не нужно внутри списка
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

                            // Supplier
                            () -> false,

                            // Factory: Используем BooleanOption (предположим, он у тебя есть)
                            (val, setter, getter) -> new BooleanOption("", val, getter, setter)
                    ),
                    new ListOption<Item>(
                            "Список Предметов",
                            List.of(Items.APPLE, Items.DIAMOND), // Дефолт (список Items)

                            // 1. ГЕТТЕР: Превращаем List<String> из конфига в List<Item> для GUI
                            () -> ConfigManager.get().selectedItemsList.stream()
                                    .map(id -> Registries.ITEM.get(Identifier.of(id))) // String -> Item
                                    .collect(Collectors.toList()),

                            // 2. СЕТТЕР: Превращаем List<Item> из GUI обратно в List<String> для конфига
                            (items) -> {
                                ConfigManager.get().selectedItemsList = items.stream()
                                        .map(item -> Registries.ITEM.getId(item).toString()) // Item -> String
                                        .collect(Collectors.toList());
                            },

                            // 3. Supplier: Что добавляем при нажатии кнопки "Добавить"?
                            () -> Items.APPLE,

                            // 4. Factory: Теперь T - это Item, и всё совпадает!
                            (itemVal, itemSetter, itemGetter) -> new ItemOption(
                                    "",
                                    itemVal,
                                    () -> itemVal,
                                    itemSetter
                            )
                    )
            ));
        };
    }
}