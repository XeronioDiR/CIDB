package net.xeroniodir.cidb.client;

import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class ModConfig {
    public boolean booleanTest = true;
    public int intSliderTest = 50;
    public float floatSliderTest = 75.655f;
    public double doubleSliderTest = 25.5;
    public String selectedItemId = Items.OAK_LOG.toString();

    // Метод для создания дефолтного конфига
    public static ModConfig createDefault() {
        return new ModConfig();
    }
}