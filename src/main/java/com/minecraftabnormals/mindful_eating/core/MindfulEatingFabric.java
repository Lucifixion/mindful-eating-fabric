package com.minecraftabnormals.mindful_eating.core;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.illusivesoulworks.diet.api.DietEvents;
import com.illusivesoulworks.diet.common.DietApiImpl;
import com.minecraftabnormals.mindful_eating.compat.AppleskinCompat;
import com.minecraftabnormals.mindful_eating.core.registry.other.MEOverrides;
import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squeek.appleskin.api.event.HUDOverlayEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class MindfulEatingFabric implements ModInitializer {
	public static final String MOD_ID = "mindful_eating";
    public static final Logger LOGGER = LoggerFactory.getLogger("Mindful Eating");

	public static HashMap<String, Integer> ORIGINAL_ITEMS = new HashMap<>();
	public static HashMap<String, FoodProperties> ORIGINAL_FOODS = new HashMap<>();

	public static final DietApiImpl DIET_API = new DietApiImpl();

	@Override
	public void onInitialize() {
		ForgeConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, MEConfig.SPEC);

		if (FabricLoader.getInstance().isModLoaded("appleskin")) {
			AppleskinCompat.init();
		}

		DietEvents.APPLY_EFFECT.register(listener -> MEConfig.COMMON.nativeDietBuffs.get());

		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public ResourceLocation getFabricId() {
				return new ResourceLocation(MindfulEatingFabric.MOD_ID, "food_changes.json");
			}

			@Override
			public void onResourceManagerReload(ResourceManager resourceManager) {
				CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

					// reload original values for food, so if changes are removed they leave no trace

					for (Map.Entry<String, Integer> entry : MindfulEatingFabric.ORIGINAL_ITEMS.entrySet()) {
						Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(entry.getKey()));
						MEOverrides.changeStackability(item, entry.getValue());
					}

					for (Map.Entry<String, FoodProperties> entry : MindfulEatingFabric.ORIGINAL_FOODS.entrySet()) {
						Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(entry.getKey()));
						MEOverrides.changeHunger(item, entry.getValue().getNutrition());
						MEOverrides.changeSaturation(item, entry.getValue().getSaturationModifier());
						MEOverrides.changeFastEating(item, entry.getValue().isFastFood());
						MEOverrides.changeCanEatWhenFull(item, entry.getValue().canAlwaysEat());
					}

					ResourceLocation path = getFabricId();
					resourceManager.getResource(path).ifPresent(resource -> {
						try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.open()))) {
							JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

							String[] names = {"hunger", "saturation", "speedy", "stackability", "gorgable"};

							for (int i = 0; i < names.length; i++) {

								JsonObject object = json.get(names[i]).getAsJsonObject();

								for (Map.Entry<String, JsonElement> map : object.entrySet()) {
									String name = map.getKey();
									Item item = BuiltInRegistries.ITEM.get(new ResourceLocation(name));

									// record original values before food is changed for the first time
									if (!MindfulEatingFabric.ORIGINAL_ITEMS.containsKey(name)) {
										MindfulEatingFabric.ORIGINAL_ITEMS.put(name, item.getMaxStackSize());
									}

									if (item.isEdible() && !MindfulEatingFabric.ORIGINAL_FOODS.containsKey(name)) {
										FoodProperties food = item.getFoodProperties();
										FoodProperties.Builder builder = (new FoodProperties.Builder()).nutrition(food.getNutrition())
												.saturationMod(food.getSaturationModifier());
										if (food.isFastFood()) builder.fast();
										if (food.canAlwaysEat()) builder.alwaysEat();
										MindfulEatingFabric.ORIGINAL_FOODS.put(name, builder.build());
									}

									// reflects values
									switch (i) {
										case 0 -> MEOverrides.changeHunger(item, map.getValue().getAsInt());
										case 1 -> MEOverrides.changeSaturation(item, map.getValue().getAsFloat());
										case 2 -> MEOverrides.changeFastEating(item, map.getValue().getAsBoolean());
										case 3 -> MEOverrides.changeStackability(item, map.getValue().getAsInt());
										case 4 -> MEOverrides.changeCanEatWhenFull(item, map.getValue().getAsBoolean());
									}
								}
							}
						} catch (IOException e) {
							MindfulEatingFabric.LOGGER.error("Failed to load config at {}", path, e);
						}
					});

				});
			}
		});

	}
}