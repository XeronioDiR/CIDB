package net.xeroniodir.cidb.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.screen.ConfigScreen;
import net.xeroniodir.cidb.client.screen.ConfigScreenBuilder;

public class ModMenuIntegrator implements ModMenuApi{
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreenBuilder()
                .addOption(new ConfigScreen.ConfigOption("Опция 1"))
                .addOption(new ConfigScreen.ConfigOption("Опция 2")).build(parent);
    }
}
