package com.yuri.commanderscompanion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.yuri.commanderscompanion.api.Database;
import com.yuri.commanderscompanion.api.GeneralHelper;
import com.yuri.commanderscompanion.api.OrganisationalUnit;

import java.util.ArrayList;
import java.util.Stack;

import dbAPI.IRow;

/**Represents the main starting activity of the app*/
public class UnitsOverviewActivity extends BaseActivity {
    public ScrollView scroll;
    public LinearLayout vertical_layout;

    /**The top level units*/
    private ArrayList<OrganisationalUnit> top_units;
    /**All other units*/
    private ArrayList<OrganisationalUnit> units;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_units_overview);

        units = new ArrayList<OrganisationalUnit>();
        top_units = new ArrayList<OrganisationalUnit>();

        // ---- DEBUG ----
        {
            OrganisationalUnit a, a1, a1A, a1B, a2, a2A, a2B, b, b1, b1A, b1B, b2, b2A, b2B, C, CA, CB, CC;
            a1A = new OrganisationalUnit("Squad", "A1A");
            a1B = new OrganisationalUnit("Squad", "A1B");
            a2A = new OrganisationalUnit("Squad", "A2A");
            a2B = new OrganisationalUnit("Squad", "A2B");
            b1A = new OrganisationalUnit("Squad", "B1A");
            b1B = new OrganisationalUnit("Squad", "B1B");
            b2A = new OrganisationalUnit("Squad", "B2A");
            b2B = new OrganisationalUnit("Squad", "B2B");
            CA = new OrganisationalUnit("Squad", "3A");
            CB = new OrganisationalUnit("Squad", "3B");
            CC = new OrganisationalUnit("Squad", "3C");

            a1 = new OrganisationalUnit("Platoon", "A1", a1A, a1B);
            a2 = new OrganisationalUnit("Platoon", "A2", a2A, a2B);
            b1 = new OrganisationalUnit("Platoon", "B1", b1A, b1B);
            b2 = new OrganisationalUnit("Platoon", "B2", b2A, b2B);
            C = new OrganisationalUnit("Platoon", "3", CA, CB, CC);

            a = new OrganisationalUnit("Company", "A", a1, a2);
            b = new OrganisationalUnit("Company", "B", b1, b2);

            GeneralHelper.currentUnit = new OrganisationalUnit("Unit", "Virtual",
                    a1A, a1B, a2A, a2B, b1A, b1B, b2A, b2B, CA, CB, CC, a1, a2, b1, b2, C, a, b);
        }
        // ---- DEBUG ----

        top_units.addAll(GeneralHelper.currentUnit.getSubUnits());

        loadUnits();

        setViews();
        configureViews();
    }

    /**Create an intent pointing to this activity
     * @param context The context that requests the intent
     * @return The intent to this activity
     */
    public static Intent makeIntent(Context context){
        return new Intent(context, UnitsOverviewActivity.class);
    }

    /**Load all the units*/
    public void loadUnits(){
        Stack<OrganisationalUnit> stack = new Stack<>();

        //debug
        for (OrganisationalUnit unit : top_units){
            stack.push(unit);
        }
        //debug

        for (IRow row : Database.UNITS){
            OrganisationalUnit unit = (OrganisationalUnit)row;
            if (units.contains(unit)) { continue; }
            top_units.add(unit);
            stack.push(unit);
        }

        while (!stack.isEmpty()) {
            OrganisationalUnit unit = stack.pop();
            if (!unit.hasSubUnits()) { continue; }

            int size = unit.getNumberOfUnits(false);
            while (--size >= 0) {
                OrganisationalUnit sub = unit.getOU(size);
                if (units.contains(sub)) { continue; }
                stack.push(sub);
                units.add(sub);
                if (top_units.contains(sub)) {
                    top_units.remove(sub);
                }
            }
        }//while
        units.clear();
    }//loadUnits()

    public void createLayout(){

    }

    @Override
    public void setViews() {
        super.setViews();
        scroll = (ScrollView)findViewById(R.id.overview_scroll);
        vertical_layout = (LinearLayout)findViewById(R.id.overview_vertical_layout);
    }

    @Override
    public void configureViews() {
        super.configureViews();
        //Set Title
        toolbar_lbl_title.setText(R.string.units_overview_title);
        //Hide the refresh button from toolbar menu
        toolbar_menu.getMenu().findItem(R.id.toolbar_menu_refresh).setVisible(false);
    }
}
