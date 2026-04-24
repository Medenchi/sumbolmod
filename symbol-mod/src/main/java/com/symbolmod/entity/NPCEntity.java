package com.symbolmod.entity;

import com.symbolmod.dialogue.DialogueSystem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NPCEntity extends PathAwareEntity implements GeoEntity {
    
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private String npcId = "generic";
    private String currentAnimation = "idle";
    
    public NPCEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(false);
    }
    
    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(2, new LookAroundGoal(this));
    }
    
    // ============ ДИАЛОГИ ============
    
    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        if (!this.getWorld().isClient) {
            // Открываем диалог с этим NPC
            DialogueSystem.openDialogue(player, this.npcId);
        }
        return ActionResult.SUCCESS;
    }
    
    // ============ GECKOLIB АНИМАЦИИ ============
    
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main_controller", 5, this::predicate));
    }
    
    private PlayState predicate(AnimationState<NPCEntity> state) {
        // Динамическая смена анимаций
        switch (currentAnimation) {
            case "talk":
                state.setAnimation(RawAnimation.begin().thenPlay("talk"));
                break;
            case "walk":
                if (state.isMoving()) {
                    state.setAnimation(RawAnimation.begin().thenLoop("walk"));
                } else {
                    state.setAnimation(RawAnimation.begin().thenLoop("idle"));
                }
                break;
            case "angry":
                state.setAnimation(RawAnimation.begin().thenPlay("angry"));
                break;
            case "sad":
                state.setAnimation(RawAnimation.begin().thenLoop("sad"));
                break;
            case "sit":
                state.setAnimation(RawAnimation.begin().thenLoop("sit"));
                break;
            default:
                state.setAnimation(RawAnimation.begin().thenLoop("idle"));
        }
        return PlayState.CONTINUE;
    }
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
    
    // ============ УПРАВЛЕНИЕ АНИМАЦИЯМИ ============
    
    public void playAnimation(String animName) {
        this.currentAnimation = animName;
    }
    
    public void setNpcId(String id) {
        this.npcId = id;
    }
    
    public String getNpcId() {
        return this.npcId;
    }
    
    // ============ СОХРАНЕНИЕ ============
    
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putString("NpcId", this.npcId);
        nbt.putString("CurrentAnimation", this.currentAnimation);
    }
    
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.npcId = nbt.getString("NpcId");
        this.currentAnimation = nbt.getString("CurrentAnimation");
    }
}
