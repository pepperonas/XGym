package io.celox.xgym.ui.equipment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import io.celox.xgym.data.entity.Equipment;

public class EquipmentTabsAdapter extends FragmentStateAdapter {
    private final Equipment equipment;
    private AusdauerEquipmentFragment ausdauerFragment;
    private KraftsportEquipmentFragment kraftsportFragment;

    public EquipmentTabsAdapter(@NonNull Fragment fragment, Equipment equipment) {
        super(fragment);
        this.equipment = equipment;
        // Pre-create both fragments to ensure they're always available
        this.ausdauerFragment = AusdauerEquipmentFragment.newInstance(equipment);
        this.kraftsportFragment = KraftsportEquipmentFragment.newInstance(equipment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return ausdauerFragment;
            case 1:
                return kraftsportFragment;
            default:
                return ausdauerFragment;
        }
    }
    
    public AusdauerEquipmentFragment getAusdauerFragment() {
        return ausdauerFragment;
    }
    
    public KraftsportEquipmentFragment getKraftsportFragment() {
        return kraftsportFragment;
    }

    @Override
    public int getItemCount() {
        return 2; // Ausdauer and Kraftsport tabs
    }
}