package com.minecraftabnormals.mindful_eating.compat;

import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.minecraftabnormals.mindful_eating.core.MindfulEatingFabric;
import com.minecraftabnormals.mindful_eating.core.ext.MindfulEatingPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import vectorwing.farmersdelight.common.Configuration;
import vectorwing.farmersdelight.common.block.PieBlock;
import vectorwing.farmersdelight.common.tag.ModTags;

import java.util.Set;

public class FarmersDelightCompat {

    public static boolean NOURISHED_HUNGER_OVERLAY = Configuration.NOURISHED_HUNGER_OVERLAY.get();
    public static boolean ENABLE_STACKABLE_SOUP_ITEMS = Configuration.ENABLE_STACKABLE_SOUP_ITEMS.get();

    public static void cakeEatenCheck(Block block, Player player, ItemStack heldItem) {
        if (block instanceof CakeBlock) {
            Set<IDietGroup> groups = MindfulEatingFabric.DIET_API.getGroups(player, new ItemStack(block));
            if (player.getFoodData().needsFood() && !groups.isEmpty() && !heldItem.is(ModTags.KNIVES)) {
                ResourceLocation currentFood = BuiltInRegistries.ITEM.getKey(block.asItem());
                ((MindfulEatingPlayer) player).mindful_eating$setLastFood(currentFood);
            }
        }
    }

    public static void pieEatenCheck(Block block, Player player, ItemStack heldItem) {
        if (block instanceof PieBlock) {
            Set<IDietGroup> groups = MindfulEatingFabric.DIET_API.getGroups(player, new ItemStack(block));
            if (player.getFoodData().needsFood() && !groups.isEmpty() && !heldItem.is(ModTags.KNIVES)) {
                ResourceLocation currentFood = BuiltInRegistries.ITEM.getKey(block.asItem());
                ((MindfulEatingPlayer) player).mindful_eating$setLastFood(currentFood);
            }
        }
    }
}
