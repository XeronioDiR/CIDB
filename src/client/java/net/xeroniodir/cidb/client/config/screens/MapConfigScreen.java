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
import net.xeroniodir.cidb.client.config.options.MapOption;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class MapConfigScreen extends Screen {
    private final Screen parent;
    private final MapOption<Object, Object> option;
    private final List<Map.Entry<Object, Object>> workingList;
    private MapEntryList mapList;

    public MapConfigScreen(Screen parent, MapOption<Object, Object> option) {
        this(parent, option, new ArrayList<>(option.getter.get().entrySet()));
    }

    private MapConfigScreen(Screen parent, MapOption<Object, Object> option, List<Map.Entry<Object, Object>> currentList) {
        super(Text.literal(option.title));
        this.parent = parent;
        this.option = option;
        this.workingList = currentList;
    }

    @Override
    protected void init() {
        super.init();
        mapList = new MapEntryList(this.client, this.width, this.height - 60, 32, 25);
        this.addDrawableChild(mapList);

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("cidb.cconfig.add"), b -> {
            Map.Entry<Object, Object> newEntry = new AbstractMap.SimpleEntry<>(
                    option.createNewKeyDefault(),
                    option.createNewValueDefault()
            );
            workingList.add(newEntry);
            this.client.setScreen(new MapConfigScreen(parent, option, workingList));
        }).dimensions(this.width / 2 - 135, this.height - 26, 75, 20).build());

        this.addDrawableChild(ButtonWidget.builder(Text.translatable("cidb.cconfig.save"), b -> {
            Map<Object, Object> result = new LinkedHashMap<>();
            for (Map.Entry<Object, Object> entry : workingList) {
                result.put(entry.getKey(), entry.getValue());
            }
            ( option.setter).accept(result);
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

    public class MapEntryList extends EntryListWidget<MapEntryList.MapEntry> {
        public MapEntryList(MinecraftClient client, int width, int height, int top, int itemHeight) {
            super(client, width, height, top, itemHeight);
            for (int i = 0; i < workingList.size(); i++) {
                this.addEntry(new MapEntry(i));
            }
        }

        @Override public int getRowWidth() { return 340; }
        @Override protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

        public class MapEntry extends EntryListWidget.Entry<MapEntry> {
            private final int index;
            private final ClickableWidget keyWidget;
            private final ClickableWidget valueWidget;
            private final ButtonWidget deleteButton;

            public MapEntry(int index) {
                this.index = index;
                Supplier<Object> keyGetter = () -> workingList.get(this.index).getKey();

                Consumer<Object> keySetter = (newKey) -> {
                    Object currentValue = workingList.get(this.index).getValue(); // Берем актуальное значение
                    Map.Entry<Object, Object> newPair = new AbstractMap.SimpleEntry<>(newKey, currentValue);
                    workingList.set(this.index, newPair);
                };

                Option<Object> keyOption = option.createKeyOption(
                        workingList.get(this.index).getKey(),
                        keySetter,
                        keyGetter
                );
                this.keyWidget = keyOption.createWidget(0, 0, 100);

                Supplier<Object> valueGetter = () -> workingList.get(this.index).getValue();

                Consumer<Object> valueSetter = (newValue) -> {
                    workingList.get(this.index).setValue(newValue);
                };

                Option<Object> valueOption = option.createValueOption(
                        workingList.get(this.index).getValue(),
                        valueSetter,
                        valueGetter
                );
                this.valueWidget = valueOption.createWidget(0, 0, 100);

                this.deleteButton = ButtonWidget.builder(Text.literal("-"), b -> {
                    workingList.remove(this.index);
                    client.setScreen(new MapConfigScreen(parent, option, workingList));
                }).dimensions(0, 0, 20, 20).build();
            }

            @Override
            public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
                keyWidget.setX(x + 2);
                keyWidget.setY(y);
                keyWidget.setWidth(100);
                keyWidget.render(context, mouseX, mouseY, delta);

                TextWidget textWidget = new TextWidget(Text.literal("->"),client.textRenderer);
                textWidget.setX(x + 105);
                textWidget.setY(y + 6);
                textWidget.setTextColor(0xAAAAAA);
                textWidget.renderWidget(context,mouseX,mouseY,delta);

                valueWidget.setX(x + 120);
                valueWidget.setY(y);
                valueWidget.setWidth(100);
                valueWidget.render(context, mouseX, mouseY, delta);

                deleteButton.setX(x + entryWidth - 25);
                deleteButton.setY(y);
                deleteButton.render(context, mouseX, mouseY, delta);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (keyWidget.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
                if (valueWidget.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
                if (deleteButton.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
                return super.mouseClicked(mouseX, mouseY, button);
            }

            @Override
            public boolean mouseReleased(double mouseX, double mouseY, int button) {
                if (valueWidget.mouseReleased(mouseX, mouseY, button)) return true;
                if (deleteButton.mouseReleased(mouseX, mouseY, button)) return true;
                if (keyWidget.mouseReleased(mouseX,mouseY,button)) return true;
                return super.mouseReleased(mouseX, mouseY, button);
            }

            @Override
            public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                if (keyWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) && keyWidget.isFocused()) {
                    return true;
                }
                if (valueWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) && valueWidget.isFocused()) {
                    return true;
                }
                return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }

            public List<? extends Element> children() { return List.of(keyWidget, valueWidget, deleteButton); }
            public List<? extends Selectable> selectableChildren() { return List.of(keyWidget, valueWidget, deleteButton); }
        }
    }
}