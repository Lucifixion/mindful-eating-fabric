package com.minecraftabnormals.mindful_eating.compat;

import net.minecraft.client.Minecraft;
import squeek.appleskin.api.event.HUDOverlayEvent;

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
        HUDOverlayEvent.Saturation.EVENT.register(saturation ->
                saturation.isCanceled = true
        );
    }

    public AppleskinPreview getCurrentPreview() {
        return new AppleskinPreview(currentPreview);
    }

    public void resetPreview() {
        currentPreview = new AppleskinPreview();
    }

    public void updateHungerPreview(int hungerRestored, int foodLevel, Minecraft mc, int right, int top, float alpha, boolean useRotten) {
        currentPreview.isActive = true;
        currentPreview.alpha = alpha;
        currentPreview.hungerLevel = Math.max(0, Math.min(20, foodLevel + hungerRestored));
        currentPreview.useRotten = useRotten;
    }
}