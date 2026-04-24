package com.symbolmod.ui;

import com.symbolmod.util.ClueCollector;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import java.util.List;

public class DiaryScreen extends Screen {

    private static final int DIARY_WIDTH = 280;
    private static final int DIARY_HEIGHT = 200;

    // Цвета потрёпанной бумаги
    private static final int COLOR_PAPER = 0xFFF5E6C8;      // старая бумага
    private static final int COLOR_INK = 0xFF2A1A0A;         // тёмные чернила
    private static final int COLOR_ACCENT = 0xFF8B4513;      // коричневый акцент
    private static final int COLOR_CRITICAL = 0xFF8B0000;    // тёмно-красный для крит. улик
    private static final int COLOR_HIGH = 0xFF654321;        // коричневый для важных
    private static final int COLOR_SHADOW = 0x88000000;      // тень

    private int selectedClueIndex = -1;
    private int scrollOffset = 0;
    private List<String> collectedClues;
    private int animTick = 0;

    public DiaryScreen() {
        super(Text.literal("Дневник улик"));
    }

    @Override
    protected void init() {
        super.init();
        refreshClues();
    }

    private void refreshClues() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            collectedClues = ClueCollector.getCollectedClues(client.player);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        animTick++;

        int startX = (this.width - DIARY_WIDTH) / 2;
        int startY = (this.height - DIARY_HEIGHT) / 2;

        // Затемнение фона
        context.fill(0, 0, this.width, this.height, 0xCC000000);

        // Тень книги
        context.fill(
            startX + 6, startY + 6,
            startX + DIARY_WIDTH + 6, startY + DIARY_HEIGHT + 6,
            COLOR_SHADOW
        );

        // Основная книга
        context.fill(
            startX, startY,
            startX + DIARY_WIDTH, startY + DIARY_HEIGHT,
            COLOR_PAPER
        );

        // Левая страница (список улик)
        renderLeftPage(context, startX, startY, mouseX, mouseY);

        // Разделитель (корешок книги)
        context.fill(
            startX + DIARY_WIDTH / 2 - 2, startY + 10,
            startX + DIARY_WIDTH / 2 + 2, startY + DIARY_HEIGHT - 10,
            COLOR_ACCENT
        );

        // Правая страница (детали улики)
        renderRightPage(context, startX + DIARY_WIDTH / 2 + 5, startY);

        // Заголовок (с символом глаза)
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            "§8✦ ДНЕВНИК УЛИК ✦",
            startX + DIARY_WIDTH / 4,
            startY + 5,
            COLOR_INK
        );

        // Счётчик улик
        context.drawTextWithShadow(
            this.textRenderer,
            "Найдено: " + (collectedClues != null ? collectedClues.size() : 0) +
            "/" + ClueCollector.ALL_CLUES.size(),
            startX + 10,
            startY + DIARY_HEIGHT - 15,
            COLOR_ACCENT
        );

        // Кнопка закрытия
        renderCloseButton(context, startX + DIARY_WIDTH - 20, startY + 5, mouseX, mouseY);

        super.render(context, mouseX, mouseY, delta);
    }

    // ============ ЛЕВАЯ СТРАНИЦА — СПИСОК ============

    private void renderLeftPage(DrawContext context, int startX, int startY,
                                int mouseX, int mouseY) {
        int pageWidth = DIARY_WIDTH / 2 - 10;
        int listY = startY + 20;

        context.drawTextWithShadow(
            this.textRenderer,
            "§8Улики:",
            startX + 10, listY,
            COLOR_INK
        );
        listY += 12;

        if (collectedClues == null || collectedClues.isEmpty()) {
            context.drawTextWithShadow(
                this.textRenderer,
                "§8§o(Пусто)",
                startX + 10, listY,
                COLOR_ACCENT
            );
            return;
        }

        int maxVisible = 13;
        int start = scrollOffset;
        int end = Math.min(start + maxVisible, collectedClues.size());

        for (int i = start; i < end; i++) {
            String clueId = collectedClues.get(i);
            ClueCollector.ClueData clue = ClueCollector.ALL_CLUES.get(clueId);
            if (clue == null) continue;

            boolean isSelected = (i == selectedClueIndex);
            boolean isHovered = (mouseX >= startX + 8 &&
                                 mouseX <= startX + pageWidth &&
                                 mouseY >= listY - 1 &&
                                 mouseY <= listY + 10);

            // Подсветка при наведении
            if (isSelected) {
                context.fill(
                    startX + 8, listY - 1,
                    startX + pageWidth, listY + 10,
                    0x44654321
                );
            } else if (isHovered) {
                context.fill(
                    startX + 8, listY - 1,
                    startX + pageWidth, listY + 10,
                    0x22654321
                );
            }

            // Цвет по важности
            int color = switch (clue.importance) {
                case CRITICAL -> COLOR_CRITICAL;
                case HIGH -> COLOR_HIGH;
                case MEDIUM -> COLOR_INK;
                default -> COLOR_ACCENT;
            };

            // Иконка важности
            String icon = switch (clue.importance) {
                case CRITICAL -> "★ ";
                case HIGH -> "◆ ";
                case MEDIUM -> "• ";
                default -> "  ";
            };

            // Обрезаем название если длинное
            String name = clue.name;
            if (name.length() > 20) name = name.substring(0, 17) + "...";

            context.drawTextWithShadow(
                this.textRenderer,
                icon + name,
                startX + 10, listY,
                color
            );

            listY += 12;
        }

        // Стрелки прокрутки
        if (scrollOffset > 0) {
            context.drawCenteredTextWithShadow(
                this.textRenderer, "▲",
                startX + pageWidth / 2, startY + 18,
                COLOR_ACCENT
            );
        }
        if (end < collectedClues.size()) {
            context.drawCenteredTextWithShadow(
                this.textRenderer, "▼",
                startX + pageWidth / 2, startY + DIARY_HEIGHT - 20,
                COLOR_ACCENT
            );
        }
    }

    // ============ ПРАВАЯ СТРАНИЦА — ДЕТАЛИ ============

    private void renderRightPage(DrawContext context, int startX, int startY) {
        int pageWidth = DIARY_WIDTH / 2 - 10;

        if (selectedClueIndex < 0 || collectedClues == null
                || selectedClueIndex >= collectedClues.size()) {
            // Пусто — рисуем символ глаза
            renderEyeSymbol(context, startX + pageWidth / 2, startY + DIARY_HEIGHT / 2);
            context.drawCenteredTextWithShadow(
                this.textRenderer,
                "§8§oВыберите улику",
                startX + pageWidth / 2,
                startY + DIARY_HEIGHT / 2 + 20,
                COLOR_ACCENT
            );
            return;
        }

        String clueId = collectedClues.get(selectedClueIndex);
        ClueCollector.ClueData clue = ClueCollector.ALL_CLUES.get(clueId);
        if (clue == null) return;

        int textY = startY + 18;

        // Название улики
        context.drawCenteredTextWithShadow(
            this.textRenderer,
            "§8" + clue.name,
            startX + pageWidth / 2,
            textY,
            COLOR_INK
        );
        textY += 14;

        // Местоположение
        context.drawTextWithShadow(
            this.textRenderer,
            "§8📍 " + clue.location,
            startX + 5, textY,
            COLOR_ACCENT
        );
        textY += 12;

        // Разделитель
        context.fill(startX + 5, textY, startX + pageWidth - 5, textY + 1, COLOR_ACCENT);
        textY += 5;

        // Рассуждение детектива (с переносом строк)
        String reasoning = clue.reasoning;
        List<String> lines = wrapText(reasoning, pageWidth - 10);

        context.drawTextWithShadow(
            this.textRenderer,
            "§8§o[Детектив:]",
            startX + 5, textY,
            COLOR_INK
        );
        textY += 11;

        int maxLines = 10;
        for (int i = 0; i < Math.min(lines.size(), maxLines); i++) {
            context.drawTextWithShadow(
                this.textRenderer,
                "§8" + lines.get(i),
                startX + 5, textY,
                COLOR_INK
            );
            textY += 10;
        }

        // Важность
        String importanceText = switch (clue.importance) {
            case CRITICAL -> "§4★ КРИТИЧЕСКАЯ УЛИКА";
            case HIGH -> "§6◆ Важная улика";
            case MEDIUM -> "§8• Улика";
            default -> "§8  Наблюдение";
        };

        context.drawTextWithShadow(
            this.textRenderer,
            importanceText,
            startX + 5,
            startY + DIARY_HEIGHT - 20,
            COLOR_INK
        );
    }

    // ============ СИМВОЛ ГЛАЗА ============

    private void renderEyeSymbol(DrawContext context, int centerX, int centerY) {
        // Анимированный пульсирующий символ глаза
        float pulse = (float)(Math.sin(animTick * 0.05f) * 0.3f + 0.7f);
        int alpha = (int)(pulse * 100);
        int color = (alpha << 24) | 0x8B4513;

        // Внешний круг (белок глаза)
        for (int i = 0; i < 360; i += 10) {
            double rad = Math.toRadians(i);
            int x = (int)(centerX + Math.cos(rad) * 20);
            int y = (int)(centerY + Math.sin(rad) * 10);
            context.fill(x - 1, y - 1, x + 1, y + 1, color);
        }

        // Зрачок
        context.fill(centerX - 5, centerY - 5, centerX + 5, centerY + 5, color);

        // Прищур (горизонтальные линии)
        context.fill(centerX - 22, centerY - 1, centerX + 22, centerY + 1, color);
    }

    // ============ ВСПОМОГАТЕЛЬНЫЕ ============

    private List<String> wrapText(String text, int maxWidth) {
        List<String> lines = new java.util.ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            String testLine = currentLine.length() > 0
                ? currentLine + " " + word
                : word;

            if (this.textRenderer.getWidth(testLine) > maxWidth - 10) {
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder(word);
                }
            } else {
                currentLine = new StringBuilder(testLine);
            }
        }
        if (currentLine.length() > 0) lines.add(currentLine.toString());

        return lines;
    }

    private void renderCloseButton(DrawContext context, int x, int y,
                                   int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + 14
                       && mouseY >= y && mouseY <= y + 14;

        context.fill(x, y, x + 14, y + 14,
            hovered ? 0xFF8B0000 : 0xFF654321);

        context.drawCenteredTextWithShadow(
            this.textRenderer,
            "✕",
            x + 7, y + 3,
            0xFFFFFFFF
        );
    }

    // ============ ВВОД ============

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = (this.width - DIARY_WIDTH) / 2;
        int startY = (this.height - DIARY_HEIGHT) / 2;

        // Клик по кнопке закрытия
        int closeX = startX + DIARY_WIDTH - 20;
        int closeY = startY + 5;
        if (mouseX >= closeX && mouseX <= closeX + 14
                && mouseY >= closeY && mouseY <= closeY + 14) {
            this.close();
            return true;
        }

        // Клик по списку улик (левая страница)
        int pageWidth = DIARY_WIDTH / 2 - 10;
        if (mouseX >= startX + 8 && mouseX <= startX + pageWidth) {
            int relY = (int)(mouseY - startY - 32);
            if (relY >= 0) {
                int index = scrollOffset + relY / 12;
                if (collectedClues != null && index < collectedClues.size()) {
                    selectedClueIndex = index;

                    // Воспроизводим первую реплику озвучки при открытии
                    String clueId = collectedClues.get(index);
                    ClueCollector.ClueData clue = ClueCollector.ALL_CLUES.get(clueId);
                    if (clue != null && clue.voiceLines.length > 0) {
                        MinecraftClient.getInstance().player.playSound(
                            com.symbolmod.registry.ModSounds.getVoiceLine(clue.voiceLines[0]),
                            net.minecraft.sound.SoundCategory.VOICE,
                            1.0f, 1.0f
                        );
                    }
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (collectedClues == null) return false;
        scrollOffset = Math.max(0,
            Math.min(scrollOffset - (int)amount,
                     Math.max(0, collectedClues.size() - 13))
        );
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { // ESC
            this.close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
