package io.celox.xgym.ui.workouts;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import io.celox.xgym.R;
import io.celox.xgym.data.entity.Equipment;
import io.celox.xgym.data.entity.Exercise;
import io.celox.xgym.ui.equipment.EquipmentViewModel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddExerciseDialog extends DialogFragment {

    private MaterialAutoCompleteTextView spinnerEquipment;
    private TextInputEditText editWeight;
    private TextInputEditText editSets;
    private TextInputEditText editReps;
    
    private List<Equipment> equipmentList;
    private Equipment selectedEquipment;
    private int sessionId;
    private AddExerciseListener listener;
    private EquipmentViewModel equipmentViewModel;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public interface AddExerciseListener {
        void onExerciseAdded(Exercise exercise);
    }

    public static AddExerciseDialog newInstance(int sessionId) {
        AddExerciseDialog dialog = new AddExerciseDialog();
        Bundle args = new Bundle();
        args.putInt("sessionId", sessionId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddExerciseListener) context;
        } catch (ClassCastException e) {
            if (getParentFragment() instanceof AddExerciseListener) {
                listener = (AddExerciseListener) getParentFragment();
            } else {
                throw new ClassCastException(context.toString() + " must implement AddExerciseListener");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        sessionId = getArguments() != null ? getArguments().getInt("sessionId") : 0;
        
        equipmentViewModel = new ViewModelProvider(this).get(EquipmentViewModel.class);
        
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_exercise, null);
        
        initializeViews(view);
        setupEquipmentSpinner();
        
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view);
        
        return builder.create();
    }

    private void initializeViews(View view) {
        spinnerEquipment = view.findViewById(R.id.spinner_equipment);
        editWeight = view.findViewById(R.id.edit_weight);
        editSets = view.findViewById(R.id.edit_sets);
        editReps = view.findViewById(R.id.edit_reps);
        
        view.findViewById(R.id.button_cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.button_save).setOnClickListener(v -> saveExercise());
        
        // Set default values
        editSets.setText("3");
        editReps.setText("10");
    }

    private void setupEquipmentSpinner() {
        equipmentViewModel.getAllEquipment().observe(this, equipment -> {
            if (equipment != null && !equipment.isEmpty()) {
                equipmentList = equipment;
                setupEquipmentAdapter();
            }
        });
    }

    private void setupEquipmentAdapter() {
        String[] equipmentNames = new String[equipmentList.size()];
        for (int i = 0; i < equipmentList.size(); i++) {
            equipmentNames[i] = equipmentList.get(i).getName();
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            equipmentNames
        );
        
        spinnerEquipment.setAdapter(adapter);
        spinnerEquipment.setOnItemClickListener((parent, view, position, id) -> {
            selectedEquipment = equipmentList.get(position);
            loadLastExerciseData();
        });
    }

    private void loadLastExerciseData() {
        if (selectedEquipment != null) {
            executorService.execute(() -> {
                // Load last exercise data for this equipment
                // This would require updating the repository to provide sync methods
                // For now, we'll set some default values based on equipment weight range
                
                requireActivity().runOnUiThread(() -> {
                    double suggestedWeight = (selectedEquipment.getMinWeight() + selectedEquipment.getMaxWeight()) / 2;
                    editWeight.setText(String.valueOf((int) suggestedWeight));
                });
            });
        }
    }

    private void saveExercise() {
        if (!validateInput()) {
            return;
        }

        try {
            double weight = Double.parseDouble(editWeight.getText().toString());
            int sets = Integer.parseInt(editSets.getText().toString());
            int reps = Integer.parseInt(editReps.getText().toString());

            Exercise exercise = new Exercise(sessionId, selectedEquipment.getId(), weight, sets, reps);
            
            if (listener != null) {
                listener.onExerciseAdded(exercise);
            }
            
            Toast.makeText(getContext(), getString(R.string.exercise_saved), Toast.LENGTH_SHORT).show();
            dismiss();
            
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput() {
        if (selectedEquipment == null) {
            Toast.makeText(getContext(), "Please select equipment", Toast.LENGTH_SHORT).show();
            return false;
        }

        String weightText = editWeight.getText().toString().trim();
        String setsText = editSets.getText().toString().trim();
        String repsText = editReps.getText().toString().trim();

        if (weightText.isEmpty() || setsText.isEmpty() || repsText.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        try {
            double weight = Double.parseDouble(weightText);
            int sets = Integer.parseInt(setsText);
            int reps = Integer.parseInt(repsText);

            if (weight <= 0 || sets <= 0 || reps <= 0) {
                Toast.makeText(getContext(), "Values must be greater than 0", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (weight < selectedEquipment.getMinWeight() || weight > selectedEquipment.getMaxWeight()) {
                Toast.makeText(getContext(), 
                    String.format("Weight must be between %.0f and %.0f kg", 
                        selectedEquipment.getMinWeight(), selectedEquipment.getMaxWeight()), 
                    Toast.LENGTH_SHORT).show();
                return false;
            }

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Please enter valid numbers", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}