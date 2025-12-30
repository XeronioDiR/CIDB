package net.xeroniodir.cidb.client.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
//? if >=1.21.6
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
//? if >=1.21 <=1.21.5 {
/*import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
*///?}
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BundleContentsComponent;
import net.minecraft.item.BundleItem;
import net.minecraft.item.ItemStack;
//? if >=1.21.2
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.xeroniodir.cidb.client.DurabilityBarStyleEnum;
import net.xeroniodir.cidb.client.ModConfig;
import net.xeroniodir.cidb.client.config.ConfigManager;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(DrawContext.class)
public class ItemDurabilityDrawMixin {
    //? if >=1.21.6 {
    @Inject(method = "drawItemBar", at = @At("HEAD"), cancellable = true)
    private void customDrawItemBar(ItemStack stack, int x, int y, CallbackInfo ci){
        ci.cancel();
        DrawContext ctx = (DrawContext)(Object)this;
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.HORIZONTAL) {
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 13;
                //? if >=1.21.6 {
                ctx.fill(RenderPipelines.GUI, i, j, i + 13, j + 2, -16777216);
                ctx.fill(RenderPipelines.GUI, i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));
                //?} else if 1.21.5 {
                /*ctx.fill(RenderLayer.getGui(),i, j, i + 13, j + 2, -16777216);
                ctx.fill(RenderLayer.getGui(),i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));
                *///?}

            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.VERTICAL){
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 2;
                //? if >=1.21.6 {
                ctx.fill(RenderPipelines.GUI, i, j, i + 1, j + 13, -16777216);
                ctx.fill(RenderPipelines.GUI, i, j - stack.getItemBarStep() + 13, i + 1, j + 13 , ColorHelper.fullAlpha(stack.getItemBarColor()));
                //?} else if 1.21.5 {
                /*ctx.fill(RenderLayer.getGui(),i, j, i + 1, j + 13, -16777216);
                ctx.fill(RenderLayer.getGui(),i, j - stack.getItemBarStep() + 13, i + 1, j + 13 , ColorHelper.fullAlpha(stack.getItemBarColor()));
                *///?}
            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.PERCENT){
            if (stack.isItemBarVisible()) {
                //? if >= 1.21.6 {
                Matrix3x2fStack matrices = ctx.getMatrices();
                matrices.pushMatrix();
                //?} else if 1.21.5 {
                /*MatrixStack matrices = ctx.getMatrices();
                matrices.push();
                *///?}
                int i = x + 1;
                int j = y + 10;
                //? if >= 1.21.6 {
                matrices.scaleAround(0.7f,i,j);
                 //?} else if 1.21.5 {
                /*matrices.peek().getPositionMatrix().scaleAround(0.7f,i,j,1);
                *///?}
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
                //? if >= 1.21.6 {
                matrices.popMatrix();
                 //?} else if 1.21.5 {
                /*matrices.pop();
                *///?}
            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.ABSOLUTE){
            if (stack.isItemBarVisible()) {
                //? if >= 1.21.6 {
                Matrix3x2fStack matrices = ctx.getMatrices();
                matrices.pushMatrix();
                //?} else if >=1.21.2 {
                /*MatrixStack matrices = ctx.getMatrices();
                matrices.push();
                *///?}
                int i = x + 1;
                int j = y + 10;
                String text;
                if(stack.getItem().getClass() == BundleItem.class){
                    text = Integer.toString(getNumeratorFromFraction(stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT).getOccupancy(),64));
                }
                else{
                    text = TextDurabilityText(stack.getMaxDamage(),stack.getDamage());
                }
                //? if >= 1.21.6 {
                matrices.scaleAround(0.7f,i,j);
                //?} else if 1.21.5 {
                /*matrices.peek().getPositionMatrix().scaleAround(0.7f,i,j,1);
                *///?}
                ctx.drawTextWithShadow(tr,text,i,j,ColorHelper.fullAlpha(stack.getItemBarColor()));
                //? if >= 1.21.6 {
                matrices.popMatrix();
                 //?} else if 1.21.5 {
                /*matrices.pop();
                *///?}

            }
        }
    }
    //?} else if >=1.21.2 {
    /*@Inject(at = @At("HEAD"), method="drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")
    private void customItemBar(TextRenderer textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci){
        DrawContext ctx = (DrawContext)(Object)this;
        TextRenderer tr = textRenderer;
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.HORIZONTAL) {
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 13;
                ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + 13, j + 2, -16777216);
                ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));

            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.VERTICAL){
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 2;
                ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + 1, j + 13, -16777216);
                ctx.fill(RenderLayer.getGuiOverlay(),i, j - stack.getItemBarStep() + 13, i + 1, j + 13 , ColorHelper.fullAlpha(stack.getItemBarColor()));
            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.PERCENT){
            if (stack.isItemBarVisible()) {
                MatrixStack matrices = ctx.getMatrices();
                matrices.push();
                int i = x + 1;
                int j = y + 10;
                matrices.peek().getPositionMatrix().scaleAround(0.7f,i,j,1);
                matrices.translate(0,0,301.0F);
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
                matrices.pop();
            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.ABSOLUTE){
            if (stack.isItemBarVisible()) {
                MatrixStack matrices = ctx.getMatrices();
                matrices.push();
                int i = x + 1;
                int j = y + 10;
                String text;
                if(stack.getItem().getClass() == BundleItem.class){
                    text = Integer.toString(getNumeratorFromFraction(stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT).getOccupancy(),64));
                }
                else{
                    text = TextDurabilityText(stack.getMaxDamage(),stack.getDamage());
                }
                matrices.peek().getPositionMatrix().scaleAround(0.7f,i,j,100);
                matrices.translate(0,0,301.0F);
                ctx.drawTextWithShadow(tr,text,i,j,ColorHelper.fullAlpha(stack.getItemBarColor()));
                matrices.pop();

            }
        }
    }
    @Inject(method = "drawItemBar", at = @At("HEAD"), cancellable = true)
    private void renderItemBar(ItemStack stack, int x, int y, CallbackInfo ci){
        ci.cancel();
    }
    *///?} else if >=1.21 {
    /*@Inject(at = @At("HEAD"), method="drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V",cancellable = true)
    private void customItemBar(TextRenderer textRenderer, ItemStack stack, int x, int y, String stackCountText, CallbackInfo ci){
        ci.cancel();
        if (stack.isEmpty()) return;
        DrawContext ctx = (DrawContext)(Object)this;
        TextRenderer tr = textRenderer;
        ctx.getMatrices().push();
        if (stack.getCount() != 1 || stackCountText != null) {
            String string = stackCountText == null ? String.valueOf(stack.getCount()) : stackCountText;
            ctx.getMatrices().translate(0.0F, 0.0F, 200.0F);
            ctx.drawText(textRenderer, string, x + 19 - 2 - textRenderer.getWidth(string), y + 6 + 3, 16777215, true);
        }
        if(stack.isItemBarVisible()){
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.HORIZONTAL) {
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 13;
                ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + 13, j + 2, -16777216);
                ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + stack.getItemBarStep(), j + 1, new Color(stack.getItemBarColor(),false).getRGB());

            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.VERTICAL){
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 2;
                ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + 1, j + 13, -16777216);
                ctx.fill(RenderLayer.getGuiOverlay(),i, j - stack.getItemBarStep() + 13, i + 1, j + 13 , new Color(stack.getItemBarColor(),false).getRGB());
            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.PERCENT){
            if (stack.isItemBarVisible()) {
                MatrixStack matrices = ctx.getMatrices();
                matrices.push();
                int i = x + 1;
                int j = y + 10;
                matrices.peek().getPositionMatrix().scaleAround(0.7f,i,j,1);
                matrices.translate(0,0,301.0F);
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
                ctx.drawTextWithShadow(tr,text,i,j,stack.getItemBarColor());
                matrices.pop();
            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.ABSOLUTE){
            if (stack.isItemBarVisible()) {
                MatrixStack matrices = ctx.getMatrices();
                matrices.push();
                int i = x + 1;
                int j = y + 10;
                String text;
                if(stack.getItem().getClass() == BundleItem.class){
                    text = Integer.toString(getNumeratorFromFraction(stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT).getOccupancy(),64));
                }
                else{
                    text = TextDurabilityText(stack.getMaxDamage(),stack.getDamage());
                }
                matrices.peek().getPositionMatrix().scaleAround(0.7f,i,j,100);
                matrices.translate(0,0,301.0F);
                ctx.drawTextWithShadow(tr,text,i,j,stack.getItemBarColor());
                matrices.pop();

            }
        }}

        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        float f = clientPlayerEntity == null ? 0.0F : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack.getItem(), MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
        if (f > 0.0F) {
            int k = y + MathHelper.floor(16.0F * (1.0F - f));
            int l = k + MathHelper.ceil(16.0F * f);
            ctx.fill(RenderLayer.getGuiOverlay(), x, k, x + 16, l, Integer.MAX_VALUE);
        }

        ctx.getMatrices().pop();
    }
    *///?}
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
        return text;
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
