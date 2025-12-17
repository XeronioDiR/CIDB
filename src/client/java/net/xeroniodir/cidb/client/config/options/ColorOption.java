package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.xeroniodir.cidb.client.config.Option;
import net.xeroniodir.cidb.client.config.widgets.ColorButtonWidget;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ColorOption extends Option<Integer> {

    public final boolean hasAlpha;

    public ColorOption(String title, Integer defaultValue, boolean hasAlpha, Supplier<Integer> getter, Consumer<Integer> setter, String description) {
        super(title, defaultValue, getter, setter, description);
        this.hasAlpha = hasAlpha;
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width) {
        return new ColorButtonWidget(x, y, width, 20, this);
    }
}