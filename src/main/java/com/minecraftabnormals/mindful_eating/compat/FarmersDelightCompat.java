package com.minecraftabnormals.mindful_eating.compat;

//import com.minecraftabnormals.mindful_eating.core.MindfulEating;
//import com.teamabnormals.blueprint.common.world.storage.tracking.IDataManager;
//import com.teamabnormals.blueprint.core.util.TagUtil;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.block.Block;
//import top.theillusivec4.diet.api.DietApi;
//import top.theillusivec4.diet.api.IDietGroup;
//import vectorwing.farmersdelight.common.Configuration;
//import vectorwing.farmersdelight.common.block.PieBlock;
//
//import java.util.Set;

import com.illusivesoulworks.diet.api.DietApi;
import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.minecraftabnormals.mindful_eating.core.ext.MindfulEatingPlayer;
import com.nhoryzon.mc.farmersdelight.Configuration;
import com.nhoryzon.mc.farmersdelight.FarmersDelightMod;
import com.nhoryzon.mc.farmersdelight.block.PieBlock;
import com.nhoryzon.mc.farmersdelight.registry.TagsRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class FarmersDelightCompat {

    public static boolean ENABLE_STACKABLE_SOUP_ITEMS = FarmersDelightMod.CONFIG.isEnableStackableSoupSize();
    public static boolean NOURISHED_HUNGER_OVERLAY = FarmersDelightMod.CONFIG.isNourishedHungerOverlay();

    public static boolean TEMPORARY_NOURISHED_HUNGER_OVERLAY = NOURISHED_HUNGER_OVERLAY;
    public static void setNourishedHungerOverlay(boolean flag) {
        TEMPORARY_NOURISHED_HUNGER_OVERLAY = NOURISHED_HUNGER_OVERLAY;
        FarmersDelightMod.CONFIG.setNourishedHungerOverlay(flag);
    }

    public static void resetNourishedHungerOverlay() {
        NOURISHED_HUNGER_OVERLAY = TEMPORARY_NOURISHED_HUNGER_OVERLAY;
    }

    public static void pieEatenCheck(Block block, Player player, ItemStack heldItem) {
        if (block instanceof PieBlock) {
            Set<IDietGroup> groups = DietApi.getInstance().getGroups(player, new ItemStack(block));
            if (player.getFoodData().needsFood() && !groups.isEmpty() && !heldItem.is(TagsRegistry.KNIVES)) {
                ResourceLocation currentFood = BuiltInRegistries.ITEM.getKey(block.asItem());
                ((MindfulEatingPlayer) player).mindful_eating$setLastFood(currentFood);
            }
        }
    }
}
