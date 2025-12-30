package net.xeroniodir.cidb.client.mixin.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
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
        float pct;
        Color twinklingColor = new Color(ConfigManager.getLoaded().twinklingDurabilityColor);
        List<Color> colorList = new ArrayList<>();
        if(ConfigManager.getLoaded().itemCustomDurabilityColor.containsKey(Registries.ITEM.getId(stack.getItem()).toString())){
            for (Integer ci : ConfigManager.getLoaded().itemCustomDurabilityColor.get(Registries.ITEM.getId(stack.getItem()).toString())){
                colorList.add(new Color(ci));
            }
        }
        else{
            for (Integer ci : ConfigManager.getLoaded().colorList){
                colorList.add(new Color(ci));
            }
        }
        double twinklingSpeed = ConfigManager.getLoaded().twinklingSpeed;

        if (stack.getDamage() > 0) {
            pct = 1f - ((float) stack.getDamage() / (float) stack.getMaxDamage());
        } else {
            pct = 1f;
        }

        pct = Math.max(0f, Math.min(1f, pct));
        int procent = ConfigManager.getLoaded().durabiltiyProcent;

        boolean blinking = pct < 0.01f * procent;
        float pos = (float) stack.getDamage() / stack.getMaxDamage() * (colorList.size() - 1);
        int index = (int) pos;
        if (index >= colorList.size() - 1) index = colorList.size() - 2;
        if (index < 0) index = 0;
        float local = pos - index;
        Color c1 = colorList.get(index);
        int ri;
        int gi;
        int bi;
        if(colorList.size() > 1){  /// If colors more than one, making transition
            Color c2 = colorList.get(index + 1);

            ri = (int)(c1.getRed() * (1 - local) + c2.getRed() * (local));
            gi = (int)(c1.getGreen() * (1 - local) + c2.getGreen() * (local));
            bi = (int)(c1.getBlue() * (1 - local) + c2.getBlue() * (local));
        }
        else{ /// If only one, using only it
            ri = (int)(c1.getRed());
            gi = (int)(c1.getGreen());
            bi = (int)(c1.getBlue());
        }

        int tr = twinklingColor.getRed(), tg =  twinklingColor.getGreen(), tb =  twinklingColor.getBlue();

        int r, g, b;

        if (blinking && ConfigManager.getLoaded().durabilityTwinkling) {
            long t = System.currentTimeMillis(); /// Using System since it will not change on tick speed (or other server stuff)

            float pulse = (float) ((Math.sin(t * 0.008 * twinklingSpeed) + 1.0) / 2);

            r = (int)(tr * (1 - pulse) + ri * pulse);
            g = (int)(tg * (1 - pulse) + gi * pulse);
            b = (int)(tb * (1 - pulse) + bi * pulse);

        } else {
            r = (ri);
            g = (gi);
            b = (bi);
        }

        int color = (r << 16) | (g << 8) | b;
        cir.setReturnValue(color);
    }
}
