package net.xeroniodir.cidb.client.config.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.options.ListOption;
import net.xeroniodir.cidb.client.config.screens.ListConfigScreen;

public class ListButtonWidget extends ButtonWidget {
    private final ListOption<?> option;

    public ListButtonWidget(int x, int y, int width, int height, ListOption<Object> option) {
        super(x, y, width, height, Text.literal("Редактировать (" + option.getter.get().size() + " элементов)"), button -> {
            // При клике открываем вложенный экран
            MinecraftClient client = MinecraftClient.getInstance();
            client.setScreen(new ListConfigScreen(client.currentScreen, option));
        }, (textSupplier) -> (MutableText)textSupplier.get());
        this.option = option;
        this.setMessage(Text.literal("(" + option.getter.get().size() + " элементов)"));
    }
}