package com.minecraftabnormals.mindful_eating.compat;

import net.minecraft.world.item.ItemStack;

public class AppleskinPreview {
    public boolean isActive;
    public boolean useRotten;
    public int hungerLevel;
    public float saturationLevel;
    public float alpha;
    public ItemStack heldItem;

    public AppleskinPreview() {
        this.isActive = false;
        this.useRotten = false;
        this.hungerLevel = 0;
        this.saturationLevel = 0.0F;
        this.alpha = 0.0F;
        this.heldItem = null;
    }

    public AppleskinPreview(boolean isActive, boolean useRotten, int hungerLevel, float saturationLevel, float alpha, ItemStack heldItem) {
        this.isActive = isActive;
        this.useRotten = useRotten;
        this.hungerLevel = hungerLevel;
        this.saturationLevel = saturationLevel;
        this.alpha = alpha;
        this.heldItem = heldItem;
    }

    public AppleskinPreview(AppleskinPreview other) {
        this.isActive = other.isActive;
        this.useRotten = other.useRotten;
        this.hungerLevel = other.hungerLevel;
        this.saturationLevel = other.saturationLevel;
        this.alpha = other.alpha;
        this.heldItem = other.heldItem;
    }
 }
