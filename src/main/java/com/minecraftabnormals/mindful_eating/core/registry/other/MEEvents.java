package com.minecraftabnormals.mindful_eating.core.registry.other;

import com.illusivesoulworks.diet.api.DietApi;
import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.illusivesoulworks.diet.common.component.DietComponents;
import com.minecraftabnormals.mindful_eating.core.ExhaustionSource;
import com.minecraftabnormals.mindful_eating.core.MEConfig;
import com.minecraftabnormals.mindful_eating.core.MindfulEatingFabric;
import com.minecraftabnormals.mindful_eating.core.ext.MindfulEatingPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.Math.max;

public class MEEvents {

    public static float exhaustionReductionShortSheen(Player player, ExhaustionSource source) {
        return exhaustionReductionLongSheen(player, source, 7);
    }

    public static float exhaustionReductionLongSheen(Player player, ExhaustionSource source) {
        return exhaustionReductionLongSheen(player, source, 15); // used to be 20
        //TODO healing makes the sheen keep going for ages for some reason, and natural generation is not counted at all
    }

    public static float exhaustionReductionLongSheen(Player player, ExhaustionSource source, int cooldown) {
        ((MindfulEatingPlayer) player).mindful_eating$setHurtOrHeal(source == ExhaustionSource.HURT || source == ExhaustionSource.HEAL);

        if (!MEConfig.COMMON.proportionalDiet.get()) {
            Set<IDietGroup> groups = MindfulEatingFabric.DIET_API.getGroups(player, new ItemStack(BuiltInRegistries.ITEM.get(((MindfulEatingPlayer) player).mindful_eating$getLastFood())));

            for (IDietGroup group : groups) {
                for (String configGroup : MEConfig.COMMON.foodGroupExhaustion[source.ordinal()].get().split("/")) {
                    if (group.getName().equals(configGroup)) {
                        ((MindfulEatingPlayer) player).mindful_eating$setSheenCooldown(cooldown);
                        return -MEConfig.COMMON.exhaustionReduction.get().floatValue();
                    }
                }
            }

            return 0.0F;
        } else {
            AtomicReference<Float> percentage = new AtomicReference<>(0.0F);
            DietComponents.DIET_TRACKER.maybeGet(player).ifPresent(tracker -> {
                for (String configGroup : MEConfig.COMMON.foodGroupExhaustion[source.ordinal()].get().split("/"))
                    percentage.set(tracker.getValue(configGroup));
            });
            if (percentage.get() > 0.0F)
                ((MindfulEatingPlayer) player).mindful_eating$setSheenCooldown(cooldown);
            return max(-percentage.get(), 1.0F);
        }
    }

}
