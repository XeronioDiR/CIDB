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
    public List<Integer> integerListTest = List.of(1, 2, 3);
    public List<List<Integer>> integerListList = List.of(List.of(1, 2, 3),List.of(1, 2, 3));
    public List<Boolean> booleanListTest = List.of(true,true,false);
    public List<String> selectedItemsList = List.of(Items.OAK_LOG.toString(),Items.STONE.toString());

    // Метод для создания дефолтного конфига
    public static ModConfig createDefault() {
        return new ModConfig();
    }
}