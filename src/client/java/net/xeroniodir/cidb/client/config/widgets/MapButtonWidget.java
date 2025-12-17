package net.xeroniodir.cidb.client.config.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.options.MapOption;
import net.xeroniodir.cidb.client.config.screens.MapConfigScreen;

public class MapButtonWidget extends ButtonWidget {

    public MapButtonWidget(int x, int y, int width, int height, MapOption<?, ?> option) {
        super(x, y, width, height, getText(option), button -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.setScreen(new MapConfigScreen(client.currentScreen, (MapOption<Object, Object>) option));
        }, (textSupplier) -> (MutableText) textSupplier.get());
    }

    private static Text getText(MapOption<?, ?> option) {
        return Text.literal(option.getter.get().size() + " пар");
    }
}