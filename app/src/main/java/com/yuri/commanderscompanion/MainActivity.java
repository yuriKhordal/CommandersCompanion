package com.yuri.commanderscompanion;

import android.os.Bundle;

/**Represents the main starting activity of the app*/
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setViews();
        configureViews();
    }

    @Override
    public void configureViews() {
        super.configureViews();
        //Set Title
        toolbar_lbl_title.setText(R.string.soldiers_title);
        //Hide the refresh button from toolbar menu
        toolbar_menu.getMenu().findItem(R.id.toolbar_menu_refresh).setVisible(false);
    }
}
