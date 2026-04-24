package com.symbolmod.story;

import com.symbolmod.cutscene.CutsceneManager;
import com.symbolmod.paranoia.ParanoiaSystem;
import com.symbolmod.registry.ModSounds;
import com.symbolmod.util.ClueCollector;
import com.symbolmod.util.LetterboxRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.*;

public class StoryManager {

    // Этапы сюжета
    public enum StoryAct {
        START_ROOM,
        ACT0_FIRING,
        ACT0_OFFICE,
        ACT0_STREET,
        ACT0_BUREAU,
        ACT0_HOME,
        ACT0_PARENTS,
        ACT1_FACTORY_ARRIVE,
        ACT1_FACTORY_EXPLORE,
        ACT1_BASEMENT,
        ACT1_FINAL_ROOM,
        ACT2_VILLAGE_ENTER,
        ACT2_VILLAGE_EXPLORE,
        ACT2_GROMOV,
        ACT3_DECISION,
        ENDING_A,
        ENDING_B,
        ENDING_C,
        ENDING_D,
        CREDITS
    }

    private static final Map<UUID, StoryState> PLAYER_STATES = new HashMap<>();

    // ============ ИНИЦИАЛИЗАЦИЯ ============

    public static void init() {
        // Тиковый обработчик
        net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents.END_SERVER_TICK.register(
            server -> server.getPlayerManager().getPlayerList()
                .forEach(StoryManager::tickStory)
        );

        // Обработчик входа игрока
        net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents.JOIN.register(
            (handler, sender, server) -> onPlayerJoin(handler.player)
        );
    }

    private static void onPlayerJoin(ServerPlayerEntity player) {
        StoryState state = loadState(player);
        PLAYER_STATES.put(player.getUuid(), state);

        // Если новый игрок — телепорт в стартовую комнату
        if (state.currentAct == StoryAct.START_ROOM) {
            player.sendMessage(
                Text.literal("§8§oДобро пожаловать в «Символ»..."),
                false
            );
        }
    }

    // ============ ПЕРЕХОД МЕЖДУ АКТАМИ ============

    public static void advanceAct(PlayerEntity player, StoryAct newAct) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        StoryState state = getState(serverPlayer);
        StoryAct oldAct = state.currentAct;
        state.currentAct = newAct;

        saveState(serverPlayer, state);

        handleActTransition(serverPlayer, oldAct, newAct);
    }

    private static void handleActTransition(ServerPlayerEntity player,
                                             StoryAct from, StoryAct to) {
        switch (to) {

            case ACT0_FIRING -> {
                // Чёрные полосы
                LetterboxRenderer.enable(player);

                // Музыка
                player.playSound(ModSounds.MUSIC_ACT0_INTRO,
                    SoundCategory.MUSIC, 0.6f, 1.0f);

                // Запуск катсцены увольнения
                CutsceneManager.startCutscene(player, "act0_firing");

                // Сообщение
                showActTitle(player, "АКТ 0", "УВОЛЬНЕНИЕ");
            }

            case ACT1_FACTORY_ARRIVE -> {
                LetterboxRenderer.enable(player);
                player.playSound(ModSounds.MUSIC_ACT1_EXPLORE,
                    SoundCategory.MUSIC, 0.5f, 1.0f);
                CutsceneManager.startCutscene(player, "act1_factory_arrive");
                showActTitle(player, "АКТ 1", "СТЕКЛО");
            }

            case ACT1_FINAL_ROOM -> {
                // Максимальная атмосфера
                player.playSound(ModSounds.MUSIC_ACT1_HORROR,
                    SoundCategory.MUSIC, 0.8f, 1.0f);
                CutsceneManager.startCutscene(player, "act1_glass_room");

                // Паранойя +3
                ParanoiaSystem.addParanoia(player, 3);
            }

            case ACT2_VILLAGE_ENTER -> {
                LetterboxRenderer.enable(player);
                player.playSound(ModSounds.MUSIC_ACT2_VILLAGE,
                    SoundCategory.MUSIC, 0.4f, 1.0f);
                showActTitle(player, "АКТ 2", "ДЕРЕВНЯ");
            }

            case ACT2_GROMOV -> {
                player.playSound(ModSounds.MUSIC_ACT3_CLIMAX,
                    SoundCategory.MUSIC, 0.7f, 1.0f);
                CutsceneManager.startCutscene(player, "act2_gromov_talk");
            }

            case ACT3_DECISION -> {
                // Финальный выбор
                showFinalChoice(player);
            }

            // ============ КОНЦОВКИ ============

            case ENDING_A -> EndingHandler.playEndingA(player);
            case ENDING_B -> EndingHandler.playEndingB(player);
            case ENDING_C -> EndingHandler.playEndingC(player);
            case ENDING_D -> EndingHandler.playEndingD(player);

            case CREDITS -> EndingHandler.playCredits(player);
        }
    }

    // ============ ФИНАЛЬНЫЙ ВЫБОР ============

    private static void showFinalChoice(ServerPlayerEntity player) {
        int paranoia = ParanoiaSystem.getParanoia(player);
        int clues = ClueCollector.getClueCount(player);

        player.sendMessage(
            Text.literal("§f[Что вы решили?]")
                .formatted(Formatting.ITALIC),
            false
        );

        // Выбор определяет концовку
        // Через систему 3D кнопок (ChoiceButton)
    }

    // ============ ТИКОВЫЕ ПРОВЕРКИ ============

    private static void tickStory(ServerPlayerEntity player) {
        StoryState state = getState(player);

        switch (state.currentAct) {
            case ACT1_FACTORY_EXPLORE -> {
                // Проверяем найдены ли ключевые улики
                int foundClues = ClueCollector.getClueCount(player);

                if (foundClues >= 5 && !state.hasFlag("basement_unlocked")) {
                    state.setFlag("basement_unlocked", true);
                    player.sendMessage(
                        Text.literal("§8§o[Вы нашли достаточно улик. Подвал теперь доступен.]"),
                        true
                    );
                }
            }

            case ACT2_VILLAGE_EXPLORE -> {
                int symbols = countSymbolsFound(player);

                if (symbols >= 6 && !state.hasFlag("paranoia_spike_1")) {
                    state.setFlag("paranoia_spike_1", true);
                    ParanoiaSystem.addParanoia(player, 2);

                    player.playSound(ModSounds.PARANOIA_STATIC,
                        SoundCategory.AMBIENT, 1.0f, 1.0f);
                }

                // Все символы найдены — открывается концовка D
                if (symbols >= 8 && !state.hasFlag("ending_d_unlocked")) {
                    state.setFlag("ending_d_unlocked", true);
                    player.sendMessage(
                        Text.literal("§4§o[Вы видите то, что не должны были видеть.]"),
                        true
                    );
                }
            }
        }
    }

    private static int countSymbolsFound(PlayerEntity player) {
        List<String> collected = ClueCollector.getCollectedClues(player);
        return (int) collected.stream()
            .filter(id -> id.startsWith("symbol_"))
            .count();
    }

    // ============ ЗАГОЛОВКИ АКТОВ ============

    private static void showActTitle(ServerPlayerEntity player,
                                      String act, String title) {
        // Показываем через title команду
        player.getServer().getCommandManager().executeWithPrefix(
            player.getServer().getCommandSource(),
            "title " + player.getName().getString() +
            " title {\"text\":\"" + act + "\",\"color\":\"gray\"}"
        );

        player.getServer().getCommandManager().executeWithPrefix(
            player.getServer().getCommandSource(),
            "title " + player.getName().getString() +
            " subtitle {\"text\":\"— " + title + " —\",\"color\":\"white\",\"italic\":true}"
        );
    }

    // ============ СОСТОЯНИЕ ИГРОКА ============

    private static StoryState getState(PlayerEntity player) {
        return PLAYER_STATES.computeIfAbsent(
            player.getUuid(),
            k -> new StoryState()
        );
    }

    private static StoryState loadState(ServerPlayerEntity player) {
        // Загрузка из NBT игрока
        NbtCompound nbt = player.getDataTracker()
            .get(com.symbolmod.util.PlayerDataTracker.SYMBOL_DATA);

        if (nbt == null || !nbt.contains("CurrentAct")) {
            return new StoryState();
        }

        StoryState state = new StoryState();
        try {
            state.currentAct = StoryAct.valueOf(nbt.getString("CurrentAct"));
        } catch (Exception e) {
            state.currentAct = StoryAct.START_ROOM;
        }

        return state;
    }

    private static void saveState(ServerPlayerEntity player, StoryState state) {
        NbtCompound nbt = new NbtCompound();
        nbt.putString("CurrentAct", state.currentAct.name());
        // Флаги сохраняем тоже
        NbtCompound flags = new NbtCompound();
        state.flags.forEach(flags::putBoolean);
        nbt.put("Flags", flags);
    }

    // ============ КЛАСС СОСТОЯНИЯ ============

    public static class StoryState {
        public StoryAct currentAct = StoryAct.START_ROOM;
        public Map<String, Boolean> flags = new HashMap<>();

        public void setFlag(String key, boolean value) {
            flags.put(key, value);
        }

        public boolean hasFlag(String key) {
            return flags.getOrDefault(key, false);
        }
    }

    // ============================================================
    // ВЛОЖЕННЫЙ КЛАСС КОНЦОВОК
    // ============================================================

    public static class EndingHandler {

        public static void playEndingA(ServerPlayerEntity player) {
            // Звонок в ФСБ
            LetterboxRenderer.enable(player);
            player.playSound(ModSounds.MUSIC_ENDING_A,
                SoundCategory.MUSIC, 0.8f, 1.0f);
            CutsceneManager.startCutscene(player, "ending_a_truth");

            // Финальный текст
            scheduleMessage(player, 200,
                "§7Папа позвонил сегодня. Первый раз сам."
            );
            scheduleMessage(player, 260,
                "§7Спросил, как дела. Я сказал — хорошо."
            );
            scheduleMessage(player, 320,
                "§7Мы оба знали, что говорим не только об этом."
            );
            scheduleMessage(player, 420,
                "§8Восьмой отдел официально расформирован."
            );
            scheduleMessage(player, 480,
                "§8Деревня Стеклово продолжает существовать."
            );

            // Через 600 тиков — титры
            scheduleAct(player, 600, StoryAct.CREDITS);
        }

        public static void playEndingB(ServerPlayerEntity player) {
            player.playSound(ModSounds.MUSIC_ENDING_B,
                SoundCategory.MUSIC, 0.7f, 1.0f);
            CutsceneManager.startCutscene(player, "ending_b_replacement");

            scheduleMessage(player, 200,
                "§7Дело №47 закрыто."
            );
            scheduleMessage(player, 260,
                "§7Детектив вышел на пенсию через три месяца."
            );
            scheduleMessage(player, 320,
                "§8§oПереехал в деревню Стеклово."
            );

            scheduleAct(player, 500, StoryAct.CREDITS);
        }

        public static void playEndingC(ServerPlayerEntity player) {
            player.playSound(ModSounds.MUSIC_ENDING_C,
                SoundCategory.MUSIC, 0.6f, 1.0f);
            CutsceneManager.startCutscene(player, "ending_c_denial");

            scheduleMessage(player, 200,
                "§7Серийный убийца — Громов В.С. Тела найдены. Дело закрыто."
            );
            scheduleMessage(player, 300,
                "§8§o[Детектив переворачивает кружку]"
            );
            scheduleMessage(player, 360,
                "§4§l[Символ]"  // символ на кружке — твист
            );
            scheduleMessage(player, 420,
                "§8Восьмой отдел учёл новое местонахождение субъекта для наблюдения."
            );

            scheduleAct(player, 550, StoryAct.CREDITS);
        }

        public static void playEndingD(ServerPlayerEntity player) {
            // Максимальная паранойя требуется
            player.playSound(ModSounds.MUSIC_ENDING_D,
                SoundCategory.MUSIC, 1.0f, 1.0f);
            CutsceneManager.startCutscene(player, "ending_d_observer");

            // Темнота экрана
            player.addStatusEffect(new net.minecraft.entity.effect.StatusEffectInstance(
                net.minecraft.entity.effect.StatusEffects.DARKNESS,
                200, 2, false, false
            ));

            scheduleMessage(player, 300,
                "§4§lНаблюдение продолжается."
            );

            scheduleAct(player, 500, StoryAct.CREDITS);
        }

        public static void playCredits(ServerPlayerEntity player) {
            LetterboxRenderer.enable(player);

            // Музыка — тихая, грустная
            player.playSound(ModSounds.MUSIC_ENDING_A,
                SoundCategory.MUSIC, 0.3f, 0.8f);

            // Финальный твист — символ в офисе Аргус
            scheduleMessage(player, 100,
                "§8§o..."
            );
            scheduleMessage(player, 200,
                "§7Стеклозавод №7 был закрыт."
            );
            scheduleMessage(player, 300,
                "§4Стеклозаводы №1–6 не были найдены."
            );

            // Через 500 тиков — возврат в стартовую комнату
            scheduleReturnToStart(player, 700);
        }

        // ============ ВСПОМОГАТЕЛЬНЫЕ ============

        private static void scheduleMessage(ServerPlayerEntity player,
                                             int ticks, String message) {
            player.getServer().send(
                new net.minecraft.util.thread.ReentrantThreadExecutor.Task<>(
                    () -> {
                        try {
                            Thread.sleep(ticks * 50L); // 1 тик = 50мс
                        } catch (InterruptedException e) { /* */ }
                        player.sendMessage(Text.literal(message), false);
                        return null;
                    }
                )
            );
        }

        private static void scheduleAct(ServerPlayerEntity player,
                                         int ticks, StoryAct act) {
            player.getServer().execute(() -> {
                try {
                    Thread.sleep(ticks * 50L);
                } catch (InterruptedException e) { /* */ }
                advanceAct(player, act);
            });
        }

        private static void scheduleReturnToStart(ServerPlayerEntity player, int ticks) {
            player.getServer().execute(() -> {
                try {
                    Thread.sleep(ticks * 50L);
                } catch (InterruptedException e) { /* */ }

                // Телепорт в стартовую комнату
                player.teleport(0, 64, 0); // координаты стартовой комнаты
                advanceAct(player, StoryAct.START_ROOM);

                player.sendMessage(
                    Text.literal("§8§o[Нажмите кнопку «НАЧАТЬ» чтобы пройти снова]"),
                    true
                );
            });
        }
    }
}
