package com.minecraftabnormals.mindful_eating.core.mixin.farmersdelight;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(vectorwing.farmersdelight.client.gui.NourishmentHungerOverlay.class)
public class NourishmentHungerOverlayMixin {
    @Inject(method = "renderNourishmentOverlay(Lnet/minecraft/client/gui/Gui;Lnet/minecraft/client/gui/GuiGraphics;)V", at = @At("HEAD"), cancellable = true)
    private static void renderNourishmentOverlay(Gui gui, GuiGraphics guiGraphics, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}
