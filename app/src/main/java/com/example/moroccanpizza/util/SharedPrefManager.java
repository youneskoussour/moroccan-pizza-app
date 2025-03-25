package com.example.moroccanpizza.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Utility class to manage shared preferences for user data
 */
public class SharedPrefManager {

    private static final String PREF_NAME = "moroccan_pizza_prefs";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    /**
     * Save user ID to shared preferences
     * 
     * @param userId The user ID to save
     */
    public void setUserId(String userId) {
        editor.putString(KEY_USER_ID, userId);
        editor.apply();
    }

    /**
     * Get the saved user ID
     * 
     * @return The user ID, or null if not set
     */
    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    /**
     * Save user name to shared preferences
     * 
     * @param name The user name to save
     */
    public void setUserName(String name) {
        editor.putString(KEY_USER_NAME, name);
        editor.apply();
    }

    /**
     * Get the saved user name
     * 
     * @return The user name, or null if not set
     */
    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    /**
     * Save user email to shared preferences
     * 
     * @param email The user email to save
     */
    public void setUserEmail(String email) {
        editor.putString(KEY_USER_EMAIL, email);
        editor.apply();
    }

    /**
     * Get the saved user email
     * 
     * @return The user email, or null if not set
     */
    public String getUserEmail() {
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    /**
     * Clear all saved user data
     */
    public void clear() {
        editor.clear();
        editor.apply();
    }
}
