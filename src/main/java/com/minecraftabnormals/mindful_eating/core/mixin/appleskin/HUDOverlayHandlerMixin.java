package com.minecraftabnormals.mindful_eating.core.mixin.appleskin;

import com.llamalad7.mixinextras.sugar.Local;
import com.minecraftabnormals.mindful_eating.compat.AppleskinCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import squeek.appleskin.api.event.HUDOverlayEvent;
import squeek.appleskin.client.HUDOverlayHandler;

@Pseudo
@Mixin(squeek.appleskin.client.HUDOverlayHandler.class)
public class HUDOverlayHandlerMixin {
    @Inject(
            method = "onRender(Lnet/minecraft/client/gui/GuiGraphics;)V",
            at = @At(value = "HEAD")
    )
    private void getDietGroups(GuiGraphics context, CallbackInfo info) {
        if (AppleskinCompat.INSTANCE != null) {
            AppleskinCompat.INSTANCE.resetPreview();
        }
    }

    @Redirect(
            method = "onRender(Lnet/minecraft/client/gui/GuiGraphics;)V",
            at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawHungerOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$HungerRestored;Lnet/minecraft/client/Minecraft;IFZ)V", ordinal = 0)
    )
    private static void drawHungerOverlay(HUDOverlayHandler handler, HUDOverlayEvent.HungerRestored event, Minecraft mc, int hunger, float alpha, boolean useRottenTextures, @Local(name = "heldItem") ItemStack heldItem) {
        if (AppleskinCompat.INSTANCE != null) {
            AppleskinCompat.INSTANCE.updateHungerPreview(hunger, event.currentFoodLevel, mc, event.x, event.y, alpha, useRottenTextures, heldItem);
        }
    }

    @Redirect(
            method = "onRender(Lnet/minecraft/client/gui/GuiGraphics;)V",
            at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawSaturationOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$Saturation;Lnet/minecraft/client/Minecraft;FF)V", ordinal = 0)
    )
    private static void drawSaturationOverlay0(HUDOverlayHandler handler, HUDOverlayEvent.Saturation event, Minecraft mc, float saturationGained, float alpha) {}

    @Redirect(
            method = "onRender(Lnet/minecraft/client/gui/GuiGraphics;)V",
            at = @At(value = "INVOKE", target = "Lsqueek/appleskin/client/HUDOverlayHandler;drawSaturationOverlay(Lsqueek/appleskin/api/event/HUDOverlayEvent$Saturation;Lnet/minecraft/client/Minecraft;FF)V", ordinal = 1)
    )
    private static void drawSaturationOverlay1(HUDOverlayHandler handler, HUDOverlayEvent.Saturation event, Minecraft mc, float saturationGained, float alpha) {
        if (AppleskinCompat.INSTANCE != null) {
            AppleskinCompat.INSTANCE.updateSaturationPreview(saturationGained, event.saturationLevel, mc, event.x, event.y, alpha);
        }
    }
}
