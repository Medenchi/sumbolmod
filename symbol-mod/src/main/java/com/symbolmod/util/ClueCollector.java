package com.symbolmod.util;

import com.symbolmod.SymbolMod;
import com.symbolmod.registry.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import java.util.*;

public class ClueCollector {

    // ============ ВСЕ УЛИКИ ============

    public static final Map<String, ClueData> ALL_CLUES = new LinkedHashMap<>();

    static {

        // ========== АКТ 1 — ЗАВОД ==========

        // ДВОР
        ALL_CLUES.put("barrel_smell", new ClueData(
            "barrel_smell",
            "Бочки с химическим запахом",
            "Двор завода",
            "Пусто. Давно пусто… Хотя… запах странный. " +
            "Химия какая-то. Не промышленная — другая. " +
            "Как будто здесь что-то варили. Недавно.",
            new String[]{
                "clue_barrel_1",  // "Пусто. Давно пусто…"
                "clue_barrel_2",  // "Запах странный. Химия."
                "clue_barrel_3"   // "Как будто здесь что-то варили. Недавно."
            },
            ClueImportance.LOW,
            false
        ));

        ALL_CLUES.put("soviet_loader", new ClueData(
            "soviet_loader",
            "Советский погрузчик с ключом",
            "Двор завода",
            "Советский ещё… На таких мой дед работал. " +
            "Ключ в замке зажигания. " +
            "Сорок пять лет, а ключ всё ещё здесь. " +
            "Кто-то ушёл очень быстро. Или его заставили уйти быстро.",
            new String[]{
                "clue_loader_1",  // "Советский ещё…"
                "clue_loader_2",  // "Ключ в замке зажигания."
                "clue_loader_3"   // "Кто-то ушёл очень быстро."
            },
            ClueImportance.LOW,
            false
        ));

        // ДОСКА ОБЪЯВЛЕНИЙ — ВАЖНАЯ
        ALL_CLUES.put("announcement_board", new ClueData(
            "announcement_board",
            "Объявление от 15.03.1979",
            "Проходная завода",
            "Объявления… ничего не разобрать. " +
            "Стоп. Вот это видно. " +
            "«Внеплановое собрание. 15 марта 1979. Всем рабочим явка обязательна.» " +
            "День закрытия завода. " +
            "Внеплановое собрание в день закрытия. " +
            "Рабочих собрали. Зачем? Чтобы сказать что завод закрывается? " +
            "Или по другой причине?",
            new String[]{
                "clue_board_1",   // "Объявления… ничего не разобрать."
                "clue_board_2",   // "Стоп. Вот это видно."
                "clue_board_3",   // "Внеплановое собрание."
                "clue_board_4",   // "День закрытия завода."
                "clue_board_5"    // "Рабочих собрали. Зачем?"
            },
            ClueImportance.HIGH,
            true
        ));

        // ЖУРНАЛ ПОСЕЩЕНИЙ — ОЧЕНЬ ВАЖНАЯ
        ALL_CLUES.put("visitor_journal", new ClueData(
            "visitor_journal",
            "Журнал посещений — трое не вышли",
            "Проходная завода",
            "Журнал посещений… Каждый день. Каждый рабочий. " +
            "Последняя запись — 14 марта 1979. Все расписались на выход. " +
            "15 марта. Приход — восемнадцать человек. Уход — пятнадцать. " +
            "Трое не расписались на выход. " +
            "Трое. " +
            "Это не случайность. На режимном заводе так не бывает. " +
            "Где они? " +
            "Если бы ушли — расписались бы. Значит не ушли. " +
            "Или не смогли.",
            new String[]{
                "clue_journal_1",  // "Журнал посещений…"
                "clue_journal_2",  // "Последняя запись — 14 марта."
                "clue_journal_3",  // "15 марта. Восемнадцать вошли."
                "clue_journal_4",  // "Трое не расписались."
                "clue_journal_5",  // "Трое."
                "clue_journal_6",  // "Это не случайность."
                "clue_journal_7"   // "Значит не ушли. Или не смогли."
            },
            ClueImportance.CRITICAL,
            true
        ));

        ALL_CLUES.put("guard_photo", new ClueData(
            "guard_photo",
            "Фотография охранника Семёныча",
            "Проходная завода",
            "Охранник наверное… Пожилой мужик. " +
            "На обороте написано: «Семёныч. 31 год службы.» " +
            "Тридцать один год. " +
            "Всю жизнь проработал здесь. " +
            "Он точно знает что произошло 15 марта. " +
            "Интересно… он вышел в тот день?",
            new String[]{
                "clue_photo_1",   // "Охранник наверное…"
                "clue_photo_2",   // "31 год службы."
                "clue_photo_3",   // "Тридцать один год."
                "clue_photo_4"    // "Интересно… он вышел в тот день?"
            },
            ClueImportance.MEDIUM,
            true
        ));

        // ЦЕХ №1
        ALL_CLUES.put("safety_poster", new ClueData(
            "safety_poster",
            "Плакат по технике безопасности 1976",
            "Цех №1",
            "Плакат 1976 года. «Стеклозавод №7». " +
            "Номер семь. " +
            "Значит были и другие заводы. " +
            "Один, два, три… до шести. " +
            "Шесть других заводов. " +
            "Где они? Что на них происходило?",
            new String[]{
                "clue_poster_1",  // "Плакат 1976 года."
                "clue_poster_2",  // "Номер семь."
                "clue_poster_3",  // "Значит были и другие заводы."
                "clue_poster_4"   // "Шесть других заводов."
            },
            ClueImportance.MEDIUM,
            true
        ));

        ALL_CLUES.put("touched_lever", new ClueData(
            "touched_lever",
            "Станок со свежим следом",
            "Цех №1",
            "Этот рычаг кто-то недавно трогал. " +
            "Пыль стёрта. " +
            "И следы свежие. " +
            "Кто-то здесь бывает регулярно. " +
            "Сорок пять лет завод закрыт — и кто-то сюда ходит.",
            new String[]{
                "clue_lever_1",   // "Этот рычаг кто-то недавно трогал."
                "clue_lever_2",   // "Пыль стёрта."
                "clue_lever_3"    // "Сорок пять лет закрыт — и кто-то ходит."
            },
            ClueImportance.HIGH,
            true
        ));

        // ФОРМА ДЛЯ ЧЕЛОВЕКА — КРИТИЧЕСКАЯ
        ALL_CLUES.put("human_mold", new ClueData(
            "human_mold",
            "Форма для литья размером с человека",
            "Цех №1",
            "Форма для литья… " +
            "Метр восемьдесят в длину. Полметра в ширину. " +
            "Это не для стекла. " +
            "Это форма для человека. " +
            "Здесь отливали не стекло. " +
            "Здесь отливали людей. " +
            "В стекло. " +
            "Господи.",
            new String[]{
                "clue_mold_1",    // "Форма для литья…"
                "clue_mold_2",    // "Метр восемьдесят. Полметра."
                "clue_mold_3",    // "Это не для стекла."
                "clue_mold_4",    // "Это форма для человека."
                "clue_mold_5",    // "Здесь отливали людей. В стекло."
                "clue_mold_6"     // "Господи." (долгая пауза)
            },
            ClueImportance.CRITICAL,
            true
        ));

        ALL_CLUES.put("fresh_tracks", new ClueData(
            "fresh_tracks",
            "Свежие следы в цехе",
            "Цех №1",
            "Следы совсем свежие. " +
            "Идут через весь цех и уходят в коридор. " +
            "Один человек. Регулярно. " +
            "Кто-то здесь работает. " +
            "Или следит.",
            new String[]{
                "clue_tracks_1",  // "Следы совсем свежие."
                "clue_tracks_2",  // "Один человек. Регулярно."
                "clue_tracks_3"   // "Кто-то здесь работает. Или следит."
            },
            ClueImportance.HIGH,
            true
        ));

        // СКЛАД
        ALL_CLUES.put("k7_invoice", new ClueData(
            "k7_invoice",
            "Накладная на состав К-7",
            "Склад",
            "Накладная на «Специальный состав К-7». " +
            "Получатель — Лаборатория. " +
            "Лаборатория. " +
            "На стекольном заводе не должно быть лаборатории. " +
            "Состав К-7. Специальный. " +
            "Для чего? " +
            "Для тех форм размером с человека?",
            new String[]{
                "clue_k7_1",      // "Накладная на состав К-7."
                "clue_k7_2",      // "Получатель — Лаборатория."
                "clue_k7_3",      // "На стекольном заводе не должно быть лаборатории."
                "clue_k7_4",      // "Для чего?"
                "clue_k7_5"       // "Для тех форм размером с человека?"
            },
            ClueImportance.CRITICAL,
            true
        ));

        ALL_CLUES.put("new_flashlight", new ClueData(
            "new_flashlight",
            "Новый фонарик на заброшенном складе",
            "Склад",
            "Новый. Совсем новый. Батарейки свежие. " +
            "Кто оставил здесь новый фонарик? " +
            "На заводе закрытом сорок пять лет? " +
            "Кто-то специально его оставил. " +
            "Для меня? " +
            "Меня здесь ждали?",
            new String[]{
                "clue_flashlight_1",  // "Новый. Совсем новый."
                "clue_flashlight_2",  // "Кто оставил здесь новый фонарик?"
                "clue_flashlight_3",  // "На заводе закрытом сорок пять лет?"
                "clue_flashlight_4"   // "Меня здесь ждали?"
            },
            ClueImportance.HIGH,
            true
        ));

        // ОФИС ДИРЕКТОРА
        ALL_CLUES.put("gromov_portrait", new ClueData(
            "gromov_portrait",
            "Портрет директора Громова",
            "Офис директора",
            "Громов Виктор Степанович. " +
            "Директор с 1961 по 1979 год. " +
            "Восемнадцать лет руководил заводом. " +
            "Значит он знал всё. " +
            "С самого начала. " +
            "Где он сейчас?",
            new String[]{
                "clue_portrait_1",  // "Громов Виктор Степанович."
                "clue_portrait_2",  // "Восемнадцать лет руководил."
                "clue_portrait_3",  // "Значит он знал всё."
                "clue_portrait_4"   // "Где он сейчас?"
            },
            ClueImportance.HIGH,
            true
        ));

        ALL_CLUES.put("open_safe", new ClueData(
            "open_safe",
            "Открытый сейф с кодом 1979",
            "Офис директора",
            "Сейф открыт. Пустой. " +
            "Код не сброшен — 1-9-7-9. " +
            "Год закрытия завода. " +
            "Кто-то открыл его именно тогда. " +
            "И не удосужился изменить код. " +
            "Или не успел.",
            new String[]{
                "clue_safe_1",    // "Сейф открыт. Пустой."
                "clue_safe_2",    // "Код — 1979."
                "clue_safe_3",    // "Год закрытия завода."
                "clue_safe_4"     // "Кто-то открыл его именно тогда."
            },
            ClueImportance.MEDIUM,
            true
        ));

        // ЗАПИСНАЯ КНИЖКА — КРИТИЧЕСКАЯ
        ALL_CLUES.put("director_notebook", new ClueData(
            "director_notebook",
            "Записная книжка Громова",
            "Офис директора",
            "14 марта 1979 — «Получено разрешение. Процедура начнётся завтра.» " +
            "Разрешение. " +
            "От кого? " +
            "Какая процедура? " +
            "15 марта — страница полностью пустая. " +
            "Полностью пустая. " +
            "Он не написал ни слова. " +
            "После того дня — ни слова. " +
            "Потому что не мог? " +
            "Или не хотел?",
            new String[]{
                "clue_notebook_1",  // "14 марта — получено разрешение."
                "clue_notebook_2",  // "Разрешение. От кого?"
                "clue_notebook_3",  // "15 марта — страница пустая."
                "clue_notebook_4",  // "После того дня — ни слова."
                "clue_notebook_5"   // "Потому что не мог? Или не хотел?"
            },
            ClueImportance.CRITICAL,
            true
        ));

        // ЦЕХ №2
        ALL_CLUES.put("tech_schema", new ClueData(
            "tech_schema",
            "Технологическая схема с К-7",
            "Цех №2",
            "Здесь снова упоминается состав К-7. " +
            "Это уже не стандартное производство стекла. " +
            "Судя по схеме… " +
            "К-7 нужен для стабилизации органики в стекле. " +
            "Органики. " +
            "Живой материи. " +
            "Боже.",
            new String[]{
                "clue_schema_1",  // "Снова К-7."
                "clue_schema_2",  // "Это не стандартное производство."
                "clue_schema_3",  // "К-7 для стабилизации органики."
                "clue_schema_4",  // "Органики. Живой материи."
                "clue_schema_5"   // "Боже." (пауза)
            },
            ClueImportance.CRITICAL,
            true
        ));

        ALL_CLUES.put("aliev_robe", new ClueData(
            "aliev_robe",
            "Роба рабочего Алиева с запиской",
            "Цех №2",
            "Роба рабочего… Алиев. " +
            "На спине написано карандашом. " +
            "Аккуратно, чтобы никто случайно не увидел. " +
            "«Если найдёшь — иди в подвал. Мы там.» " +
            "Мы там. " +
            "Они были в подвале. " +
            "Алиев… Рашид Алиев в деревне — его сын?",
            new String[]{
                "clue_robe_1",    // "Роба рабочего… Алиев."
                "clue_robe_2",    // "На спине написано карандашом."
                "clue_robe_3",    // "Если найдёшь — иди в подвал."
                "clue_robe_4",    // "Они были в подвале."
                "clue_robe_5"     // "Рашид Алиев в деревне — его сын?"
            },
            ClueImportance.CRITICAL,
            true
        ));

        // ПОДВАЛ
        ALL_CLUES.put("three_mattresses", new ClueData(
            "three_mattresses",
            "Три матраса в подвале",
            "Подвал",
            "Три матраса… " +
            "Кто-то жил здесь долго. " +
            "На стенах царапины — считали дни. " +
            "Раз, два, три… двадцать одна царапина. " +
            "Двадцать один день. " +
            "Трое людей провели здесь три недели. " +
            "Прячась. " +
            "Боясь выйти.",
            new String[]{
                "clue_mattress_1",  // "Три матраса…"
                "clue_mattress_2",  // "Кто-то жил здесь долго."
                "clue_mattress_3",  // "Двадцать одна царапина."
                "clue_mattress_4",  // "Трое людей провели три недели."
                "clue_mattress_5"   // "Прячась. Боясь выйти."
            },
            ClueImportance.HIGH,
            true
        ));

        ALL_CLUES.put("soviet_cans", new ClueData(
            "soviet_cans",
            "Советские консервы марта 1979",
            "Подвал",
            "Советские консервы марта 1979 года. " +
            "Двадцать три банки. " +
            "Трое людей… двадцать три банки… " +
            "Примерно по семь-восемь банок на человека. " +
            "Около двух недель еды. " +
            "Они планировали выживать. " +
            "Значит знали что их будут искать.",
            new String[]{
                "clue_cans_1",    // "Советские консервы. 1979."
                "clue_cans_2",    // "Двадцать три банки."
                "clue_cans_3",    // "Около двух недель еды."
                "clue_cans_4"     // "Значит знали что их будут искать."
            },
            ClueImportance.MEDIUM,
            true
        ));

        // ДНЕВНИК — СУПЕРКРИТИЧЕСКАЯ
        ALL_CLUES.put("basement_diary", new ClueData(
            "basement_diary",
            "Дневник выжившего",
            "Подвал",
            "15 марта 1979. Завод закрывают. Нам сказали уходить. " +
            "Но мы не можем. Мы видели что здесь делали. " +
            "Видели комнату. Они не могут нас выпустить. " +
            "Пауза. " +
            "17 марта. Нас трое. Я, Алиев и Сергеев. " +
            "Прячемся в подвале. Ночью слышим шаги наверху. Ищут нас. " +
            "Пауза. " +
            "21 марта. Сергеев говорит надо бежать. Алиев молчит уже два дня. " +
            "Пауза. " +
            "25 марта. Сергеев ушёл ночью. Алиев ушёл позже. Я один. " +
            "Пауза. " +
            "Если кто-то найдёт это… " +
            "Он не дописал. " +
            "Они видели какую-то комнату. " +
            "И эта комната… она здесь. В подвале.",
            new String[]{
                "clue_diary_1",   // "15 марта 1979. Завод закрывают."
                "clue_diary_2",   // "Мы видели что здесь делали."
                "clue_diary_3",   // "Видели комнату."
                "clue_diary_4",   // "17 марта. Нас трое."
                "clue_diary_5",   // "Ночью слышим шаги наверху."
                "clue_diary_6",   // "21 марта. Сергеев говорит бежать."
                "clue_diary_7",   // "25 марта. Один."
                "clue_diary_8",   // "Если кто-то найдёт это…"
                "clue_diary_9",   // "Он не дописал."
                "clue_diary_10"   // "Эта комната… она здесь."
            },
            ClueImportance.CRITICAL,
            true
        ));

        // ========== АКТ 2 — ДЕРЕВНЯ ==========

        // СИМВОЛЫ В ДОМАХ
        ALL_CLUES.put("symbol_nina_cup", new ClueData(
            "symbol_nina_cup",
            "Символ на донышке кружки у Нины",
            "Дом Нины",
            "Что это… " +
            "Символ. " +
            "Прищуренный глаз. " +
            "На донышке обычной кружки у обычной бабушки. " +
            "Она не знает. " +
            "Или делает вид что не знает. " +
            "Кто поставил этот знак? " +
            "И зачем?",
            new String[]{
                "clue_symbol_nina_1",  // "Что это…"
                "clue_symbol_nina_2",  // "Символ."
                "clue_symbol_nina_3",  // "На донышке кружки у бабушки."
                "clue_symbol_nina_4",  // "Она не знает. Или делает вид."
                "clue_symbol_nina_5"   // "Кто поставил этот знак?"
            },
            ClueImportance.HIGH,
            true
        ));

        ALL_CLUES.put("symbol_aliev_battery", new ClueData(
            "symbol_aliev_battery",
            "Символ за батареей в доме Алиевых",
            "Дом Алиевых",
            "Снова он. " +
            "За батареей в прихожей. " +
            "В доме сына человека которого убили на заводе. " +
            "Они не знают. " +
            "Отец Рашида умер там. " +
            "А в его доме — тот же символ что и на заводе. " +
            "Совпадение? " +
            "Нет.",
            new String[]{
                "clue_symbol_aliev_1",  // "Снова он."
                "clue_symbol_aliev_2",  // "В доме сына убитого."
                "clue_symbol_aliev_3",  // "Они не знают."
                "clue_symbol_aliev_4"   // "Совпадение? Нет."
            },
            ClueImportance.HIGH,
            true
        ));

        ALL_CLUES.put("symbol_tolya_floor", new ClueData(
            "symbol_tolya_floor",
            "Символ под ковром в доме Толи",
            "Дом Толи",
            "Он искал. " +
            "Сдвигал ковёр. " +
            "Но не поднял полностью. " +
            "Вот он. " +
            "Под ковром. " +
            "Он маркировал других сорок лет. " +
            "И всё это время не знал что маркируют его. " +
            "Я не знаю плакать мне или смеяться.",
            new String[]{
                "clue_symbol_tolya_1",  // "Он искал."
                "clue_symbol_tolya_2",  // "Но не поднял полностью."
                "clue_symbol_tolya_3",  // "Вот он. Под ковром."
                "clue_symbol_tolya_4",  // "Маркировал других. Не знал что себя."
                "clue_symbol_tolya_5"   // "Не знаю плакать или смеяться."
            },
            ClueImportance.CRITICAL,
            true
        ));

        ALL_CLUES.put("symbol_semyonych_icon", new ClueData(
            "symbol_semyonych_icon",
            "Символ 1981 года за иконой",
            "Дом Семёныча",
            "На обратной стороне иконы. " +
            "Дата рядом — 1981 год. " +
            "Два года после закрытия завода. " +
            "Они продолжили маркировку уже здесь, в деревне. " +
            "После всего что произошло — они не остановились. " +
            "Переехали в деревню. " +
            "И продолжили.",
            new String[]{
                "clue_symbol_sem_1",  // "На обратной стороне иконы."
                "clue_symbol_sem_2",  // "1981 год."
                "clue_symbol_sem_3",  // "Два года после закрытия."
                "clue_symbol_sem_4",  // "Они продолжили маркировку."
                "clue_symbol_sem_5"   // "Переехали. И продолжили."
            },
            ClueImportance.CRITICAL,
            true
        ));

        // ПУСТОЙ ДОМ
        ALL_CLUES.put("torn_diary_pages", new ClueData(
            "torn_diary_pages",
            "Вырванные страницы дневника",
            "Пустой дом",
            "Вырванные страницы. " +
            "Кто-то не хотел чтобы это нашли. " +
            "Осталась только одна запись. " +
            "«Я начинаю замечать знаки. На вещах. На посуде. На мебели.» " +
            "«Один и тот же знак везде.» " +
            "«Завтра спрошу у Виктора.» " +
            "Виктор. " +
            "Громов. " +
            "И после этого человек исчез.",
            new String[]{
                "clue_torn_1",    // "Вырванные страницы."
                "clue_torn_2",    // "Осталась одна запись."
                "clue_torn_3",    // "Я начинаю замечать знаки."
                "clue_torn_4",    // "Завтра спрошу у Виктора."
                "clue_torn_5",    // "Виктор. Громов."
                "clue_torn_6"     // "После этого человек исчез."
            },
            ClueImportance.CRITICAL,
            true
        ));

        ALL_CLUES.put("child_drawing", new ClueData(
            "child_drawing",
            "Детский рисунок с символами над домами",
            "Пустой дом",
            "Детский рисунок на холодильнике. " +
            "Деревня. Дома. Люди. " +
            "Всё как у детей. " +
            "Но над каждым домом — символ. " +
            "Прищуренный глаз. " +
            "Ребёнок видел их. " +
            "Для него они были частью пейзажа. " +
            "Обычными. " +
            "Ребёнок видел символы. " +
            "Рисовал их. " +
            "Не понимая что они значат. " +
            "Страшнее всего что он прав.",
            new String[]{
                "clue_drawing_1",   // "Детский рисунок на холодильнике."
                "clue_drawing_2",   // "Деревня. Дома."
                "clue_drawing_3",   // "Но над каждым домом — символ."
                "clue_drawing_4",   // "Ребёнок видел их. Для него обычные."
                "clue_drawing_5",   // "Рисовал. Не понимая что значат."
                "clue_drawing_6"    // "Страшнее всего что он прав."
            },
            ClueImportance.CRITICAL,
            true
        ));
    }

    // ============ МЕТОДЫ СБОРА УЛИК ============

    public static void collectClue(PlayerEntity player, String clueId) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        ClueData clue = ALL_CLUES.get(clueId);
        if (clue == null) {
            SymbolMod.LOGGER.error("Улика не найдена: " + clueId);
            return;
        }

        // Проверяем — не собрана ли уже
        if (isClueCollected(player, clueId)) return;

        // Сохраняем в NBT игрока
        saveClue(serverPlayer, clueId);

        // Показываем текст улики
        showClueText(serverPlayer, clue);

        // Воспроизводим озвучку (все реплики подряд с паузами)
        scheduleVoiceLines(serverPlayer, clue);

        // Звук получения улики
        serverPlayer.playSound(
            ModSounds.CLUE_DISCOVERED,
            SoundCategory.MASTER,
            0.8f,
            1.0f
        );

        // Уведомление
        serverPlayer.sendMessage(
            Text.literal("§8[§6Улика добавлена в дневник: §e")
                .append(Text.literal(clue.name).formatted(Formatting.YELLOW))
                .append(Text.literal("§8]")),
            true
        );

        // Если критическая — паранойя
        if (clue.importance == ClueImportance.CRITICAL) {
            com.symbolmod.paranoia.ParanoiaSystem.addParanoia(serverPlayer, 2);
        } else if (clue.importance == ClueImportance.HIGH) {
            com.symbolmod.paranoia.ParanoiaSystem.addParanoia(serverPlayer, 1);
        }
    }

    private static void showClueText(ServerPlayerEntity player, ClueData clue) {
        // Заголовок
        player.sendMessage(Text.literal(""), false);
        player.sendMessage(
            Text.literal("§6══════ " + clue.name + " ══════")
                .formatted(Formatting.GOLD),
            false
        );
        player.sendMessage(
            Text.literal("§7📍 " + clue.location),
            false
        );
        player.sendMessage(Text.literal(""), false);

        // Рассуждение детектива
        player.sendMessage(
            Text.literal("§e[Детектив думает:]§f " + clue.reasoning),
            false
        );
        player.sendMessage(Text.literal(""), false);
    }

    private static void scheduleVoiceLines(ServerPlayerEntity player, ClueData clue) {
        // Воспроизводим каждую реплику с задержкой
        int delay = 0;
        for (String voiceLine : clue.voiceLines) {
            final String line = voiceLine;
            final int finalDelay = delay;

            // Планируем воспроизведение через delay тиков
            player.getServer().execute(() -> {
                try {
                    Thread.sleep(finalDelay * 1000L / 20L);
                } catch (InterruptedException e) { /* игнор */ }
                player.playSound(
                    ModSounds.getVoiceLine(line),
                    SoundCategory.VOICE,
                    1.0f,
                    1.0f
                );
            });

            delay += 60; // задержка между репликами (60 тиков = 3 секунды)
        }
    }

    // ============ СОХРАНЕНИЕ УЛИК ============

    private static void saveClue(ServerPlayerEntity player, String clueId) {
        NbtCompound nbt = player.getDataTracker()
            .get(com.symbolmod.util.PlayerDataTracker.SYMBOL_DATA);
        if (nbt == null) nbt = new NbtCompound();

        NbtList clueList = nbt.contains("CollectedClues")
            ? nbt.getList("CollectedClues", 8)
            : new NbtList();

        clueList.add(NbtString.of(clueId));
        nbt.put("CollectedClues", clueList);
    }

    public static boolean isClueCollected(PlayerEntity player, String clueId) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return false;

        NbtCompound nbt = serverPlayer.getDataTracker()
            .get(com.symbolmod.util.PlayerDataTracker.SYMBOL_DATA);
        if (nbt == null || !nbt.contains("CollectedClues")) return false;

        NbtList clueList = nbt.getList("CollectedClues", 8);
        for (int i = 0; i < clueList.size(); i++) {
            if (clueList.getString(i).equals(clueId)) return true;
        }
        return false;
    }

    public static List<String> getCollectedClues(PlayerEntity player) {
        List<String> result = new ArrayList<>();
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return result;

        NbtCompound nbt = serverPlayer.getDataTracker()
            .get(com.symbolmod.util.PlayerDataTracker.SYMBOL_DATA);
        if (nbt == null || !nbt.contains("CollectedClues")) return result;

        NbtList clueList = nbt.getList("CollectedClues", 8);
        for (int i = 0; i < clueList.size(); i++) {
            result.add(clueList.getString(i));
        }
        return result;
    }

    public static int getClueCount(PlayerEntity player) {
        return getCollectedClues(player).size();
    }

    // ============ ДАННЫЕ УЛИКИ ============

    public static class ClueData {
        public final String id;
        public final String name;
        public final String location;
        public final String reasoning;
        public final String[] voiceLines;
        public final ClueImportance importance;
        public final boolean addsToDiary;

        public ClueData(String id, String name, String location,
                        String reasoning, String[] voiceLines,
                        ClueImportance importance, boolean addsToDiary) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.reasoning = reasoning;
            this.voiceLines = voiceLines;
            this.importance = importance;
            this.addsToDiary = addsToDiary;
        }
    }

    public enum ClueImportance {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
