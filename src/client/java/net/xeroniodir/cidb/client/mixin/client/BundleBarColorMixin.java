package net.xeroniodir.cidb.client.mixin.client;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.xeroniodir.cidb.client.ModConfig;
import net.xeroniodir.cidb.client.config.ConfigManager;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;

@Mixin(BundleItem.class)
public class BundleBarColorMixin {

    @Inject(method = "getItemBarColor", at = @At("HEAD"), cancellable = true)
    private void injectBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        Color empty = new Color(ConfigManager.getLoaded().bundleBarColor);
        Color full = new Color(ConfigManager.getLoaded().fullBundleBarColor);
        BundleContentsComponent bundleContentsComponent = (BundleContentsComponent)stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT);
        Color selected = bundleContentsComponent.getOccupancy().compareTo(Fraction.ONE) >= 0 ? full : empty;
        int color = (selected.getRed() << 16) | (selected.getGreen() << 8) | selected.getBlue();
        cir.setReturnValue(color);
    }
}
