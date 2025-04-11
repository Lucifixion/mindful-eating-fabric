package com.minecraftabnormals.mindful_eating.core.mixin.farmersdelight;

import com.minecraftabnormals.mindful_eating.compat.FarmersDelightCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(vectorwing.farmersdelight.common.block.PieBlock.class)
public class PieBlockMixin {

    @Inject(method = "use", at = @At(value = "HEAD"))
    protected void eat(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        FarmersDelightCompat.pieEatenCheck(blockState.getBlock(), player, player.getItemInHand(interactionHand));
    }

}
