package com.symbolmod.dialogue;

import com.symbolmod.SymbolMod;
import com.symbolmod.registry.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import com.google.gson.*;
import java.io.*;
import java.util.*;

public class DialogueSystem {
    
    private static final Map<String, DialogueTree> DIALOGUES = new HashMap<>();
    
    // ============ ИНИЦИАЛИЗАЦИЯ ============
    
    public static void init() {
        loadDialogues();
    }
    
    private static void loadDialogues() {
        // Загрузка всех диалогов из JSON файлов
        
        // АКТ 0 - Увольнение
        registerDialogue("boss_firing", createBossFiringDialogue());
        registerDialogue("detective_reaction", createDetectiveReactionDialogue());
        
        // АКТ 2 - Деревня
        registerDialogue("nina_talk", createNinaTalkDialogue());
        registerDialogue("rashid_talk", createRashidTalkDialogue());
        registerDialogue("tolya_talk", createTolyaTalkDialogue());
        registerDialogue("gromov_truth", createGromovTruthDialogue());
        
        // Концовки
        registerDialogue("ending_a_monologue", createEndingADialogue());
        registerDialogue("ending_b_acceptance", createEndingBDialogue());
        registerDialogue("ending_c_lie", createEndingCDialogue());
        registerDialogue("ending_d_final_words", createEndingDDialogue());
    }
    
    // ============ СОЗДАНИЕ ДИАЛОГОВ ============
    
    private static DialogueTree createBossFiringDialogue() {
        DialogueTree tree = new DialogueTree();
        
        tree.addNode("start", new DialogueNode()
            .setText("Ты уволен.")
            .setVoiceLine("boss_firing_1")
            .setNextNode("response"));
        
        tree.addNode("response", new DialogueNode()
            .setText("Собирай свои вещи и проваливай из моего агентства.")
            .setVoiceLine("boss_firing_2")
            .setNextNode("end"));
        
        return tree;
    }
    
    private static DialogueTree createGromovTruthDialogue() {
        DialogueTree tree = new DialogueTree();
        
        tree.addNode("start", new DialogueNode()
            .setText("Я ждал. Долго ждал. Заходи.")
            .setVoiceLine("gromov_truth_1")
            .addChoice("Вы знали, что я приду?", "knew")
            .addChoice("Что здесь происходит?", "what_happens")
            .addChoice("[Молчать]", "silent"));
        
        tree.addNode("knew", new DialogueNode()
            .setText("Завод был прикрытием. Деревня — эксперимент.")
            .setVoiceLine("gromov_truth_2")
            .setNextNode("experiment"));
        
        tree.addNode("experiment", new DialogueNode()
            .setText("Мы изучали, как люди живут, когда за ними наблюдают...")
            .setVoiceLine("gromov_truth_3")
            .setText("...и как живут, когда не знают.")
            .setVoiceLine("gromov_truth_4")
            .setNextNode("revelation"));
        
        tree.addNode("revelation", new DialogueNode()
            .setText("Самое страшное открытие было не в том, что за ними следят.")
            .setVoiceLine("gromov_truth_5")
            .setText("А в том, что людям это безразлично.")
            .setVoiceLine("gromov_truth_6")
            .addChoice("Вы... чудовище.", "monster")
            .addChoice("Почему вы рассказываете мне это?", "why_tell")
            .addChoice("[Уйти]", "leave"));
        
        tree.addNode("monster", new DialogueNode()
            .setText("Возможно. Но эксперимент продолжается. С тобой или без тебя.")
            .setVoiceLine("gromov_truth_7")
            .setNextNode("end"));
        
        return tree;
    }
    
    private static DialogueTree createTolyaTalkDialogue() {
        DialogueTree tree = new DialogueTree();
        
        tree.addNode("start", new DialogueNode()
            .setText("С завода идёшь?")
            .setVoiceLine("tolya_1")
            .addChoice("Да. А вы откуда знаете?", "how_know")
            .addChoice("А что там можно увидеть?", "what_see")
            .addChoice("[Пройти мимо]", "leave"));
        
        tree.addNode("what_see", new DialogueNode()
            .setText("Я сам там работал. Молодой был, дурной. Восьмой отдел.")
            .setVoiceLine("tolya_2")
            .setText("Нам говорили — контроль качества.")
            .setVoiceLine("tolya_3")
            .setText("Мы маркировали вещи. Каждый дом. Каждую семью. Сорок лет.")
            .setVoiceLine("tolya_4")
            .setNextNode("symbol_reveal"));
        
        tree.addNode("symbol_reveal", new DialogueNode()
            .setText("[Толя рисует в воздухе символ прищуренного глаза]")
            .setVoiceLine("tolya_5")
            .setText("Нас было пятеро. Четверо уже умерли. Я остался.")
            .setVoiceLine("tolya_6")
            .addChoice("У вас тоже есть этот символ?", "has_symbol")
            .addChoice("Зачем вы это делали?", "why_mark"));
        
        tree.addNode("has_symbol", new DialogueNode()
            .setText("Думаешь у меня тоже есть? Я искал. Не нашёл.")
            .setVoiceLine("tolya_7")
            .setText("Но это ничего не значит...")
            .setVoiceLine("tolya_8")
            .setText("Может они меня оставили как наблюдать за самим собой.")
            .setVoiceLine("tolya_9")
            .setNextNode("end"));
        
        return tree;
    }
    
    // Концовки
    
    private static DialogueTree createEndingADialogue() {
        DialogueTree tree = new DialogueTree();
        
        tree.addNode("start", new DialogueNode()
            .setText("Эксперимент не закончится.")
            .setVoiceLine("ending_a_1")
            .setNextNode("response"));
        
        tree.addNode("response", new DialogueNode()
            .setText("Я знаю.")
            .setVoiceLine("ending_a_2")
            .setNextNode("respect"));
        
        tree.addNode("respect", new DialogueNode()
            .setText("И всё равно позвонил.")
            .setVoiceLine("ending_a_3")
            .setText("[Громов кивает с уважением]")
            .setNextNode("end"));
        
        return tree;
    }
    
    private static DialogueTree createEndingDDialogue() {
        DialogueTree tree = new DialogueTree();
        
        tree.addNode("start", new DialogueNode()
            .setText("Ты нашёл всё.")
            .setVoiceLine("ending_d_1")
            .setNextNode("question"));
        
        tree.addNode("question", new DialogueNode()
            .setText("И что ты решил?")
            .setVoiceLine("ending_d_2")
            .addChoice("Я ухожу.", "leave")
            .addChoice("[Молчание]", "silence"));
        
        tree.addNode("silence", new DialogueNode()
            .setText("[Детектив медленно встаёт в ряд с жителями]")
            .setVoiceLine("ending_d_3")
            .setText("Наблюдение продолжается.")
            .setNextNode("end"));
        
        return tree;
    }
    
    // Остальные диалоги (placeholder)
    private static DialogueTree createDetectiveReactionDialogue() { return new DialogueTree(); }
    private static DialogueTree createNinaTalkDialogue() { return new DialogueTree(); }
    private static DialogueTree createRashidTalkDialogue() { return new DialogueTree(); }
    private static DialogueTree createEndingBDialogue() { return new DialogueTree(); }
    private static DialogueTree createEndingCDialogue() { return new DialogueTree(); }
    
    // ============ ОТКРЫТИЕ ДИАЛОГА ============
    
    public static void openDialogue(PlayerEntity player, String dialogueId) {
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;
        
        DialogueTree tree = DIALOGUES.get(dialogueId);
        if (tree == null) {
            SymbolMod.LOGGER.error("Диалог не найден: " + dialogueId);
            return;
        }
        
        // Запускаем диалог с первой ноды
        showNode(serverPlayer, tree, "start");
    }
    
    private static void showNode(ServerPlayerEntity player, DialogueTree tree, String nodeId) {
        DialogueNode node = tree.getNode(nodeId);
        if (node == null) return;
        
        // Показываем текст
        player.sendMessage(Text.literal("§f" + node.text), false);
        
        // Воспроизводим озвучку
        if (node.voiceLine != null) {
            player.playSound(
                ModSounds.getVoiceLine(node.voiceLine),
                SoundCategory.VOICE,
                1.0f,
                1.0f
            );
        }
        
        // Если есть выборы - показываем 3D кнопки
        if (!node.choices.isEmpty()) {
            ChoiceButton.showChoices(player, node.choices, tree);
        } else if (node.nextNode != null) {
            // Автоматический переход
            showNode(player, tree, node.nextNode);
        }
    }
    
    private static void registerDialogue(String id, DialogueTree tree) {
        DIALOGUES.put(id, tree);
    }
    
    // ============ КЛАССЫ ДАННЫХ ============
    
    public static class DialogueTree {
        private Map<String, DialogueNode> nodes = new HashMap<>();
        
        public void addNode(String id, DialogueNode node) {
            nodes.put(id, node);
        }
        
        public DialogueNode getNode(String id) {
            return nodes.get(id);
        }
    }
    
    public static class DialogueNode {
        String text;
        String voiceLine;
        String nextNode;
        List<DialogueChoice> choices = new ArrayList<>();
        
        public DialogueNode setText(String text) {
            this.text = text;
            return this;
        }
        
        public DialogueNode setVoiceLine(String voiceLine) {
            this.voiceLine = voiceLine;
            return this;
        }
        
        public DialogueNode setNextNode(String nextNode) {
            this.nextNode = nextNode;
            return this;
        }
        
        public DialogueNode addChoice(String text, String targetNode) {
            choices.add(new DialogueChoice(text, targetNode));
            return this;
        }
    }
    
    public static class DialogueChoice {
        String text;
        String targetNode;
        
        public DialogueChoice(String text, String targetNode) {
            this.text = text;
            this.targetNode = targetNode;
        }
    }
}
