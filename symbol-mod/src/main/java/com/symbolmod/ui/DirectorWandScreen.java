package com.symbolmod.ui;

import com.symbolmod.cutscene.CutsceneManager;
import com.symbolmod.item.DirectorWandItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.server.network.ServerPlayerEntity;

public class DirectorWandScreen extends Screen {

    private static final int W = 340;
    private static final int H = 260;

    private static final int COL_BG      = 0xFF111111;
    private static final int COL_HEADER  = 0xFF1C1C1C;
    private static final int COL_ACCENT  = 0xFFB8860B;
    private static final int COL_TEXT    = 0xFFE0E0E0;
    private static final int COL_BTN     = 0xFF222222;
    private static final int COL_BTN_HOV = 0xFF333333;
    private static final int COL_GREEN   = 0xFF145214;
    private static final int COL_RED     = 0xFF8B0000;
    private static final int COL_BLUE    = 0xFF003366;

    private int tab = 0;
    private static final String[] TABS = {
        "КАТСЦЕНЫ", "NPC", "КАМЕРА", "КНОПКИ", "БЛОКИ"
    };

    // Поле ввода имени пути камеры
    private TextFieldWidget pathNameField;
    // Поле ввода команды для кнопки
    private TextFieldWidget buttonCommandField;

    // Выбранный NPC
    private int selectedNpcIndex = 0;
    // Выбранная катсцена
    private int selectedCutsceneIndex = 0;

    private static final String[] NPC_TYPES = {
        "detective", "boss", "babushka_nina", "rashid",
        "tolya", "semyonych", "gromov", "valeriya",
        "mother", "father"
    };
    private static final String[] NPC_LABELS = {
        "Детектив", "Начальник", "Бабушка Нина", "Рашид",
        "Толя", "Семёныч", "Громов", "Валерия",
        "Мама", "Папа"
    };
    private static final String[] CUTSCENES = {
        "act0_firing",
        "act1_factory_arrive",
        "act1_glass_room",
        "act2_village_enter",
        "act2_gromov_talk",
        "ending_a_truth",
        "ending_b_replacement",
        "ending_c_denial",
        "ending_d_observer"
    };
    private static final String[] CUTSCENE_LABELS = {
        "Акт 0 — Увольнение",
        "Акт 1 — Прибытие на завод",
        "Акт 1 — Финальная комната",
        "Акт 2 — Вход в деревню",
        "Акт 2 — Разговор с Громовым",
        "Концовка A — Правда",
        "Концовка B — Замена",
        "Концовка C — Отрицание",
        "Концовка D — Наблюдатель"
    };

    public DirectorWandScreen() {
        super(Text.literal("Палочка Режиссёра"));
    }

    @Override
    protected void init() {
        super.init();

        int sx = (this.width - W) / 2;
        int sy = (this.height - H) / 2;

        // Поле имени пути камеры
        pathNameField = new TextFieldWidget(
            this.textRenderer,
            sx + 10, sy + 170, 160, 16,
            Text.literal("Имя пути")
        );
        pathNameField.setMaxLength(32);
        pathNameField.setText("my_path");
        this.addDrawableChild(pathNameField);

        // Поле команды кнопки
        buttonCommandField = new TextFieldWidget(
            this.textRenderer,
            sx + 10, sy + 195, 230, 16,
            Text.literal("Команда")
        );
        buttonCommandField.setMaxLength(256);
        buttonCommandField.setText("/function symbolmod:start_act0");
        this.addDrawableChild(buttonCommandField);

        // Скрываем поля по умолчанию
        updateFieldVisibility();

        buildTabButtons(sx, sy);
    }

    private void buildTabButtons(int sx, int sy) {
        clearChildren();

        // Вкладки
        int tabW = W / TABS.length;
        for (int i = 0; i < TABS.length; i++) {
            final int ti = i;
            addDrawableChild(ButtonWidget.builder(
                Text.literal(TABS[i]),
                btn -> {
                    tab = ti;
                    updateFieldVisibility();
                    buildTabButtons(sx, sy);
                }
            ).dimensions(sx + i * tabW + 1, sy + 26, tabW - 2, 20).build());
        }

        // Контент вкладки
        switch (tab) {
            case 0 -> buildCutscenesButtons(sx, sy);
            case 1 -> buildNpcButtons(sx, sy);
            case 2 -> buildCameraButtons(sx, sy);
            case 3 -> buildButtonsTab(sx, sy);
            case 4 -> buildBlocksTab(sx, sy);
        }

        // Кнопка закрытия
        addDrawableChild(ButtonWidget.builder(
            Text.literal("✕"),
            btn -> this.close()
        ).dimensions(sx + W - 22, sy + 4, 18, 18).build());
    }

    // ============ ВКЛАДКА КАТСЦЕН ============

    private void buildCutscenesButtons(int sx, int sy) {
        int btnY = sy + 55;

        for (int i = 0; i < CUTSCENES.length; i++) {
            final int idx = i;
            final String id = CUTSCENES[i];

            // Кнопка выбора
            addDrawableChild(ButtonWidget.builder(
                Text.literal((selectedCutsceneIndex == i ? "§6▶ " : "§7  ") + CUTSCENE_LABELS[i]),
                btn -> {
                    selectedCutsceneIndex = idx;
                    DirectorWandItem.setSelectedCutscene(id);
                    buildTabButtons(sx, sy);
                }
            ).dimensions(sx + 5, btnY + i * 20, W - 90, 18).build());

            // Кнопка ЗАПУСТИТЬ
            addDrawableChild(ButtonWidget.builder(
                Text.literal("▶ PLAY"),
                btn -> sendWandCommand("cutscene_play " + id)
            ).dimensions(sx + W - 82, btnY + i * 20, 75, 18).build());
        }
    }

    // ============ ВКЛАДКА NPC ============

    private void buildNpcButtons(int sx, int sy) {
        int btnY = sy + 55;
        int cols = 2;
        int cellW = (W - 15) / cols;

        for (int i = 0; i < NPC_TYPES.length; i++) {
            final int idx = i;
            final String type = NPC_TYPES[i];
            int col = i % cols;
            int row = i / cols;

            addDrawableChild(ButtonWidget.builder(
                Text.literal((selectedNpcIndex == i ? "§6" : "§f") + NPC_LABELS[i]),
                btn -> {
                    selectedNpcIndex = idx;
                    DirectorWandItem.setSelectedNpc(type);
                    DirectorWandItem.setMode(DirectorWandItem.WandMode.PLACE_NPC);
                    buildTabButtons(sx, sy);
                }
            ).dimensions(
                sx + 5 + col * (cellW + 5),
                btnY + row * 22,
                cellW, 20
            ).build());
        }

        // Кнопка смены режима
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§aРежим: РАЗМЕЩЕНИЕ NPC"),
            btn -> {
                DirectorWandItem.setMode(DirectorWandItem.WandMode.PLACE_NPC);
                btn.setMessage(Text.literal("§aРежим: РАЗМЕЩЕНИЕ NPC ✓"));
            }
        ).dimensions(sx + 5, sy + H - 45, W - 10, 18).build());
    }

    // ============ ВКЛАДКА КАМЕРЫ ============

    private void buildCameraButtons(int sx, int sy) {

        // Режим камеры
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§e📍 Режим: ДОБАВЛЕНИЕ ТОЧЕК"),
            btn -> {
                DirectorWandItem.setMode(DirectorWandItem.WandMode.CAMERA);
                sendWandCommand("mode camera");
            }
        ).dimensions(sx + 5, sy + 55, W - 10, 20).build());

        // Предпросмотр
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§b🎬 Предпросмотр пути"),
            btn -> sendWandCommand("camera_preview")
        ).dimensions(sx + 5, sy + 78, W - 10, 20).build());

        // Очистить
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§c🗑 Очистить все точки"),
            btn -> sendWandCommand("camera_clear")
        ).dimensions(sx + 5, sy + 101, W - 10, 20).build());

        // Сохранить
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§a💾 Сохранить путь"),
            btn -> {
                String name = pathNameField.getText().trim();
                if (!name.isEmpty()) {
                    sendWandCommand("camera_save " + name);
                }
            }
        ).dimensions(sx + 5, sy + 155, W / 2 - 8, 20).build());

        // Загрузить
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§6📂 Загрузить путь"),
            btn -> {
                String name = pathNameField.getText().trim();
                if (!name.isEmpty()) {
                    sendWandCommand("camera_load " + name);
                }
            }
        ).dimensions(sx + W / 2 + 3, sy + 155, W / 2 - 8, 20).build());

        // Поле имени
        pathNameField.setVisible(true);
        pathNameField.setX(sx + 5);
        pathNameField.setY(sy + 133);
    }

    // ============ ВКЛАДКА КНОПОК ============

    private void buildButtonsTab(int sx, int sy) {

        // Большая кнопка НАЧАТЬ
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§6★ Создать кнопку «НАЧАТЬ»"),
            btn -> {
                DirectorWandItem.setMode(DirectorWandItem.WandMode.BUTTON);
                sendWandCommand("create_start_button");
                this.close();
            }
        ).dimensions(sx + 5, sy + 55, W - 10, 22).build());

        // Кнопка выбора диалога
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§b◆ Создать кнопку выбора"),
            btn -> {
                DirectorWandItem.setMode(DirectorWandItem.WandMode.BUTTON);
                sendWandCommand("create_choice_button");
                this.close();
            }
        ).dimensions(sx + 5, sy + 80, W - 10, 22).build());

        // Подпись команды
        buttonCommandField.setVisible(true);
        buttonCommandField.setX(sx + 5);
        buttonCommandField.setY(sy + 130);

        // Привязать команду
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§a✓ Привязать команду"),
            btn -> {
                String cmd = buttonCommandField.getText().trim();
                if (!cmd.isEmpty()) {
                    sendWandCommand("bind_command " + cmd);
                }
            }
        ).dimensions(sx + 5, sy + 150, W - 10, 20).build());

        // Удалить кнопку
        addDrawableChild(ButtonWidget.builder(
            Text.literal("§c✕ Удалить выбранную кнопку"),
            btn -> sendWandCommand("delete_button")
        ).dimensions(sx + 5, sy + 174, W - 10, 20).build());
    }

    // ============ ВКЛАДКА БЛОКОВ ============

    private void buildBlocksTab(int sx, int sy) {
        String[][] blocks = {
            {"Прогнившие доски",        "rotten_planks"},
            {"Прогн. доски (трещины)",  "rotten_planks_cracked"},
            {"Прогн. доски (обвал)",    "rotten_planks_collapsed"},
            {"Советская плитка белая",  "soviet_tiles_white"},
            {"Советская плитка зелёная","soviet_tiles_green"},
            {"Советская плитка грязная","soviet_tiles_dirty"},
            {"Ржавая металл. панель",   "rusty_metal_panel"},
            {"Ржавая труба",            "pipe_rusty"},
            {"Вентиляц. решётка",       "vent_grate"},
            {"Старое стекло",           "old_glass"},
            {"Треснутое стекло",        "cracked_glass"},
            {"Линолеум",                "linoleum_brown"},
            {"Линолеум облезший",       "linoleum_peeled"},
            {"Обои с цветами",          "wallpaper_floral"},
            {"Рваные обои",             "wallpaper_torn"},
            {"Бетонная плита",          "concrete_slab_clean"}
        };

        int cols = 2;
        int cellW = (W - 15) / cols;
        int maxShow = 10;

        for (int i = 0; i < Math.min(maxShow, blocks.length); i++) {
            final String blockId = blocks[i][1];
            int col = i % cols;
            int row = i / cols;

            addDrawableChild(ButtonWidget.builder(
                Text.literal("§f" + blocks[i][0]),
                btn -> sendWandCommand("give_block " + blockId)
            ).dimensions(
                sx + 5 + col * (cellW + 5),
                sy + 55 + row * 20,
                cellW, 18
            ).build());
        }
    }

    // ============ ОТПРАВКА КОМАНД НА СЕРВЕР ============

    private void sendWandCommand(String command) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.networkHandler.sendCommand(
                "symbolmod_wand " + command
            );
        }
    }

    private void updateFieldVisibility() {
        if (pathNameField != null) {
            pathNameField.setVisible(tab == 2);
        }
        if (buttonCommandField != null) {
            buttonCommandField.setVisible(tab == 3);
        }
    }

    // ============ РЕНДЕР ============

    @Override
    public void render(DrawContext ctx, int mx, int my, float delta) {
        int sx = (this.width - W) / 2;
        int sy = (this.height - H) / 2;

        // Затемнение
        ctx.fill(0, 0, this.width, this.height, 0xAA000000);

        // Панель
        ctx.fill(sx, sy, sx + W, sy + H, COL_BG);

        // Заголовок
        ctx.fill(sx, sy, sx + W, sy + 25, COL_HEADER);
        ctx.drawCenteredTextWithShadow(
            this.textRenderer,
            "§6✦ ПАЛОЧКА РЕЖИССЁРА ✦  §8[Режим: " +
            DirectorWandItem.getMode().name() + "]",
            sx + W / 2, sy + 8, COL_ACCENT
        );

        // Граница
        ctx.fill(sx,       sy,       sx + W, sy + 1,     COL_ACCENT);
        ctx.fill(sx,       sy + H-1, sx + W, sy + H,     COL_ACCENT);
        ctx.fill(sx,       sy,       sx + 1, sy + H,     COL_ACCENT);
        ctx.fill(sx + W-1, sy,       sx + W, sy + H,     COL_ACCENT);

        // Разделитель вкладок
        ctx.fill(sx, sy + 46, sx + W, sy + 47, COL_ACCENT);

        // Счётчик точек камеры
        if (tab == 2) {
            ctx.drawTextWithShadow(
                this.textRenderer,
                "§7Точек записано: §f" +
                DirectorWandItem.getWaypoints().size(),
                sx + 5, sy + 105, COL_TEXT
            );
            ctx.drawTextWithShadow(
                this.textRenderer,
                "§7Имя пути:",
                sx + 5, sy + 122, COL_TEXT
            );
        }

        if (tab == 3) {
            ctx.drawTextWithShadow(
                this.textRenderer,
                "§7Команда при нажатии:",
                sx + 5, sy + 118, COL_TEXT
            );
        }

        super.render(ctx, mx, my, delta);
    }

    @Override
    public boolean keyPressed(int key, int scan, int mods) {
        if (key == 256) { this.close(); return true; }
        return super.keyPressed(key, scan, mods);
    }

    @Override
    public boolean shouldPause() { return false; }
}
