package io.celox.xgym.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "exercises",
        foreignKeys = {
            @ForeignKey(entity = WorkoutSession.class,
                       parentColumns = "id",
                       childColumns = "workoutSessionId",
                       onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Equipment.class,
                       parentColumns = "id",
                       childColumns = "equipmentId",
                       onDelete = ForeignKey.CASCADE)
        })
public class Exercise {
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    private int workoutSessionId;
    private int equipmentId;
    private double weight;
    private int sets;
    private int reps;
    private String notes;
    private long timestamp;
    
    public Exercise(int workoutSessionId, int equipmentId, double weight, int sets, int reps) {
        this.workoutSessionId = workoutSessionId;
        this.equipmentId = equipmentId;
        this.weight = weight;
        this.sets = sets;
        this.reps = reps;
        this.notes = "";
        this.timestamp = System.currentTimeMillis();
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getWorkoutSessionId() { return workoutSessionId; }
    public void setWorkoutSessionId(int workoutSessionId) { this.workoutSessionId = workoutSessionId; }
    
    public int getEquipmentId() { return equipmentId; }
    public void setEquipmentId(int equipmentId) { this.equipmentId = equipmentId; }
    
    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }
    
    public int getSets() { return sets; }
    public void setSets(int sets) { this.sets = sets; }
    
    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}