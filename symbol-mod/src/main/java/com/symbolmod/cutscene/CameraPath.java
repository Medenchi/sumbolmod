package com.symbolmod.cutscene;

import net.minecraft.util.math.Vec3d;
import java.util.*;

public class CameraPath {
    
    private int duration; // в секундах
    private String pathId;
    private String music;
    private boolean playerControl = false;
    private List<Vec3d> waypoints = new ArrayList<>();
    private List<Vec3d> rotations = new ArrayList<>();
    
    public CameraPath setDuration(int seconds) {
        this.duration = seconds;
        return this;
    }
    
    public CameraPath setPath(String pathId) {
        this.pathId = pathId;
        loadPresetPath(pathId);
        return this;
    }
    
    public CameraPath setMusic(String music) {
        this.music = music;
        return this;
    }
    
    public CameraPath enablePlayerControl(boolean enable) {
        this.playerControl = enable;
        return this;
    }
    
    public CameraPath addWaypoint(double x, double y, double z) {
        waypoints.add(new Vec3d(x, y, z));
        return this;
    }
    
    public CameraPath addRotation(double pitch, double yaw, double roll) {
        rotations.add(new Vec3d(pitch, yaw, roll));
        return this;
    }
    
    // ============ ПРЕДУСТАНОВЛЕННЫЕ ПУТИ ============
    
    private void loadPresetPath(String id) {
        switch (id) {
            case "building_exterior":
                // Камера поднимается вверх по зданию Аргус
                addWaypoint(0, 65, 0);
                addWaypoint(0, 90, 0);
                addRotation(-45, 0, 0);
                addRotation(-30, 0, 0);
                break;
                
            case "office_walk":
                // Камера следует за детективом в офисе
                addWaypoint(10, 70, 5);
                addWaypoint(15, 70, 8);
                addWaypoint(20, 70, 8);
                break;
                
            case "basement_reveal":
                // Катсцена открытия финальной комнаты
                addWaypoint(50, 30, 50);
                addWaypoint(50, 30, 55);
                addRotation(0, 180, 0);
                break;
                
            case "village_evacuation":
                // Концовка А - эвакуация деревни
                addWaypoint(100, 75, 100);
                addWaypoint(120, 80, 120);
                addRotation(-20, 45, 0);
                break;
        }
    }
    
    // ============ ИНТЕРПОЛЯЦИЯ КАМЕРЫ ============
    
    public Vec3d getPositionAt(float progress) {
        if (waypoints.isEmpty()) return Vec3d.ZERO;
        if (waypoints.size() == 1) return waypoints.get(0);
        
        float totalSegments = waypoints.size() - 1;
        float segmentIndex = progress * totalSegments;
        int index = (int) segmentIndex;
        float localProgress = segmentIndex - index;
        
        if (index >= waypoints.size() - 1) {
            return waypoints.get(waypoints.size() - 1);
        }
        
        Vec3d start = waypoints.get(index);
        Vec3d end = waypoints.get(index + 1);
        
        // Плавная интерполяция (кубическая)
        return start.lerp(end, smoothstep(localProgress));
    }
    
    public Vec3d getRotationAt(float progress) {
        if (rotations.isEmpty()) return Vec3d.ZERO;
        if (rotations.size() == 1) return rotations.get(0);
        
        float totalSegments = rotations.size() - 1;
        float segmentIndex = progress * totalSegments;
        int index = (int) segmentIndex;
        float localProgress = segmentIndex - index;
        
        if (index >= rotations.size() - 1) {
            return rotations.get(rotations.size() - 1);
        }
        
        Vec3d start = rotations.get(index);
        Vec3d end = rotations.get(index + 1);
        
        return start.lerp(end, smoothstep(localProgress));
    }
    
    private float smoothstep(float t) {
        return t * t * (3.0f - 2.0f * t);
    }
    
    public int getDuration() {
        return duration;
    }
    
    public boolean allowsPlayerControl() {
        return playerControl;
    }
}
