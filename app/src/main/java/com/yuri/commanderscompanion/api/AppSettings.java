package com.yuri.commanderscompanion.api;

import android.content.SharedPreferences;

/**Represents the app's settings as a singleton class*/
public class AppSettings {
    public static final String PREFERENCES_NAME = "settings";
    /**The singleton object*/
    protected static AppSettings appSettings;

    /**The preferences object with all the settings*/
    protected SharedPreferences preferences;

    /**Initialize a new settings class with settings from 'preferences'
     * @param preferences The preferences object with all the settings
     */
    protected AppSettings(SharedPreferences preferences){
        this.preferences = preferences;
    }

    /**Initialize the singleton class with settings from a shared preferences object
     * @param preferences The preferences object with all the settings
     */
    public static void init(SharedPreferences preferences){
        appSettings = new AppSettings(preferences);
    }

    /**Check whether the singleton was initialized
     * @return True if the singleton was initialized, false otherwise
     */
    public static boolean isInitialized(){
        return appSettings != null;
    }

    /**Get the settings object
     * @return The singleton object
     */
    public static AppSettings getAppSettings(){
        if (!isInitialized()){
            throw new NullPointerException("'appSettings' wasn't initialized and thus is null");
        }
        return appSettings;
    }

    /**Get the preference object
     * @return The preference object
     */
    public SharedPreferences getPreferences(){
        return preferences;
    }
}
