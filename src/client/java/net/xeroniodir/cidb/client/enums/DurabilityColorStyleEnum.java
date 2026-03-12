package net.xeroniodir.cidb.client.enums;

import net.minecraft.text.Text;

public enum DurabilityColorStyleEnum implements ConfigEnum{
    VANILLA,
    RAINBOW,
    GRADIENT;

    public Text getDisplayName() {
        return Text.translatable(("cdib.barcolorstyles." + name().toLowerCase()));
    }
}
