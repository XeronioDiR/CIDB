package net.xeroniodir.cidb.client.config.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.options.ItemOption;

public class ItemButtonWidget extends ButtonWidget {
    private final ItemOption option;

    public ItemButtonWidget(int x, int y, int width, int height, ItemOption option,NarrationSupplier narrationSupplier) {
        super(x, y, width, height, Text.literal(""), button -> {
            MinecraftClient client = MinecraftClient.getInstance();
                Screen parentScreen = client.currentScreen;
                client.setScreen(new CreativePickerScreen(parentScreen, option));},
                narrationSupplier
            );
        this.option = option;
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        Item currentItem = option.getter.get();
        ItemStack stack = new ItemStack(currentItem);

        int iconX = this.getX() + this.getWidth() / 2 - 8;
        int iconY = this.getY() + this.getHeight() / 2 - 8;

        context.drawItem(stack,iconX,iconY,0);
    }
}
