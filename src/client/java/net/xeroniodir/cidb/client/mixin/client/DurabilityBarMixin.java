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
import net.xeroniodir.cidb.client.enums.DurabilityBarStyleEnum;
import net.xeroniodir.cidb.client.enums.DurabilityColorStyleEnum;
import net.xeroniodir.cidb.client.enums.DurabilityTextStyleEnum;
import net.xeroniodir.cidb.client.ModConfig;
import net.xeroniodir.cidb.client.config.ConfigManager;
import net.xeroniodir.cidb.client.util.ItemBarColor;
import org.joml.Matrix3x2fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;

import static net.xeroniodir.cidb.client.util.ItemBarColor.getBarColor;
import static net.xeroniodir.cidb.client.util.ItemMixinHelper.TextDurabilityText;
import static net.xeroniodir.cidb.client.util.ItemMixinHelper.getNumeratorFromFraction;

@Mixin(DrawContext.class)
public class DurabilityBarMixin {

    //? if >=1.21.6 {
    @Inject(method = "drawItemBar", at = @At("HEAD"), cancellable = true)
    private void customDrawItemBar(ItemStack stack, int x, int y, CallbackInfo ci) {
        ci.cancel();
        DrawContext ctx = (DrawContext) (Object) this;
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        ModConfig cfg = ConfigManager.getLoaded();
        if(cfg.durabilityBarStyle != DurabilityBarStyleEnum.NONE){
            drawLineBar(stack,x,y,ctx,cfg);
        }
        if(cfg.durabilityTextStyle != DurabilityTextStyleEnum.NONE){
            drawTextBar(stack,x,y,ctx,cfg,tr);
        }
    }
    //?}

    @Unique
    public void drawLineBar(ItemStack stack, int x, int y, DrawContext ctx, ModConfig cfg){
        if (stack.isItemBarVisible()) {
            boolean HOR = cfg.durabilityBarStyle == DurabilityBarStyleEnum.HORIZONTAL;
            int i = x + 2;
            int j = HOR ? y + 13 : y + 2;
            ctx.fill(RenderPipelines.GUI, i, j, i + (HOR ? 13 : 1),
                    j + (HOR ? 2 : 13),
                    -16777216);
            if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.VANILLA || cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW) {
                int itemBarColor = getBarColor(stack);
                if(HOR)ctx.fill(RenderPipelines.GUI, i, j, i + stack.getItemBarStep(), j + 1, itemBarColor);
                else ctx.fill(RenderPipelines.GUI, i, j - stack.getItemBarStep() + 13, i + 1, j + 13, itemBarColor);
            } else if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.GRADIENT) {
                List<Color> colors = ItemBarColor.getColorList(stack.getItem()).reversed();
                int step = stack.getItemBarStep();
                int BAR_LEN = 13;
                if (colors.size() >= 2 && step > 0) {
                    if(!HOR)j+=13;
                    float segmentLen = BAR_LEN / (float) (colors.size() - 1);
                    Matrix3x2fStack matrices = ctx.getMatrices();
                    if(HOR){
                    matrices.pushMatrix();
                    matrices.rotateAbout((float) 142 / 90, i, j);}
                    for (int s = 0; s < colors.size() - 1; s++) {
                        float segStart = s * segmentLen;
                        float segEnd = segStart + segmentLen;

                        float visibleStart = segStart;
                        float visibleEnd = Math.min(segEnd, step);

                        if (visibleStart >= visibleEnd)
                            continue;
                        int c0 = ColorHelper.fullAlpha(colors.get(s).getRGB());
                        int c1 = ColorHelper.fullAlpha(colors.get(s + 1).getRGB());

                        float segLen = segEnd - segStart;

                        float t0 = (visibleStart - segStart) / segLen;
                        float t1 = (visibleEnd - segStart) / segLen;
                        int gradStart = ItemBarColor.lerpColor(c0, c1, t0);
                        int gradEnd = ItemBarColor.lerpColor(c0, c1, t1);

                        ctx.fillGradient(
                                i,
                                (int) (j - visibleEnd),
                                i + 1,
                                (int) (j - visibleStart),
                                gradEnd,
                                gradStart
                        );
                    }

                    if(HOR) matrices.popMatrix();
                } else {
                    int itemBarColor = getBarColor(stack);
                    if(HOR)ctx.fill(RenderPipelines.GUI, i, j, i + stack.getItemBarStep(), j + 1, itemBarColor);
                    else ctx.fill(RenderPipelines.GUI, i, j - stack.getItemBarStep() + 13, i + 1, j + 13, itemBarColor);
                }
            }
        }
    }

    @Unique
    public void drawTextBar(ItemStack stack, int x, int y, DrawContext ctx, ModConfig cfg, TextRenderer tr){
        boolean PER = cfg.durabilityTextStyle == DurabilityTextStyleEnum.PERCENT;
        if (stack.isItemBarVisible()) {
            Matrix3x2fStack matrices = ctx.getMatrices();
            matrices.pushMatrix();
            int i = x + 1;
            int j = y + 10;
            if (cfg.durabilityBarStyle == DurabilityBarStyleEnum.HORIZONTAL) j -= 4;
            if (cfg.durabilityBarStyle == DurabilityBarStyleEnum.VERTICAL) i += 4;
            matrices.scaleAround(0.7f, i + 8, j);
            String text;
            if (stack.getItem() instanceof BundleItem) {
                text = Float.toString( ((float) getNumeratorFromFraction(stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS,
                        BundleContentsComponent.DEFAULT).getOccupancy(), 64)) / (PER ? 64f * 100f : 1f)) ;
            } else {
                int max = stack.getMaxDamage();
                int dmg = stack.getDamage();
                text = PER ? (int) (((max - dmg) / (float) max) * 100) + "%" : TextDurabilityText(stack.getMaxDamage(), stack.getDamage());
            }
            ctx.drawTextWithShadow(tr, text, i, j, getBarColor(stack));
            matrices.popMatrix();
        }
    }
}
