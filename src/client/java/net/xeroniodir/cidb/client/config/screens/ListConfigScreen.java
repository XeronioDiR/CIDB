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
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.ConfigManager;
import net.xeroniodir.cidb.client.config.Option;
import net.xeroniodir.cidb.client.config.options.ListOption;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"rawtypes", "unchecked"})
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
        super(Text.literal("Редактирование: " + option.title));
        this.parent = parent;
        this.option = option;
        this.workingList = currentList;
    }

    @Override
    protected void init() {
        super.init();

        optionList = new ListEntryList(this.client, this.width, this.height - 60, 32, 25);
        this.addDrawableChild(optionList);

        // Кнопка Добавить элемент
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Добавить"), b -> {
            // 1. Модифицируем текущий список
            workingList.add(option.createNewItemDefault());
            // 2. Передаем модифицированный список в новый экран!
            this.client.setScreen(new ListConfigScreen(parent, option, workingList));
        }).dimensions(this.width / 2 - 190, this.height - 26, 100, 20).build());

        // Кнопка Сохранить и Выйти
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Сохранить и Выйти"), b -> {
            ((Consumer<List<Object>>) option.setter).accept(this.workingList);
            ConfigManager.save();
            this.client.setScreen(parent);
        }).dimensions(this.width / 2 - 75, this.height - 26, 150, 20).build());

        // Кнопка Отмена
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Отмена"), b -> {
            this.client.setScreen(parent);
        }).dimensions(this.width / 2 + 90, this.height - 26, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 15, 0xFFFFFF);
    }

    // --- EntryListWidget ---
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

                // 1. Сеттер: Обновляет элемент в workingList по индексу
                Consumer<Object> itemSetter = (newValue) -> workingList.set(this.index, newValue);

                // 2. Геттер: Читает элемент из workingList по индексу
                Supplier<Object> itemGetter = () -> workingList.get(this.index);

                // --- ИЗМЕНЕНИЕ ---
                // Создаем Option через фабрику, передавая ГЕТТЕР, который читает из списка!
                Option<Object> elementOption = (Option<Object>) option.createOptionForElement(
                        currentValue,
                        itemSetter,
                        itemGetter// <--- НОВЫЙ ПАРАМЕТР: Передаем геттер, который читает из workingList
                );
                this.valueWidget = elementOption.createWidget(0, 0, 150);

                this.deleteButton = ButtonWidget.builder(Text.literal("-"), b -> {
                    workingList.remove(this.index);
                    // Перезагружаем экран с обновленным списком
                    client.setScreen(new ListConfigScreen(parent, option, workingList));
                }).dimensions(0, 0, 20, 20).build();
            }

            @Override
            public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
                context.drawTextWithShadow(client.textRenderer, Text.literal("#" + (index + 1)), x + 5, y + 6, 0xAAAAAA);

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