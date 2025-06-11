package io.celox.xgym.ui.workouts;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.celox.xgym.databinding.FragmentWorkoutsBinding;
import io.celox.xgym.data.entity.WorkoutSession;
import io.celox.xgym.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkoutsFragment extends Fragment implements AddExerciseDialog.AddExerciseListener {

    private FragmentWorkoutsBinding binding;
    private WorkoutsViewModel workoutsViewModel;
    private WorkoutSessionAdapter recentWorkoutsAdapter;
    private SessionExerciseAdapter sessionExerciseAdapter;
    private Handler timerHandler;
    private Runnable timerRunnable;
    private WorkoutSession currentSession;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        workoutsViewModel = new ViewModelProvider(this).get(WorkoutsViewModel.class);
        binding = FragmentWorkoutsBinding.inflate(inflater, container, false);

        setupRecyclerViews();
        setupClickListeners();
        observeWorkoutSession();
        setupTimer();

        return binding.getRoot();
    }

    private void setupRecyclerViews() {
        // Recent workouts
        recentWorkoutsAdapter = new WorkoutSessionAdapter(this::onWorkoutSessionClick);
        binding.recyclerRecentWorkouts.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerRecentWorkouts.setAdapter(recentWorkoutsAdapter);

        // Session exercises
        sessionExerciseAdapter = new SessionExerciseAdapter(this::onExerciseClick);
        binding.recyclerSessionExercises.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerSessionExercises.setAdapter(sessionExerciseAdapter);
    }

    private void setupClickListeners() {
        binding.buttonStartWorkout.setOnClickListener(v -> startWorkout());
        binding.buttonEndWorkout.setOnClickListener(v -> endWorkout());
        binding.buttonAddExercise.setOnClickListener(v -> addExercise());
    }

    private void observeWorkoutSession() {
        workoutsViewModel.getActiveWorkoutSession().observe(getViewLifecycleOwner(), session -> {
            if (session != null) {
                currentSession = session;
                showActiveSession(session);
                workoutsViewModel.setCurrentSessionId(session.getId());
                startTimer(session.getStartTime());
            } else {
                currentSession = null;
                showNoActiveSession();
                stopTimer();
            }
        });

        workoutsViewModel.getAllWorkoutSessions().observe(getViewLifecycleOwner(), sessions -> {
            if (sessions != null && !sessions.isEmpty()) {
                recentWorkoutsAdapter.submitList(sessions.subList(0, Math.min(sessions.size(), 5)));
            }
        });

        workoutsViewModel.getCurrentSessionExercises().observe(getViewLifecycleOwner(), exercises -> {
            if (exercises != null) {
                sessionExerciseAdapter.submitList(exercises);
                updateExerciseCount(exercises.size());
            }
        });
    }

    private void showActiveSession(WorkoutSession session) {
        binding.layoutNoSession.setVisibility(View.GONE);
        binding.layoutActiveSession.setVisibility(View.VISIBLE);
    }

    private void showNoActiveSession() {
        binding.layoutNoSession.setVisibility(View.VISIBLE);
        binding.layoutActiveSession.setVisibility(View.GONE);
    }

    private void startWorkout() {
        workoutsViewModel.startWorkout();
        Toast.makeText(getContext(), getString(R.string.workout_started), Toast.LENGTH_SHORT).show();
    }

    private void endWorkout() {
        if (currentSession != null) {
            workoutsViewModel.endWorkout(currentSession);
            Toast.makeText(getContext(), getString(R.string.workout_ended), Toast.LENGTH_SHORT).show();
        }
    }

    private void addExercise() {
        if (currentSession != null) {
            AddExerciseDialog dialog = AddExerciseDialog.newInstance(currentSession.getId());
            dialog.show(getChildFragmentManager(), "AddExerciseDialog");
        }
    }

    @Override
    public void onExerciseAdded(io.celox.xgym.data.entity.Exercise exercise) {
        workoutsViewModel.addExercise(exercise.getWorkoutSessionId(), exercise.getEquipmentId(), 
                                    exercise.getWeight(), exercise.getSets(), exercise.getReps());
    }

    private void setupTimer() {
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentSession != null) {
                    updateTimerDisplay();
                    timerHandler.postDelayed(this, 1000);
                }
            }
        };
    }

    private void startTimer(long startTime) {
        if (timerHandler != null && timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
            timerHandler.post(timerRunnable);
        }
    }

    private void stopTimer() {
        if (timerHandler != null && timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
        }
    }

    private void updateTimerDisplay() {
        if (currentSession != null && binding != null) {
            long elapsed = System.currentTimeMillis() - currentSession.getStartTime();
            String timeString = formatDuration(elapsed);
            binding.textSessionTimer.setText(timeString);
        }
    }

    private void updateExerciseCount(int count) {
        if (binding != null) {
            String text = count + " exercises completed";
            binding.textExercisesCount.setText(text);
        }
    }

    private String formatDuration(long milliseconds) {
        long seconds = milliseconds / 1000;
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void onWorkoutSessionClick(WorkoutSession session) {
        // TODO: Navigate to workout details
    }

    private void onExerciseClick(io.celox.xgym.data.entity.Exercise exercise) {
        // TODO: Edit exercise
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTimer();
        binding = null;
    }
}