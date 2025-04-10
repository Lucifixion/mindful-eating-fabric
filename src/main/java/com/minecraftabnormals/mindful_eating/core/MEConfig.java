package com.minecraftabnormals.mindful_eating.core;

import com.electronwill.nightconfig.core.Config;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class MEConfig {
    public static final ForgeConfigSpec SPEC;
    public static final Common COMMON;
    static
    {
        Config.setInsertionOrderPreserved(true);
        Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }


    public static class Common {
        public final ForgeConfigSpec.ConfigValue<Boolean> proportionalDiet;
        public final ForgeConfigSpec.ConfigValue<Boolean> nativeDietBuffs;
        public final ForgeConfigSpec.ConfigValue<Double> exhaustionReduction;
        public final ForgeConfigSpec.ConfigValue<Boolean> nourishmentOverlay;
        public final ForgeConfigSpec.ConfigValue<String>[] foodGroupExhaustion;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("MindfulEating common configuration").push("common");
            builder.push("mode");

            proportionalDiet = builder.comment("Whether the saturation bonus is dependent on Diet's mechanics. If false, it will instead be based on the last food eaten. Default: false").define("Proportional Diet", false);
            nativeDietBuffs = builder.comment("Whether the buffs added by the Diet mod are enabled. Default: false").define("Native Diet Buffs", false);
            exhaustionReduction = builder.comment("The amount exhaustion is reduced by (if the above config is false). Default: 0.75").define("Exhaustion Reduction", 0.75);
            nourishmentOverlay = builder.comment("Should the hunger bar have a gilded overlay when the player has the Nourishment effect? Disable the equivalent option in Farmer's Delight. Default: true").define("Nourishment Hunger Overlay", true);

            builder.pop();
            builder.comment("For multiple food groups, separate groups with a /, for example: fruits/vegetables.").push("exhaustion-sources");

            foodGroupExhaustion = new ForgeConfigSpec.ConfigValue[8];
            String[] defaultFoodGroup = {"fruits", "vegetables", "vegetables", "grains", "proteins", "proteins", "sugars", "sugars"};

            for (int i = 0; i < foodGroupExhaustion.length; i++) {
                String exhaustionSourceName = ExhaustionSource.values()[i].toString().toLowerCase();
                foodGroupExhaustion[i] = builder.comment("The food group/s corresponding to the " + exhaustionSourceName + " exhaustion source. Default: "
                        + defaultFoodGroup[i]).define("Food Group - " + exhaustionSourceName, defaultFoodGroup[i]);
            }

            builder.pop();
            builder.pop();
        }
    }
}
