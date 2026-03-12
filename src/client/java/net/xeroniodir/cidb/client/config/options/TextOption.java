package net.xeroniodir.cidb.client.config.options;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.xeroniodir.cidb.client.ConfigCategory;
import net.xeroniodir.cidb.client.config.Option;

import javax.lang.model.type.NoType;

public class TextOption extends Option<NoType> {
    public Text title;

    public TextOption(Text title, ConfigCategory configCategory) {
        super(title.getString(), null, null, null, "", configCategory);
        this.title = title;
    }

    public ClickableWidget createWidget(int x, int y, int width) {
        return new TextWidget(x, y, width, 20, title.getWithStyle(Style.EMPTY.withColor(0xFFFFFF)).getFirst(), MinecraftClient.getInstance().textRenderer);
    }

    @Override
    public void reset() {
    }
}