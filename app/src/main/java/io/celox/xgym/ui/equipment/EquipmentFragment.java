package io.celox.xgym.ui.equipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.widget.TextView;

import io.celox.xgym.R;
import io.celox.xgym.databinding.FragmentEquipmentBinding;

public class EquipmentFragment extends Fragment {

    private FragmentEquipmentBinding binding;
    private EquipmentViewModel equipmentViewModel;
    private EquipmentAdapter equipmentAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        equipmentViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);

        binding = FragmentEquipmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setupRecyclerView();
        setupFab();
        observeEquipment();

        return root;
    }

    private void setupRecyclerView() {
        equipmentAdapter = new EquipmentAdapter(this::onEquipmentClick, this::onEquipmentMoreClick);
        binding.recyclerEquipment.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerEquipment.setAdapter(equipmentAdapter);
    }

    private void setupFab() {
        binding.fabAddEquipment.setOnClickListener(v -> {
            // TODO: Navigate to add equipment screen
            // For now, add a sample equipment
            equipmentViewModel.addSampleEquipment();
        });
    }

    private void observeEquipment() {
        equipmentViewModel.getAllEquipment().observe(getViewLifecycleOwner(), equipment -> {
            if (equipment != null && !equipment.isEmpty()) {
                binding.recyclerEquipment.setVisibility(View.VISIBLE);
                binding.textNoEquipment.setVisibility(View.GONE);
                equipmentAdapter.submitList(equipment);
            } else {
                binding.recyclerEquipment.setVisibility(View.GONE);
                binding.textNoEquipment.setVisibility(View.VISIBLE);
            }
        });
    }

    private void onEquipmentClick(io.celox.xgym.data.entity.Equipment equipment) {
        // TODO: Navigate to equipment details or edit screen
    }

    private void onEquipmentMoreClick(io.celox.xgym.data.entity.Equipment equipment, View view) {
        // TODO: Show popup menu with edit/delete options
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}