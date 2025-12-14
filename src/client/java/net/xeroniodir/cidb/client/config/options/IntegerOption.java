package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.Option;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IntegerOption extends Option<Integer> {
    private final int min;
    private final int max;

    public IntegerOption(String title, int defaultValue, int min, int max, Supplier<Integer> getter, Consumer<Integer> setter) {
        super(title, defaultValue, getter, setter);
        this.min = min;
        this.max = max;
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width) {
        double initialProgress = (double)(getter.get() - min) / (double)(max - min);

        return new SliderWidget(x, y, width, 20, Text.literal(String.valueOf(getter.get())), initialProgress) {
            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal(String.valueOf(getValueInt())));
            }

            @Override
            protected void applyValue() {
                setter.accept(getValueInt());
            }

            private int getValueInt() {
                return min + (int)(this.value * (max - min));
            }
        };
    }
}