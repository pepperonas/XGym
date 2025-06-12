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

public class AusdauerEquipmentFragment extends Fragment {
    private TextInputEditText editEquipmentName;
    private AutoCompleteTextView dropdownEquipmentType;
    private Equipment equipment;

    private static final String[] AUSDAUER_EQUIPMENT_TYPES = {
        "Laufband",
        "Crosstrainer",
        "Ergometer",
        "Rudergerät",
        "Stepper",
        "Spinning Bike"
    };

    public static AusdauerEquipmentFragment newInstance(Equipment equipment) {
        AusdauerEquipmentFragment fragment = new AusdauerEquipmentFragment();
        fragment.equipment = equipment;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                           @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ausdauer_equipment, container, false);
        
        editEquipmentName = view.findViewById(R.id.edit_equipment_name);
        dropdownEquipmentType = view.findViewById(R.id.dropdown_equipment_type);
        
        setupDropdown();
        
        if (equipment != null) {
            editEquipmentName.setText(equipment.getName());
            dropdownEquipmentType.setText(equipment.getType(), false);
        }
        
        return view;
    }

    private void setupDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, AUSDAUER_EQUIPMENT_TYPES);
        dropdownEquipmentType.setAdapter(adapter);
    }

    public String getEquipmentName() {
        return editEquipmentName.getText().toString().trim();
    }

    public String getEquipmentType() {
        return dropdownEquipmentType.getText().toString().trim();
    }
}