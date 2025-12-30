package net.xeroniodir.cidb.client.config.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.options.ItemOption;

public class CreativePickerScreen extends Screen {
    public final Screen parent;
    private final ItemOption option;
    private ItemListWidget itemList;

    private static final int HEADER_HEIGHT = 30;
    private static final int FOOTER_HEIGHT = 30;

    public CreativePickerScreen(Screen parent, ItemOption option) {
        super(Text.literal("Выберите Предмет"));
        this.parent = parent;
        this.option = option;
    }

    @Override
    protected void init() {
        super.init();

        itemList = new ItemListWidget(
                this.client,
                this.width,
                this.height - HEADER_HEIGHT - FOOTER_HEIGHT,
                HEADER_HEIGHT,
                this.height - FOOTER_HEIGHT,
                this.option,
                this
        );
        this.addDrawableChild(itemList);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Назад"), b -> {
            this.client.setScreen(parent);
        }).dimensions(10, this.height - 25, 60, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Сброс"), b -> {
            option.reset();
            this.client.setScreen(parent);
        }).dimensions(this.width - 70, this.height - 25, 60, 20).build());

        int selectedItemCenterX = this.width / 2;
        Item currentItem = option.getter.get();
        this.addDrawableChild(ButtonWidget.builder(
                currentItem.getName(),
                b -> {}
        ).dimensions(selectedItemCenterX - 75, 5, 150, 20).build());
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        itemList.render(context, mouseX, mouseY, delta);

        super.render(context, mouseX, mouseY, delta);
    }
}