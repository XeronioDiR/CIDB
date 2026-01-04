package net.xeroniodir.cidb.client.mixin.client;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
//? if >=1.21.2
import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.xeroniodir.cidb.client.ModConfig;
import net.xeroniodir.cidb.client.config.ConfigManager;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.awt.*;
//? if >=1.21.2 {
@Mixin(BundleItem.class)
//?} else {
/*@Mixin(Item.class)
*///?}
public class BundleBarColorMixin {
    //? if >=1.21.2 {
    @Inject(method = "getItemBarColor", at = @At("HEAD"), cancellable = true)
    private void injectBarColor(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        Color empty = new Color(ConfigManager.getLoaded().bundleBarColor);
        Color full = new Color(ConfigManager.getLoaded().fullBundleBarColor);
        BundleContentsComponent bundleContentsComponent = (BundleContentsComponent)stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT);
        Color selected = bundleContentsComponent.getOccupancy().compareTo(Fraction.ONE) >= 0 ? full : empty;
        int color = (selected.getRed() << 16) | (selected.getGreen() << 8) | selected.getBlue();
        cir.setReturnValue(color);
    }
    //?}
}
