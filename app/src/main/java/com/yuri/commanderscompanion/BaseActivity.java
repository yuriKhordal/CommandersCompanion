package com.yuri.commanderscompanion;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**Represents a base activity that the other activities extend*/
public abstract class BaseActivity extends AppCompatActivity {
    /**The toolbar of the activity*/
    protected Toolbar toolbar;
    /**The title textview on the toolbar*/
    protected TextView toolbar_lbl_title;
    /**The three dots button on the toolbar*/
    protected ImageButton toolbar_btn_menu;
    /**The menu showed by the three dots button*/
    protected PopupMenu toolbar_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (!AppSettings.isInitialized()){
            AppSettings.init(getSharedPreferences(AppSettings.PREFERENCES_NAME, MODE_PRIVATE));
        }*/

        setViews();
        setSupportActionBar(toolbar);
    }

    /**The toolbar's three dots button on click event
     * @param view The button
     */
    public void toolbar_btn_menu_onClick(View view) {
        toolbar_menu.show();
    }

    /**The toolbar's three dots selection menu click on item event
     * @param item The item on the menu that was clicked on
     * @return True to stop the search for the item here, false to continue searching for the item in remaining functions
     */
    public boolean toolbar_btn_menu_onMenuItemClick(@NotNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_menu_settings:
                Intent intent = SettingsActivity.makeIntent(this);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

    /**Set the view objects*/
    public void setViews(){
        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        toolbar_lbl_title = (TextView)findViewById(R.id.toolbar_lbl_title);
        toolbar_btn_menu = (ImageButton)findViewById(R.id.toolbar_btn_menu);
        toolbar_menu = new PopupMenu(this, toolbar_btn_menu);
    }

    /**Configure the views after setting them*/
    public void configureViews() {
        toolbar_menu.setOnMenuItemClickListener(this::toolbar_btn_menu_onMenuItemClick);
        MenuInflater inflater = toolbar_menu.getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, toolbar_menu.getMenu());
    }
}
