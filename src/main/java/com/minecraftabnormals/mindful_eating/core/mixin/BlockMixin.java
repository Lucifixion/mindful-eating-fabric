package com.minecraftabnormals.mindful_eating.core.mixin;

import com.minecraftabnormals.mindful_eating.core.ExhaustionSource;
import com.minecraftabnormals.mindful_eating.core.registry.other.MEEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(at = @At("TAIL"), method = "playerWillDestroy")
    protected void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player, CallbackInfo ci) {
        MEEvents.exhaustionReductionShortSheen(player, ExhaustionSource.MINE);
    }

    @Inject(at = @At("TAIL"), method = "playerDestroy")
    protected void playerDestroy(Level level, Player player, BlockPos blockPos, BlockState blockState, BlockEntity blockEntity, ItemStack itemStack, CallbackInfo ci) {
        float ratio = MEEvents.exhaustionReductionLongSheen(player, ExhaustionSource.MINE);
        player.causeFoodExhaustion(0.005F * ratio);
    }
}
