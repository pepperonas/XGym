package io.celox.xgym.ui.statistics;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.celox.xgym.databinding.FragmentStatisticsBinding;

import java.util.Locale;

public class StatisticsFragment extends Fragment {

    private FragmentStatisticsBinding binding;
    private StatisticsViewModel statisticsViewModel;
    private AchievementAdapter achievementAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        statisticsViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);
        binding = FragmentStatisticsBinding.inflate(inflater, container, false);

        setupRecyclerViews();
        observeStatistics();

        return binding.getRoot();
    }

    private void setupRecyclerViews() {
        achievementAdapter = new AchievementAdapter();
        binding.recyclerAchievements.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerAchievements.setAdapter(achievementAdapter);
    }

    private void observeStatistics() {
        statisticsViewModel.getTotalWorkouts().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.textTotalWorkoutsValue.setText(String.valueOf(count));
            }
        });

        statisticsViewModel.getTotalWorkoutTime().observe(getViewLifecycleOwner(), time -> {
            if (time != null) {
                long hours = time / (1000 * 60 * 60);
                binding.textTotalTimeValue.setText(String.format(Locale.getDefault(), "%dh", hours));
            }
        });

        statisticsViewModel.getEquipmentCount().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.textEquipmentCountValue.setText(String.valueOf(count));
            }
        });

        statisticsViewModel.getUnlockedAchievements().observe(getViewLifecycleOwner(), count -> {
            if (count != null) {
                binding.textAchievementsValue.setText(String.valueOf(count));
            }
        });

        statisticsViewModel.getRecentAchievements().observe(getViewLifecycleOwner(), achievements -> {
            if (achievements != null && !achievements.isEmpty()) {
                achievementAdapter.submitList(achievements.subList(0, Math.min(achievements.size(), 5)));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}