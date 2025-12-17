package net.xeroniodir.cidb.client.config;

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
import net.xeroniodir.cidb.client.config.Option; // Импорт твоего класса Option

import java.util.List;
import java.util.Objects;

public class ConfigScreen extends Screen {
    private final Screen parent;
    private final List<Option<?>> options;
    private ConfigOptionList optionList;
    private ButtonWidget cancelButton;

    public ConfigScreen(Screen parent, List<Option<?>> options) {
        super(Text.literal("Настройки CIDB"));
        this.parent = parent;
        this.options = options;
    }

    @Override
    protected void init() {
        super.init();
            optionList = new ConfigOptionList(this.client, this.width, this.height - 60, 32, 25);

        for (Option<?> option : options) {
            optionList.addOption(option);
        }
        this.addDrawableChild(optionList);
        int buttonY = this.height - 26;
        int buttonHeight = 20;
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("cidb.cconfig.save"), b -> {
            ConfigManager.save();

        }).dimensions(this.width / 2 - 37, buttonY, 75, buttonHeight).build());
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("cidb.cconfig.reset"), b -> {
            for (Option<?> option : options) {
                option.reset();
            }
            this.client.setScreen(new ConfigScreen(parent, options));
        }).dimensions(this.width / 2 - 135, buttonY, 75, buttonHeight).build());
        cancelButton = ButtonWidget.builder(Text.translatable(ConfigManager.isEqual() ? "cidb.cconfig.exit" : "cidb.cconfig.cancel"), b -> {
            if(ConfigManager.isEqual()){
                this.client.setScreen(parent);
            }
            else{
                ConfigManager.load();
                this.client.setScreen(this);
            }
        }).dimensions(this.width / 2 + 61, this.height - 26, 75, 20).build();
        this.addDrawableChild(cancelButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        cancelButton.setMessage(Text.translatable(ConfigManager.isEqual() ? "cidb.cconfig.exit" : "cidb.cconfig.cancel"));

        TextWidget titleText = new TextWidget(this.title,client.textRenderer).alignRight().setTextColor(0xFFFFFF);
        titleText.setX(this.width / 2 - titleText.getWidth() / 2);
        titleText.setY(10);
        titleText.renderWidget(context,mouseX,mouseY,delta);
    }

    @Override
    public void close() {
        this.client.setScreen(parent);
    }

    public class ConfigOptionList extends EntryListWidget<ConfigOptionList.OptionEntry> {

        public ConfigOptionList(MinecraftClient client, int width, int height, int top, int itemHeight) {
            super(client, width, height, top, itemHeight);
            this.centerListVertically = false;
        }

        public void addOption(Option<?> option) {
            this.addEntry(new OptionEntry(option));
        }

        @Override
        public int getRowWidth() {
            return Math.min(width - 100, width / 2 + 370); // Используем почти всю ширину экрана
        }

        protected int getScrollbarPositionX() {
            return width - 10;
        }

        @Override
        protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

        public class OptionEntry extends EntryListWidget.Entry<OptionEntry> {
            private final Option<?> option;
            private final ClickableWidget valueWidget;
            private final ButtonWidget resetButton;

            private static final int WIDGET_WIDTH = 100;
            private static final int RESET_BTN_WIDTH = 20;
            private static final int SPACING = 5;

            public OptionEntry(Option<?> option) {
                this.option = option;

                this.valueWidget = option.createWidget(0, 0, WIDGET_WIDTH);

                this.resetButton = ButtonWidget.builder(Text.literal("⟳"), b -> {
                    option.reset();
                    client.setScreen(new ConfigScreen(ConfigScreen.this.parent, ConfigScreen.this.options));
                }).dimensions(0, 0, RESET_BTN_WIDTH, 20).build();
            }

            public List<? extends Element> children() {
                return List.of(valueWidget, resetButton);
            }

            public List<? extends Selectable> selectableChildren() {
                return List.of(valueWidget, resetButton);
            }

            @Override
            public boolean mouseClicked(double mouseX, double mouseY, int button) {
                if (valueWidget.mouseClicked(mouseX, mouseY, button)) return true;
                if (resetButton.mouseClicked(mouseX, mouseY, button)) return true;
                return super.mouseClicked(mouseX, mouseY, button);
            }

            @Override
            public boolean mouseReleased(double mouseX, double mouseY, int button) {
                if (valueWidget.mouseReleased(mouseX, mouseY, button)) return true;
                if (resetButton.mouseReleased(mouseX, mouseY, button)) return true;
                return super.mouseReleased(mouseX, mouseY, button);
            }

            @Override
            public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
                if (valueWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) return true;
                return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
            }

            @Override
            public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
                TextWidget titleText = new TextWidget(Text.translatable(option.title),client.textRenderer).setTextColor(0xFFFFFF);
                titleText.setX(x + 5);
                titleText.setY(y + 6);
                titleText.renderWidget(context,mouseX,mouseY,delta);

                int resetX = x + entryWidth - RESET_BTN_WIDTH - SPACING;
                resetButton.setX(resetX);
                resetButton.setY(y);
                resetButton.render(context, mouseX, mouseY, delta);

                int widgetX = resetX - WIDGET_WIDTH - SPACING;
                valueWidget.setX(widgetX);
                valueWidget.setY(y);
                valueWidget.render(context, mouseX, mouseY, delta);

                if (valueWidget.isHovered() && !Objects.equals(option.description, "")) {
                    context.drawTooltip(Text.translatable(option.description), mouseX, mouseY);
                }
            }
        }
    }
}