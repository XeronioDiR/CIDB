package net.xeroniodir.cidb.client.enums;

import net.minecraft.text.Text;

public enum DurabilityTextStyleEnum implements ConfigEnum{
    ABSOLUTE,
    PERCENT,
    NONE;

    public Text getDisplayName() {
        return Text.translatable(("cdib.textstyles." + name().toLowerCase()));
    }
}
