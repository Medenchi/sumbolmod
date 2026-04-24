package com.symbolmod.cutscene;

import com.symbolmod.SymbolMod;
import com.symbolmod.registry.ModSounds;
import com.symbolmod.util.LetterboxRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.*;

public class CutsceneManager {
    
    private static final Map<String, CutsceneData> CUTSCENES = new HashMap<>();
    private static final Map<UUID, ActiveCutscene> ACTIVE_CUTSCENES = new HashMap<>();
    
    // ============ ИНИЦИАЛИЗАЦИЯ КАТСЦЕН ============
    
    public static void init() {
        // АКТ 0 - Увольнение
        registerCutscene("act0_firing", new CutsceneData()
            .addSegment(new CameraPath()
                .setDuration(45) // секунд
                .setPath("building_exterior") // движение камеры вверх по зданию
                .setMusic("act0_intro"))
            .addSegment(new CameraPath()
                .setDuration(165)
                .setPath("office_walk")
                .enablePlayerControl(false))
            .addDialogue("boss", "boss_firing", 0)
            .addDialogue("detective", "detective_reaction", 5)
        );
        
        // АКТ 1 - Стекло (финальная комната)
        registerCutscene("act1_glass_room", new CutsceneData()
            .addSegment(new CameraPath()
                .setDuration(180)
                .setPath("basement_reveal")
                .setMusic("act1_horror"))
            .addDialogue("detective", "glass_discovery", 0)
        );
        
        // АКТ 2 - Деревня (встреча с Громовым)
        registerCutscene("act2_gromov_talk", new CutsceneData()
            .addSegment(new CameraPath()
                .setDuration(240)
                .setPath("gromov_house")
                .enablePlayerControl(false))
            .addDialogue("gromov", "gromov_truth", 0)
            .addDialogue("detective", "detective_realization", 120)
        );
        
        // Все 4 концовки
        registerEndings();
    }
    
    private static void registerEndings() {
        // Концовка A - Правда
        registerCutscene("ending_a_truth", new CutsceneData()
            .addSegment(new CameraPath()
                .setDuration(180)
                .setPath("village_evacuation")
                .setMusic("ending_a"))
            .addDialogue("detective", "ending_a_monologue", 0)
            .addCredits(120)
        );
        
        // Концовка B - Замена
        registerCutscene("ending_b_replacement", new CutsceneData()
            .addSegment(new CameraPath()
                .setDuration(120)
                .setPath("village_integration")
                .setMusic("ending_b_dark"))
            .addDialogue("detective", "ending_b_acceptance", 0)
        );
        
        // Концовка C - Отрицание
        registerCutscene("ending_c_denial", new CutsceneData()
            .addSegment(new CameraPath()
                .setDuration(90)
                .setPath("detective_apartment")
                .setMusic("ending_c_false"))
            .addDialogue("detective", "ending_c_lie", 0)
            .addTwist(60) // символ на кружке
        );
        
        // Концовка D - Наблюдатель
        registerCutscene("ending_d_observer", new CutsceneData()
            .addSegment(new CameraPath()
                .setDuration(150)
                .setPath("village_circle")
                .setMusic("ending_d_horror"))
            .addDialogue("gromov", "ending_d_final_words", 0)
        );
    }
    
    // ============ ЗАПУСК КАТСЦЕНЫ ============
    
    public static void startCutscene(PlayerEntity player, String cutsceneId) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        
        CutsceneData data = CUTSCENES.get(cutsceneId);
        if (data == null) {
            SymbolMod.LOGGER.error("Катсцена не найдена: " + cutsceneId);
            return;
        }
        
        // Создаём активную катсцену
        ActiveCutscene active = new ActiveCutscene(serverPlayer, data);
        ACTIVE_CUTSCENES.put(player.getUuid(), active);
        
        // Включаем чёрные полосы
        LetterboxRenderer.enable(player);
        
        // Блокируем управление
        freezePlayer(serverPlayer);
        
        // Запускаем музыку
        if (data.music != null) {
            serverPlayer.playSound(
                ModSounds.getMusic(data.music), 
                SoundCategory.MUSIC, 
                0.7f, 
                1.0f
            );
        }
        
        // Отправляем сообщение
        serverPlayer.sendMessage(Text.literal("§7[Катсцена началась]"), true);
    }
    
    // ============ ОБНОВЛЕНИЕ КАТСЦЕН (вызывается каждый тик) ============
    
    public static void tick() {
        Iterator<Map.Entry<UUID, ActiveCutscene>> iterator = ACTIVE_CUTSCENES.entrySet().iterator();
        
        while (iterator.hasNext()) {
            Map.Entry<UUID, ActiveCutscene> entry = iterator.next();
            ActiveCutscene cutscene = entry.getValue();
            
            cutscene.tick();
            
            if (cutscene.isFinished()) {
                endCutscene(cutscene.player);
                iterator.remove();
            }
        }
    }
    
    private static void endCutscene(ServerPlayerEntity player) {
        // Отключаем чёрные полосы
        LetterboxRenderer.disable(player);
        
        // Разблокируем управление
        unfreezePlayer(player);
        
        player.sendMessage(Text.literal("§7[Катсцена завершена]"), true);
    }
    
    // ============ УПРАВЛЕНИЕ ИГРОКОМ ============
    
    private static void freezePlayer(ServerPlayerEntity player) {
        // Сохраняем NBT тег для блокировки движений
        player.getDataTracker().set(/* custom data tracker */, true);
    }
    
    private static void unfreezePlayer(ServerPlayerEntity player) {
        player.getDataTracker().set(/* custom data tracker */, false);
    }
    
    private static void registerCutscene(String id, CutsceneData data) {
        CUTSCENES.put(id, data);
    }
    
    // ============ ВСПОМОГАТЕЛЬНЫЕ КЛАССЫ ============
    
    public static class CutsceneData {
        private List<CameraPath> segments = new ArrayList<>();
        private Map<Integer, DialogueEntry> dialogues = new HashMap<>();
        private String music;
        private boolean hasCredits;
        private int twistTime = -1;
        
        public CutsceneData addSegment(CameraPath path) {
            segments.add(path);
            return this;
        }
        
        public CutsceneData addDialogue(String speaker, String dialogueId, int startTick) {
            dialogues.put(startTick, new DialogueEntry(speaker, dialogueId));
            return this;
        }
        
        public CutsceneData addCredits(int startTick) {
            this.hasCredits = true;
            return this;
        }
        
        public CutsceneData addTwist(int tick) {
            this.twistTime = tick;
            return this;
        }
    }
    
    private static class DialogueEntry {
        String speaker;
        String dialogueId;
        
        DialogueEntry(String speaker, String dialogueId) {
            this.speaker = speaker;
            this.dialogueId = dialogueId;
        }
    }
    
    private static class ActiveCutscene {
        ServerPlayerEntity player;
        CutsceneData data;
        int currentTick = 0;
        int currentSegment = 0;
        
        ActiveCutscene(ServerPlayerEntity player, CutsceneData data) {
            this.player = player;
            this.data = data;
        }
        
        void tick() {
            currentTick++;
            
            // Проверяем диалоги
            if (data.dialogues.containsKey(currentTick)) {
                DialogueEntry entry = data.dialogues.get(currentTick);
                // Воспроизводим диалог
                player.sendMessage(Text.literal("§e" + entry.speaker + ": §f[Диалог " + entry.dialogueId + "]"), false);
            }
        }
        
        boolean isFinished() {
            return currentSegment >= data.segments.size();
        }
    }
}
