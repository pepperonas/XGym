package io.celox.xgym.ui.equipment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import io.celox.xgym.R;
import io.celox.xgym.data.entity.Equipment;

public class KraftsportEquipmentFragment extends Fragment {
    private TextInputEditText editEquipmentName;
    private AutoCompleteTextView dropdownEquipmentType;
    private TextInputEditText editWeight;
    private Equipment equipment;

    private static final String[] KRAFTSPORT_EQUIPMENT_TYPES = {
        "Beinpresse",
        "Latzug",
        "Bankdrücken",
        "Beinstrecker",
        "Beinbeuger",
        "Butterfly",
        "Kabelzug",
        "Schulterdrücken",
        "Bizeps Curl",
        "Trizeps",
        "Bauchtrainer",
        "Rückenstrecker",
        "Hantelbank",
        "Kurzhantel",
        "Langhantel"
    };

    public static KraftsportEquipmentFragment newInstance(Equipment equipment) {
        KraftsportEquipmentFragment fragment = new KraftsportEquipmentFragment();
        fragment.equipment = equipment;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kraftsport_equipment, container, false);
        
        editEquipmentName = view.findViewById(R.id.edit_equipment_name);
        dropdownEquipmentType = view.findViewById(R.id.dropdown_equipment_type);
        editWeight = view.findViewById(R.id.edit_weight);
        
        setupDropdown();
        
        if (equipment != null) {
            editEquipmentName.setText(equipment.getName());
            // Only set type if it's a Kraftsport equipment or empty
            if (equipment.getType() != null && 
                ("kraftsport".equals(equipment.getCategory()) || equipment.getCategory() == null)) {
                dropdownEquipmentType.setText(equipment.getType(), false);
            }
            if (equipment.getCurrentWeight() > 0) {
                editWeight.setText(String.valueOf(equipment.getCurrentWeight()));
            }
        }
        
        return view;
    }

    private void setupDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, KRAFTSPORT_EQUIPMENT_TYPES);
        dropdownEquipmentType.setAdapter(adapter);
    }

    public String getEquipmentName() {
        return editEquipmentName.getText().toString().trim();
    }

    public String getEquipmentType() {
        return dropdownEquipmentType.getText().toString().trim();
    }

    public double getWeight() {
        String weightText = editWeight.getText().toString().trim();
        if (weightText.isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(weightText);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}