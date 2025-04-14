package com.minecraftabnormals.mindful_eating.core.mixin.appleskin;

import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.minecraftabnormals.mindful_eating.client.FoodGroups;
import com.minecraftabnormals.mindful_eating.compat.AppleskinTooltipRenderer;
import com.minecraftabnormals.mindful_eating.core.MindfulEatingFabric;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import squeek.appleskin.api.food.FoodValues;

import java.util.Set;



@Pseudo
@Mixin(squeek.appleskin.client.TooltipOverlayHandler.FoodOverlay.class)
public class FoodOverlayMixin implements AppleskinTooltipRenderer {
    private static final ResourceLocation GUI_HUNGER_ICONS_LOCATION = new ResourceLocation(MindfulEatingFabric.MOD_ID, "textures/gui/hunger_icons.png");
    private static final ResourceLocation GUI_NOURISHMENT_ICONS_LOCATION = new ResourceLocation(MindfulEatingFabric.MOD_ID, "textures/gui/nourished_icons.png");
    private static final ResourceLocation GUI_SATURATION_ICONS_LOCATION = new ResourceLocation(MindfulEatingFabric.MOD_ID, "textures/gui/saturation_icons.png");

    private IDietGroup[] groups;

    @Inject(
            method = "<init>(Lnet/minecraft/world/item/ItemStack;Lsqueek/appleskin/api/food/FoodValues;Lsqueek/appleskin/api/food/FoodValues;Lnet/minecraft/world/entity/player/Player;)V",
            at = @At(value = "TAIL")
    )
    private void getDietGroups(ItemStack itemStack, FoodValues defaultFood, FoodValues modifiedFood, Player player, CallbackInfo info) {
        if (player != null && itemStack != null) {
            Set<IDietGroup> group_set = MindfulEatingFabric.DIET_API.getGroups(player, itemStack);
            this.groups = group_set.toArray(new IDietGroup[0]);
        }
    }

    public void renderHungerTooltip(GuiGraphics context, ResourceLocation texture, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight, int i) {
        FoodGroups foodGroup;
        if (groups == null || groups.length == 0) {
            foodGroup = FoodGroups.PROTEINS;
        } else {
            foodGroup = FoodGroups.byDietGroup(groups[i % groups.length]);
        }
        int group = foodGroup != null ? foodGroup.getTextureOffset() : 0;
        context.blit(GUI_HUNGER_ICONS_LOCATION, x, y, z, u - 16.0F, (float) group, width, height, 126, 45);
    }
}
