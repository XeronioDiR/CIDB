package net.xeroniodir.cidb.client;

import net.minecraft.text.Text;

public enum DurabilityBarStyleEnum {
    HORIZONTAL,
    VERTICAL,
    ABSOLUTE,
    PERCENT;

    public Text getDisplayName() {
        return Text.translatable(("cdib.barstyles." + name().toLowerCase()));
    }
}
