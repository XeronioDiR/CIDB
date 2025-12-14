package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.Option;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class BooleanOption extends Option<Boolean> {

    public BooleanOption(String title, boolean defaultValue, Supplier<Boolean> getter, Consumer<Boolean> setter) {
        super(title, defaultValue, getter, setter);
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width) {
        return ButtonWidget.builder(getText(), button -> {
            boolean newValue = !getter.get();
            setter.accept(newValue);
            button.setMessage(getText());
        }).dimensions(x, y, width, 20).build();
    }

    private Text getText() {
        return Text.literal(getter.get() ? "§aON" : "§cOFF"); // Зеленый ON, Красный OFF
    }
}