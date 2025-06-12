package io.celox.xgym.ui.equipment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import io.celox.xgym.data.entity.Equipment;

public class EquipmentTabsAdapter extends FragmentStateAdapter {
    private final Equipment equipment;

    public EquipmentTabsAdapter(@NonNull FragmentActivity fragmentActivity, Equipment equipment) {
        super(fragmentActivity);
        this.equipment = equipment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return AusdauerEquipmentFragment.newInstance(equipment);
            case 1:
                return KraftsportEquipmentFragment.newInstance(equipment);
            default:
                return AusdauerEquipmentFragment.newInstance(equipment);
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Ausdauer and Kraftsport tabs
    }
}