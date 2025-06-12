package io.celox.xgym.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import io.celox.xgym.data.dao.*;
import io.celox.xgym.data.entity.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = {Equipment.class, WorkoutSession.class, Exercise.class, Achievement.class},
    version = 2,
    exportSchema = false
)
public abstract class XGymDatabase extends RoomDatabase {
    
    public abstract EquipmentDao equipmentDao();
    public abstract WorkoutSessionDao workoutSessionDao();
    public abstract ExerciseDao exerciseDao();
    public abstract AchievementDao achievementDao();
    
    private static volatile XGymDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Add new columns to equipment table (nullable to match entity definition)
            database.execSQL("ALTER TABLE equipment ADD COLUMN type TEXT");
            database.execSQL("ALTER TABLE equipment ADD COLUMN category TEXT");
            database.execSQL("ALTER TABLE equipment ADD COLUMN currentWeight REAL NOT NULL DEFAULT 0.0");
        }
    };
    
    public static XGymDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (XGymDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            XGymDatabase.class, "xgym_database")
                            .addMigrations(MIGRATION_1_2)
                            .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    
    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);
            
            databaseWriteExecutor.execute(() -> {
                // Populate the database with initial achievements
                AchievementDao achievementDao = INSTANCE.achievementDao();
                
                // Workout achievements
                achievementDao.insertAchievement(new Achievement(
                    "First Workout", "Complete your first workout session", 
                    "trophy", 1, "workouts"));
                achievementDao.insertAchievement(new Achievement(
                    "Workout Warrior", "Complete 10 workout sessions", 
                    "star", 10, "workouts"));
                achievementDao.insertAchievement(new Achievement(
                    "Gym Legend", "Complete 50 workout sessions", 
                    "crown", 50, "workouts"));
                
                // Equipment achievements
                achievementDao.insertAchievement(new Achievement(
                    "Equipment Collector", "Add 5 pieces of equipment", 
                    "dumbbell", 5, "equipment"));
                achievementDao.insertAchievement(new Achievement(
                    "Gym Owner", "Add 20 pieces of equipment", 
                    "gym", 20, "equipment"));
                
                // Time achievements
                achievementDao.insertAchievement(new Achievement(
                    "Marathon", "Workout for 60 minutes in a single session", 
                    "clock", 60, "time"));
                achievementDao.insertAchievement(new Achievement(
                    "Iron Will", "Accumulate 10 hours of total workout time", 
                    "fire", 600, "time"));
                
                // Consistency achievements
                achievementDao.insertAchievement(new Achievement(
                    "Consistent", "Workout for 7 consecutive days", 
                    "calendar", 7, "consistency"));
                achievementDao.insertAchievement(new Achievement(
                    "Dedicated", "Workout for 30 consecutive days", 
                    "medal", 30, "consistency"));
            });
        }
    };
}