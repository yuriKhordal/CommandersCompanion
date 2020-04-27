package com.yuri.commanderscompanion.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**Represents the app's settings as a static class*/
public class AppSettings {
    /**The tag for the log*/
    public static final String TAG = AppSettings.class.getName();
    /**The name of the preferences file*/
    public static final String PREFERENCES_NAME = "settings";

    /**The name of the 'exists' setting*/
    protected static final String EXISTS_NAME = "exists";
    /**The default value of the 'exist' setting*/
    protected static final boolean EXISTS_DEFAULT_VALUE = false;
    /**The name of the 'appTheme' setting*/
    public static final String APP_THEME_NAME = "appTheme";
    /**The default value of the 'appTheme' setting*/
    public static final Theme APP_THEME_DEFAULT_VALUE = Theme.Light;
    /**The name of the 'startingPage' setting*/
    public static final String STARTING_PAGE_NAME = "startingPage";
    /**The default value of the 'startingPage' setting*/
    public static final Page STARTING_PAGE_DEFAULT = Page.UnitsOverview;

    /**The preferences object with all the settings*/
    protected static SharedPreferences preferences;
    /**The preferences editor*/
    protected static SharedPreferences.Editor editor;

    /**Represents the different themes in the app*/
    public enum Theme{ Dark, Light}
    /**The current chosen theme*/
    protected static Theme appTheme;

    public enum Page{ UnitsOverview }
    protected static Page startingPage;

    // ---- AppSettings and SharedPreferences methods ----

    /**Initialize the static class with settings from a shared preferences object
     * @param context A context for getting the preferences
     */
    public static void init(Context context){
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        boolean exists = preferences.getBoolean(EXISTS_NAME, EXISTS_DEFAULT_VALUE);
        if (!exists){
            setDefaultSettings(context);
            return;
        }

        if (preferences.contains(APP_THEME_NAME)){
            String str = preferences.getString(APP_THEME_NAME, "");
            appTheme = Theme.valueOf(str);
        } else {
            setAppTheme(APP_THEME_DEFAULT_VALUE, context);
        }

        if (preferences.contains(STARTING_PAGE_NAME)){
            startingPage = Page.valueOf(preferences.getString(STARTING_PAGE_NAME, ""));
        } else {
            setStartingPage(STARTING_PAGE_DEFAULT, context);
        }

        String appThemeString = AppSettings.preferences.getString(APP_THEME_NAME, APP_THEME_DEFAULT_VALUE.name());
        appTheme = appThemeString == Theme.Light.name() ? Theme.Light : Theme.Dark;
    }

    /**Check whether the static class was initialized
     * @return True if the class was initialized, false otherwise
     */
    public static boolean isInitialized(){ return preferences != null; }

    /**Get the preference object
     * @return The preference object
     */
    public static SharedPreferences getPreferences(){ return preferences; }

    /**Open an editor for editing
     * @return An {@link SharedPreferences.Editor Editor} object for editing preferences
     */
    public static SharedPreferences.Editor openEditor(){
        if (editor == null){
            editor = preferences.edit();
        }
        return editor;
    }

    /**Close the editor
     * @param context Optional, a context for making a text bubble
     */
    public static void closeEditor(Context context) {
        android.util.Log.v(TAG, "Closing editor");
        if (commitEditor(editor, context)) {
            Log.v(TAG, "Editor closed successfully");
        }
    }
    /**Close the editor*/
    public static void closeEditor(){closeEditor(null);}

    // ---- The different settings

    /**Set the settings in the class and the preferences file to their default values
     * @param context Optional, a context for making a text bubble
     */
    public static void setDefaultSettings(Context context){
        openEditor();
        editor.putBoolean(EXISTS_NAME, true);
        editor.putString(APP_THEME_NAME, APP_THEME_DEFAULT_VALUE.name());
        editor.putString(STARTING_PAGE_NAME, STARTING_PAGE_DEFAULT.name());
        closeEditor(context);
        appTheme = APP_THEME_DEFAULT_VALUE;
    }
    public static void setDefaultSettings(){ setDefaultSettings(null); }

    /**Get the app's current theme
     * @return A {@link Theme Theme} enum that contains the app's theme
     */
    public static Theme getAppTheme(){ return appTheme; }

    /**Get the app's starting page
     * @return The starting page
     */
    public static Page getStartingPage(){ return startingPage; }

    /**Set this app's current theme
     * @param appTheme The new theme
     * @param context Optional, a context for making a text bubble
     */
    public static void setAppTheme(Theme appTheme, Context context) {
        Log.v(TAG, "Setting appTheme to " + appTheme.name());
        AppSettings.appTheme = appTheme;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_THEME_NAME, appTheme.name());
        if (commitEditor(editor, context)) {
            Log.v(TAG, "Finished setting appTheme to " + appTheme.name());
        }
    }
    /**Set this app's current theme
     * @param appTheme The new theme
     */
    public static void setAppTheme(Theme appTheme) { setAppTheme(appTheme, null); }

    /**Set the app's starting page
     * @param page The new starting page
     * @param context Optional, a context for making a text bubble
     */
    public static void setStartingPage(Page page, Context context){
        Log.v(TAG, "Setting startingPage to " + page.name());
        AppSettings.startingPage = page;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(startingPage.name(), Page.UnitsOverview.name());
        if (commitEditor(editor, context)) {
            Log.v(TAG, "Finished setting startingPage to " + startingPage.name());
        }
    }
    public static void setStartingPage(Page page) { setStartingPage(page, null); }

    // ---- Internal methods ----

    /**Commit an editor
     * @param editor The editor object
     * @param context Optional, a context for making a text bubble
     * @return <code>true</code> if the committed successfully, otherwise <code>false</code>
     */
    protected static boolean commitEditor(SharedPreferences.Editor editor, Context context){
        boolean success;
        Log.v(TAG, "Attempting to commit the editor");
        if (!(success = editor.commit())){
            android.util.Log.w(TAG, "editor couldn't commit settings. Settings were not saved!");
            if (context != null) {
                Toast.makeText(context, "Error: Settings could not be saved, try again!",
                        Toast.LENGTH_LONG).show();
            }
        }
        if (success) { Log.v(TAG, "Editor Committed successfully"); }
        return success;
    }
}
