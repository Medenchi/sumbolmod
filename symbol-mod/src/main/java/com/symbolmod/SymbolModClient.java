package com.symbolmod;

import com.symbolmod.entity.*;
import com.symbolmod.paranoia.ParanoiaSystem;
import com.symbolmod.util.LetterboxRenderer;
import com.symbolmod.registry.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class SymbolModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        // Регистрация рендереров NPC
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            ModEntities.DETECTIVE,
            ctx -> new GeoEntityRenderer<>(ctx, new NPCModel())
        );
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            ModEntities.BOSS,
            ctx -> new GeoEntityRenderer<>(ctx, new NPCModel())
        );
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            ModEntities.BABUSHKA_NINA,
            ctx -> new GeoEntityRenderer<>(ctx, new NPCModel())
        );
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            ModEntities.RASHID,
            ctx -> new GeoEntityRenderer<>(ctx, new NPCModel())
        );
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            ModEntities.TOLYA,
            ctx -> new GeoEntityRenderer<>(ctx, new NPCModel())
        );
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            ModEntities.SEMYONYCH,
            ctx -> new GeoEntityRenderer<>(ctx, new NPCModel())
        );
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            ModEntities.GROMOV,
            ctx -> new GeoEntityRenderer<>(ctx, new NPCModel())
        );
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            ModEntities.VALERIYA,
            ctx -> new GeoEntityRenderer<>(ctx, new NPCModel())
        );
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            ModEntities.MOTHER,
            ctx -> new GeoEntityRenderer<>(ctx, new NPCModel())
        );
        net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry.register(
            ModEntities.FATHER,
            ctx -> new GeoEntityRenderer<>(ctx, new NPCModel())
        );

        // Инициализация клиентских систем
        LetterboxRenderer.initClient();
        ParanoiaSystem.initClient();
    }
}
