package net.xeroniodir.cidb.client.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.ColorHelper;
import net.xeroniodir.cidb.client.DurabilityBarStyleEnum;
import net.xeroniodir.cidb.client.ModConfig;
import net.xeroniodir.cidb.client.config.ConfigManager;
import org.apache.commons.lang3.math.Fraction;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DrawContext.class)
public class ItemDurabilityDrawMixin {
    @Inject(method = "drawItemBar", at = @At("HEAD"), cancellable = true)
    private void customDrawItemBar(ItemStack stack, int x, int y, CallbackInfo ci){
        ci.cancel(); /// Unnecessary, can do ci.cancel() after checking, ignoring Horizontal
        DrawContext ctx = (DrawContext)(Object)this; /// Getting DrawContext for bar render
        TextRenderer tr = MinecraftClient.getInstance().textRenderer; /// For Percent and Text style
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.HORIZONTAL) {
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 13;
                ctx.fill(RenderPipelines.GUI, i, j, i + 13, j + 2, -16777216);
                ctx.fill(RenderPipelines.GUI, i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));
            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.VERTICAL){
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 2;
                ctx.fill(RenderPipelines.GUI, i, j, i + 1, j + 13, -16777216);
                ctx.fill(RenderPipelines.GUI, i, j - stack.getItemBarStep() + 13, i + 1, j + 13 , ColorHelper.fullAlpha(stack.getItemBarColor()));
                /// reverted from horizontal, decreasing from up to down
            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.PERCENT){
            if (stack.isItemBarVisible()) {
                Matrix3x2fStack matrices = ctx.getMatrices();
                matrices.pushMatrix();
                int i = x + 1;
                int j = y + 10;
                matrices.scaleAround(0.7f,i,j); /// no matrices.scale(0.5f) since origin point at left-upper corner of window/display.
                int max = stack.getMaxDamage();
                int dmg = stack.getDamage();
                int percent;
                if(stack.getItem().getClass() == BundleItem.class){
                    percent = (int)(((float)getNumeratorFromFraction(stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT).getOccupancy(),64)) / 64f * 100f);
                }
                else{
                    percent = (int) (((max - dmg) / (float) max) * 100);
                }
                String text = percent + "%";
                ctx.drawTextWithShadow(tr,text,i,j,ColorHelper.fullAlpha(stack.getItemBarColor()));
                matrices.popMatrix();
            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.ABSOLUTE){
            if (stack.isItemBarVisible()) {
                Matrix3x2fStack matrices = ctx.getMatrices();
                matrices.pushMatrix();
                int i = x + 1;
                int j = y + 10;
                String text;
                if(stack.getItem().getClass() == BundleItem.class){
                    text = Integer.toString(getNumeratorFromFraction(stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT).getOccupancy(),64));
                }
                else{
                    text = TextDurabilityText(stack.getMaxDamage(),stack.getDamage());
                }
                matrices.scaleAround(0.7f,i,j); /// same here
                ctx.drawTextWithShadow(tr,text,i,j,ColorHelper.fullAlpha(stack.getItemBarColor()));
                matrices.popMatrix();
            }
        }
    }

    private static String TextDurabilityText(int maxDamage,int damage){
        String text;
        int trueDurability = maxDamage - damage;
        text = Integer.toString(trueDurability);
        if(text.length()>=10){
            if(text.length()==10 && text.charAt(1) != 0 ){
                text = text.charAt(0) + "." + text.charAt(1) + "B";
            }
            else if(text.length()==10 && text.charAt(1) == 0 ){
                text = text.charAt(0) + "B";
            }
            else{
                text = trueDurability / 1000000000 + "B";
            }
        }
        else if (text.length()>=7) {
            if(text.length()==7 && text.charAt(1) != 0){
                text = text.charAt(0) + "." + text.charAt(1) + "m";
            }
            else if(text.length()==7 && text.charAt(1) == 0 ){
                text = text.charAt(0) + "m";
            }
            else{
                text = trueDurability / 1000000 + "m";
            }
        }
        else if (text.length()>=4) {
            if(text.length()==4 && text.charAt(1) != 0){
                text = text.charAt(0) + "." + text.charAt(1) + "k";
            }
            else if(text.length()==4 && text.charAt(1) == 0){
                text = text.charAt(0) + "k";
            }
            else{
                text = trueDurability / 1000 + "k";
            }
        }
        return text; /// Could be actually better, but who even need more than 2 billions
    }

    private static int getNumeratorFromFraction(Fraction ihateyou, int max){ /// Gets
        int numerator = ihateyou.getNumerator();
        int denominator = ihateyou.getDenominator();
        int moh = max / denominator;
        if(denominator != max){
            return numerator * moh;
        }
        else return numerator;
    }
}
