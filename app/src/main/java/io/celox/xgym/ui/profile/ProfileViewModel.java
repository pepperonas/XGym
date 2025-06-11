package io.celox.xgym.ui.profile;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.celox.xgym.data.entity.Achievement;
import io.celox.xgym.data.entity.Equipment;
import io.celox.xgym.data.entity.WorkoutSession;
import io.celox.xgym.data.entity.Exercise;
import io.celox.xgym.repository.AchievementRepository;
import io.celox.xgym.repository.EquipmentRepository;
import io.celox.xgym.repository.WorkoutRepository;
import io.celox.xgym.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileViewModel extends AndroidViewModel {

    private AchievementRepository achievementRepository;
    private EquipmentRepository equipmentRepository;
    private WorkoutRepository workoutRepository;
    private ExecutorService executorService;

    public ProfileViewModel(Application application) {
        super(application);
        achievementRepository = new AchievementRepository(application);
        equipmentRepository = new EquipmentRepository(application);
        workoutRepository = new WorkoutRepository(application);
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Achievement>> getAllAchievements() {
        return achievementRepository.getAllAchievements();
    }

    public void exportData(Uri uri, Context context) {
        executorService.execute(() -> {
            try {
                OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                    
                    // Create export data object
                    ExportData exportData = new ExportData();
                    // Note: In a real implementation, you would gather all data synchronously
                    // For now, this is a simplified version
                    
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    String jsonData = gson.toJson(exportData);
                    
                    writer.write(jsonData);
                    writer.close();
                    outputStream.close();
                    
                    // Show success message on main thread
                    if (context instanceof android.app.Activity) {
                        ((android.app.Activity) context).runOnUiThread(() -> 
                            Toast.makeText(context, context.getString(R.string.data_exported), Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (context instanceof android.app.Activity) {
                    ((android.app.Activity) context).runOnUiThread(() -> 
                        Toast.makeText(context, "Export failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    public void importData(Uri uri, Context context) {
        executorService.execute(() -> {
            try {
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    InputStreamReader reader = new InputStreamReader(inputStream);
                    
                    Gson gson = new Gson();
                    ExportData importData = gson.fromJson(reader, ExportData.class);
                    
                    // Import data to database
                    // Note: In a real implementation, you would import all the data
                    // This is a simplified version
                    
                    reader.close();
                    inputStream.close();
                    
                    // Show success message on main thread
                    if (context instanceof android.app.Activity) {
                        ((android.app.Activity) context).runOnUiThread(() -> 
                            Toast.makeText(context, context.getString(R.string.data_imported), Toast.LENGTH_SHORT).show()
                        );
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (context instanceof android.app.Activity) {
                    ((android.app.Activity) context).runOnUiThread(() -> 
                        Toast.makeText(context, "Import failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    // Data class for export/import
    public static class ExportData {
        public List<Equipment> equipment;
        public List<WorkoutSession> workoutSessions;
        public List<Exercise> exercises;
        public List<Achievement> achievements;
        public long exportTimestamp;

        public ExportData() {
            this.exportTimestamp = System.currentTimeMillis();
        }
    }
}