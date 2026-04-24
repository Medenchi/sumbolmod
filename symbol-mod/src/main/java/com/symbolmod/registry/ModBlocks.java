package com.symbolmod.registry;

import com.symbolmod.SymbolMod;
import com.symbolmod.block.*;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    
    // ============ ГЛАВНЫЕ БЛОКИ ============
    public static final Block START_BUTTON = registerBlock("start_button",
        new StartButtonBlock(FabricBlockSettings.of(Material.METAL)
            .strength(5.0f)
            .requiresTool()
            .luminance(7)
            .nonOpaque()));
    
    // ============ ДЕКОРАТИВНЫЕ БЛОКИ ============
    
    // ПРОГНИВШИЕ ДОСКИ (с анимацией провала!)
    public static final Block ROTTEN_PLANKS = registerBlock("rotten_planks",
        new RottenPlanksBlock(FabricBlockSettings.of(Material.WOOD)
            .strength(0.5f)
            .sounds(BlockSoundGroup.WOOD)));
    
    public static final Block ROTTEN_PLANKS_CRACKED = registerBlock("rotten_planks_cracked",
        new RottenPlanksBlock(FabricBlockSettings.of(Material.WOOD)
            .strength(0.3f)
            .sounds(BlockSoundGroup.WOOD)));
    
    public static final Block ROTTEN_PLANKS_COLLAPSED = registerBlock("rotten_planks_collapsed",
        new RottenPlanksBlock(FabricBlockSettings.of(Material.WOOD)
            .strength(0.1f)
            .sounds(BlockSoundGroup.WOOD)));
    
    // СОВЕТСКАЯ ПЛИТКА
    public static final Block SOVIET_TILES_WHITE = registerBlock("soviet_tiles_white",
        new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
            .strength(1.5f)
            .requiresTool()
            .sounds(BlockSoundGroup.STONE)));
    
    public static final Block SOVIET_TILES_GREEN = registerBlock("soviet_tiles_green",
        new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
            .strength(1.5f)
            .requiresTool()));
    
    public static final Block SOVIET_TILES_DIRTY = registerBlock("soviet_tiles_dirty",
        new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
            .strength(1.5f)
            .requiresTool()));
    
    // РЖАВЫЙ МЕТАЛЛ
    public static final Block RUSTY_METAL_PANEL = registerBlock("rusty_metal_panel",
        new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
            .strength(3.0f)
            .requiresTool()
            .sounds(BlockSoundGroup.METAL)));
    
    public static final Block RUSTY_METAL_HEAVY = registerBlock("rusty_metal_heavy",
        new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
            .strength(4.0f)
            .requiresTool()));
    
    // СТАРОЕ СТЕКЛО
    public static final Block OLD_GLASS = registerBlock("old_glass",
        new DecorativeBlock(FabricBlockSettings.of(Material.GLASS)
            .strength(0.3f)
            .sounds(BlockSoundGroup.GLASS)
            .nonOpaque()));
    
    public static final Block CRACKED_GLASS = registerBlock("cracked_glass",
        new DecorativeBlock(FabricBlockSettings.of(Material.GLASS)
            .strength(0.2f)
            .nonOpaque()));
    
    // БЕТОН
    public static final Block CONCRETE_SLAB_CLEAN = registerBlock("concrete_slab_clean",
        new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
            .strength(2.0f)
            .requiresTool()));
    
    public static final Block CONCRETE_SLAB_WORN = registerBlock("concrete_slab_worn",
        new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
            .strength(1.8f)));
    
    public static final Block CONCRETE_SLAB_BROKEN = registerBlock("concrete_slab_broken",
        new DecorativeBlock(FabricBlockSettings.of(Material.STONE)
            .strength(1.5f)));
    
    // ЛИНОЛЕУМ
    public static final Block LINOLEUM_BROWN = registerBlock("linoleum_brown",
        new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
            .strength(0.5f)
            .sounds(BlockSoundGroup.WOOL)));
    
    public static final Block LINOLEUM_PEELED = registerBlock("linoleum_peeled",
        new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
            .strength(0.3f)));
    
    // ОБШАРПАННЫЕ СТЕНЫ
    public static final Block WALLPAPER_FLORAL = registerBlock("wallpaper_floral",
        new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
            .strength(0.3f)));
    
    public static final Block WALLPAPER_TORN = registerBlock("wallpaper_torn",
        new DecorativeBlock(FabricBlockSettings.of(Material.WOOL)
            .strength(0.2f)));
    
    // ПРОМЫШЛЕННЫЕ ЭЛЕМЕНТЫ
    public static final Block PIPE_RUSTY = registerBlock("pipe_rusty",
        new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
            .strength(2.0f)
            .nonOpaque()));
    
    public static final Block VENT_GRATE = registerBlock("vent_grate",
        new DecorativeBlock(FabricBlockSettings.of(Material.METAL)
            .strength(1.5f)
            .nonOpaque()));
    
    
    // ============ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ============
    
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, 
            new Identifier(SymbolMod.MOD_ID, name), block);
    }
    
    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, 
            new Identifier(SymbolMod.MOD_ID, name),
            new BlockItem(block, new FabricItemSettings()));
    }
    
    public static void register() {
        SymbolMod.LOGGER.info("Регистрация блоков для " + SymbolMod.MOD_ID);
    }
}
