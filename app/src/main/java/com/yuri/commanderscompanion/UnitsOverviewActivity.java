package com.yuri.commanderscompanion;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yuri.commanderscompanion.api.Database;
import com.yuri.commanderscompanion.api.GeneralHelper;
import com.yuri.commanderscompanion.api.OrganisationalUnit;

import java.util.ArrayList;
import java.util.Stack;

import androidx.core.widget.TextViewCompat;
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
        OrganisationalUnit a, a1, a1A, a1B, a2, a2A, a2B, b, b1, b1A, b1B, b2, b2A, b2B, C, CA, CB, CC;
        {
            a1A = new OrganisationalUnit("כיתה", "א1א");
            a1B = new OrganisationalUnit("כיתה", "א1ב");
            a2A = new OrganisationalUnit("כיתה", "א2א");
            a2B = new OrganisationalUnit("כיתה", "א2ב");
            b1A = new OrganisationalUnit("כיתה", "ב1א");
            b1B = new OrganisationalUnit("כיתה", "ב1ב");
            b2A = new OrganisationalUnit("כיתה", "ב2א");
            b2B = new OrganisationalUnit("כיתה", "ב22");
            CA = new OrganisationalUnit("כיתה", "3א");
            CB = new OrganisationalUnit("כיתה", "3ב");
            CC = new OrganisationalUnit("כיתה", "3ג");

            a1 = new OrganisationalUnit("מחלקה", "א1", a1A, a1B);
            a2 = new OrganisationalUnit("מחלקה", "א2", a2A, a2B);
            b1 = new OrganisationalUnit("מחלקה", "ב1", b1A, b1B);
            b2 = new OrganisationalUnit("מחלקה", "ב2", b2A, b2B);
            C = new OrganisationalUnit("מחלקה", "3", CA, CB, CC);

            a = new OrganisationalUnit("פלוגה", "א", a1, a2);
            b = new OrganisationalUnit("פלוגה", "ב", b1, b2);

            GeneralHelper.currentUnit = new OrganisationalUnit("Unit", "Virtual",
                    a1A, a1B, a2A, a2B, b1A, b1B, b2A, b2B, CA, CB, CC, a1, a2, b1, b2, C, a, b);
        }
        // ---- DEBUG ----

        top_units.addAll(GeneralHelper.currentUnit.getSubUnits());

        loadUnits();

        GeneralHelper.quickSort(top_units,
                (u1, u2) -> GeneralHelper.stringCompare(u1.getName(), u2.getName()));

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

    /**Create a LinearLayout of a unit unit and it's sub units
     * @param unit The unit to to draw
     * @param level The folding level of the unit, 0 is top level of the activity, 1 is a sub unit
     *              of 0, and so on
     */
    public void createUnitLayout(OrganisationalUnit unit, int level, LinearLayout parent){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //Setting up the main layout
        LinearLayout mother = new LinearLayout(this);
        mother.setLayoutParams(params);
        mother.setOrientation(LinearLayout.VERTICAL);
        if (level > 0) { mother.setVisibility(View.GONE); }

        //Setting up the first row of the main unit
        createUnitRow(unit, level, mother);

        parent.addView(mother);

        ArrayList<OrganisationalUnit> subUnits = new ArrayList<>(unit.getSubUnits());
        GeneralHelper.quickSort(subUnits,
                (u1, u2) -> GeneralHelper.stringCompare(u1.getName(), u2.getName()));
        for (OrganisationalUnit sub : subUnits){
            if (sub.hasSubUnits()){
                createUnitLayout(sub, level + 1, mother);
            } else {
                createUnitRow(sub, level + 1, mother);
            }
        }
    }

    /**Create a single unit horizontal linear layout and add it to the given layout
     * @param unit The unit to construct the layout from
     * @param level The level of the unit
     * @param layout The parent layout
     */
    public void createUnitRow(OrganisationalUnit unit, int level, LinearLayout layout){
        LinearLayout.LayoutParams params;
        GradientDrawable grad = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{0x40000000, 0x00000000, 0x00000000, 0x40000000});

        LinearLayout row = new LinearLayout(this);
        params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(params);
        row.setBackground(grad);
        if (level > 0 && !unit.hasSubUnits()) {
            row.setVisibility(View.GONE);
        }

        TextView text = new TextView(this);
        params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        text.setLayoutParams(params);
        String str = "";
        if (level > 0){
            str = "|" + GeneralHelper.multiplyString("=", level) + ">";
        }
        text.setText(str + unit.getType() + " " + unit.getName());
        TextViewCompat.setAutoSizeTextTypeWithDefaults(text, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        text.setOnClickListener(this::onUnitClick);
        row.addView(text);


        ImageButton arrowButton = new ImageButton(this);
        int size = GeneralHelper.dpToPixels(this, 24);
//        params = new LinearLayout.LayoutParams(
//            ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params = new LinearLayout.LayoutParams(size, size);
        arrowButton.setLayoutParams(params);
        if (unit.hasSubUnits()) {
            arrowButton.setAdjustViewBounds(false);
            arrowButton.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            arrowButton.setBackgroundResource(R.drawable.button_pressed);
            arrowButton.setRotation(270);
            arrowButton.setTag(false);
            arrowButton.setOnClickListener(this::onArrowButtonClick);
        } else {
            arrowButton.setVisibility(View.INVISIBLE);
        }
        row.addView(arrowButton);


        layout.addView(row);
    }

    /**The method that gets called whenever the arrow button near the unit name gets clicked
     * The button folds/expands the sub units of the unit that was clicked
     * @param view The arrow button
     */
    public void onArrowButtonClick(View view){
        ImageButton btn = (ImageButton)view;
        boolean inflated = (Boolean) btn.getTag();
        LinearLayout row = (LinearLayout)btn.getParent();
        //Get the vertical layout of the button, with all the 'rows' (horizontal layouts) of the sub units
        LinearLayout vert = (LinearLayout) btn.getParent().getParent();
        int vert_child_count = vert.getChildCount();

        if (inflated){
            btn.setRotation(270);
            //hide all except the parent unit
            for (int i = 0; i < vert_child_count; i++){
                View child = vert.getChildAt(i);
                if (child == row){ continue; }
                child.setVisibility(View.GONE);
            }
            btn.setTag(false);
        } else {
            btn.setRotation(0);
            vert.setVisibility(View.VISIBLE);
            for (int i = 0; i < vert_child_count; i++){
                View child = vert.getChildAt(i);
                child.setVisibility(View.VISIBLE);
            }
            btn.setTag(true);
        }
    }

    /**TODO:Write
     * @param view
     */
    public void onUnitClick(View view){
        OrganisationalUnit unit = (OrganisationalUnit) view.getTag();
        //TODO:Finish
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

        for (OrganisationalUnit unit : top_units){
            createUnitLayout(unit, 0, vertical_layout);
        }
    }
}
