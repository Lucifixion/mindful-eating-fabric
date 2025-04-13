package com.minecraftabnormals.mindful_eating.core.mixin.appleskin;

import com.llamalad7.mixinextras.sugar.Local;
import com.minecraftabnormals.mindful_eating.compat.AppleskinTooltipRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import squeek.appleskin.client.TooltipOverlayHandler;

@Pseudo
@Mixin(squeek.appleskin.client.TooltipOverlayHandler.class)
public class TooltipOverlayHandlerMixin {
    @Redirect(
            method = "onRenderTooltip(Lnet/minecraft/client/gui/GuiGraphics;Lsqueek/appleskin/client/TooltipOverlayHandler$FoodOverlay;IIILnet/minecraft/client/gui/Font;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V", ordinal = 0)
    )
    private void drawHungerTexture0(GuiGraphics context, ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, @Local(name = "i") int i, @Local(argsOnly = true) TooltipOverlayHandler.FoodOverlay foodOverlay) {
        ((AppleskinTooltipRenderer) foodOverlay).renderHungerTooltip(context, texture, x, y, z, u, v, width, height, textureWidth, textureHeight, i);
    }

    @Redirect(
            method = "onRenderTooltip(Lnet/minecraft/client/gui/GuiGraphics;Lsqueek/appleskin/client/TooltipOverlayHandler$FoodOverlay;IIILnet/minecraft/client/gui/Font;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V", ordinal = 1)
    )
    private void drawHungerTexture1(GuiGraphics context, ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, @Local(name = "i") int i, @Local(argsOnly = true) TooltipOverlayHandler.FoodOverlay foodOverlay) {
        ((AppleskinTooltipRenderer) foodOverlay).renderHungerTooltip(context, texture, x, y, z, u, v, width, height, textureWidth, textureHeight, i);
    }

    @Redirect(
            method = "onRenderTooltip(Lnet/minecraft/client/gui/GuiGraphics;Lsqueek/appleskin/client/TooltipOverlayHandler$FoodOverlay;IIILnet/minecraft/client/gui/Font;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V", ordinal = 2)
    )
    private void drawHungerTexture2(GuiGraphics context, ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, @Local(name = "i") int i, @Local(argsOnly = true) TooltipOverlayHandler.FoodOverlay foodOverlay) {
        ((AppleskinTooltipRenderer) foodOverlay).renderHungerTooltip(context, texture, x, y, z, u, v, width, height, textureWidth, textureHeight, i);
    }

    @Redirect(
            method = "onRenderTooltip(Lnet/minecraft/client/gui/GuiGraphics;Lsqueek/appleskin/client/TooltipOverlayHandler$FoodOverlay;IIILnet/minecraft/client/gui/Font;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V", ordinal = 3)
    )
    private void drawHungerTexture3(GuiGraphics context, ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, @Local(name = "i") int i, @Local(argsOnly = true) TooltipOverlayHandler.FoodOverlay foodOverlay) {
        ((AppleskinTooltipRenderer) foodOverlay).renderHungerTooltip(context, texture, x, y, z, u, v, width, height, textureWidth, textureHeight, i);
    }

    @Redirect(
            method = "onRenderTooltip(Lnet/minecraft/client/gui/GuiGraphics;Lsqueek/appleskin/client/TooltipOverlayHandler$FoodOverlay;IIILnet/minecraft/client/gui/Font;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V", ordinal = 4)
    )
    private void drawHungerTexture4(GuiGraphics context, ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, @Local(name = "i") int i, @Local(argsOnly = true) TooltipOverlayHandler.FoodOverlay foodOverlay) {
        ((AppleskinTooltipRenderer) foodOverlay).renderHungerTooltip(context, texture, x, y, z, u, v, width, height, textureWidth, textureHeight, i);
    }

    @Redirect(
            method = "onRenderTooltip(Lnet/minecraft/client/gui/GuiGraphics;Lsqueek/appleskin/client/TooltipOverlayHandler$FoodOverlay;IIILnet/minecraft/client/gui/Font;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V", ordinal = 5)
    )
    private void drawHungerTexture5(GuiGraphics context, ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, @Local(name = "i") int i, @Local(argsOnly = true) TooltipOverlayHandler.FoodOverlay foodOverlay) {
        ((AppleskinTooltipRenderer) foodOverlay).renderHungerTooltip(context, texture, x, y, z, u, v, width, height, textureWidth, textureHeight, i);
    }

    @Redirect(
            method = "onRenderTooltip(Lnet/minecraft/client/gui/GuiGraphics;Lsqueek/appleskin/client/TooltipOverlayHandler$FoodOverlay;IIILnet/minecraft/client/gui/Font;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIFFIIII)V", ordinal = 6)
    )
    private void drawHungerTexture6(GuiGraphics context, ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, @Local(name = "i") int i, @Local(argsOnly = true) TooltipOverlayHandler.FoodOverlay foodOverlay) {
        ((AppleskinTooltipRenderer) foodOverlay).renderHungerTooltip(context, texture, x, y, z, u, v, width, height, textureWidth, textureHeight, i);
    }
}
