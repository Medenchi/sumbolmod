package com.symbolmod.paranoia;

import com.symbolmod.registry.ModSounds;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.random.Random;
import java.util.*;

public class ParanoiaSystem {

    private static final Map<UUID, Integer> PARANOIA_LEVELS = new HashMap<>();
    private static final int MAX_PARANOIA = 10;

    public static void init() {
        // Регистрируем тиковый обработчик
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(
            server -> server.getPlayerManager().getPlayerList().forEach(ParanoiaSystem::tickParanoia)
        );
    }

    // ============ УПРАВЛЕНИЕ УРОВНЕМ ============

    public static void addParanoia(PlayerEntity player, int amount) {
        UUID uuid = player.getUuid();
        int current = PARANOIA_LEVELS.getOrDefault(uuid, 0);
        int newLevel = Math.min(current + amount, MAX_PARANOIA);
        PARANOIA_LEVELS.put(uuid, newLevel);

        // Сообщение о нарастании паранойи
        if (newLevel >= 3 && current < 3) {
            player.sendMessage(
                net.minecraft.text.Text.literal("§8§o[Что-то не так...]"),
                true
            );
        } else if (newLevel >= 7 && current < 7) {
            player.sendMessage(
                net.minecraft.text.Text.literal("§4§o[Они знают что я здесь.]"),
                true
            );
        } else if (newLevel >= 10 && current < 10) {
            player.sendMessage(
                net.minecraft.text.Text.literal("§4§l[ЗА ТОБОЙ НАБЛЮДАЮТ]"),
                true
            );
        }
    }

    public static int getParanoia(PlayerEntity player) {
        return PARANOIA_LEVELS.getOrDefault(player.getUuid(), 0);
    }

    // ============ ТИКОВАЯ ОБРАБОТКА ЭФФЕКТОВ ============

    private static void tickParanoia(ServerPlayerEntity player) {
        int level = getParanoia(player);
        if (level == 0) return;

        Random random = player.getWorld().getRandom();
        int tickCount = (int) player.getWorld().getTime();

        // Уровень 1-3: редкие звуки
        if (level >= 1 && tickCount % 400 == 0) {
            if (random.nextFloat() < 0.3f) {
                player.playSound(
                    ModSounds.PARANOIA_BREATH,
                    SoundCategory.AMBIENT,
                    0.3f,
                    0.8f + random.nextFloat() * 0.4f
                );
            }
        }

        // Уровень 3-5: шёпот
        if (level >= 3 && tickCount % 300 == 0) {
            if (random.nextFloat() < 0.4f) {
                player.playSound(
                    ModSounds.PARANOIA_WHISPER,
                    SoundCategory.AMBIENT,
                    0.5f,
                    0.9f + random.nextFloat() * 0.2f
                );
            }
        }

        // Уровень 5-7: статика и сердцебиение
        if (level >= 5 && tickCount % 200 == 0) {
            if (random.nextFloat() < 0.5f) {
                player.playSound(
                    ModSounds.PARANOIA_HEARTBEAT,
                    SoundCategory.MASTER,
                    0.6f,
                    1.0f
                );
            }
        }

        // Уровень 7-9: звонок телефона
        if (level >= 7 && tickCount % 800 == 0) {
            if (random.nextFloat() < 0.25f) {
                player.playSound(
                    ModSounds.PARANOIA_PHONE,
                    SoundCategory.AMBIENT,
                    0.8f,
                    1.0f
                );

                player.sendMessage(
                    net.minecraft.text.Text.literal("§8§o[Телефон звонит. Но телефона нет.]"),
                    true
                );
            }
        }

        // Уровень 10: максимальная паранойя
        if (level >= 10 && tickCount % 100 == 0) {
            if (random.nextFloat() < 0.3f) {
                player.playSound(
                    ModSounds.PARANOIA_STATIC,
                    SoundCategory.MASTER,
                    1.0f,
                    1.0f
                );
            }

            // Добавляем эффекты замедления и слепоты
            player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                net.minecraft.entity.effect.StatusEffects.DARKNESS,
                40, 0, false, false
            ));
        }
    }

    // ============ ВИЗУАЛЬНЫЕ ЭФФЕКТЫ (КЛИЕНТ) ============

    public static void initClient() {
        // Наложение паранойи на экран
        net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.EVENT.register(
            (matrices, tickDelta) -> {
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player == null) return;

                int level = getParanoia(client.player);
                if (level < 3) return;

                // Красный виньет по краям экрана
                renderParanoiaVignette(matrices, level);

                // Мерцание при уровне 7+
                if (level >= 7 && client.player.getWorld().getTime() % 20 < 2) {
                    renderScreenFlicker(matrices);
                }
            }
        );
    }

    private static void renderParanoiaVignette(
        net.minecraft.client.util.math.MatrixStack matrices, int level) {

        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        float alpha = (level / (float) MAX_PARANOIA) * 0.4f;

        // Красный виньет — рисуем по краям
        net.minecraft.client.gui.DrawContext context =
            new net.minecraft.client.gui.DrawContext(
                client, client.getBufferBuilders().getEntityVertexConsumers()
            );

        // Верхний край
        context.fill(0, 0, width, 30, (int)(alpha * 255) << 24 | 0xFF0000);
        // Нижний край
        context.fill(0, height - 30, width, height, (int)(alpha * 255) << 24 | 0xFF0000);
        // Левый край
        context.fill(0, 0, 30, height, (int)(alpha * 255) << 24 | 0xFF0000);
        // Правый край
        context.fill(width - 30, 0, width, height, (int)(alpha * 255) << 24 | 0xFF0000);
    }

    private static void renderScreenFlicker(
        net.minecraft.client.util.math.MatrixStack matrices) {

        MinecraftClient client = MinecraftClient.getInstance();
        int width = client.getWindow().getScaledWidth();
        int height = client.getWindow().getScaledHeight();

        // Лёгкое белое мерцание
        net.minecraft.client.gui.DrawContext context =
            new net.minecraft.client.gui.DrawContext(
                client, client.getBufferBuilders().getEntityVertexConsumers()
            );
        context.fill(0, 0, width, height, 0x15FFFFFF);
    }
}
