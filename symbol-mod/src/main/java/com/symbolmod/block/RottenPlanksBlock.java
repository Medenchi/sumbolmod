package com.symbolmod.block;

import com.symbolmod.registry.ModSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RottenPlanksBlock extends Block {
    private static final int COLLAPSE_DELAY = 10; // тиков до провала
    
    public RottenPlanksBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {
        if (!world.isClient && entity instanceof PlayerEntity) {
            // Запуск анимации провала
            world.scheduleBlockTick(pos, this, COLLAPSE_DELAY);
            
            // Звук треска
            world.playSound(null, pos, ModSounds.WOOD_CRACK, 
                SoundCategory.BLOCKS, 1.0f, 0.8f);
            
            // Частицы пыли
            if (world instanceof ServerWorld serverWorld) {
                for (int i = 0; i < 20; i++) {
                    serverWorld.spawnParticles(
                        ParticleTypes.SMOKE,
                        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                        1, 0.3, 0.1, 0.3, 0.01
                    );
                }
            }
        }
        super.onSteppedOn(world, pos, state, entity);
    }
    
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        // АНИМАЦИЯ ПРОВАЛА
        
        // 1. Звук разрушения
        world.playSound(null, pos, ModSounds.WOOD_COLLAPSE, 
            SoundCategory.BLOCKS, 1.5f, 0.5f);
        
        // 2. Массивные частицы
        for (int i = 0; i < 50; i++) {
            world.spawnParticles(
                ParticleTypes.LARGE_SMOKE,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                1, 0.5, 0.3, 0.5, 0.05
            );
            world.spawnParticles(
                ParticleTypes.ITEM_SLIME, // осколки
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                1, 0.4, 0.2, 0.4, 0.1
            );
        }
        
        // 3. Удаление блока
        world.removeBlock(pos, false);
        
        // 4. Урон игроку в радиусе
        world.getEntitiesByClass(PlayerEntity.class, 
            new net.minecraft.util.math.Box(pos).expand(1.5), 
            player -> true
        ).forEach(player -> {
            player.damage(world.getDamageSources().fall(), 4.0f);
            player.addVelocity(0, -0.5, 0); // толкает вниз
        });
    }
}
