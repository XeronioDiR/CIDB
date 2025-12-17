package net.xeroniodir.cidb.client;

import net.minecraft.item.Items;

import java.awt.*;
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
            Items.IRON_AXE.toString(), List.of(0xFFFCFCFC,Color.red.getRGB()));
    public DurabilityBarStyleEnum durabilityBarStyle = DurabilityBarStyleEnum.HORIZONTAL;

    // Метод для создания дефолтного конфига
    public static ModConfig createDefault() {
        return new ModConfig();
    }
}
