package net.xeroniodir.cidb.client.config.widgets;

import net.minecraft.client.MinecraftClient;
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

// Список, отображающий предметы в виде сетки
public class ItemListWidget extends EntryListWidget<ItemListWidget.ItemRowEntry> {
    private final ItemOption option;
    private final CreativePickerScreen parentScreen;

    // Константы для сетки
    public static final int ITEM_SIZE = 18; // 16x16 + 2px padding
    private static final int ROW_HEIGHT = 25; // Высота строки чуть больше размера иконки

    // Определяем количество предметов в строке на основе ширины виджета
    public final int itemsPerRow;
    private final int itemsMarginLeft; // Отступ слева для центрирования сетки

    public ItemListWidget(MinecraftClient client, int width, int height, int top, int bottom, ItemOption option, CreativePickerScreen parentScreen) {
        super(client, width, height, top, ROW_HEIGHT);
        this.option = option;
        this.parentScreen = parentScreen;

        // Расчет сетки
        this.itemsPerRow = Math.max(1, (this.width - 20) / ITEM_SIZE);
        this.itemsMarginLeft = (this.width - (this.itemsPerRow * ITEM_SIZE)) / 2;

        // Группируем предметы в строки
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
        // Добавляем оставшиеся предметы (если строка не заполнилась)
        if (!currentItems.isEmpty()) {
            this.addEntry(new ItemRowEntry(currentItems));
        }
    }

    @Override
    public int getRowWidth() {
        return this.width; // Используем всю ширину
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

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
            for (int i = 0; i < rowItems.size(); i++) {
                Item item = rowItems.get(i);
                ItemStack stack = new ItemStack(item);

                int itemX = x + itemsMarginLeft + i * ITEM_SIZE;
                int itemY = y + 1;

                context.drawItem(stack, itemX, itemY, 0);

                if (item == option.getter.get()) {
                    context.drawBorder(itemX - 1, itemY - 1, ITEM_SIZE, ITEM_SIZE, 0xFF00FF00); // Зеленая рамка
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

        public List<? extends Selectable> selectableChildren() { return List.of(); }
        public List<? extends Element> children() { return List.of(); }
    }
}