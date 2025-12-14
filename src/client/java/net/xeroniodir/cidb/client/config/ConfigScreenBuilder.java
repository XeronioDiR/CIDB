package net.xeroniodir.cidb.client.config;

import net.minecraft.client.gui.screen.Screen;
import java.util.ArrayList;
import java.util.List;

public class ConfigScreenBuilder {
    private final List<Option<?>> options = new ArrayList<>();

    public ConfigScreenBuilder addOption(Option<?> option) {
        this.options.add(option);
        return this;
    }

    public Screen build(Screen parent) {
        return new ConfigScreen(parent, options);
    }
}