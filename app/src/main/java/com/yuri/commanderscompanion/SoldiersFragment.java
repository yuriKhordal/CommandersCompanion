package com.yuri.commanderscompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.yuri.commanderscompanion.api.Database;
import com.yuri.commanderscompanion.api.GeneralHelper;
import com.yuri.commanderscompanion.api.OrganisationalUnit;
import com.yuri.commanderscompanion.api.Soldier;

import java.util.ArrayList;
import java.util.Stack;

import androidx.annotation.IntDef;
import androidx.arch.core.util.Function;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SoldiersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SoldiersFragment extends Fragment {
    /**The name of the rowid in the bundle*/
    public static final String ROWID_BUNDLE_NAME = "rowid";

    @IntDef({SORT_BY_RANK, SORT_BY_NAME, SORT_BY_ROLE})
    @interface SortBy {}
    public static final int SORT_BY_RANK = 0, SORT_BY_NAME = 1, SORT_BY_ROLE = 2;

    /**How to sort the soldiers table*/
    static @SortBy int sort_by = SORT_BY_NAME;

    OrganisationalUnit unit;
    ArrayList<Soldier> soldiers;

    TextView lbl_sort;
    Spinner cmb_sort;
    TableLayout tbl;
    Button btn_add;

    /**Initialize an empty new soldiers fragment*/
    public SoldiersFragment() { }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param rowid The id of the logs' unit
     * @return A new instance of fragment SoldiersFragment.
     */
    public static SoldiersFragment newInstance(int rowid) {
        SoldiersFragment fragment = new SoldiersFragment();
        Bundle args = new Bundle();
        args.putInt(ROWID_BUNDLE_NAME, rowid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            int rowid = getArguments().getInt(ROWID_BUNDLE_NAME);
            unit = Database.UNITS.getRowById(rowid);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated = inflater.inflate(R.layout.fragment_soldiers, container, false);

        setViews(inflated);
        configureViews();

        return inflated;
    }

    /**The onClick event of a remove button inside a soldier row
     * <br> <br>
     * Removes the soldier from database and the row from the table
     * @param view The image button that was clicked
     */
    public void btn_remove_onClick(View view){
        ImageButton btn = (ImageButton) view;
        Soldier soldier = (Soldier) btn.getTag();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_delete_title);
        builder.setTitle(getString(R.string.dialog_delete_msg, soldier.getName()));
        builder.setNegativeButton(R.string.dialog_no, null);
        builder.setPositiveButton(R.string.dialog_yes, (dialogInterface, i) -> {
            TableRow row = (TableRow) btn.getParent();

            Database.SOLDIERS.removeRow(soldier);
            soldier.getUnit().removeSoldier(soldier);
            tbl.removeView(row);
        });
        GeneralHelper.setDialogRTL(builder.create()).show();
    }

    /**The onClick event of the whole soldier row
     * <br><br>
     * Opens the soldier activity
     * @param view The table row layout that was clicked
     */
    public void tbl_row_onClick(View view){

    }

    /**Create a row containing a solider's info and add it to the table
     * @param soldier The soldier to show in the row
     */
    public void createSoldierRow(Soldier soldier){
        Context context = getContext();
        int dp24 = GeneralHelper.dpToPixels(context, 24);
        int dp10 = GeneralHelper.dpToPixels(context, 10);
        int dp5 = GeneralHelper.dpToPixels(context, 5);
        TableLayout.LayoutParams row_params = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        TableRow.LayoutParams view_params = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams dp24_params = new TableRow.LayoutParams(dp24, dp24);

        TableRow row = new TableRow(context);
        row.setLayoutParams(row_params);
        row.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        row.setTag(soldier);
        row.setOnClickListener(this::tbl_row_onClick);
        tbl.addView(row);

        TextView id = new TextView(context);
        view_params.column = 0;
        id.setBackgroundResource(R.drawable.table_cell);
        id.setPaddingRelative(dp10, 0, dp10, 0);
        id.setText(String.valueOf(soldier.getID()));
        id.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TextViewCompat.setTextAppearance(id, R.style.TextAppearance_AppCompat_Large);
        row.addView(id);

        TextView name = new TextView(context);
        view_params = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view_params.column = 1;
        name.setLayoutParams(view_params);
        name.setBackgroundResource(R.drawable.table_cell);
        name.setPaddingRelative(dp10, 0, dp10, 0);
        name.setText(String.valueOf(soldier.getName()));
        name.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TextViewCompat.setTextAppearance(name, R.style.TextAppearance_AppCompat_Large);
        row.addView(name);

        TextView rank = new TextView(context);
        view_params = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view_params.column = 2;
        rank.setLayoutParams(view_params);
        rank.setBackgroundResource(R.drawable.table_cell);
        rank.setPaddingRelative(dp10, 0, dp10, 0);
        rank.setText(String.valueOf(soldier.getRank()));
        rank.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TextViewCompat.setTextAppearance(rank, R.style.TextAppearance_AppCompat_Large);
        row.addView(rank);

        TextView role = new TextView(context);
        view_params = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view_params.column = 3;
        role.setLayoutParams(view_params);
        role.setBackgroundResource(R.drawable.table_cell);
        role.setPaddingRelative(dp10, 0, dp10, 0);
        role.setText(String.valueOf(soldier.getRole()));
        role.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TextViewCompat.setTextAppearance(role, R.style.TextAppearance_AppCompat_Large);
        row.addView(role);

        ImageButton remove = new ImageButton(context);
        dp24_params.column = 4;
        dp24_params.gravity = Gravity.CENTER;
        dp24_params.setMarginEnd(dp5);
        remove.setLayoutParams(dp24_params);
        remove.setBackgroundResource(R.drawable.button_pressed);
        remove.setContentDescription(getString(R.string.remove_desc));
        remove.setImageResource(R.drawable.ic_remove_black_24dp);
        remove.setOnClickListener(this::btn_remove_onClick);
        row.addView(remove);
    }

    /**Sort the soldiers list and populate the table with the sorted list's contents
     * @param sort_by How to sort the table
     */
    public void sortTable(@SortBy int sort_by){
        if (soldiers.isEmpty()) { return; }
        Function<Soldier, String> getStr;

        if (sort_by == SORT_BY_RANK){
            getStr = Soldier::getRank;
        } else if (sort_by == SORT_BY_NAME){
            getStr = Soldier::getName;
        } else {
            getStr = Soldier::getRole;
        }

        GeneralHelper.quickSort(soldiers, (s1, s2) ->
                GeneralHelper.stringCompare(getStr.apply(s1), getStr.apply(s2)));

        tbl.removeAllViews();
        for (Soldier soldier : soldiers) {
            createSoldierRow(soldier);
        }
    }

    /**Set the view objects
     * @param fragment The fragment view
     */
    public void setViews(View fragment){
        lbl_sort = fragment.findViewById(R.id.soldiers_lbl_sort);
        cmb_sort = fragment.findViewById(R.id.soldiers_cmb_sort);
        tbl = fragment.findViewById(R.id.soldiers_tbl);
        btn_add = fragment.findViewById(R.id.soldiers_btn_add);
    }

    /**Configure the views after setting them*/
    public void configureViews(){
        //Get all the soldiers
        soldiers = new ArrayList<>();
        Stack<OrganisationalUnit> units = new Stack<>();

        units.push(unit);
        while (!units.isEmpty()){
            OrganisationalUnit sub = units.pop();

            if (sub.getCommander() != null){
                soldiers.add(sub.getCommander());
            }
            if (sub.hasSoldiers()) {
                soldiers.addAll(sub.getSoldiers());
            }
            if (sub.hasSubUnits()) {
                units.addAll(sub.getSubUnits());
            }
        }

        cmb_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sort_by = i;
                sortTable(i);
            }
            @Override public void onNothingSelected(AdapterView<?> adapterView) {}});
        cmb_sort.setSelection(sort_by);

        //Sort and populate the table with the soldiers
        //sortTable(sort_by);
    }
}
