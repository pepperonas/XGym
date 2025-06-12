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
        
        adapter = new EquipmentTabsAdapter(requireActivity(), equipment);
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

        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(equipment == null ? "Neues Gerät" : "Gerät bearbeiten")
                .setView(view)
                .setPositiveButton("Speichern", (dialog, which) -> saveEquipment())
                .setNegativeButton("Abbrechen", null)
                .create();
    }

    private void saveEquipment() {
        int currentTab = viewPager.getCurrentItem();
        
        if (equipment == null) {
            equipment = new Equipment("", "", 0, 100);
        }
        
        if (currentTab == 0) {
            // Ausdauer tab
            AusdauerEquipmentFragment fragment = (AusdauerEquipmentFragment) 
                getChildFragmentManager().findFragmentByTag("f" + currentTab);
            if (fragment != null) {
                equipment.setName(fragment.getEquipmentName());
                equipment.setType(fragment.getEquipmentType());
                equipment.setCategory("ausdauer");
            }
        } else {
            // Kraftsport tab
            KraftsportEquipmentFragment fragment = (KraftsportEquipmentFragment) 
                getChildFragmentManager().findFragmentByTag("f" + currentTab);
            if (fragment != null) {
                equipment.setName(fragment.getEquipmentName());
                equipment.setType(fragment.getEquipmentType());
                equipment.setCategory("kraftsport");
                equipment.setCurrentWeight(fragment.getWeight());
            }
        }
        
        if (listener != null) {
            listener.onEquipmentSaved(equipment);
        }
    }
}