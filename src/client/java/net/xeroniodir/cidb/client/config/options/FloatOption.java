package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.Option;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class FloatOption extends Option<Float> {
    private final float min;
    private final float max;

    public FloatOption(String title, float defaultValue, float min, float max, Supplier<Float> getter, Consumer<Float> setter) {
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
                this.setMessage(Text.literal(String.valueOf(getValueFloat())));
            }

            @Override
            protected void applyValue() {
                setter.accept(getValueFloat());
            }

            private float getValueFloat() {
                return min + (float)(this.value * (max - min));
            }
        };
    }
}