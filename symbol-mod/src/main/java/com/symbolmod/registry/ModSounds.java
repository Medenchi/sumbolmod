package com.symbolmod.registry;

import com.symbolmod.SymbolMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    // ============ ЗВУКИ БЛОКОВ ============
    public static final SoundEvent WOOD_CRACK = register("wood_crack");
    public static final SoundEvent WOOD_COLLAPSE = register("wood_collapse");
    public static final SoundEvent BUTTON_START = register("button_start");
    public static final SoundEvent HEAVY_DOOR = register("heavy_door");
    public static final SoundEvent INTERCOM_BUZZ = register("intercom_buzz");
    public static final SoundEvent CLUE_DISCOVERED = register("clue_discovered");

    // ============ МУЗЫКА ============
    public static final SoundEvent MUSIC_ACT0_INTRO = register("music.act0_intro");
    public static final SoundEvent MUSIC_ACT1_EXPLORE = register("music.act1_explore");
    public static final SoundEvent MUSIC_ACT1_HORROR = register("music.act1_horror");
    public static final SoundEvent MUSIC_ACT2_VILLAGE = register("music.act2_village");
    public static final SoundEvent MUSIC_ACT2_PARANOIA = register("music.act2_paranoia");
    public static final SoundEvent MUSIC_ACT3_CLIMAX = register("music.act3_climax");
    public static final SoundEvent MUSIC_ENDING_A = register("music.ending_a");
    public static final SoundEvent MUSIC_ENDING_B = register("music.ending_b_dark");
    public static final SoundEvent MUSIC_ENDING_C = register("music.ending_c_false");
    public static final SoundEvent MUSIC_ENDING_D = register("music.ending_d_horror");

    // ============ ПАРАНОЙЯ ЗВУКИ ============
    public static final SoundEvent PARANOIA_HEARTBEAT = register("paranoia.heartbeat");
    public static final SoundEvent PARANOIA_WHISPER = register("paranoia.whisper");
    public static final SoundEvent PARANOIA_STATIC = register("paranoia.static");
    public static final SoundEvent PARANOIA_BREATH = register("paranoia.breath");
    public static final SoundEvent PARANOIA_PHONE = register("paranoia.phone_ring");

    // ============ УЛИКИ — ОЗВУЧКА ДЕТЕКТИВА ============

    // Бочки
    public static final SoundEvent CLUE_BARREL_1 = register("detective.clue_barrel_1");
    public static final SoundEvent CLUE_BARREL_2 = register("detective.clue_barrel_2");
    public static final SoundEvent CLUE_BARREL_3 = register("detective.clue_barrel_3");

    // Доска объявлений
    public static final SoundEvent CLUE_BOARD_1 = register("detective.clue_board_1");
    public static final SoundEvent CLUE_BOARD_2 = register("detective.clue_board_2");
    public static final SoundEvent CLUE_BOARD_3 = register("detective.clue_board_3");
    public static final SoundEvent CLUE_BOARD_4 = register("detective.clue_board_4");
    public static final SoundEvent CLUE_BOARD_5 = register("detective.clue_board_5");

    // Журнал посещений
    public static final SoundEvent CLUE_JOURNAL_1 = register("detective.clue_journal_1");
    public static final SoundEvent CLUE_JOURNAL_2 = register("detective.clue_journal_2");
    public static final SoundEvent CLUE_JOURNAL_3 = register("detective.clue_journal_3");
    public static final SoundEvent CLUE_JOURNAL_4 = register("detective.clue_journal_4");
    public static final SoundEvent CLUE_JOURNAL_5 = register("detective.clue_journal_5");
    public static final SoundEvent CLUE_JOURNAL_6 = register("detective.clue_journal_6");
    public static final SoundEvent CLUE_JOURNAL_7 = register("detective.clue_journal_7");

    // Форма для человека (самая важная)
    public static final SoundEvent CLUE_MOLD_1 = register("detective.clue_mold_1");
    public static final SoundEvent CLUE_MOLD_2 = register("detective.clue_mold_2");
    public static final SoundEvent CLUE_MOLD_3 = register("detective.clue_mold_3");
    public static final SoundEvent CLUE_MOLD_4 = register("detective.clue_mold_4");
    public static final SoundEvent CLUE_MOLD_5 = register("detective.clue_mold_5");
    public static final SoundEvent CLUE_MOLD_6 = register("detective.clue_mold_6");

    // Дневник подвала
    public static final SoundEvent CLUE_DIARY_1 = register("detective.clue_diary_1");
    public static final SoundEvent CLUE_DIARY_2 = register("detective.clue_diary_2");
    public static final SoundEvent CLUE_DIARY_3 = register("detective.clue_diary_3");
    public static final SoundEvent CLUE_DIARY_4 = register("detective.clue_diary_4");
    public static final SoundEvent CLUE_DIARY_5 = register("detective.clue_diary_5");
    public static final SoundEvent CLUE_DIARY_6 = register("detective.clue_diary_6");
    public static final SoundEvent CLUE_DIARY_7 = register("detective.clue_diary_7");
    public static final SoundEvent CLUE_DIARY_8 = register("detective.clue_diary_8");
    public static final SoundEvent CLUE_DIARY_9 = register("detective.clue_diary_9");
    public static final SoundEvent CLUE_DIARY_10 = register("detective.clue_diary_10");

    // Детский рисунок
    public static final SoundEvent CLUE_DRAWING_1 = register("detective.clue_drawing_1");
    public static final SoundEvent CLUE_DRAWING_2 = register("detective.clue_drawing_2");
    public static final SoundEvent CLUE_DRAWING_3 = register("detective.clue_drawing_3");
    public static final SoundEvent CLUE_DRAWING_4 = register("detective.clue_drawing_4");
    public static final SoundEvent CLUE_DRAWING_5 = register("detective.clue_drawing_5");
    public static final SoundEvent CLUE_DRAWING_6 = register("detective.clue_drawing_6");

    // ============ ДИАЛОГИ NPC ============

    // Начальник
    public static final SoundEvent BOSS_FIRING_1 = register("npc.boss_firing_1");
    public static final SoundEvent BOSS_FIRING_2 = register("npc.boss_firing_2");

    // Валерия
    public static final SoundEvent VALERIYA_1 = register("npc.valeriya_1");
    public static final SoundEvent VALERIYA_2 = register("npc.valeriya_2");

    // Нина
    public static final SoundEvent NINA_1 = register("npc.nina_1");
    public static final SoundEvent NINA_2 = register("npc.nina_2");

    // Рашид
    public static final SoundEvent RASHID_1 = register("npc.rashid_1");
    public static final SoundEvent RASHID_2 = register("npc.rashid_2");

    // Толя
    public static final SoundEvent TOLYA_1 = register("npc.tolya_1");
    public static final SoundEvent TOLYA_2 = register("npc.tolya_2");
    public static final SoundEvent TOLYA_3 = register("npc.tolya_3");
    public static final SoundEvent TOLYA_4 = register("npc.tolya_4");
    public static final SoundEvent TOLYA_5 = register("npc.tolya_5");
    public static final SoundEvent TOLYA_6 = register("npc.tolya_6");
    public static final SoundEvent TOLYA_7 = register("npc.tolya_7");
    public static final SoundEvent TOLYA_8 = register("npc.tolya_8");
    public static final SoundEvent TOLYA_9 = register("npc.tolya_9");

    // Семёныч
    public static final SoundEvent SEMYONYCH_1 = register("npc.semyonych_1");
    public static final SoundEvent SEMYONYCH_2 = register("npc.semyonych_2");

    // Громов
    public static final SoundEvent GROMOV_1 = register("npc.gromov_truth_1");
    public static final SoundEvent GROMOV_2 = register("npc.gromov_truth_2");
    public static final SoundEvent GROMOV_3 = register("npc.gromov_truth_3");
    public static final SoundEvent GROMOV_4 = register("npc.gromov_truth_4");
    public static final SoundEvent GROMOV_5 = register("npc.gromov_truth_5");
    public static final SoundEvent GROMOV_6 = register("npc.gromov_truth_6");
    public static final SoundEvent GROMOV_7 = register("npc.gromov_truth_7");

    // Мама
    public static final SoundEvent MOTHER_1 = register("npc.mother_1");
    public static final SoundEvent MOTHER_2 = register("npc.mother_2");

    // Отец
    public static final SoundEvent FATHER_1 = register("npc.father_1");

    // ============ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ============

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(SymbolMod.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static SoundEvent getVoiceLine(String lineId) {
        Identifier id = new Identifier(SymbolMod.MOD_ID, "detective." + lineId);
        return Registries.SOUND_EVENT.getOrEmpty(id)
            .orElse(SoundEvent.of(id));
    }

    public static SoundEvent getMusic(String musicId) {
        Identifier id = new Identifier(SymbolMod.MOD_ID, "music." + musicId);
        return Registries.SOUND_EVENT.getOrEmpty(id)
            .orElse(SoundEvent.of(id));
    }

    public static void register() {
        SymbolMod.LOGGER.info("Регистрация звуков для " + SymbolMod.MOD_ID);
    }
}
