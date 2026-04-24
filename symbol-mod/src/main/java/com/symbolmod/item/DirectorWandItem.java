package com.symbolmod.item;

import com.symbolmod.ui.DirectorWandScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DirectorWandItem extends Item {
    
    public DirectorWandItem(Settings settings) {
        super(settings);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        
        if (world.isClient) {
            // Открываем GUI палочки
            MinecraftClient.getInstance().setScreen(new DirectorWandScreen());
            
            // Звук магии
            world.playSound(player, player.getBlockPos(), 
                SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, 
                SoundCategory.PLAYERS, 0.5f, 1.5f);
            
            // Частицы вокруг игрока
            for (int i = 0; i < 20; i++) {
                double angle = (i / 20.0) * Math.PI * 2;
                double x = player.getX() + Math.cos(angle) * 1.5;
                double z = player.getZ() + Math.sin(angle) * 1.5;
                world.addParticle(ParticleTypes.ENCHANT, 
                    x, player.getY() + 1.0, z, 
                    0, 0.05, 0);
            }
        }
        
        return TypedActionResult.success(stack);
    }
    
    @Override
    public boolean hasGlint(ItemStack stack) {
        return true; // всегда светится как зачарованный предмет
    }
}
