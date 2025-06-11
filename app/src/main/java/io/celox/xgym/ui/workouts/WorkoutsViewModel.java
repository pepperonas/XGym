package io.celox.xgym.ui.workouts;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import io.celox.xgym.data.entity.WorkoutSession;
import io.celox.xgym.data.entity.Exercise;
import io.celox.xgym.repository.WorkoutRepository;

import java.util.List;

public class WorkoutsViewModel extends AndroidViewModel {

    private WorkoutRepository repository;
    private LiveData<WorkoutSession> activeWorkoutSession;
    private LiveData<List<WorkoutSession>> allWorkoutSessions;
    private MutableLiveData<Integer> currentSessionId = new MutableLiveData<>();
    private LiveData<List<Exercise>> currentSessionExercises;

    public WorkoutsViewModel(Application application) {
        super(application);
        repository = new WorkoutRepository(application);
        activeWorkoutSession = repository.getActiveWorkoutSession();
        allWorkoutSessions = repository.getAllWorkoutSessions();
        
        currentSessionExercises = Transformations.switchMap(currentSessionId, sessionId -> {
            if (sessionId != null && sessionId > 0) {
                return repository.getExercisesForSession(sessionId);
            }
            return new MutableLiveData<>();
        });
    }

    public LiveData<WorkoutSession> getActiveWorkoutSession() {
        return activeWorkoutSession;
    }

    public LiveData<List<WorkoutSession>> getAllWorkoutSessions() {
        return allWorkoutSessions;
    }

    public LiveData<List<Exercise>> getCurrentSessionExercises() {
        return currentSessionExercises;
    }

    public void setCurrentSessionId(int sessionId) {
        currentSessionId.setValue(sessionId);
    }

    public void startWorkout() {
        repository.startWorkoutSession();
    }

    public void endWorkout(WorkoutSession session) {
        repository.endWorkoutSession(session);
        currentSessionId.setValue(null);
    }

    public void addExercise(int sessionId, int equipmentId, double weight, int sets, int reps) {
        Exercise exercise = new Exercise(sessionId, equipmentId, weight, sets, reps);
        repository.insertExercise(exercise);
    }

    public void updateExercise(Exercise exercise) {
        repository.updateExercise(exercise);
    }

    public Exercise getLastExerciseForEquipment(int equipmentId) {
        return repository.getLastExerciseForEquipment(equipmentId);
    }
}