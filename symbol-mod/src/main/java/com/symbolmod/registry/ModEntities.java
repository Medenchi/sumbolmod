package com.symbolmod.registry;

import com.symbolmod.SymbolMod;
import com.symbolmod.entity.*;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    
    // NPC персонажи
    public static final EntityType<NPCEntity> DETECTIVE = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(SymbolMod.MOD_ID, "detective"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f)) // размер Steve
            .build()
    );
    
    public static final EntityType<NPCEntity> BOSS = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(SymbolMod.MOD_ID, "boss"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build()
    );
    
    public static final EntityType<NPCEntity> BABUSHKA_NINA = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(SymbolMod.MOD_ID, "babushka_nina"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build()
    );
    
    public static final EntityType<NPCEntity> RASHID = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(SymbolMod.MOD_ID, "rashid"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build()
    );
    
    public static final EntityType<NPCEntity> TOLYA = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(SymbolMod.MOD_ID, "tolya"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build()
    );
    
    public static final EntityType<NPCEntity> SEMYONYCH = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(SymbolMod.MOD_ID, "semyonych"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build()
    );
    
    public static final EntityType<NPCEntity> GROMOV = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(SymbolMod.MOD_ID, "gromov"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build()
    );
    
    public static final EntityType<NPCEntity> VALERIYA = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(SymbolMod.MOD_ID, "valeriya"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build()
    );
    
    public static final EntityType<NPCEntity> MOTHER = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(SymbolMod.MOD_ID, "mother"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build()
    );
    
    public static final EntityType<NPCEntity> FATHER = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(SymbolMod.MOD_ID, "father"),
        FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NPCEntity::new)
            .dimensions(EntityDimensions.fixed(0.6f, 1.8f))
            .build()
    );
    
    public static void register() {
        SymbolMod.LOGGER.info("Регистрация NPC для " + SymbolMod.MOD_ID);
    }
}
