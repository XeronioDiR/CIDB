package net.xeroniodir.cidb.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigScreen extends Screen{
    private final Screen parent;

    private final List<TextEntry> texts = new ArrayList<>();
    private final List<ButtonEntry> buttons = new ArrayList<>();
    private final List<ConfigOption> options;
    private ConfigOptionList optionList;

    private static final int OPTION_WIDTH = 160;
    private static final int OPTION_HEIGHT = 20;
    private static final int OPTION_SPACING = 4;

    public ConfigScreen(Screen parent,List<ConfigOption> options) {
        super(Text.literal("CONFIGG"));
        this.parent = parent;
        this.options = options;
    }

    public ConfigScreen addText(Text text, int x, int y, int color) {
        texts.add(new TextEntry(text, x, y, color));
        return this;
    }

    public ConfigScreen addButton(Text text, int x, int y, int width, int height, ButtonWidget.PressAction action) {
        buttons.add(new ButtonEntry(text, x, y, width, height, action));
        return this;
    }

    @Override
    protected void init() {
        super.init();

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("X"), button -> {
                    assert this.client != null;
                    this.client.setScreen(this.parent);
                }).dimensions(5, 5, 20, 20).build()
        );

        for (ButtonEntry entry : buttons) {
            this.addDrawableChild(
                    ButtonWidget.builder(entry.text(), entry.action())
                            .dimensions(entry.x(), entry.y(), entry.width(), entry.height())
                            .build()
            );
        }

        int listWidth = 200;
        int rightX = this.width - listWidth - 10;

        optionList = new ConfigOptionList(
                this.client,
                this.width,
                this.height,
                30,
                20
        );

        for (ConfigOption option : options) {
            optionList.addOption(option);
        }

        this.addDrawableChild(optionList);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        context.drawText(this.textRenderer, "Привет — это текст на экране", this.width / 2,  this.height / 2, 0xFF0000, true);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    public record TextEntry(Text text, int x, int y, int color) {}

    public record ButtonEntry(Text text, int x, int y, int width, int height, ButtonWidget.PressAction action) {}

    public class ConfigOptionList extends EntryListWidget<ConfigOptionList.Entry> {

        public ConfigOptionList(MinecraftClient client, int width, int height, int top, int itemHeight) {
            super(client, width, height, top, itemHeight, 1);
        }

        public void addOption(ConfigOption option) {
            this.addEntry(new Entry(option));
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {

        }

        public static class Entry
                extends EntryListWidget.Entry<Entry> {

            private final ButtonWidget dummyButton;
            private final ConfigOption option;

            public Entry(ConfigOption option) {
                this.dummyButton = ButtonWidget.builder(
                        Text.literal(option.title),
                        b -> {}
                ).dimensions(0, 0, 120, 20).build();
                this.option = option;
            }

            public List<? extends Element> children() {
                return List.of(dummyButton);
            }

            public List<? extends Drawable> drawables() {
                return List.of(dummyButton);
            }

            @Override
            public void render(
                    DrawContext context,
                    int index,
                    int y,
                    int x,
                    int entryWidth,
                    int entryHeight,
                    int mouseX,
                    int mouseY,
                    boolean hovered,
                    float delta
            ) {
                dummyButton.setPosition(
                        x + entryWidth - 120,
                        y
                );
                dummyButton.render(context, mouseX, mouseY, delta);
            }
        }
    }

    public static class ConfigOption {
        private final String title;

        public ConfigOption(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }
}
