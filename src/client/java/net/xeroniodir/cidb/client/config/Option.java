package net.xeroniodir.cidb.client.config;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class Option<T> {
    public final String title;
    public final T defaultValue;
    public final Supplier<T> getter;
    public final Consumer<T> setter;

    public Option(String title, T defaultValue, Supplier<T> getter, Consumer<T> setter) {
        this.title = title;
        this.defaultValue = defaultValue;
        this.getter = getter;
        this.setter = setter;
    }

    public void reset() {
        setter.accept(defaultValue);
    }

    public abstract ClickableWidget createWidget(int x, int y, int width);
}