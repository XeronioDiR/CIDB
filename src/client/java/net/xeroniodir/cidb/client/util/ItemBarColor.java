package net.xeroniodir.cidb.client.util;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.xeroniodir.cidb.client.ModConfig;
import net.xeroniodir.cidb.client.config.ConfigManager;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class ItemBarColor {
    public static List<Color> getColorList(Item item){
        ModConfig lcfg = ConfigManager.getLoaded();
        List<Color> lc = new ArrayList<>();
        if(lcfg.itemCustomDurabilityColor.containsKey(Registries.ITEM.getId(item).toString())){
            for(int C : lcfg.itemCustomDurabilityColor.get(Registries.ITEM.getId(item).toString())){
                lc.add(new Color(C));
            }
        }
        else{
            for(int C : lcfg.colorList){
                lc.add(new Color(C));
            }
        }
        return lc;
    }

    public static int lerpColor(int colorA, int colorB, float delta) {
        int aA = (colorA >> 24) & 0xFF;
        int rA = (colorA >> 16) & 0xFF;
        int gA = (colorA >> 8) & 0xFF;
        int bA = colorA & 0xFF;

        int aB = (colorB >> 24) & 0xFF;
        int rB = (colorB >> 16) & 0xFF;
        int gB = (colorB >> 8) & 0xFF;
        int bB = colorB & 0xFF;

        int aR = (int) (aA + delta * (aB - aA));
        int rR = (int) (rA + delta * (rB - rA));
        int gR = (int) (gA + delta * (gB - gA));
        int bR = (int) (bA + delta * (bB - bA));

        return (aR << 24) | (rR << 16) | (gR << 8) | bR;
    }
}
