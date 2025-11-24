package net.xeroniodir.cidb.client.mixin.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.xeroniodir.cidb.client.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
import java.util.List;

@Mixin(Item.class)
public class ItemDurabilityColorMixin {

    @Inject(method = "getItemBarColor", at = @At("HEAD"), cancellable = true)
    private void injectBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        float pct;
        Color basicColor = ModConfig.HANDLER.instance().basicDurabilityColor;
        Color twinklingColor = ModConfig.HANDLER.instance().twinklingDurabilityColor;
        List<Color> colorList = ModConfig.HANDLER.instance().colorList;
        double twinklingSpeed = ModConfig.HANDLER.instance().twinklingSpeed;

        if (stack.getMaxDamage() > 0) {
            pct = 1f - ((float) stack.getDamage() / (float) stack.getMaxDamage());
        } else {
            pct = 1f;
        }

        pct = Math.max(0f, Math.min(1f, pct));
        int procent = ModConfig.HANDLER.instance().durabilityProcent;

        boolean blinking = pct < 0.01f * procent;
        float pos = (Math.min(0, pct * (colorList.size() - 1)));
        int index = (int) pos;
        float local = pos % colorList.size();
        Color c1 = colorList.get(index);
        int ri;
        int gi;
        int bi;
        if(colorList.size() > 1){
            Color c2 = colorList.get(index + 1);

            ri = (int)(c1.getRed() * (1 - local) + c2.getRed() * (local));
            gi = (int)(c1.getGreen());
            bi = (int)(c1.getBlue());
        }
        else{
            ri = (int)(c1.getRed());
            gi = (int)(c1.getGreen());
            bi = (int)(c1.getBlue());
        }

        int tr = twinklingColor.getRed(), tg =  twinklingColor.getGreen(), tb =  twinklingColor.getBlue();

        int r, g, b;

        if (blinking && ModConfig.HANDLER.instance().durabilityTwinkling) {
            long t = System.currentTimeMillis();

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
