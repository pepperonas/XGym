package io.celox.xgym.ui.statistics;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import io.celox.xgym.data.entity.Achievement;
import io.celox.xgym.repository.WorkoutRepository;
import io.celox.xgym.repository.EquipmentRepository;
import io.celox.xgym.repository.AchievementRepository;

import java.util.List;

public class StatisticsViewModel extends AndroidViewModel {

    private WorkoutRepository workoutRepository;
    private EquipmentRepository equipmentRepository;
    private AchievementRepository achievementRepository;

    private LiveData<Integer> totalWorkouts;
    private LiveData<Long> totalWorkoutTime;
    private LiveData<Integer> equipmentCount;
    private LiveData<Integer> unlockedAchievements;
    private LiveData<List<Achievement>> recentAchievements;

    public StatisticsViewModel(Application application) {
        super(application);
        workoutRepository = new WorkoutRepository(application);
        equipmentRepository = new EquipmentRepository(application);
        achievementRepository = new AchievementRepository(application);

        totalWorkouts = workoutRepository.getCompletedWorkoutCount();
        totalWorkoutTime = workoutRepository.getTotalWorkoutTime();
        equipmentCount = equipmentRepository.getEquipmentCount();
        unlockedAchievements = achievementRepository.getUnlockedAchievementCount();
        recentAchievements = achievementRepository.getUnlockedAchievements();
    }

    public LiveData<Integer> getTotalWorkouts() {
        return totalWorkouts;
    }

    public LiveData<Long> getTotalWorkoutTime() {
        return totalWorkoutTime;
    }

    public LiveData<Integer> getEquipmentCount() {
        return equipmentCount;
    }

    public LiveData<Integer> getUnlockedAchievements() {
        return unlockedAchievements;
    }

    public LiveData<List<Achievement>> getRecentAchievements() {
        return recentAchievements;
    }
}