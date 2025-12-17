package net.xeroniodir.cidb.client;

import net.minecraft.item.Items;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    public Map<String,Integer> itemLimitMap = Map.of(Items.DIAMOND.toString(), 64, Items.IRON_INGOT.toString(), 16);
    public int colorTest = 0xFFFFFFFF;
    public Map<String,List<Integer>> itemCustomDurabilityColor = Map.of(Items.DIAMOND_AXE.toString(), List.of(Color.blue.getRGB(),Color.red.getRGB()),
            Items.IRON_AXE.toString(), List.of(Color.white.getRGB(),Color.red.getRGB()));

    // Метод для создания дефолтного конфига
    public static ModConfig createDefault() {
        return new ModConfig();
    }
}