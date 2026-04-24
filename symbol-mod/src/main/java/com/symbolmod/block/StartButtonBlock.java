package com.symbolmod.block;

import com.symbolmod.cutscene.CutsceneManager;
import com.symbolmod.util.LetterboxRenderer;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class StartButtonBlock extends Block {
    // 3D модель кнопки (парящая, 1.5 блока высотой)
    private static final VoxelShape SHAPE = Block.createCuboidShape(
        2.0, 0.0, 2.0, 14.0, 24.0, 14.0
    );
    
    public StartButtonBlock(Settings settings) {
        super(settings);
    }
    
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, 
                                     BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
    
    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, 
                             PlayerEntity player, Hand hand, BlockHitResult hit) {
        
        if (!world.isClient) {
            // Звук нажатия
            world.playSound(null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, 
                SoundCategory.BLOCKS, 1.0f, 0.7f);
            
            // Частицы
            if (world instanceof ServerWorld serverWorld) {
                for (int i = 0; i < 30; i++) {
                    serverWorld.spawnParticles(
                        ParticleTypes.END_ROD,
                        pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5,
                        1, 0.3, 0.3, 0.3, 0.1
                    );
                }
            }
            
            // Запуск АКТ 0 - Увольнение
            CutsceneManager.startCutscene(player, "act0_firing");
            
            // Включение чёрных полос
            LetterboxRenderer.enable(player);
            
            // Удаление кнопки
            world.removeBlock(pos, false);
        }
        
        return ActionResult.SUCCESS;
    }
    
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL; // используем JSON модель
    }
}
