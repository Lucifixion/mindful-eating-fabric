package com.minecraftabnormals.mindful_eating.core.mixin;

import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.minecraftabnormals.mindful_eating.client.HungerOverlay;
import com.minecraftabnormals.mindful_eating.core.MindfulEatingFabric;
import com.minecraftabnormals.mindful_eating.core.ext.MindfulEatingPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(Gui.class)
public abstract class GuiMixin {

    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private RandomSource random;

    @Shadow @Final private static ResourceLocation GUI_ICONS_LOCATION;

    @Shadow private int screenWidth;

    @Shadow private int screenHeight;

    @Shadow protected abstract Player getCameraPlayer();

//    @WrapOperation(method = "renderPlayerHealth", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/Gui;getVehicleMaxHearts(Lnet/minecraft/world/entity/LivingEntity;)I"))
//    @SuppressWarnings("unused")
//    private int hookRenderStatusBars(Gui instance, LivingEntity livingEntity, Operation<Integer> original) {
//        return -1;
//    }

    // THIS IS FOR COMPATIBILITY
    @WrapOperation(method = "renderPlayerHealth", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 3))
    private void renderPlayerHealth0(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, int k, int l, int m, int n, Operation<Void> original) {}

    @WrapOperation(method = "renderPlayerHealth", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 4))
    private void renderPlayerHealth1(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, int k, int l, int m, int n, Operation<Void> original) {}

    @WrapOperation(method = "renderPlayerHealth", at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 5))
    private void renderPlayerHealth2(GuiGraphics instance, ResourceLocation resourceLocation, int i, int j, int k, int l, int m, int n, Operation<Void> original) {}

    @Inject(method = "renderPlayerHealth", at = @At(value = "TAIL"))
    protected void renderPlayerHealth(GuiGraphics guiGraphics, CallbackInfo ci) {
        Player player = this.getCameraPlayer();
        if (player != null) {
            int left = this.screenWidth / 2 + 91;
            int top = this.screenHeight - 39;
            FoodData stats = player.getFoodData();
            Set<IDietGroup> groups = MindfulEatingFabric.DIET_API.getGroups(player, new ItemStack(BuiltInRegistries.ITEM.get(((MindfulEatingPlayer) player).mindful_eating$getLastFood())));

            HungerOverlay.drawHungerIcons(guiGraphics, player, stats, top, left, groups.toArray(new IDietGroup[0]));
        }
    }
}
