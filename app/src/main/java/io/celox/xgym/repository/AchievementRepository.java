package io.celox.xgym.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import io.celox.xgym.data.XGymDatabase;
import io.celox.xgym.data.dao.AchievementDao;
import io.celox.xgym.data.entity.Achievement;

import java.util.List;

public class AchievementRepository {

    private AchievementDao achievementDao;
    private LiveData<List<Achievement>> allAchievements;

    public AchievementRepository(Application application) {
        XGymDatabase database = XGymDatabase.getDatabase(application);
        achievementDao = database.achievementDao();
        allAchievements = achievementDao.getAllAchievements();
    }

    public LiveData<List<Achievement>> getAllAchievements() {
        return allAchievements;
    }

    public LiveData<List<Achievement>> getUnlockedAchievements() {
        return achievementDao.getUnlockedAchievements();
    }

    public LiveData<List<Achievement>> getLockedAchievements() {
        return achievementDao.getLockedAchievements();
    }

    public LiveData<Integer> getUnlockedAchievementCount() {
        return achievementDao.getUnlockedAchievementCount();
    }

    public void updateAchievement(Achievement achievement) {
        XGymDatabase.databaseWriteExecutor.execute(() -> {
            achievementDao.updateAchievement(achievement);
        });
    }

    public void checkAndUpdateAchievements(int totalWorkouts, int totalEquipment, long totalTime) {
        XGymDatabase.databaseWriteExecutor.execute(() -> {
            List<Achievement> allAchievements = achievementDao.getAllAchievements().getValue();
            if (allAchievements != null) {
                for (Achievement achievement : allAchievements) {
                    updateAchievementProgress(achievement, totalWorkouts, totalEquipment, totalTime);
                }
            }
        });
    }

    private void updateAchievementProgress(Achievement achievement, int totalWorkouts, int totalEquipment, long totalTime) {
        switch (achievement.getCategory()) {
            case "workouts":
                achievement.setCurrentValue(totalWorkouts);
                break;
            case "equipment":
                achievement.setCurrentValue(totalEquipment);
                break;
            case "time":
                // Convert milliseconds to minutes
                achievement.setCurrentValue((int) (totalTime / (1000 * 60)));
                break;
        }
        
        if (achievement.getCurrentValue() >= achievement.getTargetValue() && !achievement.isUnlocked()) {
            achievement.setUnlocked(true);
            achievement.setUnlockedAt(System.currentTimeMillis());
        }
        
        achievementDao.updateAchievement(achievement);
    }
}