package net.xeroniodir.cidb.client.mixin.client;
//? if >=1.21.6 {
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//?}

import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.client.gui.DrawContext;

import java.awt.Color;
import java.util.List;

import static net.xeroniodir.cidb.client.util.ItemMixinHelper.TextDurabilityText;
import static net.xeroniodir.cidb.client.util.ItemMixinHelper.getNumeratorFromFraction;

@Mixin(DrawContext.class)
public class ItemBarMixin {
    //? if >=1.21.6 {
    @Inject(method = "drawItemBar", at = @At("HEAD"), cancellable = true)
    private void customDrawItemBar(ItemStack stack, int x, int y, CallbackInfo ci) {
        ci.cancel();
        DrawContext ctx = (DrawContext) (Object) this;
        TextRenderer tr = MinecraftClient.getInstance().textRenderer;
        ModConfig cfg = ConfigManager.getLoaded();
        if (cfg.durabilityBarStyle == DurabilityBarStyleEnum.HORIZONTAL) {
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 13;
                ctx.fill(RenderPipelines.GUI, i, j, i + 13, j + 2, -16777216);
                if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.VANILLA  || cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW) {
                    ctx.fill(RenderPipelines.GUI, i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));
                } else if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.GRADIENT) {
                   List<Color> colors = ItemBarColor.getColorList(stack.getItem());
                    int step = stack.getItemBarStep();
                    int BAR_LEN = 13;
                    if (colors.size() >= 2 && step > 0) {
                        float segmentLen = BAR_LEN / (float) (colors.size() - 1);
                        Matrix3x2fStack matrices = ctx.getMatrices();
                        matrices.pushMatrix();
                        matrices.rotateAbout((float) 142 / 90, i, j);
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

                        matrices.popMatrix();
                    } else {
                        ctx.fill(RenderPipelines.GUI, i, j, i + stack.getItemBarStep(), j + 1, ColorHelper.fullAlpha(stack.getItemBarColor()));
                    }
                }
            }
        }
        if (cfg.durabilityBarStyle == DurabilityBarStyleEnum.VERTICAL) {
            if (stack.isItemBarVisible()) {
                int i = x + 2;
                int j = y + 2;
                ctx.fill(RenderPipelines.GUI, i, j, i + 1, j + 13, -16777216);
                if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.VANILLA || cfg.durabilityColorStyle == DurabilityColorStyleEnum.RAINBOW) {
                    ctx.fill(RenderPipelines.GUI, i, j - stack.getItemBarStep() + 13, i + 1, j + 13, ColorHelper.fullAlpha(stack.getItemBarColor()));
                } else if (cfg.durabilityColorStyle == DurabilityColorStyleEnum.GRADIENT) {
                    j += 13;
                    List<Color> colors = ItemBarColor.getColorList(stack.getItem());
                    int step = stack.getItemBarStep();
                    int BAR_LEN = 12;
                    if (colors.size() >= 2 && step > 0) {
                        float segmentLen = BAR_LEN / (float) (colors.size() - 1);
                        Matrix3x2fStack matrices = ctx.getMatrices();
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
                    } else {
                        ctx.fill(RenderPipelines.GUI, i, j - stack.getItemBarStep(), i + 1, j, ColorHelper.fullAlpha(stack.getItemBarColor()));
                    }
                }
            }
        }
        if (cfg.durabilityTextStyle == DurabilityTextStyleEnum.PERCENT) {
            if (stack.isItemBarVisible()) {
                Matrix3x2fStack matrices = ctx.getMatrices();
                matrices.pushMatrix();
                int i = x + 1;
                int j = y + 10;
                matrices.scaleAround(0.7f, i, j);
                int max = stack.getMaxDamage();
                int dmg = stack.getDamage();
                int percent;
                if (stack.getItem().getClass() == BundleItem.class) {
                    percent = (int) (((float) getNumeratorFromFraction(stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT).getOccupancy(), 64)) / 64f * 100f);
                } else {
                    percent = (int) (((max - dmg) / (float) max) * 100);
                }
                String text = percent + "%";
                ctx.drawTextWithShadow(tr, text, i, j, ColorHelper.fullAlpha(stack.getItemBarColor()));
                matrices.popMatrix();
            }
        }
        if (cfg.durabilityTextStyle == DurabilityTextStyleEnum.ABSOLUTE) {
            if (stack.isItemBarVisible()) {
                Matrix3x2fStack matrices = ctx.getMatrices();
                matrices.pushMatrix();
                int i = x + 1;
                int j = y + 10;
                String text;
                if (stack.getItem().getClass() == BundleItem.class) {
                    text = Integer.toString(getNumeratorFromFraction(stack.getOrDefault(DataComponentTypes.BUNDLE_CONTENTS, BundleContentsComponent.DEFAULT).getOccupancy(), 64));
                } else {
                    text = TextDurabilityText(stack.getMaxDamage(), stack.getDamage());
                }
                matrices.scaleAround(0.7f, i, j);
                ctx.drawTextWithShadow(tr, text, i, j, ColorHelper.fullAlpha(stack.getItemBarColor()));
                matrices.popMatrix();

            }
        }
    }
    //?}
}
