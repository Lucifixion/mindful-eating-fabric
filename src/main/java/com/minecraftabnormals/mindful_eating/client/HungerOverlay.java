package com.minecraftabnormals.mindful_eating.client;

import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.minecraftabnormals.mindful_eating.compat.AppleskinCompat;
import com.minecraftabnormals.mindful_eating.compat.FarmersDelightCompat;
import com.minecraftabnormals.mindful_eating.core.MindfulEatingFabric;
import com.minecraftabnormals.mindful_eating.core.ext.MindfulEatingPlayer;
import com.nhoryzon.mc.farmersdelight.registry.EffectsRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import squeek.appleskin.ModConfig;

import java.util.Random;

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

    public static void drawHungerIcons(GuiGraphics guiGraphics, Player player, FoodData stats, int top, int left, IDietGroup[] groups) {
        minecraft.getProfiler().push("food");
        ResourceLocation texture = GUI_HUNGER_ICONS_LOCATION;

        int level = stats.getFoodLevel();
        int ticks = minecraft.gui.getGuiTicks();
        float modifiedSaturation = Math.min(stats.getSaturationLevel(), 20);

        int yOffset = player.tickCount % Mth.ceil(10 + 5.0F);

        for (int i = 0; i < 10; ++i) {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;
            if (((MindfulEatingPlayer) player).mindful_eating$getSheenCooldown() > 0 && i == yOffset)
                y-=2;
            int icon = 0;

            FoodGroups foodGroup = FoodGroups.byDietGroup(groups[i % groups.length]);
            int group = foodGroup != null ? foodGroup.getTextureOffset() : 0;
            byte background = 0;

            if (player.hasEffect(MobEffects.HUNGER)) {
                icon += 36;
                background = 13;
            }

            if (FabricLoader.getInstance().isModLoaded("farmersdelight")
                    && player.hasEffect(EffectsRegistry.NOURISHMENT.get())
                    && FarmersDelightCompat.NOURISHED_HUNGER_OVERLAY) {
                FarmersDelightCompat.setNourishedHungerOverlay(false);
                texture = GUI_NOURISHMENT_ICONS_LOCATION;
                icon -= player.hasEffect(MobEffects.HUNGER) ? 45 : 27;
                background = 0;
            }

            if (player.getFoodData().getSaturationLevel() <= 0.0F && ticks % (level * 3 + 1) == 0) {
                y = top + (random.nextInt(3) - 1);
            }

            guiGraphics.blit(texture, x, y, background * 9, group, 9, 9, 126, 45);
            if (idx < level) {
                guiGraphics.blit(texture, x, y, icon + 36, group, 9, 9, 126, 45);
            } else if (idx == level) {
                guiGraphics.blit(texture, x, y, icon + 45, group, 9, 9, 126, 45);
            }

            if (FabricLoader.getInstance().isModLoaded("appleskin")) {
                if (!ModConfig.INSTANCE.showSaturationHudOverlay)
                    return;
                float effectiveSaturationOfBar = (modifiedSaturation / 2.0F) - i;

                int v = group;
                int u;

                if (effectiveSaturationOfBar >= 1)
                    u = 4 * 9;
                else if (effectiveSaturationOfBar > .75)
                    u = 3 * 9;
                else if (effectiveSaturationOfBar > .5)
                    u = 2 * 9;
                else if (effectiveSaturationOfBar > .25)
                    u = 9;
                else
                    u = 0;

                guiGraphics.blit(GUI_SATURATION_ICONS_LOCATION, x, y, u, v, 9, 9, 126, 45);
            }
        }
        minecraft.getProfiler().pop();
    }
}
