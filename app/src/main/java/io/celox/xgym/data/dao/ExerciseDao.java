package io.celox.xgym.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import io.celox.xgym.data.entity.Exercise;
import java.util.List;

@Dao
public interface ExerciseDao {
    
    @Query("SELECT * FROM exercises WHERE workoutSessionId = :sessionId ORDER BY timestamp ASC")
    LiveData<List<Exercise>> getExercisesForSession(int sessionId);
    
    @Query("SELECT * FROM exercises WHERE workoutSessionId = :sessionId ORDER BY timestamp ASC")
    List<Exercise> getExercisesForSessionSync(int sessionId);
    
    @Query("SELECT * FROM exercises WHERE equipmentId = :equipmentId ORDER BY timestamp DESC")
    LiveData<List<Exercise>> getExercisesForEquipment(int equipmentId);
    
    @Query("SELECT * FROM exercises WHERE equipmentId = :equipmentId ORDER BY timestamp DESC LIMIT 1")
    Exercise getLastExerciseForEquipment(int equipmentId);
    
    @Query("SELECT MAX(weight) FROM exercises WHERE equipmentId = :equipmentId")
    LiveData<Double> getMaxWeightForEquipment(int equipmentId);
    
    @Query("SELECT * FROM exercises WHERE id = :id")
    LiveData<Exercise> getExerciseById(int id);
    
    @Insert
    long insertExercise(Exercise exercise);
    
    @Update
    void updateExercise(Exercise exercise);
    
    @Delete
    void deleteExercise(Exercise exercise);
    
    @Query("SELECT COUNT(*) FROM exercises")
    LiveData<Integer> getTotalExerciseCount();
    
    @Query("SELECT equipmentId FROM exercises GROUP BY equipmentId ORDER BY COUNT(*) DESC LIMIT 1")
    LiveData<Integer> getMostUsedEquipmentId();
    
    @Query("SELECT * FROM exercises ORDER BY timestamp DESC LIMIT :limit")
    LiveData<List<Exercise>> getRecentExercises(int limit);
}