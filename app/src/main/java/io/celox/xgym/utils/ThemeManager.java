package io.celox.xgym.utils;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    private static final String THEME_PREFS = "theme_preferences";
    private static final String THEME_KEY = "selected_theme";
    
    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_SYSTEM = 2;
    
    private static ThemeManager instance;
    private SharedPreferences preferences;
    
    private ThemeManager(Context context) {
        preferences = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE);
    }
    
    public static ThemeManager getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeManager(context.getApplicationContext());
        }
        return instance;
    }
    
    public void setTheme(int theme) {
        preferences.edit().putInt(THEME_KEY, theme).apply();
        applyTheme(theme);
    }
    
    public int getTheme() {
        return preferences.getInt(THEME_KEY, THEME_SYSTEM);
    }
    
    public void applyTheme(int theme) {
        switch (theme) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
    
    public void initializeTheme() {
        applyTheme(getTheme());
    }
}