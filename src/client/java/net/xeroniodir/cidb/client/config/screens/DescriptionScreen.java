package net.xeroniodir.cidb.client.config.screens;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.MultilineTextWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

public class DescriptionScreen extends Screen {
    private final Screen parent;
    private final Text description;

    public DescriptionScreen(Screen parent, Text title, Text description) {
        super(title);
        this.parent = parent;
        this.description = description;
    }

    @Override
    protected void init() {
        this.addDrawableChild(ButtonWidget.builder(Text.translatable("cidb.cconfig.back"), button -> {
            this.client.setScreen(parent);
        }).dimensions(this.width / 2 - 100, this.height - 40, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        TextWidget titleText = new TextWidget(this.title, client.textRenderer).alignCenter().setTextColor(0xFFFFFF);
        titleText.setX(this.width / 2 - titleText.getWidth() / 2);
        titleText.setY(20);
        titleText.renderWidget(context, mouseX, mouseY, delta);

        MultilineTextWidget descriptionText = new MultilineTextWidget(this.description, client.textRenderer).setTextColor(0xFFFFFF)
                .setCentered(true).setMaxWidth(420);
        descriptionText.setX(this.width / 2 - descriptionText.getWidth() / 2);
        descriptionText.setY(50);
        descriptionText.renderWidget(context, mouseX, mouseY, delta);
    }
}
