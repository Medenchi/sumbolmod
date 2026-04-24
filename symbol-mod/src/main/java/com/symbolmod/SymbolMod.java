package com.symbolmod;

import net.fabricmc.api.ModInitializer;
import com.symbolmod.registry.*;
import com.symbolmod.story.StoryManager;
import com.symbolmod.paranoia.ParanoiaSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymbolMod implements ModInitializer {
    public static final String MOD_ID = "symbolmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    @Override
    public void onInitialize() {
        LOGGER.info("=== СИМВОЛ МОД ЗАГРУЖАЕТСЯ ===");
        
        // Регистрация всех элементов
        ModBlocks.register();
        ModItems.register();
        ModEntities.register();
        ModSounds.register();
        
        // Инициализация систем
        StoryManager.init();
        ParanoiaSystem.init();
        
        LOGGER.info("=== МОД СИМВОЛ ЗАГРУЖЕН ===");
    }
}
