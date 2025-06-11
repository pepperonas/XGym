package io.celox.xgym.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import io.celox.xgym.data.XGymDatabase;
import io.celox.xgym.data.dao.WorkoutSessionDao;
import io.celox.xgym.data.dao.ExerciseDao;
import io.celox.xgym.data.entity.WorkoutSession;
import io.celox.xgym.data.entity.Exercise;

import java.util.List;

public class WorkoutRepository {

    private WorkoutSessionDao workoutSessionDao;
    private ExerciseDao exerciseDao;
    private LiveData<List<WorkoutSession>> allWorkoutSessions;

    public WorkoutRepository(Application application) {
        XGymDatabase database = XGymDatabase.getDatabase(application);
        workoutSessionDao = database.workoutSessionDao();
        exerciseDao = database.exerciseDao();
        allWorkoutSessions = workoutSessionDao.getAllWorkoutSessions();
    }

    public LiveData<List<WorkoutSession>> getAllWorkoutSessions() {
        return allWorkoutSessions;
    }

    public LiveData<WorkoutSession> getActiveWorkoutSession() {
        return workoutSessionDao.getActiveWorkoutSession();
    }

    public LiveData<List<Exercise>> getExercisesForSession(int sessionId) {
        return exerciseDao.getExercisesForSession(sessionId);
    }

    public void startWorkoutSession() {
        XGymDatabase.databaseWriteExecutor.execute(() -> {
            WorkoutSession session = new WorkoutSession(System.currentTimeMillis());
            workoutSessionDao.insertWorkoutSession(session);
        });
    }

    public void endWorkoutSession(WorkoutSession session) {
        XGymDatabase.databaseWriteExecutor.execute(() -> {
            session.setEndTime(System.currentTimeMillis());
            session.setCompleted(true);
            workoutSessionDao.updateWorkoutSession(session);
        });
    }

    public void insertExercise(Exercise exercise) {
        XGymDatabase.databaseWriteExecutor.execute(() -> {
            exerciseDao.insertExercise(exercise);
        });
    }

    public void updateExercise(Exercise exercise) {
        XGymDatabase.databaseWriteExecutor.execute(() -> {
            exerciseDao.updateExercise(exercise);
        });
    }

    public Exercise getLastExerciseForEquipment(int equipmentId) {
        return exerciseDao.getLastExerciseForEquipment(equipmentId);
    }

    public LiveData<Integer> getCompletedWorkoutCount() {
        return workoutSessionDao.getCompletedWorkoutCount();
    }

    public LiveData<Long> getTotalWorkoutTime() {
        return workoutSessionDao.getTotalWorkoutTime();
    }
}