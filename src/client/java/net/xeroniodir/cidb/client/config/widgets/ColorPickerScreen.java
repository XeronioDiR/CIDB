package net.xeroniodir.cidb.client.config.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.xeroniodir.cidb.client.config.options.ColorOption;

import java.awt.Color;

public class ColorPickerScreen extends Screen {
    private final Screen parent;
    private final ColorOption option;
    private final boolean hasAlpha;

    private SliderWidget redSlider;
    private SliderWidget greenSlider;
    private SliderWidget blueSlider;
    private SliderWidget alphaSlider;

    private int red;
    private int green;
    private int blue;
    private int alphav;

    public ColorPickerScreen(Screen parent, ColorOption option) {
        super(Text.literal(option.title));
        this.parent = parent;
        this.option = option;
        this.hasAlpha = option.hasAlpha;
    }

    private void updateColor() {
        int newColor = (alphav << 24) | (red << 16) | (green << 8) | blue;
        option.setter.accept(newColor);
    }

    @Override
    protected void init() {
        super.init();

        int currentVal = option.getter.get();
        Color c = new Color(currentVal, hasAlpha);

        this.red = c.getRed();
        this.green = c.getGreen();
        this.blue = c.getBlue();
        this.alphav = c.getAlpha();

        int sliderWidth = 150;
        int sliderX = this.width / 2 - sliderWidth / 2;
        int startY = this.height / 2 - (hasAlpha ? 60 : 40);

        redSlider = new SliderWidget(sliderX, startY, sliderWidth, 8, Text.literal("R: " + this.red), this.red / 255.0) {
            @Override
            protected void updateMessage() {
                int newRed = (int) (this.value * 255);
                setMessage(Text.literal("R: " + newRed));
            }

            @Override
            protected void applyValue() {
                red = (int) (this.value * 255);
                updateColor();
            }
        };
        this.addDrawableChild(redSlider);

        greenSlider = new SliderWidget(sliderX, startY + 24, sliderWidth, 8, Text.literal("G: " + this.green), this.green / 255.0) {
            @Override
            protected void updateMessage() {
                int newGreen = (int) (this.value * 255);
                setMessage(Text.literal("G: " + newGreen));
            }

            @Override
            protected void applyValue() {
                green = (int) (this.value * 255);
                updateColor();
            }
        };
        this.addDrawableChild(greenSlider);

        blueSlider = new SliderWidget(sliderX, startY + 48, sliderWidth, 8, Text.literal("B: " + this.blue), this.blue / 255.0) {
            @Override
            protected void updateMessage() {
                int newBlue = (int) (this.value * 255);
                setMessage(Text.literal("B: " + newBlue));
            }

            @Override
            protected void applyValue() {
                blue = (int) (this.value * 255);
                updateColor();
            }
        };
        this.addDrawableChild(blueSlider);

        if (hasAlpha) {
            alphaSlider = new SliderWidget(sliderX, startY + 72, sliderWidth, 8, Text.literal("A: " + this.alphav), this.alphav / 255.0) {
                @Override
                protected void updateMessage() {
                    int newAlpha = (int) (this.value * 255);
                    setMessage(Text.literal("A: " + newAlpha));
                }

                @Override
                protected void applyValue() {
                    alphav = (int) (this.value * 255);
                    updateColor();
                }
            };
            this.addDrawableChild(alphaSlider);
        } else {
            this.alphav = 255;
        }

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Назад"), b -> this.client.setScreen(parent))
                .dimensions(this.width / 2 - 50, this.height - 26, 100, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        int previewSize = 50;
        int previewX = this.width / 2 - previewSize / 2;
        int previewY = redSlider.getY() - previewSize - 5;

        context.fill(previewX + previewSize / 2, previewY + previewSize / 2, previewX + previewSize + 2, previewY + previewSize + 2, Color.DARK_GRAY.getRGB());
        context.fill(previewX - 2, previewY - 2, previewX + previewSize / 2, previewY + previewSize / 2, Color.DARK_GRAY.getRGB());
        context.fill(previewX - 2, previewY + previewSize / 2, previewX + previewSize / 2, previewY + previewSize + 2, Color.GRAY.getRGB());
        context.fill(previewX + previewSize / 2, previewY - 2, previewX + previewSize + 2, previewY + previewSize / 2, Color.GRAY.getRGB());

        context.fill(previewX, previewY, previewX + previewSize, previewY + previewSize, new Color(red,green,blue,alphav).getRGB());
    }
}