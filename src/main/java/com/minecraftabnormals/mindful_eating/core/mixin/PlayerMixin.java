package com.minecraftabnormals.mindful_eating.core.mixin;

import com.illusivesoulworks.diet.api.DietApi;
import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.illusivesoulworks.diet.common.DietApiImpl;
import com.minecraftabnormals.mindful_eating.core.ExhaustionSource;
import com.minecraftabnormals.mindful_eating.core.MindfulEatingFabric;
import com.minecraftabnormals.mindful_eating.core.ext.MindfulEatingPlayer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Set;

import static com.minecraftabnormals.mindful_eating.core.registry.other.MEEvents.*;
import static java.lang.Math.max;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements MindfulEatingPlayer {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique private static final EntityDataAccessor<String> LAST_FOOD_ID; //TrackedData.Builder.create(DataProcessors.RESOURCE_LOCATION, () -> new ResourceLocation("cooked_beef")).enableSaving().build();
    @Unique private static final EntityDataAccessor<Integer> SHEEN_COOLDOWN_ID; //TrackedData.Builder.create(DataProcessors.INT, () -> 0).enableSaving().build();
    @Unique private static final EntityDataAccessor<Boolean> HURT_OR_HEAL_ID; //TrackedData.Builder.create(DataProcessors.BOOLEAN, () -> false).enableSaving().build();

    static {
        LAST_FOOD_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.STRING);
        SHEEN_COOLDOWN_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.INT);
        HURT_OR_HEAL_ID = SynchedEntityData.defineId(Player.class, EntityDataSerializers.BOOLEAN);
    }

    public ResourceLocation mindful_eating$getLastFood() {
        ResourceLocation resourceLocation = ResourceLocation.tryParse(this.entityData.get(LAST_FOOD_ID));
        if (resourceLocation == null) {
            resourceLocation = BuiltInRegistries.ITEM.getKey(Items.COOKED_BEEF);
            mindful_eating$setLastFood(resourceLocation);
        }
        return resourceLocation;
    }

    public void mindful_eating$setLastFood(ResourceLocation lastFood) {
        this.entityData.set(LAST_FOOD_ID, lastFood.toString());
    }

    public int mindful_eating$getSheenCooldown() {
        return this.entityData.get(SHEEN_COOLDOWN_ID);
    }

    public void mindful_eating$setSheenCooldown(int cooldown) {
        this.entityData.set(SHEEN_COOLDOWN_ID, cooldown);
    }

    public boolean mindful_eating$getHurtOrHeal() {
        return this.entityData.get(HURT_OR_HEAL_ID);
    }

    public void mindful_eating$setHurtOrHeal(boolean hurtOrHeal) {
        this.entityData.set(HURT_OR_HEAL_ID, hurtOrHeal);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    protected void tail(CallbackInfo ci) {
        Player player = (((Player) (Object) this));

        if (player.getActiveEffects().size() != 0) {
            for (MobEffectInstance effect : player.getActiveEffects()) {
                if (effect.getEffect() == MobEffects.HUNGER) {
                    player.causeFoodExhaustion(0.0025F * (float) (player.getEffect(MobEffects.HUNGER).getAmplifier() + 1) * exhaustionReductionShortSheen(player, ExhaustionSource.EFFECT));
                    break;
                }
            }
        }

        float reduction = 0;

        double disX = player.getX() - player.xOld;
        double disY = player.getY() - player.yOld;
        double disZ = player.getZ() - player.zOld;

        if (player.level().isClientSide ^ ((MindfulEatingPlayer) player).mindful_eating$getHurtOrHeal()) {
            ((MindfulEatingPlayer) player).mindful_eating$setSheenCooldown(max(0, ((MindfulEatingPlayer) player).mindful_eating$getSheenCooldown() - 1));
        }

        if (player.getDeltaMovement().length() == 0.0 || disX == 0.0 && disZ == 0.0) {
            return;
        }

        int distance = Math.round(Mth.sqrt((float) disX * (float) disX + (float) disZ * (float) disZ) * 100.0F);

        if (player.isSwimming() || player.isEyeInFluid(FluidTags.WATER)) {
            reduction = 0.0001F * exhaustionReductionShortSheen(player, ExhaustionSource.SWIM) * Math.round(Mth.sqrt((float) disX * (float) disX + (float) disZ * (float) disZ) * 100.0F);
        } else if (player.isInWater()) {
            reduction = 0.0001F * exhaustionReductionShortSheen(player, ExhaustionSource.SWIM) * distance;
        } else if (player.onGround() && player.isSprinting()) {
            reduction = 0.001F * exhaustionReductionShortSheen(player, ExhaustionSource.SPRINT) * distance;
        }

        player.getFoodData().addExhaustion(reduction);
    }

    @Inject(at = @At("TAIL"), method = "jumpFromGround")
    protected void jumpFromGround(CallbackInfo ci) {
        Player player = (((Player) (Object) this));
        float ratio = exhaustionReductionLongSheen(player, ExhaustionSource.JUMP);
        if (player.isSprinting()) {
            player.causeFoodExhaustion(0.2F * ratio);
        } else {
            player.causeFoodExhaustion(0.05F * ratio);
        }
    }

    @Inject(at = @At("TAIL"), method = "attack")
    protected void attack(CallbackInfo ci) {
        Player player = (((Player) (Object) this));
        float ratio = exhaustionReductionLongSheen(player, ExhaustionSource.ATTACK);
        player.causeFoodExhaustion(0.1F * ratio);
    }

    @Inject(at = @At("TAIL"), method = "hurt")
    protected void hurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        Player player = (((Player) (Object) this));
        float ratio = exhaustionReductionLongSheen(player, ExhaustionSource.HURT);
        player.causeFoodExhaustion(damageSource.getFoodExhaustion() * ratio);
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    protected void defineSynchedData(CallbackInfo ci) {
        this.entityData.define(LAST_FOOD_ID, BuiltInRegistries.ITEM.getKey(Items.COOKED_BEEF).toString());
        this.entityData.define(SHEEN_COOLDOWN_ID, 0);
        this.entityData.define(HURT_OR_HEAL_ID, false);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    protected void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putBoolean("HurtOrHeal", mindful_eating$getHurtOrHeal());
        compoundTag.putString("LastFoodEaten", mindful_eating$getLastFood().toString());
        compoundTag.putInt("SheenCooldown", mindful_eating$getSheenCooldown());
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    protected void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        Player player = (((Player) (Object) this));
        ResourceLocation lastFood = new ResourceLocation(compoundTag.getString("LastFoodEaten"));
        if (MindfulEatingFabric.DIET_API.getGroups(player, new ItemStack(BuiltInRegistries.ITEM.get(lastFood))).isEmpty()) {
            lastFood = BuiltInRegistries.ITEM.getKey(Items.COOKED_BEEF);
        }

        mindful_eating$setHurtOrHeal(compoundTag.getBoolean("HurtOrHeal"));
        mindful_eating$setLastFood(lastFood);
        mindful_eating$setSheenCooldown(compoundTag.getInt("SheenCooldown"));
    }
}













