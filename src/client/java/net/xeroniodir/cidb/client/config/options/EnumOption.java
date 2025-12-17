package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.Option;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class EnumOption<E extends Enum<E>> extends Option<E> {

    private final E[] enumValues;

    public EnumOption(String title, E defaultValue, Supplier<E> getter, Consumer<E> setter, Class<E> enumClass, String d) {
        super(title, defaultValue, getter, setter, d);
        this.enumValues = enumClass.getEnumConstants();
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width) {
        return ButtonWidget.builder(getText(), button -> {
            E currentValue = getter.get();
            int nextIndex = (currentValue.ordinal() + 1) % enumValues.length;
            E nextValue = enumValues[nextIndex];
            setter.accept(nextValue);
            button.setMessage(getText());
        }).dimensions(x, y, width, 20).build();
    }

    private Text getText() {
        return Text.literal(getter.get().name());
    }
}
