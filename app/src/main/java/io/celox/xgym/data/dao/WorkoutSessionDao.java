package io.celox.xgym.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import io.celox.xgym.data.entity.WorkoutSession;
import java.util.List;

@Dao
public interface WorkoutSessionDao {
    
    @Query("SELECT * FROM workout_sessions ORDER BY startTime DESC")
    LiveData<List<WorkoutSession>> getAllWorkoutSessions();
    
    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    LiveData<WorkoutSession> getWorkoutSessionById(int id);
    
    @Query("SELECT * FROM workout_sessions WHERE id = :id")
    WorkoutSession getWorkoutSessionByIdSync(int id);
    
    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 0 ORDER BY startTime DESC LIMIT 1")
    LiveData<WorkoutSession> getActiveWorkoutSession();
    
    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 0 ORDER BY startTime DESC LIMIT 1")
    WorkoutSession getActiveWorkoutSessionSync();
    
    @Insert
    long insertWorkoutSession(WorkoutSession workoutSession);
    
    @Update
    void updateWorkoutSession(WorkoutSession workoutSession);
    
    @Delete
    void deleteWorkoutSession(WorkoutSession workoutSession);
    
    @Query("SELECT COUNT(*) FROM workout_sessions WHERE isCompleted = 1")
    LiveData<Integer> getCompletedWorkoutCount();
    
    @Query("SELECT SUM(duration) FROM workout_sessions WHERE isCompleted = 1")
    LiveData<Long> getTotalWorkoutTime();
    
    @Query("SELECT * FROM workout_sessions WHERE isCompleted = 1 AND startTime >= :startDate AND startTime <= :endDate ORDER BY startTime DESC")
    LiveData<List<WorkoutSession>> getWorkoutSessionsInDateRange(long startDate, long endDate);
}