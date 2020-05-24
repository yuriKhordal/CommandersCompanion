package com.yuri.commanderscompanion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.yuri.commanderscompanion.api.AppSettings;
import com.yuri.commanderscompanion.api.Database;
import com.yuri.commanderscompanion.api.GeneralHelper;
import com.yuri.commanderscompanion.api.OrganisationalUnit;

import androidx.fragment.app.Fragment;

/**An activity for unit info\management*/
public class UnitActivity extends BaseActivity {

    /**The text view of the type of unit*/
    TextView lbl_type;
    /**The text view of the commander name*/
    TextView lbl_commander;
    /**The text view for the unit id*/
    TextView lbl_id;
    /**The edit text for the unit id*/
    EditText txt_id;
    /**The main tab layout*/
    TabLayout tabs;
    /**The tab layout tabs*/
    TabLayout.Tab tab_notes, tab_soldiers, tab_logs;

    /**The unit in this activity*/
    public OrganisationalUnit unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit);

        unit = GeneralHelper.currentUnit;

        setViews();
        //configureViews(); Happens in onResume() -> toolbar_menu_refresh_onClick()
    }

    @Override
    protected void onResume() {
        super.onResume();
        toolbar_menu_refresh_onClick();
    }

    /**
     * Create an intent pointing to this activity
     *
     * @param context The context that requests the intent
     * @return The intent to this activity
     */
    public static Intent makeIntent(Context context) {
        return new Intent(context, UnitActivity.class);
    }

    /**Select a tab from the tab layout
     * @param tab The tab to select
     */
    public void selectTab(TabLayout.Tab tab){
        Fragment fragment;
        if (tab == tab_notes){
            fragment = NotesFragment.newInstance(unit.getRowId());
        } else if (tab == tab_soldiers){
            fragment = SoldiersFragment.newInstance(unit.getRowId());
        } else {
            fragment = LogsFragment.newInstance(unit.getRowId());
        }
        getSupportFragmentManager().beginTransaction().replace(
                R.id.unit_side_scroll, fragment).commit();
    }

    @Override
    public void toolbar_menu_refresh_onClick() {
        unit = Database.UNITS.getRowById(unit.getRowId());
        super.toolbar_menu_refresh_onClick();

        if (isAfterRefresh()) {
            TabLayout.Tab tab = tabs.getTabAt(tabs.getSelectedTabPosition());
            selectTab(tab);
        }
        _after_refresh = true;
    }

    @Override
    public void setViews() {
        super.setViews();
        lbl_id = findViewById(R.id.unit_lbl_id);
        lbl_type = findViewById(R.id.unit_lbl_type);
        lbl_commander = findViewById(R.id.unit_lbl_commander);
        txt_id = findViewById(R.id.unit_txt_id);

        tabs = findViewById(R.id.unit_tabs);
        if (tabs != null){//because setViews runs twice, once from BaseActivity, and then from here
            tab_notes = tabs.getTabAt(0);
            tab_soldiers = tabs.getTabAt(1);
            tab_logs = tabs.getTabAt(2);
        }
    }

    @Override
    public void configureViews() {
        super.configureViews();
        //Set Title
        toolbar_lbl_title.setText(unit.getType() + " " + unit.getName());
        //Hide the refresh button from toolbar menu
//        toolbar_menu.getMenu().findItem(R.id.toolbar_menu_refresh).setVisible(false);

        if (AppSettings.isDebugMode()){
            lbl_id.setVisibility(View.VISIBLE);
            txt_id.setVisibility(View.VISIBLE);
            txt_id.setText(String.valueOf(unit.getRowId()));
        }

        lbl_type.setText(getString(R.string.unit_lbl_type, unit.getType()));
        lbl_commander.setText(getString(R.string.unit_lbl_commander,
                unit.getCommander() == null ? "" : unit.getCommander().getName()));

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectTab(tab);
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });
        if (!isAfterRefresh()) {
            tabs.selectTab(tab_soldiers);
        }
    }
}
