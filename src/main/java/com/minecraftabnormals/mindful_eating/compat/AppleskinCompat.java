package com.minecraftabnormals.mindful_eating.compat;

import squeek.appleskin.api.event.HUDOverlayEvent;

public class AppleskinCompat {
    public static void init() {
        HUDOverlayEvent.Saturation.EVENT.register(saturation ->
                saturation.isCanceled = true
        );

        HUDOverlayEvent.HungerRestored.EVENT.register(hungerRestored ->
                hungerRestored.isCanceled = true
        );
    }
}
