package net.xeroniodir.cidb.client.mixin.client;

import com.terraformersmc.modmenu.util.mod.Mod;
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
import net.minecraft.util.math.RotationAxis;
import net.xeroniodir.cidb.client.DurabilityBarStyleEnum;
import net.xeroniodir.cidb.client.DurabilityColorStyleEnum;
import net.xeroniodir.cidb.client.ModConfig;
import net.xeroniodir.cidb.client.config.ConfigManager;
import net.xeroniodir.cidb.client.util.ItemBarColor;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.Color;
import java.util.List;

@Mixin(DrawContext.class)
public class ItemDurabilityDrawMixin {
    //? if >=1.21.6 {
    @Inject(method = "drawItemBar", at = @At("HEAD"), cancellable = true)
    private void customDrawItemBar(ItemStack stack, int x, int y, CallbackInfo ci){
        ci.cancel();
        DrawContext ctx = (DrawContext)(Object)this;
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        ModConfig cfg = ConfigManager.getLoaded();
        if (cfg.durabilityBarStyle == DurabilityBarStyleEnum.HORIZONTAL) {
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 13;
                ctx.fill(RenderPipelines.GUI, i, j, i + 13, j + 2, -16777216);
                //? if >=1.21.6 {
                if(cfg.durabilityColorStyle == DurabilityColorStyleEnum.VANILLA || cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW){
                    ctx.fill(RenderPipelines.GUI, i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));}
                else if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.GRADIENT) {
                    List<Color> colors = ItemBarColor.getColorList(stack.getItem());
                    int step = stack.getItemBarStep();
                    int BAR_LEN = 13;
                    if (colors.size() >= 2 && step > 0) {
                        float segmentLen = BAR_LEN / (float)(colors.size() - 1);
                        Matrix3x2fStack matrices = ctx.getMatrices();
                        matrices.pushMatrix();
                        matrices.rotateAbout((float) 142 / 90, i, j);
                        for (int s = 0; s < colors.size() - 1; s++) {
                            float segStart = s * segmentLen;
                            float segEnd   = segStart + segmentLen;

                            float visibleStart = segStart;
                            float visibleEnd   = Math.min(segEnd, step);

                            if (visibleStart >= visibleEnd)
                                continue;
                            int c0 = ColorHelper.fullAlpha(colors.get(s).getRGB());
                            int c1 = ColorHelper.fullAlpha(colors.get(s + 1).getRGB());

                            float segLen = segEnd - segStart;

                            float t0 = (visibleStart - segStart) / segLen;
                            float t1 = (visibleEnd   - segStart) / segLen;
                            int gradStart = ItemBarColor.lerpColor(c0, c1, t0);
                            int gradEnd   = ItemBarColor.lerpColor(c0, c1, t1);

                            ctx.fillGradient(
                                    i,
                                    (int)(j - visibleEnd),
                                    i + 1,
                                    (int)(j - visibleStart),
                                    gradEnd,    // порядок оставляем как ты указал
                                    gradStart
                            );
                        }

                        matrices.popMatrix();
                    }
                    else{
                        ctx.fill(RenderPipelines.GUI, i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));
                    }
                }
                //?}
            }
        }
        if (cfg.durabilityBarStyle == DurabilityBarStyleEnum.VERTICAL){
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 2;
                //? if >=1.21.6 {
                ctx.fill(RenderPipelines.GUI, i, j, i + 1, j + 13, -16777216);
                if(cfg.durabilityColorStyle == DurabilityColorStyleEnum.VANILLA || cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW) {
                    ctx.fill(RenderPipelines.GUI, i, j - stack.getItemBarStep() + 13, i + 1, j + 13, ColorHelper.fullAlpha(stack.getItemBarColor()));
                }
                else if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.GRADIENT) {
                    j += 13;
                    List<Color> colors = ItemBarColor.getColorList(stack.getItem());
                    int step = stack.getItemBarStep();
                    int BAR_LEN = 13;
                    if (colors.size() >= 2 && step > 0) {
                        float segmentLen = BAR_LEN / (float)(colors.size() - 1);
                        Matrix3x2fStack matrices = ctx.getMatrices();
                        for (int s = 0; s < colors.size() - 1; s++) {
                            float segStart = s * segmentLen;
                            float segEnd   = segStart + segmentLen;

                            float visibleStart = segStart;
                            float visibleEnd   = Math.min(segEnd, step);

                            if (visibleStart >= visibleEnd)
                                continue;
                            int c0 = ColorHelper.fullAlpha(colors.get(s).getRGB());
                            int c1 = ColorHelper.fullAlpha(colors.get(s + 1).getRGB());

                            float segLen = segEnd - segStart;

                            float t0 = (visibleStart - segStart) / segLen;
                            float t1 = (visibleEnd   - segStart) / segLen;
                            int gradStart = ItemBarColor.lerpColor(c0, c1, t0);
                            int gradEnd   = ItemBarColor.lerpColor(c0, c1, t1);

                            ctx.fillGradient(
                                    i,
                                    (int)(j - visibleEnd),
                                    i + 1,
                                    (int)(j - visibleStart),
                                    gradEnd,    // порядок оставляем как ты указал
                                    gradStart
                            );
                        }
                    }
                    else{
                        ctx.fill(RenderPipelines.GUI, i, j - stack.getItemBarStep(), i + 1, j, ColorHelper.fullAlpha(stack.getItemBarColor()));
                    }
                }
                //?}
            }
        }
        if (cfg.durabilityBarStyle == DurabilityBarStyleEnum.PERCENT){
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
        if (cfg.durabilityBarStyle == DurabilityBarStyleEnum.ABSOLUTE){
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
        ModConfig cfg = ConfigManager.getLoaded();
        if (!stack.isItemBarVisible()) return;
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.HORIZONTAL) {
            int i = x + 2;
            int j = y + 13;
            ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + 13, j + 2, -16777216);
            if(cfg.durabilityColorStyle == DurabilityColorStyleEnum.VANILLA || cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW){
                if (stack.isItemBarVisible()) {
                    ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));

                }
            }
            else if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.GRADIENT) {
                List<Color> colors = ItemBarColor.getColorList(stack.getItem());
                int step = stack.getItemBarStep();
                int BAR_LEN = 13;
                if (colors.size() >= 2 && step > 0) {
                    float segmentLen = BAR_LEN / (float)(colors.size() - 1);
                    MatrixStack matrices = ctx.getMatrices();
                    matrices.push();
                    matrices.translate(i, j, 0);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) 142 / 90));
                    matrices.translate(-i, -j, 301.0F);
                    for (int s = 0; s < colors.size() - 1; s++) {
                        float segStart = s * segmentLen;
                        float segEnd   = segStart + segmentLen;

                        float visibleStart = segStart;
                        float visibleEnd   = Math.min(segEnd, step);

                        if (visibleStart >= visibleEnd)
                            continue;
                        int c0 = ColorHelper.fullAlpha(colors.get(s).getRGB());
                        int c1 = ColorHelper.fullAlpha(colors.get(s + 1).getRGB());

                        float segLen = segEnd - segStart;

                        float t0 = (visibleStart - segStart) / segLen;
                        float t1 = (visibleEnd   - segStart) / segLen;
                        int gradStart = ItemBarColor.lerpColor(c0, c1, t0);
                        int gradEnd   = ItemBarColor.lerpColor(c0, c1, t1);

                        ctx.fillGradient(
                                i,
                                (int)(j - visibleEnd),
                                i + 1,
                                (int)(j - visibleStart),
                                gradEnd,
                                gradStart
                        );
                    }

                    matrices.pop();
                }
                else{
                    ctx.fill(RenderLayer.getGuiOverlay(), i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));
                }
            }
        }
        if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.VERTICAL){
            int i = x + 2;
            int j = y + 2;
            ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + 1, j + 13, -16777216);
            if(cfg.durabilityColorStyle == DurabilityColorStyleEnum.VANILLA || cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW){
                if (stack.isItemBarVisible()) {
                    ctx.fill(RenderLayer.getGuiOverlay(),i, j - stack.getItemBarStep() + 13, i + 1, j + 13 , ColorHelper.fullAlpha(stack.getItemBarColor()));
                }
            }
            else if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.GRADIENT) {
                List<Color> colors = ItemBarColor.getColorList(stack.getItem());
                int step = stack.getItemBarStep();
                int BAR_LEN = 13;
                if (colors.size() >= 2 && step > 0) {
                    j += 13;
                    float segmentLen = BAR_LEN / (float)(colors.size() - 1);
                    for (int s = 0; s < colors.size() - 1; s++) {
                        MatrixStack matrices = ctx.getMatrices();
                        matrices.push();
                        matrices.translate(0, 0, 301.0f);
                        float segStart = s * segmentLen;
                        float segEnd   = segStart + segmentLen;

                        float visibleStart = segStart;
                        float visibleEnd   = Math.min(segEnd, step);

                        if (visibleStart >= visibleEnd)
                            continue;
                        int c0 = ColorHelper.fullAlpha(colors.get(s).getRGB());
                        int c1 = ColorHelper.fullAlpha(colors.get(s + 1).getRGB());

                        float segLen = segEnd - segStart;

                        float t0 = (visibleStart - segStart) / segLen;
                        float t1 = (visibleEnd   - segStart) / segLen;
                        int gradStart = ItemBarColor.lerpColor(c0, c1, t0);
                        int gradEnd   = ItemBarColor.lerpColor(c0, c1, t1);

                        ctx.fillGradient(
                                i,
                                (int)(j - visibleEnd),
                                i + 1,
                                (int)(j - visibleStart),
                                gradEnd,
                                gradStart
                        );
                        matrices.pop();
                    }
                }
                else{
                    ctx.fill(RenderLayer.getGuiOverlay(), i, j - stack.getItemBarStep(), i + 1, j + 13, ColorHelper.fullAlpha(stack.getItemBarColor()));
                }
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
        ModConfig cfg = ConfigManager.getLoaded();
        ctx.getMatrices().push();
        if (stack.getCount() != 1 || stackCountText != null) {
            String string = stackCountText == null ? String.valueOf(stack.getCount()) : stackCountText;
            ctx.getMatrices().translate(0.0F, 0.0F, 301.0F);
            ctx.drawText(textRenderer, string, x + 19 - 2 - textRenderer.getWidth(string), y + 6 + 3, 16777215, true);
        }
        if(stack.isItemBarVisible()){
            if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.HORIZONTAL) {
                int i = x + 2;
                int j = y + 13;
                ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + 13, j + 2, -16777216);
                if(cfg.durabilityColorStyle == DurabilityColorStyleEnum.VANILLA || cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW){
                    if (stack.isItemBarVisible()) {
                        ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + stack.getItemBarStep(), j + 1, new Color(stack.getItemBarColor(),false).getRGB());

                    }
                }
                else if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.GRADIENT) {
                    List<Color> colors = ItemBarColor.getColorList(stack.getItem());
                    int step = stack.getItemBarStep();
                    int BAR_LEN = 13;
                    if (colors.size() >= 2 && step > 0) {
                        float segmentLen = BAR_LEN / (float)(colors.size() - 1);
                        MatrixStack matrices = ctx.getMatrices();
                        matrices.push();
                        matrices.translate(i, j, 0);
                        matrices.multiply(RotationAxis.POSITIVE_Z.rotation((float) 142 / 90));
                        matrices.translate(-i, -j, 301.0f);
                        for (int s = 0; s < colors.size() - 1; s++) {
                            float segStart = s * segmentLen;
                            float segEnd   = segStart + segmentLen;

                            float visibleStart = segStart;
                            float visibleEnd   = Math.min(segEnd, step);

                            if (visibleStart >= visibleEnd)
                                continue;
                            int c0 = new Color(colors.get(s).getRGB(),false).getRGB();
                            int c1 = new Color(colors.get(s + 1).getRGB(),false).getRGB();

                            float segLen = segEnd - segStart;

                            float t0 = (visibleStart - segStart) / segLen;
                            float t1 = (visibleEnd   - segStart) / segLen;
                            int gradStart = ItemBarColor.lerpColor(c0, c1, t0);
                            int gradEnd   = ItemBarColor.lerpColor(c0, c1, t1);

                            ctx.fillGradient(
                                    i,
                                    (int)(j - visibleEnd),
                                    i + 1,
                                    (int)(j - visibleStart),
                                    gradEnd,
                                    gradStart
                            );
                        }

                        matrices.pop();
                    }
                    else{
                        ctx.fill(RenderLayer.getGuiOverlay(), i, j, i + stack.getItemBarStep(), j + 1, new Color(stack.getItemBarColor(),false).getRGB());
                    }
                }
            }
            if (ConfigManager.getLoaded().durabilityBarStyle == DurabilityBarStyleEnum.VERTICAL){
                int i = x + 2;
                int j = y + 2;
                ctx.fill(RenderLayer.getGuiOverlay(),i, j, i + 1, j + 13, -16777216);
                if(cfg.durabilityColorStyle == DurabilityColorStyleEnum.VANILLA || cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW){
                    if (stack.isItemBarVisible()) {
                        ctx.fill(RenderLayer.getGuiOverlay(),i, j - stack.getItemBarStep() + 13, i + 1, j + 13 , new Color(stack.getItemBarColor(),false).getRGB());
                    }
                }
                else if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.GRADIENT) {
                    List<Color> colors = ItemBarColor.getColorList(stack.getItem());
                    int step = stack.getItemBarStep();
                    int BAR_LEN = 13;
                    if (colors.size() >= 2 && step > 0) {
                        j += 13;
                        float segmentLen = BAR_LEN / (float)(colors.size() - 1);
                        for (int s = 0; s < colors.size() - 1; s++) {
                            MatrixStack matrices = ctx.getMatrices();
                            matrices.push();
                            matrices.translate(0,0, 301.0f);
                            float segStart = s * segmentLen;
                            float segEnd   = segStart + segmentLen;

                            float visibleStart = segStart;
                            float visibleEnd   = Math.min(segEnd, step);

                            if (visibleStart >= visibleEnd)
                                continue;
                            int c0 = new Color(colors.get(s).getRGB(),false).getRGB();
                            int c1 = new Color(colors.get(s + 1).getRGB(),false).getRGB();

                            float segLen = segEnd - segStart;

                            float t0 = (visibleStart - segStart) / segLen;
                            float t1 = (visibleEnd   - segStart) / segLen;
                            int gradStart = ItemBarColor.lerpColor(c0, c1, t0);
                            int gradEnd   = ItemBarColor.lerpColor(c0, c1, t1);

                            ctx.fillGradient(
                                    i,
                                    (int)(j - visibleEnd),
                                    i + 1,
                                    (int)(j - visibleStart),
                                    gradEnd,    // порядок оставляем как ты указал
                                    gradStart
                            );
                            matrices.pop();
                        }
                    }
                    else{
                        ctx.fill(RenderLayer.getGuiOverlay(), i, j - stack.getItemBarStep(), i + 1, j + 13, new Color(stack.getItemBarColor(),false).getRGB());
                    }
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
