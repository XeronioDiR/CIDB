package net.xeroniodir.cidb.client;

import net.minecraft.text.Text;

public enum DurabilityColorStyleEnum {
    VANILLA,
    RAINBOW,
    GRADIENT;

    public Text getDisplayName() {
        return Text.translatable(("cdib.barcolorstyles." + name().toLowerCase()));
    }
}
