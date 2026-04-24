package com.symbolmod.item;

import com.symbolmod.cutscene.CameraPath;
import com.symbolmod.cutscene.CutsceneManager;
import com.symbolmod.entity.NPCEntity;
import com.symbolmod.registry.ModEntities;
import com.symbolmod.ui.DirectorWandScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import java.util.*;

public class DirectorWandItem extends Item {

    // Режимы палочки
    public enum WandMode {
        SELECT,       // выбор и просмотр
        PLACE_NPC,    // расстановка NPC
        CAMERA,       // добавление точек камеры
        BUTTON,       // создание 3D кнопок
        CUTSCENE      // управление катсценами
    }

    // Текущий NPC для расстановки
    private static String selectedNpcType = "detective";
    // Текущая катсцена для предпросмотра
    private static String selectedCutscene = "act0_firing";
    // Режим палочки
    private static WandMode currentMode = WandMode.SELECT;
    // Записанные точки камеры
    private static final List<CameraWaypoint> recordedWaypoints = new ArrayList<>();

    public DirectorWandItem(Settings settings) {
        super(settings);
    }

    // ============ ПКМ — открываем GUI ============

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.isClient) {
            MinecraftClient.getInstance().setScreen(new DirectorWandScreen());
        }

        return TypedActionResult.success(stack);
    }

    // ============ ЛКМ на блок — действие по режиму ============

    @Override
    public ActionResult useOnBlock(
            net.minecraft.item.ItemUsageContext context) {

        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        BlockPos pos = context.getBlockPos().up();

        if (world.isClient) return ActionResult.SUCCESS;

        switch (currentMode) {

            case PLACE_NPC -> {
                // Размещаем NPC
                spawnNPC(world, player, pos, selectedNpcType);
                player.sendMessage(
                    Text.literal("§6[Режиссёр] §fNPC «" + selectedNpcType +
                        "» размещён на " + pos.toShortString()),
                    true
                );
            }

            case CAMERA -> {
                // Добавляем точку камеры
                Vec3d look = player.getRotationVec(1.0f);
                CameraWaypoint wp = new CameraWaypoint(
                    Vec3d.ofCenter(pos),
                    player.getPitch(),
                    player.getYaw()
                );
                recordedWaypoints.add(wp);

                player.sendMessage(
                    Text.literal("§6[Режиссёр] §fТочка камеры #" +
                        recordedWaypoints.size() + " добавлена: " +
                        pos.toShortString()),
                    true
                );

                // Звук подтверждения
                world.playSound(null, pos,
                    SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(),
                    SoundCategory.PLAYERS, 0.5f,
                    1.0f + recordedWaypoints.size() * 0.1f
                );
            }

            case BUTTON -> {
                // Создаём 3D кнопку выбора
                createChoiceButton(world, player, pos);
                player.sendMessage(
                    Text.literal("§6[Режиссёр] §f3D кнопка создана на " +
                        pos.toShortString()),
                    true
                );
            }

            default -> {}
        }

        return ActionResult.SUCCESS;
    }

    // ============ SHIFT+ПКМ — смена режима ============

    @Override
    public ActionResult useOnEntity(
            ItemStack stack, PlayerEntity user,
            net.minecraft.entity.LivingEntity entity, Hand hand) {

        if (!user.getWorld().isClient && entity instanceof NPCEntity npc) {
            // Открываем редактор NPC
            user.sendMessage(
                Text.literal("§6[Режиссёр] §fNPC: " + npc.getNpcId() +
                    "\n§7Анимация: " + npc.getCurrentAnimationName() +
                    "\n§7Диалог: " + npc.getDialogueId()),
                false
            );
        }

        return ActionResult.SUCCESS;
    }

    // ============ РАЗМЕЩЕНИЕ NPC ============

    private static void spawnNPC(World world, PlayerEntity player,
                                  BlockPos pos, String npcType) {

        NPCEntity npc = ModEntities.DETECTIVE.create(world);
        if (npc == null) return;

        npc.refreshPositionAndAngles(
            pos.getX() + 0.5,
            pos.getY(),
            pos.getZ() + 0.5,
            player.getYaw() + 180f,
            0f
        );

        npc.setNpcId(npcType);
        world.spawnEntity(npc);

        // Сохраняем в NBT для восстановления
        NbtCompound tag = npc.writeNbt(new NbtCompound());
        tag.putString("NpcType", npcType);
    }

    // ============ СОЗДАНИЕ 3D КНОПКИ ============

    private static void createChoiceButton(World world, PlayerEntity player,
                                            BlockPos pos) {
        // Спавним блок-кнопку через setBlockState
        world.setBlockState(pos,
            com.symbolmod.registry.ModBlocks.CHOICE_BUTTON_BLOCK.getDefaultState()
        );
    }

    // ============ ПРЕДПРОСМОТР ПУТИ КАМЕРЫ ============

    public static void previewCameraPath(ServerPlayerEntity player) {
        if (recordedWaypoints.isEmpty()) {
            player.sendMessage(
                Text.literal("§c[Режиссёр] Нет точек камеры для предпросмотра"),
                true
            );
            return;
        }

        // Строим путь и запускаем катсцену предпросмотра
        CameraPath path = new CameraPath();
        path.setDuration(recordedWaypoints.size() * 3);

        for (CameraWaypoint wp : recordedWaypoints) {
            path.addWaypointFull(wp.position, wp.pitch, wp.yaw);
        }

        CutsceneManager.startPreview(player, path);

        player.sendMessage(
            Text.literal("§6[Режиссёр] §fПредпросмотр запущен (" +
                recordedWaypoints.size() + " точек)"),
            true
        );
    }

    // ============ СОХРАНЕНИЕ ПУТИ КАМЕРЫ ============

    public static void saveCameraPath(ServerPlayerEntity player, String pathName) {
        if (recordedWaypoints.isEmpty()) return;

        NbtCompound data = new NbtCompound();
        NbtList list = new NbtList();

        for (CameraWaypoint wp : recordedWaypoints) {
            NbtCompound wpNbt = new NbtCompound();
            wpNbt.putDouble("x", wp.position.x);
            wpNbt.putDouble("y", wp.position.y);
            wpNbt.putDouble("z", wp.position.z);
            wpNbt.putFloat("pitch", wp.pitch);
            wpNbt.putFloat("yaw", wp.yaw);
            list.add(wpNbt);
        }

        data.put("Waypoints", list);
        data.putString("PathName", pathName);

        // Сохраняем в мировые данные
        player.getServer().getOverworld()
            .getPersistentStateManager()
            .set("symbol_camera_" + pathName, new CameraPathState(data));

        player.sendMessage(
            Text.literal("§6[Режиссёр] §fПуть «" + pathName +
                "» сохранён (" + recordedWaypoints.size() + " точек)"),
            true
        );
    }

    // ============ ЗАГРУЗКА ПУТИ КАМЕРЫ ============

    public static void loadCameraPath(ServerPlayerEntity player, String pathName) {
        CameraPathState state = player.getServer().getOverworld()
            .getPersistentStateManager()
            .getOrCreate(
                nbt -> new CameraPathState(nbt),
                CameraPathState::new,
                "symbol_camera_" + pathName
            );

        recordedWaypoints.clear();

        NbtList list = state.data.getList("Waypoints", 10);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound wpNbt = list.getCompound(i);
            recordedWaypoints.add(new CameraWaypoint(
                new Vec3d(wpNbt.getDouble("x"), wpNbt.getDouble("y"), wpNbt.getDouble("z")),
                wpNbt.getFloat("pitch"),
                wpNbt.getFloat("yaw")
            ));
        }

        player.sendMessage(
            Text.literal("§6[Режиссёр] §fПуть «" + pathName +
                "» загружен (" + recordedWaypoints.size() + " точек)"),
            true
        );
    }

    // ============ ЗАПУСК КАТСЦЕНЫ ============

    public static void launchCutscene(ServerPlayerEntity player, String cutsceneId) {
        CutsceneManager.startCutscene(player, cutsceneId);
        player.sendMessage(
            Text.literal("§6[Режиссёр] §fКатсцена «" + cutsceneId + "» запущена"),
            true
        );
    }

    // ============ ОЧИСТКА ТОЧЕК ============

    public static void clearWaypoints(ServerPlayerEntity player) {
        int count = recordedWaypoints.size();
        recordedWaypoints.clear();
        player.sendMessage(
            Text.literal("§6[Режиссёр] §fОчищено " + count + " точек камеры"),
            true
        );
    }

    // ============ СМЕНА РЕЖИМА ============

    public static void setMode(WandMode mode) {
        currentMode = mode;
    }

    public static WandMode getMode() {
        return currentMode;
    }

    public static void setSelectedNpc(String npcType) {
        selectedNpcType = npcType;
    }

    public static void setSelectedCutscene(String cutsceneId) {
        selectedCutscene = cutsceneId;
    }

    public static List<CameraWaypoint> getWaypoints() {
        return Collections.unmodifiableList(recordedWaypoints);
    }

    // ============ ВСПОМОГАТЕЛЬНЫЕ КЛАССЫ ============

    public static class CameraWaypoint {
        public final Vec3d position;
        public final float pitch;
        public final float yaw;

        public CameraWaypoint(Vec3d position, float pitch, float yaw) {
            this.position = position;
            this.pitch = pitch;
            this.yaw = yaw;
        }
    }

    public static class CameraPathState
            extends net.minecraft.world.PersistentState {
        public NbtCompound data;

        public CameraPathState() {
            this.data = new NbtCompound();
        }

        public CameraPathState(NbtCompound nbt) {
            this.data = nbt;
        }

        @Override
        public NbtCompound writeNbt(NbtCompound nbt) {
            nbt.copyFrom(this.data);
            return nbt;
        }
    }
}
