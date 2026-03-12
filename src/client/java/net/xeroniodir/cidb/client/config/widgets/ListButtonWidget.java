package net.xeroniodir.cidb.client.config.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.options.ListOption;
import net.xeroniodir.cidb.client.config.screens.ListConfigScreen;

public class ListButtonWidget extends ButtonWidget {
    private final ListOption<?> option;

    public ListButtonWidget(int x, int y, int width, int height, ListOption<Object> option) {
        super(x, y, width, height, net.minecraft.text.Text.literal(+ option.getter.get().size() + " элементов"), button -> {
            MinecraftClient client = MinecraftClient.getInstance();
            client.setScreen(new ListConfigScreen(client.currentScreen, option));
        }, (textSupplier) -> (MutableText)textSupplier.get());
        this.option = option;
        this.setMessage(net.minecraft.text.Text.literal(+ option.getter.get().size() + " ").append(net.minecraft.text.Text.translatable("cidb.cconfig.elements")));
    }

    //? >=1.21.11 {
    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.drawButton(context);
        this.drawLabel(context.getHoverListener(this, DrawContext.HoverType.NONE));
    }
    //?}
}