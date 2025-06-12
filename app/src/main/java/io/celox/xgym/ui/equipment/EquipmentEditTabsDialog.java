package io.celox.xgym.ui.equipment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import io.celox.xgym.R;
import io.celox.xgym.data.entity.Equipment;

public class EquipmentEditTabsDialog extends DialogFragment {
    private Equipment equipment;
    private OnEquipmentSavedListener listener;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private EquipmentTabsAdapter adapter;

    public interface OnEquipmentSavedListener {
        void onEquipmentSaved(Equipment equipment);
    }

    public static EquipmentEditTabsDialog newInstance(Equipment equipment) {
        EquipmentEditTabsDialog dialog = new EquipmentEditTabsDialog();
        dialog.equipment = equipment;
        return dialog;
    }

    public void setOnEquipmentSavedListener(OnEquipmentSavedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_equipment_tabs, null);
        
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        
        adapter = new EquipmentTabsAdapter(this, equipment);
        viewPager.setAdapter(adapter);
        
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Ausdauer");
                    break;
                case 1:
                    tab.setText("Kraftsport");
                    break;
            }
        }).attach();
        
        // Set initial tab based on equipment category
        if (equipment != null && "kraftsport".equals(equipment.getCategory())) {
            viewPager.setCurrentItem(1, false);
        } else {
            viewPager.setCurrentItem(0, false);
        }

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(equipment == null ? "Neues Gerät" : "Gerät bearbeiten")
                .setView(view)
                .setPositiveButton("Speichern", (dialog, which) -> saveEquipment())
                .setNegativeButton("Abbrechen", null)
                .create();
    }

    private void saveEquipment() {
        int currentTab = viewPager.getCurrentItem();
        
        boolean isNewEquipment = (equipment == null);
        if (isNewEquipment) {
            equipment = new Equipment("", "", 0, 100);
        }
        
        String name = "";
        String type = "";
        double weight = 0.0;
        
        // Always try to get data from both fragments, but prioritize the current tab
        AusdauerEquipmentFragment ausdauerFragment = adapter.getAusdauerFragment();
        KraftsportEquipmentFragment kraftsportFragment = adapter.getKraftsportFragment();
        
        if (currentTab == 0 && ausdauerFragment != null) {
            // Ausdauer tab is active
            name = ausdauerFragment.getEquipmentName();
            type = ausdauerFragment.getEquipmentType();
            equipment.setName(name);
            equipment.setType(type);
            equipment.setCategory("ausdauer");
            equipment.setCurrentWeight(0.0); // Reset weight for cardio
        } else if (currentTab == 1 && kraftsportFragment != null) {
            // Kraftsport tab is active
            name = kraftsportFragment.getEquipmentName();
            type = kraftsportFragment.getEquipmentType();
            weight = kraftsportFragment.getWeight();
            equipment.setName(name);
            equipment.setType(type);
            equipment.setCategory("kraftsport");
            equipment.setCurrentWeight(weight);
        }
        
        // Validate that we have required data
        if (name.trim().isEmpty()) {
            // Use equipment type as fallback name
            if (!type.trim().isEmpty()) {
                equipment.setName(type);
            } else {
                equipment.setName("Neues Gerät");
            }
        }
        
        // Mark as updated to ensure UI refresh
        equipment.markAsUpdated();
        
        if (listener != null) {
            listener.onEquipmentSaved(equipment);
        }
    }
}