package com.minecraftabnormals.mindful_eating.core.mixin;

import com.illusivesoulworks.diet.api.DietApi;
import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minecraftabnormals.mindful_eating.client.FoodGroups;
import com.minecraftabnormals.mindful_eating.compat.AppleskinCompat;
import com.minecraftabnormals.mindful_eating.compat.FarmersDelightCompat;
import com.minecraftabnormals.mindful_eating.core.ext.MindfulEatingPlayer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

import static com.minecraftabnormals.mindful_eating.client.HungerOverlay.*;

@Mixin(Gui.class)
public class GuiMixin {

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private RandomSource random;

    @Shadow @Final private static ResourceLocation GUI_ICONS_LOCATION;

    @Shadow private int screenWidth;

    @Shadow private int screenHeight;

    @WrapOperation(method = "renderPlayerHealth", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
    @SuppressWarnings("unused")
    private int hookRenderStatusBars(Gui instance, LivingEntity livingEntity, Operation<Integer> original) {
        return -1;
    }
    @Inject(method = "renderPlayerHealth", at = @At(value = "TAIL"))
    protected void renderPlayerHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
//        RenderSystem.enableBlend();
        minecraft.getProfiler().push("food");

        int left = this.screenWidth / 2 + 91;
        int top = this.screenHeight - 39;
        Player player = minecraft.player;
        FoodData stats = minecraft.player.getFoodData();
        IDietGroup[] groups = DietApi.getInstance().getGroups(player, new ItemStack(BuiltInRegistries.ITEM.get(((MindfulEatingPlayer) player).mindful_eating$getLastFood()))).toArray(new IDietGroup[0]);

        for (IDietGroup group : groups) {
            System.out.println(group.getName());
        }

        int level = stats.getFoodLevel();
        int ticks = minecraft.gui.getGuiTicks();
        float modifiedSaturation = Math.min(stats.getSaturationLevel(), 20);

        for (int i = 0; i < 10; ++i) {
            int idx = i * 2 + 1;
            int x = left - i * 8 - 9;
            int y = top;
            int icon = 0;

            FoodGroups foodGroup = FoodGroups.byDietGroup(groups[i % groups.length]);
            int group = foodGroup != null ? foodGroup.getTextureOffset() : 0;
            byte background = 0;

            if (player.hasEffect(MobEffects.HUNGER)) {
                icon += 36;
                background = 13;
            }

            if (FabricLoader.getInstance().isModLoaded("farmersdelight")
                    && player.hasEffect(BuiltInRegistries.MOB_EFFECT.get(new ResourceLocation("farmersdelight", "nourishment")))
                    && FarmersDelightCompat.NOURISHED_HUNGER_OVERLAY) {
//                FarmersDelightCompat.setNourishedHungerOverlay(false);
                icon -= player.hasEffect(MobEffects.HUNGER) ? 45 : 27;
                background = 0;
            }

            if (player.getFoodData().getSaturationLevel() <= 0.0F && ticks % (level * 3 + 1) == 0) {
                y = top + (random.nextInt(3) - 1);
            }

            guiGraphics.blit(GUI_HUNGER_ICONS_LOCATION, x, y, background * 9, group, 9, 9, 126, 45);
            if (idx < level) {
                guiGraphics.blit(GUI_HUNGER_ICONS_LOCATION, x, y, icon + 36, group, 9, 9, 126, 45);
            } else if (idx == level) {
                guiGraphics.blit(GUI_HUNGER_ICONS_LOCATION, x, y, icon + 45, group, 9, 9, 126, 45);
            }

            if (FabricLoader.getInstance().isModLoaded("appleskin") && AppleskinCompat.SHOW_SATURATION_OVERLAY) {
                float effectiveSaturationOfBar = (modifiedSaturation / 2.0F) - i;

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

                guiGraphics.blit(GUI_SATURATION_ICONS_LOCATION, x, y, u, group, 9, 9, 126, 45);
            }

            if (idx <= level) {
                int tick = ticks % 20;
                if (((MindfulEatingPlayer) player).mindful_eating$getSheenCooldown() > 0 && ((tick < idx + level / 4 && tick > idx - level / 4)
                        || (tick == 49 && i == 0))) {
                    int uOffset = idx == level ? 18 : 9;
                    guiGraphics.blit(GUI_NOURISHMENT_ICONS_LOCATION, x, y, uOffset, group, 9, 9, 126, 45);
                }
            }
        }
        minecraft.getProfiler().pop();
//        RenderSystem.disableBlend();
    }
}
