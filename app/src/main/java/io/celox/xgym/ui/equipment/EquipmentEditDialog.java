package io.celox.xgym.ui.equipment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import io.celox.xgym.R;
import io.celox.xgym.data.entity.Equipment;

public class EquipmentEditDialog extends DialogFragment {
    
    private static final String ARG_EQUIPMENT = "equipment";
    
    private Equipment equipment;
    private OnEquipmentSavedListener listener;
    
    public interface OnEquipmentSavedListener {
        void onEquipmentSaved(Equipment equipment);
    }
    
    public static EquipmentEditDialog newInstance(Equipment equipment) {
        EquipmentEditDialog fragment = new EquipmentEditDialog();
        Bundle args = new Bundle();
        args.putSerializable(ARG_EQUIPMENT, equipment);
        fragment.setArguments(args);
        return fragment;
    }
    
    public void setOnEquipmentSavedListener(OnEquipmentSavedListener listener) {
        this.listener = listener;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            equipment = (Equipment) getArguments().getSerializable(ARG_EQUIPMENT);
        }
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_equipment, null);
        
        EditText editName = view.findViewById(R.id.edit_equipment_name);
        EditText editMinWeight = view.findViewById(R.id.edit_min_weight);
        EditText editMaxWeight = view.findViewById(R.id.edit_max_weight);
        
        if (equipment != null) {
            editName.setText(equipment.getName());
            editMinWeight.setText(String.valueOf((int)equipment.getMinWeight()));
            editMaxWeight.setText(String.valueOf((int)equipment.getMaxWeight()));
        }
        
        return new MaterialAlertDialogBuilder(requireContext())
                .setTitle(equipment != null ? "Edit Equipment" : "Add Equipment")
                .setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = editName.getText().toString().trim();
                    String minWeightStr = editMinWeight.getText().toString().trim();
                    String maxWeightStr = editMaxWeight.getText().toString().trim();
                    
                    if (!name.isEmpty() && !minWeightStr.isEmpty() && !maxWeightStr.isEmpty()) {
                        try {
                            float minWeight = Float.parseFloat(minWeightStr);
                            float maxWeight = Float.parseFloat(maxWeightStr);
                            
                            if (equipment != null) {
                                equipment.setName(name);
                                equipment.setMinWeight(minWeight);
                                equipment.setMaxWeight(maxWeight);
                                equipment.setUpdatedAt(System.currentTimeMillis());
                            } else {
                                equipment = new Equipment(name, null, minWeight, maxWeight);
                            }
                            
                            if (listener != null) {
                                listener.onEquipmentSaved(equipment);
                            }
                        } catch (NumberFormatException e) {
                            // Handle invalid number format
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }
}