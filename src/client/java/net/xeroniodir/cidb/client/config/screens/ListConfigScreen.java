package net.xeroniodir.cidb.client.config.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.ConfigManager;
import net.xeroniodir.cidb.client.config.Option;
import net.xeroniodir.cidb.client.config.options.ListOption;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ListConfigScreen extends Screen {
    private final Screen parent;
    private final ListOption<Object> option;
    private final List<Object> workingList; // Список, с которым мы работаем прямо сейчас
    private ListEntryList optionList;

    // Конструктор 1: Вызывается из главного меню (читает из конфига)
    public ListConfigScreen(Screen parent, ListOption<Object> option) {
        this(parent, option, new ArrayList<>(option.getter.get()));
    }

    // Конструктор 2: Вызывается при обновлении экрана (Add/Remove) - принимает уже измененный список
    private ListConfigScreen(Screen parent, ListOption<Object> option, List<Object> currentList) {
        super(Text.literal(option.title));
        this.parent = parent;
        this.option = option;
        this.workingList = currentList;
    }

    @Override
    protected void init() {
        super.init();

        optionList = new ListEntryList(this.client, this.width, this.height - 60, 32, 25);
        this.addDrawableChild(optionList);

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("cidb.cconfig.add"), b -> {
            workingList.add(option.createNewItemDefault());
            this.client.setScreen(new ListConfigScreen(parent, option, workingList));
        }).dimensions(this.width / 2 - 135, this.height - 26, 75, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("cidb.cconfig.save"), b -> {
            (option.setter).accept(this.workingList);
            this.client.setScreen(parent);
        }).dimensions(this.width / 2 - 37, this.height - 26, 75, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("cidb.cconfig.cancel"), b -> {
            this.client.setScreen(parent);
        }).dimensions(this.width / 2 + 61, this.height - 26, 75, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        TextWidget titleText = new TextWidget(this.title,client.textRenderer).alignRight().setTextColor(0xFFFFFF);
        titleText.setX(this.width / 2 - titleText.getWidth() / 2);
        titleText.setY(10);
        titleText.renderWidget(context,mouseX,mouseY,delta);
    }

    public class ListEntryList extends EntryListWidget<ListEntryList.ListEntry> {

        public ListEntryList(MinecraftClient client, int width, int height, int top, int itemHeight) {
            super(client, width, height, top, itemHeight);
            for (int i = 0; i < workingList.size(); i++) {
                this.addEntry(new ListEntry(i));
            }
        }

        @Override public int getRowWidth() { return 300; }
        @Override protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

        public class ListEntry extends EntryListWidget.Entry<ListEntry> {
            private final int index;
            private final ClickableWidget valueWidget;
            private final ButtonWidget deleteButton;

            public ListEntry(int index) {
                this.index = index;
                Object currentValue = workingList.get(index);

                Consumer<Object> itemSetter = (newValue) -> workingList.set(this.index, newValue);

                Supplier<Object> itemGetter = () -> workingList.get(this.index);

                Option<Object> elementOption = option.createOptionForElement(
                        currentValue,
                        itemSetter,
                        itemGetter
                );
                this.valueWidget = elementOption.createWidget(0, 0, 150);

                this.deleteButton = ButtonWidget.builder(Text.literal("-"), b -> {
                    workingList.remove(this.index);
                    client.setScreen(new ListConfigScreen(parent, option, workingList));
                }).dimensions(0, 0, 20, 20).build();
            }

            @Override
            public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {

                TextWidget textWidget = new TextWidget(Text.literal("#" + (index + 1)),client.textRenderer);
                textWidget.setX(x + 5);
                textWidget.setY(y + 6);
                textWidget.setTextColor(0xAAAAAA);
                textWidget.renderWidget(context,mouseX,mouseY,delta);

                int widgetX = x + entryWidth - 25 - 150 - 5;
                valueWidget.setX(widgetX);
                valueWidget.setY(y);
                valueWidget.render(context, mouseX, mouseY, delta);

                deleteButton.setX(x + entryWidth - 25);
                deleteButton.setY(y);
                deleteButton.render(context, mouseX, mouseY, delta);
            }

            public List<? extends Element> children() { return List.of(valueWidget, deleteButton); }
            public List<? extends Selectable> selectableChildren() { return List.of(valueWidget, deleteButton); }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (valueWidget.mouseClicked(mouseX, mouseY, button)) return true;
                if (deleteButton.mouseClicked(mouseX, mouseY, button)) return true;
                return super.mouseClicked(mouseX, mouseY, button);
            }

            @Override
            public boolean mouseReleased(double mouseX, double mouseY, int button) {
                if (valueWidget.mouseReleased(mouseX, mouseY, button)) return true;
                if (deleteButton.mouseReleased(mouseX, mouseY, button)) return true;
                return super.mouseReleased(mouseX, mouseY, button);
            }

            @Override
            public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                if (valueWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
                    return true;
                }
                return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }
        }
    }
}