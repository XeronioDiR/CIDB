package net.xeroniodir.cidb.client.config;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.xeroniodir.cidb.client.ConfigCategory;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Option<T> {
    public final String title;
    public final T defaultValue;
    public final Supplier<T> getter;
    public final Consumer<T> setter;
    public final String description;
    public final ConfigCategory configCategory;

    public Option(String title, T defaultValue, Supplier<T> getter, Consumer<T> setter,String description, ConfigCategory configCategory) {
        this.title = title;
        this.defaultValue = defaultValue;
        this.getter = getter;
        this.setter = setter;
        this.description = description;
        this.configCategory = configCategory;
    }

    public void reset() {
        setter.accept(defaultValue);
    }

    public abstract ClickableWidget createWidget(int x, int y, int width);

    public ConfigCategory getCategory(){
        return configCategory;
    }
}