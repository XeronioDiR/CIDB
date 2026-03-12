package net.xeroniodir.cidb.client.enums;

import net.minecraft.text.Text;

public enum DurabilityBarStyleEnum implements ConfigEnum {
    HORIZONTAL,
    VERTICAL,
    NONE;

    public Text getDisplayName() {
        return Text.translatable(("cdib.barstyles." + name().toLowerCase()));
    }
}
