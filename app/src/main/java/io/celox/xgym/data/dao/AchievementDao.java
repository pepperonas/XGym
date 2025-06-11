package io.celox.xgym.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import io.celox.xgym.data.entity.Achievement;
import java.util.List;

@Dao
public interface AchievementDao {
    
    @Query("SELECT * FROM achievements ORDER BY isUnlocked DESC, category ASC")
    LiveData<List<Achievement>> getAllAchievements();
    
    @Query("SELECT * FROM achievements WHERE isUnlocked = 1 ORDER BY unlockedAt DESC")
    LiveData<List<Achievement>> getUnlockedAchievements();
    
    @Query("SELECT * FROM achievements WHERE isUnlocked = 0 ORDER BY currentValue DESC")
    LiveData<List<Achievement>> getLockedAchievements();
    
    @Query("SELECT * FROM achievements WHERE category = :category ORDER BY isUnlocked DESC")
    LiveData<List<Achievement>> getAchievementsByCategory(String category);
    
    @Query("SELECT * FROM achievements WHERE id = :id")
    LiveData<Achievement> getAchievementById(int id);
    
    @Query("SELECT * FROM achievements WHERE id = :id")
    Achievement getAchievementByIdSync(int id);
    
    @Insert
    long insertAchievement(Achievement achievement);
    
    @Update
    void updateAchievement(Achievement achievement);
    
    @Delete
    void deleteAchievement(Achievement achievement);
    
    @Query("SELECT COUNT(*) FROM achievements WHERE isUnlocked = 1")
    LiveData<Integer> getUnlockedAchievementCount();
    
    @Query("SELECT COUNT(*) FROM achievements")
    LiveData<Integer> getTotalAchievementCount();
}