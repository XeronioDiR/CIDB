package net.xeroniodir.cidb.client;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModConfig {
    public boolean durabilityTwinkling = true;
    public boolean durabilityBarLengthOnCritical = true;
    public double twinklingSpeed = 1;
    public int durabiltiyProcent = 25;
    public int bundleBarColor = 0xFF7087FF;
    public int fullBundleBarColor = 0xFFFF5555;
    public List<Integer> colorList = List.of(0xFF00FF00,0xFFFF0000);
    public int twinklingDurabilityColor = 0xFFFF9898;
    public Map<String,List<Integer>> itemCustomDurabilityColor = Map.of(Items.DIAMOND_AXE.toString(), List.of(0xFF32E8C9,Color.red.getRGB()),
            Items.IRON_AXE.toString(), List.of(0xFFFCFCFC,Color.green.getRGB(),Color.red.getRGB()));
    public DurabilityBarStyleEnum durabilityBarStyle = DurabilityBarStyleEnum.HORIZONTAL;
    public DurabilityColorStyleEnum durabilityColorStyle = DurabilityColorStyleEnum.VANILLA;

    public static ModConfig createDefault() {
        return new ModConfig();
    }

    public static ModConfig getPreset1(){
        ModConfig preset = new ModConfig();
        preset.durabilityColorStyle = DurabilityColorStyleEnum.RAINBOW;
        preset.itemCustomDurabilityColor = Map.of();
        preset.colorList = List.of(
                new Color(0xFF0000).getRGB(),
                new Color(0xFF8800).getRGB(),
                new Color(0xFFFF00).getRGB(),
                new Color(0x00FF00).getRGB(),
                new Color(0x00FFFF).getRGB(),
                new Color(0x0000FF).getRGB(),
                new Color(0x8C00FF).getRGB()
        );
        return preset;
    }

    public static ModConfig getPreset2(){
        ModConfig preset = new ModConfig();
        preset.durabilityColorStyle = DurabilityColorStyleEnum.VANILLA;
        int diamondColor = new Color(0x00FFEC).getRGB();
        int netheriteColor = new Color(0x4C4143).getRGB();
        int goldColor = new Color(0xFDF55F).getRGB();
        int ironColor = new Color(0xD8D8D8).getRGB();
        int woodenColor = new Color(0x866526).getRGB();
        int stoneColor = new Color(0xB3B1AF).getRGB();

        int copperColor = new Color(0xE77C56).getRGB();
        Map<String, List<Integer>> customMap = new HashMap<>();

        addItemsToMap(customMap, diamondColor, Items.DIAMOND_AXE, Items.DIAMOND_PICKAXE,
                Items.DIAMOND_SHOVEL, Items.DIAMOND_HOE, Items.DIAMOND_SWORD, Items.DIAMOND_HELMET,
                Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS, Items.DIAMOND_HORSE_ARMOR);

        addItemsToMap(customMap, ironColor, Items.IRON_AXE, Items.IRON_PICKAXE, Items.IRON_SHOVEL,
                Items.IRON_HOE, Items.IRON_SWORD, Items.IRON_HELMET, Items.IRON_CHESTPLATE,
                Items.IRON_LEGGINGS, Items.IRON_BOOTS, Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE,
                Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS, Items.IRON_HORSE_ARMOR, Items.SHEARS);

        addItemsToMap(customMap, goldColor, Items.GOLDEN_AXE, Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL,
                Items.GOLDEN_HOE, Items.GOLDEN_SWORD, Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE,
                Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLDEN_HORSE_ARMOR);

        addItemsToMap(customMap, stoneColor, Items.STONE_AXE, Items.STONE_PICKAXE, Items.STONE_SHOVEL,
                Items.STONE_HOE, Items.STONE_SWORD);

        addItemsToMap(customMap, woodenColor, Items.WOODEN_AXE, Items.WOODEN_PICKAXE, Items.WOODEN_SHOVEL,
                Items.WOODEN_HOE, Items.WOODEN_SWORD, Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE,
                Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS, Items.LEATHER_HORSE_ARMOR, Items.SHIELD);

        addItemsToMap(customMap, netheriteColor, Items.NETHERITE_AXE, Items.NETHERITE_PICKAXE,
                Items.NETHERITE_SHOVEL, Items.NETHERITE_HOE, Items.NETHERITE_SWORD, Items.NETHERITE_HELMET,
                Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS);

        addItemsToMap(customMap, stoneColor, Items.ELYTRA, Items.FLINT_AND_STEEL);
        addItemsToMap(customMap, woodenColor, Items.BOW, Items.CROSSBOW, Items.CARROT_ON_A_STICK,
                Items.WARPED_FUNGUS_ON_A_STICK,Items.FISHING_ROD);
        addItemsToMap(customMap,new Color(0xE9C097).getRGB(),Items.BRUSH);
        addItemsToMap(customMap,new Color(0x46BC49).getRGB(),Items.TURTLE_HELMET);
        addItemsToMap(customMap,new Color(0x65DFC2).getRGB(),Items.TRIDENT);
        addItemsToMap(customMap,new Color(0x787FC1).getRGB(),Items.MACE);

        //? if >=1.21.9 {
        addItemsToMap(customMap, copperColor, Items.COPPER_AXE, Items.COPPER_PICKAXE, Items.COPPER_HOE,
                Items.COPPER_SHOVEL, Items.COPPER_SWORD, Items.COPPER_HELMET, Items.COPPER_CHESTPLATE,
                Items.COPPER_LEGGINGS, Items.COPPER_BOOTS, Items.COPPER_HORSE_ARMOR);
        //?}
        preset.itemCustomDurabilityColor = customMap;
        preset.colorList = List.of(
                new Color(0x00FF00).getRGB(),
                new Color(0xFFFF00).getRGB(),
                new Color(0xFF0000).getRGB()
        );
        return preset;
    }

    private static void addItemsToMap(Map<String, List<Integer>> map, int color, Item... items) {
        for (Item item : items) {
            map.put(item.toString(), List.of(color | 0xFF000000)); // Ensure Alpha channel is set
        }
    }
}
