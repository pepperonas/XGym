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
        // Add some sample equipment for testing with type and category
        Equipment laufband = new Equipment("Laufband", "", 0.0, 0.0);
        laufband.setType("Laufband");
        laufband.setCategory("ausdauer");
        
        Equipment beinpresse = new Equipment("Beinpresse", "", 50.0, 300.0);
        beinpresse.setType("Beinpresse");
        beinpresse.setCategory("kraftsport");
        beinpresse.setCurrentWeight(120.0);
        
        Equipment crosstrainer = new Equipment("Crosstrainer", "", 0.0, 0.0);
        crosstrainer.setType("Crosstrainer");
        crosstrainer.setCategory("ausdauer");
        
        insert(laufband);
        insert(beinpresse);
        insert(crosstrainer);
    }
}