package com.minecraftabnormals.mindful_eating.client;

import com.illusivesoulworks.diet.api.DietApi;
import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.minecraftabnormals.mindful_eating.compat.FarmersDelightCompat;
import com.minecraftabnormals.mindful_eating.core.MindfulEatingFabric;
import com.minecraftabnormals.mindful_eating.core.ext.MindfulEatingPlayer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;

import java.util.Random;
import java.util.Set;

public class HungerOverlay {

    public static final ResourceLocation GUI_HUNGER_ICONS_LOCATION = new ResourceLocation(MindfulEatingFabric.MOD_ID, "textures/gui/hunger_icons.png");
    public static final ResourceLocation GUI_NOURISHMENT_ICONS_LOCATION = new ResourceLocation(MindfulEatingFabric.MOD_ID, "textures/gui/nourished_icons.png");
    public static final ResourceLocation GUI_SATURATION_ICONS_LOCATION = new ResourceLocation(MindfulEatingFabric.MOD_ID, "textures/gui/saturation_icons.png");

    private static final Minecraft minecraft = Minecraft.getInstance();

    private static final Random random = new Random();
    public static int foodIconOffset;
//
//    @SubscribeEvent(priority = EventPriority.LOWEST)
//    public static void hungerIconOverride(RenderGameOverlayEvent.PreLayer event) {
//        if (event.getOverlay() == ForgeIngameGui.FOOD_LEVEL_ELEMENT && ModList.get().isLoaded("farmersdelight")) {
//            FarmersDelightCompat.resetNourishedHungerOverlay();
//        }
//    }
//
//    public static void init() {
//        MinecraftForge.EVENT_BUS.register(new HungerOverlay());
//
//        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.FOOD_LEVEL_ELEMENT, "Mindful Eating Hunger", ((gui, poseStack, partialTicks, width, height) -> {
//            boolean isMounted = minecraft.player != null && minecraft.player.getVehicle() instanceof LivingEntity;
//            if (!isMounted && !minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
//                renderHungerIcons(gui, poseStack);
//            }
//        }));
//    }
//
//    public static void renderHungerIcons(ForgeIngameGui gui, PoseStack poseStack) {
//        Player player = minecraft.player;
//        Set<IDietGroup> groups = DietApi.getInstance().getGroups(player, new ItemStack(BuiltInRegistries.ITEM.get(((MindfulEatingPlayer) player).mindful_eating$getLastFood())));
//        if (groups.isEmpty()) return;
//
//
//        FoodData foodData = player.getFoodData();
//        foodIconOffset = gui.right_height;
//
//        int top = minecraft.getWindow().getGuiScaledHeight() - foodIconOffset + 10;
//        foodIconOffset += 10;
//
//        int left = minecraft.getWindow().getGuiScaledWidth() / 2 + 91;
//
//        drawHungerIcons(player, foodData, top, left, poseStack, groups.toArray(new IDietGroup[0]));
//    }
//
//    public static void drawHungerIcons(Player player, FoodData stats, int top, int left, PoseStack poseStack, IDietGroup[] groups) {
//
//    }
}
