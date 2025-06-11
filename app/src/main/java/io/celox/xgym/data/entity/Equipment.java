package io.celox.xgym.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "equipment")
public class Equipment {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private String name;
    private String imagePath;
    private double minWeight;
    private double maxWeight;
    private long createdAt;
    private long updatedAt;
    
    public Equipment(String name, String imagePath, double minWeight, double maxWeight) {
        this.name = name;
        this.imagePath = imagePath;
        this.minWeight = minWeight;
        this.maxWeight = maxWeight;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name;
        this.updatedAt = System.currentTimeMillis();
    }
    
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { 
        this.imagePath = imagePath;
        this.updatedAt = System.currentTimeMillis();
    }
    
    public double getMinWeight() { return minWeight; }
    public void setMinWeight(double minWeight) { 
        this.minWeight = minWeight;
        this.updatedAt = System.currentTimeMillis();
    }
    
    public double getMaxWeight() { return maxWeight; }
    public void setMaxWeight(double maxWeight) { 
        this.maxWeight = maxWeight;
        this.updatedAt = System.currentTimeMillis();
    }
    
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    public long getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(long updatedAt) { this.updatedAt = updatedAt; }
}