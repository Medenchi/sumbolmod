package com.symbolmod.ui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class DirectorWandScreen extends Screen {

    private static final int PANEL_WIDTH = 320;
    private static final int PANEL_HEIGHT = 240;

    private static final int COLOR_BG = 0xFF1A1A1A;
    private static final int COLOR_HEADER = 0xFF2A2A2A;
    private static final int COLOR_ACCENT = 0xFFB8860B;
    private static final int COLOR_TEXT = 0xFFE8E8E8;
    private static final int COLOR_HOVER = 0xFF3A3A3A;
    private static final int COLOR_BUTTON = 0xFF2A2A2A;

    private int currentTab = 0;
    private static final String[] TABS = {
        "КАТСЦЕНЫ", "NPC", "КНОПКИ", "БЛОКИ", "КАМЕРА"
    };

    // Состояние для каждой вкладки
    private String selectedCutscene = "";
    private String selectedNpc = "";
    private float cameraSpeed = 1.0f;

    public DirectorWandScreen() {
        super(Text.literal("Палочка Режиссёра"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int startX = (this.width - PANEL_WIDTH) / 2;
        int startY = (this.height - PANEL_HEIGHT) / 2;

        // Тёмный фон
        context.fill(0, 0, this.width, this.height, 0xAA000000);

        // Основная панель
        context.fill(startX, startY,
                     startX + PANEL_WIDTH, startY + PANEL_HEIGHT,
                     COLOR_BG);

        // Заголовок
        context.fill(startX, startY,
                     startX + PANEL_WIDTH, startY + 25,
                     COLOR_HEADER);

        context.drawCenteredTextWithShadow(
            this.textRenderer,
            "§6✦ ПАЛОЧКА РЕЖИССЁРА ✦",
            startX + PANEL_WIDTH / 2,
            startY + 8,
            COLOR_ACCENT
        );

        // Кнопка закрытия
        boolean closeHover = mouseX >= startX + PANEL_WIDTH - 22 &&
                             mouseX <= startX + PANEL_WIDTH - 5 &&
                             mouseY >= startY + 4 && mouseY <= startY + 21;

        context.fill(startX + PANEL_WIDTH - 22, startY + 4,
                     startX + PANEL_WIDTH - 5, startY + 21,
                     closeHover ? 0xFF8B0000 : 0xFF550000);
        context.drawCenteredTextWithShadow(
            this.textRenderer, "✕",
            startX + PANEL_WIDTH - 13, startY + 8,
            0xFFFFFFFF
        );

        // Вкладки
        renderTabs(context, startX, startY + 25, mouseX, mouseY);

        // Контент вкладки
        renderTabContent(context, startX + 5, startY + 55,
                         PANEL_WIDTH - 10, PANEL_HEIGHT - 70,
                         mouseX, mouseY);

        // Граница
        drawBorder(context, startX, startY, PANEL_WIDTH, PANEL_HEIGHT, COLOR_ACCENT);

        super.render(context, mouseX, mouseY, delta);
    }

    // ============ ВКЛАДКИ ============

    private void renderTabs(DrawContext context, int x, int y,
                            int mouseX, int mouseY) {
        int tabWidth = PANEL_WIDTH / TABS.length;

        for (int i = 0; i < TABS.length; i++) {
            int tx = x + i * tabWidth;
            boolean isSelected = (i == currentTab);
            boolean isHovered = mouseX >= tx && mouseX <= tx + tabWidth
                             && mouseY >= y && mouseY <= y + 22;

            context.fill(
                tx + 1, y + 1,
                tx + tabWidth - 1, y + 22,
                isSelected ? COLOR_ACCENT : (isHovered ? COLOR_HOVER : COLOR_HEADER)
            );

            context.drawCenteredTextWithShadow(
                this.textRenderer,
                isSelected ? "§0" + TABS[i] : "§7" + TABS[i],
                tx + tabWidth / 2,
                y + 7,
                isSelected ? 0xFF000000 : COLOR_TEXT
            );
        }
    }

    // ============ КОНТЕНТ ВКЛАДОК ============

    private void renderTabContent(DrawContext context, int x, int y,
                                  int w, int h, int mouseX, int mouseY) {
        switch (currentTab) {
            case 0 -> renderCutscenesTab(context, x, y, w, h, mouseX, mouseY);
            case 1 -> renderNpcTab(context, x, y, w, h, mouseX, mouseY);
            case 2 -> renderButtonsTab(context, x, y, w, h, mouseX, mouseY);
            case 3 -> renderBlocksTab(context, x, y, w, h, mouseX, mouseY);
            case 4 -> renderCameraTab(context, x, y, w, h, mouseX, mouseY);
        }
    }

    // --- ВКЛАДКА КАТСЦЕН ---

    private void renderCutscenesTab(DrawContext context, int x, int y,
                                    int w, int h, int mouseX, int mouseY) {
        context.drawTextWithShadow(
            this.textRenderer, "§6Доступные катсцены:", x, y, COLOR_TEXT
        );

        String[] cutscenes = {
            "act0_firing",
            "act1_glass_room",
            "act2_gromov_talk",
            "ending_a_truth",
            "ending_b_replacement",
            "ending_c_denial",
            "ending_d_observer"
        };

        for (int i = 0; i < cutscenes.length; i++) {
            int btnY = y + 15 + i * 20;
            boolean hovered = mouseX >= x && mouseX <= x + w - 80
                           && mouseY >= btnY && mouseY <= btnY + 16;
            boolean selected = cutscenes[i].equals(selectedCutscene);

            context.fill(x, btnY, x + w - 85, btnY + 16,
                selected ? 0xFF8B4513 : (hovered ? COLOR_HOVER : COLOR_BUTTON));

            context.drawTextWithShadow(
                this.textRenderer,
                "§f" + cutscenes[i],
                x + 5, btnY + 4, COLOR_TEXT
            );

            // Кнопка запуска
            boolean playHover = mouseX >= x + w - 80 && mouseX <= x + w - 5
                             && mouseY >= btnY && mouseY <= btnY + 16;
            context.fill(x + w - 80, btnY, x + w - 5, btnY + 16,
                playHover ? 0xFF228B22 : 0xFF145214);
            context.drawCenteredTextWithShadow(
                this.textRenderer, "▶ ИГРАТЬ",
                x + w - 42, btnY + 4, 0xFF90EE90
            );
        }
    }

    // --- ВКЛАДКА NPC ---

    private void renderNpcTab(DrawContext context, int x, int y,
                               int w, int h, int mouseX, int mouseY) {
        context.drawTextWithShadow(
            this.textRenderer, "§6NPC персонажи:", x, y, COLOR_TEXT
        );

        String[] npcs = {
            "detective", "boss", "babushka_nina",
            "rashid", "tolya", "semyonych", "gromov", "valeriya",
            "mother", "father"
        };
        String[] labels = {
            "Детектив", "Начальник", "Бабушка Нина",
            "Рашид", "Толя", "Семёныч", "Громов", "Валерия",
            "Мама", "Папа"
        };

        int cols = 2;
        int cellW = (w - 10) / cols;

        for (int i = 0; i < npcs.length; i++) {
            int col = i % cols;
            int row = i / cols;
            int bx = x + col * cellW;
            int by = y + 15 + row * 22;

            boolean hovered = mouseX >= bx && mouseX <= bx + cellW - 5
                           && mouseY >= by && mouseY <= by + 18;

            context.fill(bx, by, bx + cellW - 5, by + 18,
                hovered ? COLOR_HOVER : COLOR_BUTTON);

            context.drawTextWithShadow(
                this.textRenderer,
                "§f+ §e" + labels[i],
                bx + 5, by + 5, COLOR_TEXT
            );
        }

        // Подсказка
        context.drawTextWithShadow(
            this.textRenderer,
            "§8ПКМ на блок чтобы разместить NPC",
            x, y + h - 15, 0xFF666666
        );
    }

    // --- ВКЛАДКА КНОПОК ---

    private void renderButtonsTab(DrawContext context, int x, int y,
                                   int w, int h, int mouseX, int mouseY) {
        context.drawTextWithShadow(
            this.textRenderer, "§6Типы 3D кнопок:", x, y, COLOR_TEXT
        );

        // Большая кнопка НАЧАТЬ
        boolean hBig = mouseX >= x && mouseX <= x + w - 5
                    && mouseY >= y + 20 && mouseY <= y + 50;
        context.fill(x, y + 20, x + w - 5, y + 50,
            hBig ? 0xFF8B4513 : COLOR_BUTTON);
        context.drawTextWithShadow(
            this.textRenderer,
            "§6★ Большая кнопка «НАЧАТЬ»",
            x + 10, y + 28, COLOR_TEXT
        );
        context.drawTextWithShadow(
            this.textRenderer,
            "§8Запускает катсцену при нажатии",
            x + 10, y + 38, 0xFF888888
        );

        // Кнопка выбора
        boolean hChoice = mouseX >= x && mouseX <= x + w - 5
                       && mouseY >= y + 60 && mouseY <= y + 90;
        context.fill(x, y + 60, x + w - 5, y + 90,
            hChoice ? 0xFF8B4513 : COLOR_BUTTON);
        context.drawTextWithShadow(
            this.textRenderer,
            "§6◆ Кнопка выбора диалога",
            x + 10, y + 68, COLOR_TEXT
        );
        context.drawTextWithShadow(
            this.textRenderer,
            "§8Блокирует игрока до выбора",
            x + 10, y + 78, 0xFF888888
        );

        // Поле команды
        context.drawTextWithShadow(
            this.textRenderer,
            "§7Команда при нажатии:",
            x, y + 105, COLOR_TEXT
        );
        context.fill(x, y + 118, x + w - 5, y + 135, 0xFF111111);
        context.drawTextWithShadow(
            this.textRenderer,
            "§8/function symbolmod:start_act0",
            x + 5, y + 123, 0xFF666666
        );
    }

    // --- ВКЛАДКА БЛОКОВ ---

    private void renderBlocksTab(DrawContext context, int x, int y,
                                  int w, int h, int mouseX, int mouseY) {
        context.drawTextWithShadow(
            this.textRenderer, "§6Декоративные блоки:", x, y, COLOR_TEXT
        );

        String[][] blocks = {
            {"Прогнившие доски", "rotten_planks"},
            {"Прогн. доски (трещины)", "rotten_planks_cracked"},
            {"Прогн. доски (обвал)", "rotten_planks_collapsed"},
            {"Советская плитка белая", "soviet_tiles_white"},
            {"Советская плитка зелёная", "soviet_tiles_green"},
            {"Советская плитка грязная", "soviet_tiles_dirty"},
            {"Ржавая металл. панель", "rusty_metal_panel"},
            {"Старое стекло", "old_glass"},
            {"Треснутое стекло", "cracked_glass"},
            {"Бетонная плита", "concrete_slab_clean"},
            {"Линолеум", "linoleum_brown"},
            {"Линолеум (облезший)", "linoleum_peeled"},
            {"Обои с цветами", "wallpaper_floral"},
            {"Рваные обои", "wallpaper_torn"},
            {"Ржавая труба", "pipe_rusty"},
            {"Вентиляционная решётка", "vent_grate"}
        };

        int maxVisible = 8;
        for (int i = 0; i < Math.min(maxVisible, blocks.length); i++) {
            int by = y + 15 + i * 20;
            boolean hovered = mouseX >= x && mouseX <= x + w - 5
                           && mouseY >= by && mouseY <= by + 16;

            context.fill(x, by, x + w - 5, by + 16,
                hovered ? COLOR_HOVER : COLOR_BUTTON);

            context.drawTextWithShadow(
                this.textRenderer,
                "§f" + blocks[i][0],
                x + 5, by + 4, COLOR_TEXT
            );

            // Кнопка выдачи
            boolean giveHover = mouseX >= x + w - 55 && mouseX <= x + w - 5
                             && mouseY >= by && mouseY <= by + 16;
            context.fill(x + w - 55, by, x + w - 5, by + 16,
                giveHover ? 0xFF006400 : 0xFF003200);
            context.drawCenteredTextWithShadow(
                this.textRenderer, "ВЗЯТЬ",
                x + w - 30, by + 4, 0xFF90EE90
            );
        }
    }

    // --- ВКЛАДКА КАМЕРЫ ---

    private void renderCameraTab(DrawContext context, int x, int y,
                                  int w, int h, int mouseX, int mouseY) {
        context.drawTextWithShadow(
            this.textRenderer, "§6Настройки камеры:", x, y, COLOR_TEXT
        );

        // Кнопки добавления точек
        renderWandButton(context, x, y + 20, w, "➕ Добавить точку камеры здесь",
                         mouseX, mouseY);
        renderWandButton(context, x, y + 42, w, "🎬 Предпросмотр пути камеры",
                         mouseX, mouseY);
        renderWandButton(context, x, y + 64, w, "🗑 Очистить все точки",
                         mouseX, mouseY);
        renderWandButton(context, x, y + 86, w, "💾 Сохранить путь камеры",
                         mouseX, mouseY);

        // Скорость
        context.drawTextWithShadow(
            this.textRenderer,
            "§7Скорость: §f" + String.format("%.1f", cameraSpeed) + "x",
            x, y + 115, COLOR_TEXT
        );
        // Ползунок скорости
        int sliderX = x + 80;
        int sliderW = w - 85;
        context.fill(sliderX, y + 113, sliderX + sliderW, y + 120, 0xFF333333);
        int thumbX = (int)(sliderX + (cameraSpeed / 3.0f) * sliderW);
        context.fill(thumbX - 3, y + 110, thumbX + 3, y + 123, COLOR_ACCENT);

        // Список точек
        context.drawTextWithShadow(
            this.textRenderer,
            "§7Точки пути: §f0 добавлено",
            x, y + 135, COLOR_TEXT
        );
    }

    private void renderWandButton(DrawContext context, int x, int y, int w,
                                   String label, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + w - 5
                       && mouseY >= y && mouseY <= y + 18;
        context.fill(x, y, x + w - 5, y + 18,
            hovered ? COLOR_HOVER : COLOR_BUTTON);
        context.drawTextWithShadow(
            this.textRenderer, "§f" + label,
            x + 7, y + 5, COLOR_TEXT
        );
    }

    // ============ ВСПОМОГАТЕЛЬНЫЕ ============

    private void drawBorder(DrawContext ctx, int x, int y, int w, int h, int color) {
        ctx.fill(x, y, x + w, y + 1, color);         // верх
        ctx.fill(x, y + h - 1, x + w, y + h, color); // низ
        ctx.fill(x, y, x + 1, y + h, color);          // лево
        ctx.fill(x + w - 1, y, x + w, y + h, color);  // право
    }

    // ============ ВВОД ============

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = (this.width - PANEL_WIDTH) / 2;
        int startY = (this.height - PANEL_HEIGHT) / 2;

        // Вкладки
        int tabW = PANEL_WIDTH / TABS.length;
        for (int i = 0; i < TABS.length; i++) {
            int tx = startX + i * tabW;
            if (mouseX >= tx && mouseX <= tx + tabW
                    && mouseY >= startY + 25 && mouseY <= startY + 47) {
                currentTab = i;
                return true;
            }
        }

        // Закрытие
        if (mouseX >= startX + PANEL_WIDTH - 22 && mouseX <= startX + PANEL_WIDTH - 5
                && mouseY >= startY + 4 && mouseY <= startY + 21) {
            this.close();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) {
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() { return false; }
}
