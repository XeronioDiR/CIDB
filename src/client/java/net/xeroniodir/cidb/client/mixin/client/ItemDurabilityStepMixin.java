package net.xeroniodir.cidb.client.mixin.client;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.xeroniodir.cidb.client.ModConfig;
import net.xeroniodir.cidb.client.config.ConfigManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemDurabilityStepMixin {
    @Inject(method = "getItemBarStep", at = @At("HEAD"), cancellable = true)
    private void injectBarStep(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.getMaxDamage() <= 0) {
            cir.setReturnValue(0);
            return;
        }

        boolean durBarLenOnCrit = ConfigManager.getLoaded().durabilityBarLengthOnCritical;
        int procent = ConfigManager.getLoaded().durabiltiyProcent;

        float damage = stack.getDamage();
        float max = stack.getMaxDamage();
        float pct = damage / max;

        int step;

        if(durBarLenOnCrit){
        if (pct <= 1f - 0.01f * procent) {
            float normalized = pct / (1f - 0.01f * procent);
            step = (int)((1 - normalized) * 13f);
        } else {
            float normalized = (pct - (1f - 0.01f * procent)) / (0.01f * procent);
            step = (int)((1 - normalized) * 13f);
        }

        if (step < 0) step = 0;
        if (step > 13) step = 13;

        cir.setReturnValue(step);}
        /// i tried adding more precise steps, but it didn't work
        /// but bar will not be going further back or forward this mixin, so it's good.
    }
}
