package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.config.Option;

import javax.lang.model.type.NoType;

public class TextOption extends Option<NoType> {
    public Text title;

    public TextOption(Text title) {
        super(title.getString(), null, null, null, "");
        this.title = title;
    }

    public ClickableWidget createWidget(int x, int y, int width) {
        return new TextWidget(x, y, width, 20, title, MinecraftClient.getInstance().textRenderer).setTextColor(0xFFFFFF);
    }

    @Override
    public void reset() {
    }
}