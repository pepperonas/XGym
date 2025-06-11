package io.celox.xgym.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "achievements")
public class Achievement {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String title;
    private String description;
    private String iconName;
    private int targetValue;
    private int currentValue;
    private boolean isUnlocked;
    private long unlockedAt;
    private String category; // "workouts", "equipment", "time", "weight", etc.
    
    public Achievement(String title, String description, String iconName, int targetValue, String category) {
        this.title = title;
        this.description = description;
        this.iconName = iconName;
        this.targetValue = targetValue;
        this.currentValue = 0;
        this.isUnlocked = false;
        this.category = category;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getIconName() { return iconName; }
    public void setIconName(String iconName) { this.iconName = iconName; }
    
    public int getTargetValue() { return targetValue; }
    public void setTargetValue(int targetValue) { this.targetValue = targetValue; }
    
    public int getCurrentValue() { return currentValue; }
    public void setCurrentValue(int currentValue) { 
        this.currentValue = currentValue;
        if (currentValue >= targetValue && !isUnlocked) {
            this.isUnlocked = true;
            this.unlockedAt = System.currentTimeMillis();
        }
    }
    
    public boolean isUnlocked() { return isUnlocked; }
    public void setUnlocked(boolean unlocked) { this.isUnlocked = unlocked; }
    
    public long getUnlockedAt() { return unlockedAt; }
    public void setUnlockedAt(long unlockedAt) { this.unlockedAt = unlockedAt; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getProgressPercentage() {
        if (targetValue == 0) return 100;
        return Math.min(100, (currentValue * 100) / targetValue);
    }
}