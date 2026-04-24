package com.symbolmod.entity;

import com.symbolmod.SymbolMod;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class NPCModel extends GeoModel<NPCEntity> {
    
    @Override
    public Identifier getModelResource(NPCEntity entity) {
        // Разные модели для разных NPC
        String npcId = entity.getNpcId();
        return new Identifier(SymbolMod.MOD_ID, "geo/" + npcId + ".geo.json");
    }
    
    @Override
    public Identifier getTextureResource(NPCEntity entity) {
        String npcId = entity.getNpcId();
        return new Identifier(SymbolMod.MOD_ID, "textures/entity/" + npcId + ".png");
    }
    
    @Override
    public Identifier getAnimationResource(NPCEntity entity) {
        String npcId = entity.getNpcId();
        return new Identifier(SymbolMod.MOD_ID, "animations/" + npcId + ".animation.json");
    }
}
