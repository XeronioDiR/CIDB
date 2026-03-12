package net.xeroniodir.cidb.client.config.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.options.ColorOption;

public class ColorButtonWidget extends ButtonWidget {
    private final ColorOption option;

    public ColorButtonWidget(int x, int y, int width, int height, ColorOption option) {
        super(x, y, width, height, net.minecraft.text.Text.literal(""),
                button -> {
                    MinecraftClient client = MinecraftClient.getInstance();
                    client.setScreen(new ColorPickerScreen(client.currentScreen, option));
                },
                DEFAULT_NARRATION_SUPPLIER);
        this.option = option;
    }

    //? if <1.21.11 {
    /*@Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        int color = option.getter.get();
        context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, color);
    }
    *///?} else {

    @Override
    protected void drawIcon(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        this.drawButton(context);
        this.drawLabel(context.getHoverListener(this, DrawContext.HoverType.NONE));
        int color = option.getter.get();
        context.fill(this.getX() + 1, this.getY() + 1, this.getX() + this.getWidth() - 1, this.getY() + this.getHeight() - 1, color);
    }
    //?}
}