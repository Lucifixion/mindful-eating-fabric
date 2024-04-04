package com.minecraftabnormals.mindful_eating.core.registry.other;

import com.google.gson.JsonObject;
import com.minecraftabnormals.mindful_eating.core.MindfulEatingFabric;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

public class MEOverrides {

    public static void generateDefaultConfig() {
        JsonObject object = new JsonObject();

        JsonObject hunger = new JsonObject();
        JsonObject saturation = new JsonObject();
        JsonObject speedy = new JsonObject();
        JsonObject stackability = new JsonObject();
        JsonObject gorgable = new JsonObject();

        for (Item item : new Item[]{Items.COOKED_MUTTON, Items.COOKED_RABBIT, Items.COOKED_SALMON, Items.COOKED_COD}) {
            saturation.addProperty(BuiltInRegistries.ITEM.getKey(item).toString(), Math.round(10.0F * item.getFoodProperties().getSaturationModifier() - item.getFoodProperties().getNutrition()) / 10.0F);
        }

        for (Item item : new Item[]{Items.MELON_SLICE, Items.SWEET_BERRIES, Items.GLOW_BERRIES, Items.COOKED_MUTTON, Items.COOKED_RABBIT,
                Items.COOKED_SALMON, Items.COOKED_COD, Items.BEETROOT, Items.BEETROOT_SOUP}) {
            speedy.addProperty(BuiltInRegistries.ITEM.getKey(item).toString(), true);
        }

        for (Item item: new Item[]{Items.BEETROOT_SOUP,
                Items.MUSHROOM_STEW, Items.SUSPICIOUS_STEW, Items.RABBIT_STEW}) {
            stackability.addProperty(BuiltInRegistries.ITEM.getKey(item).toString(), 16);
        }
        stackability.addProperty(BuiltInRegistries.ITEM.getKey(Items.CAKE).toString(), 64);

        object.add("hunger", hunger);
        object.add("saturation", saturation);
        object.add("speedy", speedy);
        object.add("stackability", stackability);
        object.add("gorgable", gorgable);

        File file = new File(Minecraft.getInstance().gameDirectory,
                Paths.get("..", "src", "main", "resources", "data", MindfulEatingFabric.MOD_ID, "food_changes.json").toString()).getAbsoluteFile();

        String data = object.toString();

        try(FileWriter writer = new FileWriter(file)) {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void changeHunger(Item item, int hunger) {
        if (item.getFoodProperties() != null)
            item.getFoodProperties().nutrition = hunger;
        // System.out.println("Changed hunger of " + item + " to " + hunger);
    }

    public static void changeSaturation(Item item, float saturation) {
        if (item.getFoodProperties() != null)
            item.getFoodProperties().saturationModifier = saturation;
        // System.out.println("Changed saturation of " + item + " to " + saturation);
    }

    public static void changeFastEating(Item item, boolean fast) {
        if (item.getFoodProperties() != null)
            item.getFoodProperties().fastFood = fast;
        // System.out.println("Changed fastEating of " + item + " to " + fast);
    }

    public static void changeCanEatWhenFull(Item item, boolean gorgable) {
        if (item.getFoodProperties() != null)
            item.getFoodProperties().canAlwaysEat = gorgable;
        // System.out.println("Changed canEatWhenFull of " + item + " to " + gorgable);
    }

    public static void changeStackability(Item item, int size) {
        item.maxStackSize = size;
        // System.out.println("Changed stackability of " + item + " to " + size);
    }

}
