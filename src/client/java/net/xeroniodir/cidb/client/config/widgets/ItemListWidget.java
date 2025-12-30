package net.xeroniodir.cidb.client.config.widgets;

import net.minecraft.client.MinecraftClient;
//? if >=1.21.9
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.xeroniodir.cidb.client.config.options.ItemOption;

import java.util.ArrayList;
import java.util.List;

public class ItemListWidget extends EntryListWidget<ItemListWidget.ItemRowEntry> {
    private final ItemOption option;
    private final CreativePickerScreen parentScreen;

    public static final int ITEM_SIZE = 18;
    private static final int ROW_HEIGHT = 25;

    public final int itemsPerRow;
    private final int itemsMarginLeft;

    public ItemListWidget(MinecraftClient client, int width, int height, int top, int bottom, ItemOption option, CreativePickerScreen parentScreen) {
        super(client, width, height, top, ROW_HEIGHT);
        this.option = option;
        this.parentScreen = parentScreen;

        this.itemsPerRow = Math.max(1, (this.width - 20) / ITEM_SIZE);
        this.itemsMarginLeft = (this.width - (this.itemsPerRow * ITEM_SIZE)) / 2;

        List<Item> allItems = Registries.ITEM.stream()
                .filter(item -> item != Items.AIR)
                .toList();

        List<Item> currentItems = new ArrayList<>();
        for (Item item : allItems) {
            currentItems.add(item);
            if (currentItems.size() >= itemsPerRow) {
                this.addEntry(new ItemRowEntry(List.copyOf(currentItems)));
                currentItems.clear();
            }
        }

        if (!currentItems.isEmpty()) {
            this.addEntry(new ItemRowEntry(currentItems));
        }
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    protected int getScrollbarPositionX() {
        return this.width - 10;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

    public class ItemRowEntry extends EntryListWidget.Entry<ItemRowEntry> {
        private final List<Item> rowItems;

        public ItemRowEntry(List<Item> items) {
            this.rowItems = items;
        }
        //? if <=1.21.8 {
        /*@Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
            for (int i = 0; i < rowItems.size(); i++) {
                Item item = rowItems.get(i);
                ItemStack stack = new ItemStack(item);

                int itemX = x + itemsMarginLeft + i * ITEM_SIZE;
                int itemY = y + 1;

                context.drawItem(stack, itemX, itemY, 0);

                if (item == option.getter.get()) {

                }
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                for (int i = 0; i < rowItems.size(); i++) {
                    int itemX = getX() + itemsMarginLeft + i * ITEM_SIZE;
                    int itemY = getY() + 1;

                    if (mouseX >= itemX && mouseX < itemX + ITEM_SIZE) {
                        Item item = rowItems.get(i);
                        option.setter.accept(item);

                        client.setScreen(parentScreen.parent);
                        return true;
                    }
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
        *///?} else if >= 1.21.9 {
        @Override
        public void render(DrawContext context, int mouseX, int mouseY, boolean hovered, float deltaTicks) {
            int x = getX();
            int y = getY();
            for (int i = 0; i < rowItems.size(); i++) {
                Item item = rowItems.get(i);
                ItemStack stack = new ItemStack(item);

                int itemX = x + itemsMarginLeft + i * ITEM_SIZE;
                int itemY = y + 1;

                context.drawItem(stack, itemX, itemY, 0);

                if (item == option.getter.get()) {
                }
            }
        }
        public boolean mouseClicked(Click click, boolean doubled) {
                for (int i = 0; i < rowItems.size(); i++) {
                    int itemX = getX() + itemsMarginLeft + i * ITEM_SIZE;

                    if (click.x() >= itemX && click.x() < itemX + ITEM_SIZE) {
                        Item item = rowItems.get(i);
                        option.setter.accept(item);

                        client.setScreen(parentScreen.parent);
                        return true;
                    }
                }
            return super.mouseClicked(click,doubled);
        }
        //?}
        public List<? extends Selectable> selectableChildren() { return List.of(); }
        public List<? extends Element> children() { return List.of(); }
    }
}