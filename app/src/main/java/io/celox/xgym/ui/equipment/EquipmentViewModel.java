package io.celox.xgym.ui.equipment;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import io.celox.xgym.data.XGymDatabase;
import io.celox.xgym.data.entity.Equipment;
import io.celox.xgym.repository.EquipmentRepository;

import java.util.List;

public class EquipmentViewModel extends AndroidViewModel {

    private EquipmentRepository repository;
    private LiveData<List<Equipment>> allEquipment;

    public EquipmentViewModel(Application application) {
        super(application);
        repository = new EquipmentRepository(application);
        allEquipment = repository.getAllEquipment();
    }

    public LiveData<List<Equipment>> getAllEquipment() {
        return allEquipment;
    }

    public void insert(Equipment equipment) {
        repository.insert(equipment);
    }

    public void update(Equipment equipment) {
        repository.update(equipment);
    }

    public void updateEquipment(Equipment equipment) {
        repository.update(equipment);
    }

    public void delete(Equipment equipment) {
        repository.delete(equipment);
    }

    public void deleteEquipment(Equipment equipment) {
        repository.delete(equipment);
    }

    public void addSampleEquipment() {
        // Add some sample equipment for testing
        Equipment benchPress = new Equipment("Bench Press", "", 20.0, 100.0);
        Equipment squat = new Equipment("Squat Rack", "", 40.0, 200.0);
        Equipment dumbbells = new Equipment("Dumbbells", "", 5.0, 50.0);
        
        insert(benchPress);
        insert(squat);
        insert(dumbbells);
    }
}