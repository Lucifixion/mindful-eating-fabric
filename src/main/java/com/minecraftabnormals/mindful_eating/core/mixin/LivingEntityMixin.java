package com.minecraftabnormals.mindful_eating.core.mixin;

import com.minecraftabnormals.mindful_eating.core.ExhaustionSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.minecraftabnormals.mindful_eating.core.registry.other.MEEvents.exhaustionReductionLongSheen;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(at = @At("TAIL"), method = "heal")
    protected void heal(float f, CallbackInfo ci) {
        if((((LivingEntity) (Object) this)) instanceof Player player) {
            float ratio = exhaustionReductionLongSheen(player, ExhaustionSource.HEAL);
            player.causeFoodExhaustion(6.0F * f * ratio);
        }
    }

}
