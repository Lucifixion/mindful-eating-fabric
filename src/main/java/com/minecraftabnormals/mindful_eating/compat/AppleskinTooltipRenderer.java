package com.minecraftabnormals.mindful_eating.compat;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public interface AppleskinTooltipRenderer {
    void renderHungerTooltip(GuiGraphics context, ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, int i);
}
