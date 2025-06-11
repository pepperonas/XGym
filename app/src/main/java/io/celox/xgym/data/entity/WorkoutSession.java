package io.celox.xgym.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "workout_sessions")
public class WorkoutSession {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private long startTime;
    private long endTime;
    private long duration; // in milliseconds
    private String notes;
    private boolean isCompleted;
    
    public WorkoutSession(long startTime) {
        this.startTime = startTime;
        this.isCompleted = false;
        this.notes = "";
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public long getStartTime() { return startTime; }
    public void setStartTime(long startTime) { this.startTime = startTime; }
    
    public long getEndTime() { return endTime; }
    public void setEndTime(long endTime) { 
        this.endTime = endTime;
        this.duration = endTime - startTime;
    }
    
    public long getDuration() { return duration; }
    public void setDuration(long duration) { this.duration = duration; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { this.isCompleted = completed; }
}