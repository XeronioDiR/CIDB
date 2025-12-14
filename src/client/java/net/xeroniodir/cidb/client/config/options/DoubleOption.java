package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.Option;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DoubleOption extends Option<Double> {
    private final double min;
    private final double max;

    public DoubleOption(String title, double defaultValue, double min, double max, Supplier<Double> getter, Consumer<Double> setter) {
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
                this.setMessage(Text.literal(String.valueOf(getValueDouble())));
            }

            @Override
            protected void applyValue() {
                setter.accept(getValueDouble());
            }

            private double getValueDouble() {
                return Math.ceil(Math.pow(10, 3) * (min + (this.value * (max - min)))) / Math.pow(10, 3);
            }
        };
    }
}