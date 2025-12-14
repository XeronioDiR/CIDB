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
                    )
            ));
        };
    }
}