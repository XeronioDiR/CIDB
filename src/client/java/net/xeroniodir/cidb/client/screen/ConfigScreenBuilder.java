package net.xeroniodir.cidb.client.screen;

import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public class ConfigScreenBuilder {
    private final List<ConfigScreen.ConfigOption> options = new ArrayList<>();

    public ConfigScreenBuilder addOption(ConfigScreen.ConfigOption option) {
        options.add(option);
        return this;
    }

    public Screen build(Screen parent) {
        return new ConfigScreen(parent, options);
    }
}
