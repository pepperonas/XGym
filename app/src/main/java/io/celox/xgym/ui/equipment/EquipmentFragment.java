package io.celox.xgym.ui.equipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
            showEditEquipmentDialog(null);
        });
        
        // Long press to add sample equipment for testing
        binding.fabAddEquipment.setOnLongClickListener(v -> {
            equipmentViewModel.addSampleEquipment();
            Toast.makeText(getContext(), "Sample equipment added", Toast.LENGTH_SHORT).show();
            return true;
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
        showEditEquipmentDialog(equipment);
    }

    private void onEquipmentMoreClick(io.celox.xgym.data.entity.Equipment equipment, View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.equipment_item_menu, popup.getMenu());
        
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_edit) {
                showEditEquipmentDialog(equipment);
                return true;
            } else if (item.getItemId() == R.id.action_delete) {
                showDeleteConfirmationDialog(equipment);
                return true;
            }
            return false;
        });
        
        popup.show();
    }

    private void showEditEquipmentDialog(io.celox.xgym.data.entity.Equipment equipment) {
        EquipmentEditTabsDialog dialog = EquipmentEditTabsDialog.newInstance(equipment);
        dialog.setOnEquipmentSavedListener(savedEquipment -> {
            if (equipment == null) {
                // Adding new equipment
                equipmentViewModel.insert(savedEquipment);
                Toast.makeText(getContext(), "Equipment added", Toast.LENGTH_SHORT).show();
            } else {
                // Updating existing equipment
                equipmentViewModel.updateEquipment(savedEquipment);
                Toast.makeText(getContext(), "Equipment updated", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show(getParentFragmentManager(), "edit_equipment");
    }

    private void showDeleteConfirmationDialog(io.celox.xgym.data.entity.Equipment equipment) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Equipment")
                .setMessage("Are you sure you want to delete " + equipment.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    equipmentViewModel.deleteEquipment(equipment);
                    Toast.makeText(getContext(), "Equipment deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}