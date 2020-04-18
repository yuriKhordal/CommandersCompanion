package com.yuri.commanderscompanion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.yuri.commanderscompanion.api.AppSettings;

/**Represents the app settings activity*/
public class SettingsActivity extends BaseActivity {
    /**The name of the class for logging*/
    public static final String TAG = "SettingsActivity";

    /**The editor for the settings*/
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setViews();
        configureViews();

        //Open the shared preferences editor
        editor = AppSettings.openEditor();
    }

    /**Create an intent pointing to this activity
     * @param context The context that requests the intent
     * @return The intent to this activity
     */
    public static Intent makeIntent(Context context){
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    public void setViews() {
        super.setViews();
    }

    @Override
    public void configureViews() {
        toolbar_lbl_title.setText(R.string.settings_title);
        toolbar_btn_menu.setVisibility(View.GONE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        AppSettings.closeEditor(this);
        editor = null;
    }
}
