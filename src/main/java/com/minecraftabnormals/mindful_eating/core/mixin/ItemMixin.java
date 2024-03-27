package com.minecraftabnormals.mindful_eating.core.mixin;

import com.illusivesoulworks.diet.api.DietApi;
import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.minecraftabnormals.mindful_eating.compat.FarmersDelightCompat;
import com.minecraftabnormals.mindful_eating.core.ext.MindfulEatingPlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(at = @At("TAIL"), method = "finishUsingItem")
    protected void finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity, CallbackInfoReturnable<ItemStack> cir) {
        if (itemStack.isEdible() && livingEntity instanceof Player player) {
            ResourceLocation currentFood = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
            Set<IDietGroup> groups = DietApi.getInstance().getGroups(player, itemStack);

            if (!groups.isEmpty()) {
                ((MindfulEatingPlayer) player).mindful_eating$setLastFood(currentFood);
            }

            if (FabricLoader.getInstance().isModLoaded("farmersdelight") && FarmersDelightCompat.ENABLE_STACKABLE_SOUP_ITEMS && !(itemStack.getItem() instanceof SuspiciousStewItem))
                return;

            if (itemStack.getItem() instanceof BowlFoodItem || itemStack.getItem() instanceof SuspiciousStewItem) {
                itemStack.shrink(1);
                if (itemStack.isEmpty()) {
                    cir.setReturnValue(new ItemStack(Items.BOWL));
                } else {
                    if (!player.getAbilities().instabuild) {
                        ItemStack itemstack = new ItemStack(Items.BOWL);
                        if (!player.getInventory().add(itemstack)) {
                            player.drop(itemstack, false);
                        }
                    }

                    cir.setReturnValue(itemStack);
                }
            }
        }
    }
    
}
