package com.symbolmod.registry;

import com.symbolmod.SymbolMod;
import com.symbolmod.item.DirectorWandItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {
    
    // Палочка Режиссёра
    public static final Item DIRECTOR_WAND = registerItem("director_wand",
        new DirectorWandItem(new FabricItemSettings()
            .maxCount(1)
            .rarity(Rarity.EPIC)));
    
    // Дневник улик (книга)
    public static final Item CLUE_DIARY = registerItem("clue_diary",
        new Item(new FabricItemSettings()
            .maxCount(1)
            .rarity(Rarity.RARE)));
    
    
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, 
            new Identifier(SymbolMod.MOD_ID, name), item);
    }
    
    public static void register() {
        SymbolMod.LOGGER.info("Регистрация предметов для " + SymbolMod.MOD_ID);
    }
}
