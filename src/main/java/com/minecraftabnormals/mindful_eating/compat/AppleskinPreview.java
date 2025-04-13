package com.minecraftabnormals.mindful_eating.compat;

public class AppleskinPreview {
    public boolean isActive;
    public boolean useRotten;
    public int hungerLevel;
    public float saturationLevel;
    public float alpha;

    public AppleskinPreview() {
        this.isActive = false;
        this.useRotten = false;
        this.hungerLevel = 0;
        this.saturationLevel = 0.0F;
        this.alpha = 0.0F;
    }

    public AppleskinPreview(boolean isActive, boolean useRotten, int hungerLevel, float saturationLevel, float alpha) {
        this.isActive = isActive;
        this.useRotten = useRotten;
        this.hungerLevel = hungerLevel;
        this.saturationLevel = saturationLevel;
        this.alpha = alpha;
    }

    public AppleskinPreview(AppleskinPreview other) {
        this.isActive = other.isActive;
        this.useRotten = other.useRotten;
        this.hungerLevel = other.hungerLevel;
        this.saturationLevel = other.saturationLevel;
        this.alpha = other.alpha;
    }
 }
