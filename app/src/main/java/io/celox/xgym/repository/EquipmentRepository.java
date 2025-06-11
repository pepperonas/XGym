package io.celox.xgym.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;

import io.celox.xgym.data.XGymDatabase;
import io.celox.xgym.data.dao.EquipmentDao;
import io.celox.xgym.data.entity.Equipment;

import java.util.List;

public class EquipmentRepository {

    private EquipmentDao equipmentDao;
    private LiveData<List<Equipment>> allEquipment;

    public EquipmentRepository(Application application) {
        XGymDatabase database = XGymDatabase.getDatabase(application);
        equipmentDao = database.equipmentDao();
        allEquipment = equipmentDao.getAllEquipment();
    }

    public LiveData<List<Equipment>> getAllEquipment() {
        return allEquipment;
    }

    public LiveData<Equipment> getEquipmentById(int id) {
        return equipmentDao.getEquipmentById(id);
    }

    public void insert(Equipment equipment) {
        XGymDatabase.databaseWriteExecutor.execute(() -> {
            equipmentDao.insertEquipment(equipment);
        });
    }

    public void update(Equipment equipment) {
        XGymDatabase.databaseWriteExecutor.execute(() -> {
            equipmentDao.updateEquipment(equipment);
        });
    }

    public void delete(Equipment equipment) {
        XGymDatabase.databaseWriteExecutor.execute(() -> {
            equipmentDao.deleteEquipment(equipment);
        });
    }

    public LiveData<Integer> getEquipmentCount() {
        return equipmentDao.getEquipmentCount();
    }
}