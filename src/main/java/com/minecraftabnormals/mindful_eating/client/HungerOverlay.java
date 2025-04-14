package com.minecraftabnormals.mindful_eating.client;

import com.illusivesoulworks.diet.api.DietApi;
import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.minecraftabnormals.mindful_eating.compat.AppleskinCompat;
import com.minecraftabnormals.mindful_eating.compat.AppleskinPreview;
import com.minecraftabnormals.mindful_eating.core.MEConfig;
import com.minecraftabnormals.mindful_eating.core.MindfulEatingFabric;
import com.minecraftabnormals.mindful_eating.core.ext.MindfulEatingPlayer;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import org.lwjgl.opengl.GL11;
import squeek.appleskin.ModConfig;
import vectorwing.farmersdelight.common.registry.ModEffects;

import java.util.Random;
import java.util.Set;

public class HungerOverlay {

    public static final ResourceLocation GUI_HUNGER_ICONS_LOCATION = new ResourceLocation(MindfulEatingFabric.MOD_ID, "textures/gui/hunger_icons.png");
    public static final ResourceLocation GUI_NOURISHMENT_ICONS_LOCATION = new ResourceLocation(MindfulEatingFabric.MOD_ID, "textures/gui/nourished_icons.png");
    public static final ResourceLocation GUI_SATURATION_ICONS_LOCATION = new ResourceLocation(MindfulEatingFabric.MOD_ID, "textures/gui/saturation_icons.png");

    private static final Minecraft minecraft = Minecraft.getInstance();

    private static final Random random = new Random();

    public static boolean savedBlend = false;

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
        float previewSaturation;
        AppleskinPreview preview;
        if (AppleskinCompat.INSTANCE != null) {
            preview = AppleskinCompat.INSTANCE.getCurrentPreview();
            previewSaturation = Math.min(preview.saturationLevel, 20);
        } else {
            preview = new AppleskinPreview();
            previewSaturation = modifiedSaturation;
        }

        int yOffset = player.tickCount % Mth.ceil(10 + 5.0F);

        IDietGroup[] previewGroups;
        boolean nourishmentPreview = false;
        if (preview.heldItem != null) {
            Set<IDietGroup> previewGroupSet = DietApi.getInstance().getGroups(player, preview.heldItem);
            previewGroups = previewGroupSet.toArray(new IDietGroup[0]);
            if (FabricLoader.getInstance().isModLoaded("farmersdelight")
                    && MEConfig.COMMON.nourishmentOverlay.get()
                    && preview.heldItem.getItem() != null
                    && preview.heldItem.getItem().isEdible()) {
                FoodProperties itemFood = preview.heldItem.getItem().getFoodProperties();
                if (itemFood != null) {
                    nourishmentPreview = itemFood.getEffects().stream().anyMatch (pair -> pair.getFirst().getEffect() == ModEffects.NOURISHMENT.get());
                }
            }
        } else {
            previewGroups = groups;
        }

        if (preview.isActive) {
            enableAlpha();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0f);
        }

        for (int i = 0; i < 10; ++i) {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;
            if (((MindfulEatingPlayer) player).mindful_eating$getSheenCooldown() > 0 && i == yOffset)
                y-=2;
            int icon = 0;

            FoodGroups foodGroup;
            if (groups.length == 0)
                foodGroup = FoodGroups.PROTEINS;
            else
                foodGroup = FoodGroups.byDietGroup(groups[i % groups.length]);

            FoodGroups previewFoodGroup;
            if (previewGroups.length == 0)
                previewFoodGroup = foodGroup;
            else
                previewFoodGroup = FoodGroups.byDietGroup(previewGroups[i % previewGroups.length]);

            int group = foodGroup != null ? foodGroup.getTextureOffset() : 0;
            int previewGroup = previewFoodGroup != null ? previewFoodGroup.getTextureOffset() : 0;
            byte background = 0;
            int previewIcon = 0;
            byte previewBackground = 1;

            if (player.hasEffect(MobEffects.HUNGER)) {
                icon += 36;
                background = 13;
            }

            if (preview.useRotten) {
                previewIcon += 36;
                previewBackground = 13;
            }

            ResourceLocation previewTexture = texture;

            if (FabricLoader.getInstance().isModLoaded("farmersdelight")
                    && MEConfig.COMMON.nourishmentOverlay.get()) {
                if (player.hasEffect(ModEffects.NOURISHMENT.get())) {
                    texture = GUI_NOURISHMENT_ICONS_LOCATION;
                    previewTexture = GUI_NOURISHMENT_ICONS_LOCATION;
                    icon -= player.hasEffect(MobEffects.HUNGER) ? 45 : 27;
                    background = 0;
                    previewIcon -= preview.useRotten ? 45 : 27;
                    previewBackground = 0;
                } else if (nourishmentPreview) {
                    previewTexture = GUI_NOURISHMENT_ICONS_LOCATION;
                    previewIcon -= preview.useRotten ? 45 : 27;
                    previewBackground = 0;
                }
            }

            if (player.getFoodData().getSaturationLevel() <= 0.0F && ticks % (level * 3 + 1) == 0) {
                y = top + (random.nextInt(3) - 1);
            }

            if (idx <= level) {
                previewGroup = group;
            }
            guiGraphics.blit(texture, x, y, background * 9, previewGroup, 9, 9, 126, 45);
            if (idx < level) {
                guiGraphics.blit(texture, x, y, icon + 36, group, 9, 9, 126, 45);
            } else if (idx == level) {
                guiGraphics.blit(texture, x, y, icon + 45, group, 9, 9, 126, 45);
            }

            if (preview.isActive && (idx >= level || nourishmentPreview || preview.useRotten) && idx <= preview.hungerLevel && preview.alpha > 0.0F) {
                int previewLevel = preview.hungerLevel;

                if (idx >= level) {
                    // very faint background
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, preview.alpha * 0.25F);
                    guiGraphics.blit(previewTexture, x, y, previewBackground * 9, previewGroup, 9, 9, 126, 45);
                }
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, preview.alpha);
                if (idx < previewLevel) {
                    guiGraphics.blit(previewTexture, x, y, previewIcon + 36, previewGroup, 9, 9, 126, 45);
                } else {
                    guiGraphics.blit(previewTexture, x, y, previewIcon + 45, previewGroup, 9, 9, 126, 45);
                }
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            }

            if (FabricLoader.getInstance().isModLoaded("appleskin")) {
                if (!ModConfig.INSTANCE.showSaturationHudOverlay)
                    continue;
                int u = getSaturationIndex(modifiedSaturation, i);

                guiGraphics.blit(GUI_SATURATION_ICONS_LOCATION, x, y, u, group, 9, 9, 126, 45);

                if (preview.isActive && previewSaturation > modifiedSaturation && previewSaturation / 2.0F > i && preview.alpha > 0.0F) {
                    int u2 = getSaturationIndex(previewSaturation, i);

                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, preview.alpha);
                    guiGraphics.blit(GUI_SATURATION_ICONS_LOCATION, x, y, u2, previewGroup, 9, 9, 126, 45);
                    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                }
            }
        }
        if (preview.isActive) disableAlpha();
        minecraft.getProfiler().pop();
    }

    private static int getSaturationIndex(float previewSaturation, int i) {
        float effectiveSaturationOfPreviewBar = (previewSaturation / 2.0F) - i;

        int idx;

        if (effectiveSaturationOfPreviewBar >= 1)
            idx = 4 * 9;
        else if (effectiveSaturationOfPreviewBar > .75)
            idx = 3 * 9;
        else if (effectiveSaturationOfPreviewBar > .5)
            idx = 2 * 9;
        else if (effectiveSaturationOfPreviewBar > .25)
            idx = 9;
        else
            idx = 0;
        return idx;
    }

    private static void enableAlpha()
	{
		savedBlend = GL11.glIsEnabled(GL11.GL_BLEND);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
	}

	private static void disableAlpha()
	{
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		if (!savedBlend)
			RenderSystem.disableBlend();
	}
}
