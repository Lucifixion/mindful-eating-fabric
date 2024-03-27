package com.minecraftabnormals.mindful_eating.core.ext;

import net.minecraft.resources.ResourceLocation;

public interface MindfulEatingPlayer {

    ResourceLocation mindful_eating$getLastFood();
    void mindful_eating$setLastFood(ResourceLocation lastFood);

    int mindful_eating$getSheenCooldown();
    void mindful_eating$setSheenCooldown(int cooldown);

    boolean mindful_eating$getHurtOrHeal();
    void mindful_eating$setHurtOrHeal(boolean hurtOrHeal);

}
