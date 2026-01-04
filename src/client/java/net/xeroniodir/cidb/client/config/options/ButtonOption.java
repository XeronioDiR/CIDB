package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.Item;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.Option;
import net.xeroniodir.cidb.client.config.widgets.ItemButtonWidget;

import javax.lang.model.type.NoType;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ButtonOption extends Option<NoType> {
    public final ButtonWidget.PressAction action;
    public ButtonOption(String title, ButtonWidget.PressAction action) {
        super(title, null, null, null, "");
        this.action = action;
    }

    public ClickableWidget createWidget(int x, int y, int width, ButtonWidget.PressAction additionalAction) {
        return ButtonWidget.builder(Text.translatable(title),button -> {
            action.onPress(button);
            additionalAction.onPress(button);
        }).dimensions(x, y, width, 20).build();
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width) {
        return ButtonWidget.builder(Text.translatable(title),action).dimensions(x, y, width, 20).build();
    }

    @Override
    public void reset() {
    }
}