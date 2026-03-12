package net.xeroniodir.cidb.client.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.xeroniodir.cidb.client.enums.DurabilityColorStyleEnum;
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

    public static int getBarColor(ItemStack stack) {
        ModConfig cfg = ConfigManager.getLoaded();
        double twinklingSpeed = cfg.twinklingSpeed;
        float pct = 1f;
        if (stack.getDamage() > 0) {
            pct = 1f - ((float) stack.getDamage() / (float) stack.getMaxDamage());
        }
        pct = Math.max(0f, Math.min(1f, pct));

        int finalColor;

        if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW) {
            List<Color> colorList = getColorList(stack.getItem());

            if (colorList.isEmpty()) {
                colorList.add(Color.WHITE);
            }

            long t = System.currentTimeMillis();
            double speed = cfg.twinklingSpeed * 0.002;
            float pos = (float)((t * speed) % colorList.size());
            int index = (int) pos;
            int nextIndex = (index + 1) % colorList.size();
            float local = pos - index;

            Color c1 = colorList.get(index);
            Color c2 = colorList.get(nextIndex);

            int r = (int)(c1.getRed() * (1 - local) + c2.getRed() * local);
            int g = (int)(c1.getGreen() * (1 - local) + c2.getGreen() * local);
            int b = (int)(c1.getBlue() * (1 - local) + c2.getBlue() * local);

            finalColor = (r << 16) | (g << 8) | b;
        } else {
            Color twinklingColor = new Color(cfg.twinklingDurabilityColor);
            List<Color> colorList = getColorList(stack.getItem());

            int procent = cfg.durabiltiyProcent;
            boolean blinking = pct < 0.01f * procent;

            float posVanilla = (float) stack.getDamage() / stack.getMaxDamage() * (colorList.size() - 1);
            int index = (int) posVanilla;
            if (index >= colorList.size() - 1) index = colorList.size() - 2;
            if (index < 0) index = 0;
            float local = posVanilla - index;

            Color c1 = colorList.get(index);
            Color c2 = colorList.get(Math.min(index + 1, colorList.size() - 1));

            int ri = (int)(c1.getRed() * (1 - local) + c2.getRed() * local);
            int gi = (int)(c1.getGreen() * (1 - local) + c2.getGreen() * local);
            int bi = (int)(c1.getBlue() * (1 - local) + c2.getBlue() * local);

            int tr = twinklingColor.getRed(), tg = twinklingColor.getGreen(), tb = twinklingColor.getBlue();
            int r, g, b;

            if (blinking && cfg.durabilityTwinkling) {
                float pulse = (float)((Math.sin(System.currentTimeMillis() * 0.008 * twinklingSpeed) + 1.0) / 2);
                r = (int)(tr * (1 - pulse) + ri * pulse);
                g = (int)(tg * (1 - pulse) + gi * pulse);
                b = (int)(tb * (1 - pulse) + bi * pulse);
            } else {
                r = ri;
                g = gi;
                b = bi;
            }

            finalColor = (r << 16) | (g << 8) | b;
        }
        return finalColor | -16777216;
    }
}
