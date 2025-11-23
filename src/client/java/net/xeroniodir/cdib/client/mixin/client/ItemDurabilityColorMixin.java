package net.xeroniodir.cdib.client.mixin.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.xeroniodir.cdib.client.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(Item.class)
public class ItemDurabilityColorMixin {

    @Inject(method = "getItemBarColor", at = @At("HEAD"), cancellable = true)
    private void injectBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        float pct;
        Color basicColor = ModConfig.HANDLER.instance().basicDurabilityColor;
        Color twinklingColor = ModConfig.HANDLER.instance().twinklingDurabilityColor;
        double twinklingSpeed = ModConfig.HANDLER.instance().twinklingSpeed;

        if (stack.getMaxDamage() > 0) {
            pct = 1f - ((float) stack.getDamage() / (float) stack.getMaxDamage());
        } else {
            pct = 1f;
        }

        pct = Math.max(0f, Math.min(1f, pct));
        int procent = ModConfig.HANDLER.instance().durabilityProcent;

        boolean blinking = pct < 0.01f * procent;

        int vr = basicColor.getRed(), vg =  basicColor.getGreen(), vb =  basicColor.getBlue();
        int br = twinklingColor.getRed(), bg =  twinklingColor.getGreen(), bb =  twinklingColor.getBlue();

        int r, g, b;

        if (blinking && ModConfig.HANDLER.instance().durabilityTwinkling) {
            long t = System.currentTimeMillis();

            float pulse = (float) ((Math.sin(t * 0.008 * twinklingSpeed) + 1.0) / 2);

            r = (int)(br * (1 - pulse) + vr * pulse);
            g = (int)(bg * (1 - pulse) + vg * pulse);
            b = (int)(bb * (1 - pulse) + vb * pulse);

        } else {
            r = (vr);
            g = (vg);
            b = (vb);
        }

        int color = (r << 16) | (g << 8) | b;
        cir.setReturnValue(color);
    }
}
