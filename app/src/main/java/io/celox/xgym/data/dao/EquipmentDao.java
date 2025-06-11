package io.celox.xgym.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import io.celox.xgym.data.entity.Equipment;
import java.util.List;

@Dao
public interface EquipmentDao {
    
    @Query("SELECT * FROM equipment ORDER BY name ASC")
    LiveData<List<Equipment>> getAllEquipment();
    
    @Query("SELECT * FROM equipment WHERE id = :id")
    LiveData<Equipment> getEquipmentById(int id);
    
    @Query("SELECT * FROM equipment WHERE id = :id")
    Equipment getEquipmentByIdSync(int id);
    
    @Insert
    long insertEquipment(Equipment equipment);
    
    @Update
    void updateEquipment(Equipment equipment);
    
    @Delete
    void deleteEquipment(Equipment equipment);
    
    @Query("DELETE FROM equipment WHERE id = :id")
    void deleteEquipmentById(int id);
    
    @Query("SELECT COUNT(*) FROM equipment")
    LiveData<Integer> getEquipmentCount();
}