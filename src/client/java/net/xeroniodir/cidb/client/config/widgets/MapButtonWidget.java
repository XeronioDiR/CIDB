package net.xeroniodir.cidb.client.config.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
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

    private static  net.minecraft.text.Text getText(MapOption<?, ?> option) {
        return net.minecraft.text.Text.literal(option.getter.get().size() + " ").append( net.minecraft.text.Text.translatable("cidb.cconfig.pairs"));
    }
    //? >=1.21.11 {
    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.drawButton(context);
        this.drawLabel(context.getHoverListener(this, DrawContext.HoverType.NONE));
    }
    //?}
}