package io.celox.xgym.ui.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import io.celox.xgym.databinding.FragmentProfileBinding;
import io.celox.xgym.ui.statistics.AchievementAdapter;
import io.celox.xgym.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private AchievementAdapter achievementAdapter;
    private static final int EXPORT_REQUEST_CODE = 1001;
    private static final int IMPORT_REQUEST_CODE = 1002;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        binding = FragmentProfileBinding.inflate(inflater, container, false);

        setupRecyclerView();
        setupClickListeners();
        observeData();
        updateMemberSince();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        achievementAdapter = new AchievementAdapter();
        binding.recyclerAllAchievements.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerAllAchievements.setAdapter(achievementAdapter);
    }

    private void setupClickListeners() {
        binding.buttonExportData.setOnClickListener(v -> exportData());
        binding.buttonImportData.setOnClickListener(v -> importData());
        binding.buttonAbout.setOnClickListener(v -> showAbout());
    }

    private void observeData() {
        profileViewModel.getAllAchievements().observe(getViewLifecycleOwner(), achievements -> {
            if (achievements != null) {
                achievementAdapter.submitList(achievements);
            }
        });
    }

    private void updateMemberSince() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        String memberSince = "Member since " + sdf.format(new Date());
        binding.textMemberSince.setText(memberSince);
    }

    private void exportData() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        intent.putExtra(Intent.EXTRA_TITLE, "xgym_backup_" + System.currentTimeMillis() + ".json");
        startActivityForResult(intent, EXPORT_REQUEST_CODE);
    }

    private void importData() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/json");
        startActivityForResult(intent, IMPORT_REQUEST_CODE);
    }

    private void showAbout() {
        Toast.makeText(getContext(), 
            "XGym v1.0\nYour Personal Gym Companion\n\nTrack workouts, monitor progress, achieve goals!", 
            Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == getActivity().RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                if (requestCode == EXPORT_REQUEST_CODE) {
                    profileViewModel.exportData(uri, getContext());
                } else if (requestCode == IMPORT_REQUEST_CODE) {
                    profileViewModel.importData(uri, getContext());
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}