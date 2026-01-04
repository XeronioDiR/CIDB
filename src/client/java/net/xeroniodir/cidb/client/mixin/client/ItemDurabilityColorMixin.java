package net.xeroniodir.cidb.client.mixin.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.xeroniodir.cidb.client.DurabilityColorStyleEnum;
import net.xeroniodir.cidb.client.ModConfig;
import net.xeroniodir.cidb.client.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.List;
import java.util.ArrayList;

@Mixin(Item.class)
public class ItemDurabilityColorMixin {

    @Inject(method = "getItemBarColor", at = @At("HEAD"), cancellable = true)
    private void injectBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        ModConfig cfg = ConfigManager.getLoaded();
        double twinklingSpeed = cfg.twinklingSpeed;
        float pct = 1f;
        if (stack.getDamage() > 0) {
            pct = 1f - ((float) stack.getDamage() / (float) stack.getMaxDamage());
        }
        pct = Math.max(0f, Math.min(1f, pct));

        int finalColor;

        if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW) {
            List<Color> colorList = new ArrayList<>();
            if(cfg.itemCustomDurabilityColor.containsKey(Registries.ITEM.getId(stack.getItem()).toString())){
                for (Integer ci : cfg.itemCustomDurabilityColor.get(Registries.ITEM.getId(stack.getItem()).toString())){
                    colorList.add(new Color(ci));
                }
            } else {
                for (Integer ci : cfg.colorList){
                    colorList.add(new Color(ci));
                }
            }

            if (colorList.isEmpty()) {
                colorList.add(Color.WHITE); // на всякий случай
            }

            long t = System.currentTimeMillis();
            double speed = cfg.twinklingSpeed * 0.002; // скорость переливания
            float pos = (float)((t * speed) % colorList.size()); // позиция в списке цветов по времени
            int index = (int) pos;
            int nextIndex = (index + 1) % colorList.size();
            float local = pos - index;

            Color c1 = colorList.get(index);
            Color c2 = colorList.get(nextIndex);

            int r = (int)(c1.getRed() * (1 - local) + c2.getRed() * local);
            int g = (int)(c1.getGreen() * (1 - local) + c2.getGreen() * local);
            int b = (int)(c1.getBlue() * (1 - local) + c2.getBlue() * local);

            finalColor = (r << 16) | (g << 8) | b;
        } else { // VANILLA
            // Берём мерцание и плавные переходы из colorList
            Color twinklingColor = new Color(cfg.twinklingDurabilityColor);
            List<Color> colorList = new ArrayList<>();
            if(cfg.itemCustomDurabilityColor.containsKey(Registries.ITEM.getId(stack.getItem()).toString())){
                for (Integer ci : cfg.itemCustomDurabilityColor.get(Registries.ITEM.getId(stack.getItem()).toString())){
                    colorList.add(new Color(ci));
                }
            } else {
                for (Integer ci : cfg.colorList){
                    colorList.add(new Color(ci));
                }
            }

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

        cir.setReturnValue(finalColor);
    }
}
