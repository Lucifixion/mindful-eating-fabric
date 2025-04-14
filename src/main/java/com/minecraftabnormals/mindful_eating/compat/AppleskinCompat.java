package com.minecraftabnormals.mindful_eating.compat;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;

public class AppleskinCompat {
    public static AppleskinCompat INSTANCE;

    private AppleskinPreview currentPreview;

    AppleskinCompat() {
        this.currentPreview = new AppleskinPreview();
    }

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = new AppleskinCompat();
        }
    }

    public AppleskinPreview getCurrentPreview() {
        return new AppleskinPreview(currentPreview);
    }

    public void resetPreview() {
        currentPreview = new AppleskinPreview();
    }

    public void updateHungerPreview(int hungerRestored, int foodLevel, Minecraft mc, int right, int top, float alpha, boolean useRotten, ItemStack heldItem) {
        currentPreview.isActive = true;
        currentPreview.alpha = alpha;
        currentPreview.hungerLevel = Math.max(0, Math.min(20, foodLevel + hungerRestored));
        currentPreview.useRotten = useRotten;
        currentPreview.heldItem = heldItem;
    }

    public void updateSaturationPreview(float saturationRestored, float saturationLevel, Minecraft mc, int right, int top, float alpha) {
        currentPreview.isActive = true;
        currentPreview.alpha = alpha;
        currentPreview.saturationLevel = Math.min(20.0F, saturationLevel + saturationRestored);
    }
}