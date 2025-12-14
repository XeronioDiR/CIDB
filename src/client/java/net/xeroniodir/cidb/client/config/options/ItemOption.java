package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ItemStackWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.Option;
import net.xeroniodir.cidb.client.config.widgets.ItemButtonWidget;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemOption extends Option<Item> {

    public ItemOption(String title, Item defaultValue, Supplier<Item> getter, Consumer<Item> setter) {
        super(title, defaultValue, getter, setter);
    }

    @Override
    public ClickableWidget createWidget(int x, int y, int width) {
        return new ItemButtonWidget(x, y, width, 20, this,(textSupplier) -> (MutableText)textSupplier.get());
    }
}