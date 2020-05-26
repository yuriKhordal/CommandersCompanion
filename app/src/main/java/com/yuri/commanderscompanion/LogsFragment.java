package com.yuri.commanderscompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yuri.commanderscompanion.api.Database;
import com.yuri.commanderscompanion.api.GeneralHelper;
import com.yuri.commanderscompanion.api.Log;
import com.yuri.commanderscompanion.api.LogEntry;
import com.yuri.commanderscompanion.api.LogType;
import com.yuri.commanderscompanion.api.OrganisationalUnit;
import com.yuri.commanderscompanion.api.SQLiteDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogsFragment extends Fragment {
    /**The name of the rowid in the bundle*/
    public static final String ROWID_BUNDLE_NAME = "rowid";

    /**The logs' unit*/
    OrganisationalUnit unit;

    LinearLayout tbl_types;
    Button btn_add;

    /**Initialize a new fragment*/
    public LogsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param rowid The id of the logs' unit
     * @return A new instance of fragment LogsFragment.
     */
    public static LogsFragment newInstance(int rowid) {
        LogsFragment fragment = new LogsFragment();
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
        View inflated = inflater.inflate(R.layout.fragment_logs, container, false);
        setViews(inflated);
        configureViews();
        return inflated;
    }

    /**Create a linear layout from a log type with the following structure
     * <br><br>
     * LogType (vertical LinearLayout) :<UL>
     *     <LI>LogType's info (horizontal LinearLayout) :<UL>
     *         <LI>Name (TextView)</LI>
     *         <LI>Fold arrow (ImageButton)</LI>
     *         <LI>Delete type (Image Button)</LI>
     *     </UL></LI>
     *     <LI>Log #1 (vertical LinearLayout) :<UL>
     *         <LI>Log's info (horizontal LinearLayout) :<UL>
     *             <LI>Date (TextView)</LI>
     *             <LI>Fold arrow (ImageButton)</LI>
     *             <LI>Delete log (Image Button)</LI>
     *         </UL></LI>
     *         <LI>LogEntry #1 (horizontal LinearLayout) :<UL>
     *             <LI>Soldier (TextView)</LI>
     *             <LI>Text/Data (TextView)</LI>
     *         </UL></LI>
     *         <LI>LogEntry #2 (horizontal LinearLayout)</LI>
     *         <LI>LogEntry #N (horizontal LinearLayout)</LI>
     *     </UL></LI>
     *     <LI>Log #2 (vertical LinearLayout)</LI>
     *     <LI>Log #N (vertical LinearLayout)</LI>
     * </UL>
     * @param type
     * @param logs
     */
    public void createLogTypeLayout(LogType type, List<Log> logs){
        Context context = getContext();
        int dp5 = GeneralHelper.dpToPixels(context, 5);
        int dp24 = GeneralHelper.dpToPixels(context, 24);
        LinearLayout.LayoutParams layout_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams view_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams view_weight_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(dp24, dp24);

        //Create the main type layout
        LinearLayout type_layout = new LinearLayout(context);
        type_layout.setLayoutParams(layout_params);
        type_layout.setOrientation(LinearLayout.VERTICAL);
        tbl_types.addView(type_layout);

        //Create the type row within type_layout
        LinearLayout type_row = new LinearLayout(context);
        type_row.setLayoutParams(layout_params);
        type_layout.addView(type_row);

        TextView lbl_type_name = new TextView(context);
        lbl_type_name.setLayoutParams(view_params);
        lbl_type_name.setBackgroundResource(R.drawable.table_cell);
        lbl_type_name.setText(type.getName());
        TextViewCompat.setTextAppearance(lbl_type_name, R.style.TextAppearance_AppCompat_Large);
        type_row.addView(lbl_type_name);

        ImageButton btn_type_arrow = new ImageButton(context);
        btn_type_arrow.setLayoutParams(img_params);
        btn_type_arrow.setTag(false);
        btn_type_arrow.setBackgroundResource(R.drawable.button_pressed);
        btn_type_arrow.setContentDescription(getString(R.string.fold_arrow));
        btn_type_arrow.setPaddingRelative(dp5, 0, dp5, 0);
        btn_type_arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        btn_type_arrow.setOnClickListener(this::btn_arrow_onClick);
        type_row.addView(btn_type_arrow);

        ImageButton btn_type_remove = new ImageButton(context);
        btn_type_remove.setLayoutParams(img_params);
        btn_type_remove.setTag(new Pair<LogType, List<Log>>(type, logs));
        btn_type_remove.setBackgroundResource(R.drawable.button_pressed);
        btn_type_remove.setContentDescription(getString(R.string.remove_desc));
        btn_type_remove.setPaddingRelative(dp5, 0, dp5, 0);
        btn_type_remove.setImageResource(R.drawable.ic_remove_black_24dp);
        btn_type_remove.setOnClickListener(this::btn_type_remove_onClick);
        type_row.addView(btn_type_remove);

        for (Log log : logs){
            //create a log layout with all the entries
            LinearLayout log_layout = new LinearLayout(context);
            log_layout.setLayoutParams(layout_params);
            log_layout.setOrientation(LinearLayout.VERTICAL);
            type_layout.addView(log_layout);

            //create a log row within row layout
            LinearLayout log_row = new LinearLayout(context);
            log_row.setLayoutParams(layout_params);
            log_row.setTag(log);
            log_row.setOnClickListener(this::tbl_row_log_onClick);
            log_layout.addView(log_row);

            TextView lbl_log_name = new TextView(context);
            lbl_log_name.setLayoutParams(view_params);
            lbl_log_name.setBackgroundResource(R.drawable.table_cell);
            String date = SQLiteDatabaseHelper.SIMPLE_DATETIME_FORMATTER.format(log.getTime());
            lbl_log_name.setText(date);
            TextViewCompat.setTextAppearance(lbl_log_name, R.style.TextAppearance_AppCompat_Large);
            log_row.addView(lbl_log_name);

            ImageButton btn_log_arrow = new ImageButton(context);
            btn_log_arrow.setLayoutParams(img_params);
            btn_log_arrow.setTag(false);
            btn_log_arrow.setBackgroundResource(R.drawable.button_pressed);
            btn_log_arrow.setContentDescription(getString(R.string.fold_arrow));
            btn_log_arrow.setPaddingRelative(dp5, 0, dp5, 0);
            btn_log_arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            btn_log_arrow.setOnClickListener(this::btn_arrow_onClick);
            log_row.addView(btn_log_arrow);

            ImageButton btn_log_remove = new ImageButton(context);
            btn_log_remove.setLayoutParams(img_params);
            btn_log_remove.setTag(log);
            btn_log_remove.setBackgroundResource(R.drawable.button_pressed);
            btn_log_remove.setContentDescription(getString(R.string.remove_desc));
            btn_log_remove.setPaddingRelative(dp5, 0, dp5, 0);
            btn_log_remove.setImageResource(R.drawable.ic_remove_black_24dp);
            btn_log_remove.setOnClickListener(this::btn_log_remove_onClick);
            log_row.addView(btn_log_remove);

            for (LogEntry entry: log.entries){
                if (entry.getText().isEmpty()) { continue; }
                //create an entry row
                LinearLayout entry_row = new LinearLayout(context);
                entry_row.setLayoutParams(layout_params);
                log_layout.addView(entry_row);

                TextView lbl_entry_soldier = new TextView(context);
                lbl_entry_soldier.setLayoutParams(view_params);
                lbl_entry_soldier.setBackgroundResource(R.drawable.table_cell);
                String soldier = entry.getSoldier().getName();
                lbl_entry_soldier.setText(soldier);
                TextViewCompat.setTextAppearance(lbl_entry_soldier, R.style.TextAppearance_AppCompat_Large);
                entry_row.addView(lbl_entry_soldier);

                TextView lbl_entry_text = new TextView(context);
                lbl_entry_text.setLayoutParams(view_weight_params);
                lbl_entry_text.setBackgroundResource(R.drawable.table_cell);
                lbl_entry_text.setGravity(Gravity.CENTER);
                lbl_entry_text.setText(entry.getText());
                TextViewCompat.setTextAppearance(lbl_entry_text, R.style.TextAppearance_AppCompat_Large);
                entry_row.addView(lbl_entry_text);
            }
        }
    }

    public void tbl_row_log_onClick(View view){
        Log log = (Log) view.getTag();
        Intent intent = LogActivity.makeIntent(getContext(), log.getRowId(), LogActivity.MODE_UPDATE);
        startActivity(intent);
    }

    /**The type remove button onClick event
     * @param view The remove button
     */
    public void btn_type_remove_onClick(View view){
        ImageButton remove = (ImageButton) view;
        LinearLayout layout = (LinearLayout) remove.getParent().getParent();
        @SuppressWarnings("unchecked")
        Pair<LogType, List<Log>> type = (Pair<LogType, List<Log>>) remove.getTag();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_delete_title);
        builder.setMessage(getString(R.string.dialog_delete_msg, type.first.getName()));
        builder.setNegativeButton(R.string.dialog_no, null);
        builder.setPositiveButton(R.string.dialog_yes, ((dialogInterface, i) -> {
            for (Log log : type.second){
                Database.LOGS.removeRow(log);
            }
            Database.LOG_TYPES.removeRow(type.first);

            tbl_types.removeView(layout);
        }));

        GeneralHelper.setDialogRTL(builder.create()).show();
    }

    /**The log remove button onClick event
     * @param view The remove button
     */
    public void btn_log_remove_onClick(View view){
        ImageButton remove = (ImageButton) view;
        LinearLayout layout = (LinearLayout) remove.getParent().getParent();
        LinearLayout parent = (LinearLayout) layout.getParent();
        Log log = (Log) remove.getTag();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_delete_title);
        String name = log.getType().getName() + " : "
                + SQLiteDatabaseHelper.SIMPLE_DATETIME_FORMATTER.format(log.getTime());
        builder.setMessage(getString(R.string.dialog_delete_msg, name));
        builder.setNegativeButton(R.string.dialog_no, null);
        builder.setPositiveButton(R.string.dialog_yes, ((dialogInterface, i) -> {
            Database.LOGS.removeRow(log);
            log.getUnit().removeLog(log);
            parent.removeView(layout);
        }));

        GeneralHelper.setDialogRTL(builder.create()).show();
    }

    /**The arrow button onClick event
     * @param view The arrow button
     */
    public void btn_arrow_onClick(View view){
        ImageButton arrow = (ImageButton) view;
        LinearLayout layout = (LinearLayout) arrow.getParent().getParent();
        boolean fold = !(boolean) arrow.getTag();
        float rotation;
        int visibility;

        if (fold){
            rotation = 270;
            visibility = View.GONE;
        } else {
            rotation = 0;
            visibility = View.VISIBLE;
        }

        for (int i = 1; i < layout.getChildCount(); i++){
            layout.getChildAt(i).setVisibility(visibility);
        }

        arrow.setRotation(rotation);
        arrow.setTag(fold);
    }

    /**The add log button onClick event
     * @param view The button
     */
    public void btn_add_onClick(View view){
        Intent intent = LogActivity.makeIntent(getContext(), unit.getRowId(), LogActivity.MODE_CREATE);
        startActivity(intent);
    }

    /**Set the view objects
     * @param fragment The fragment view
     */
    public void setViews(View fragment){
        tbl_types = fragment.findViewById(R.id.logs_tbl_types);
        btn_add = fragment.findViewById(R.id.logs_btn_add);
    }

    /**Configure the views after setting them*/
    public void configureViews(){
        List<Log> logs = unit.getLogs();

        if (!logs.isEmpty()){
            HashMap<LogType, ArrayList<Log>> map = new HashMap<>();
            for (Log log : unit.getLogs()){
                LogType type = log.getType();
                if (!map.containsKey(type)){
                    map.put(type, new ArrayList<>());
                }
                map.get(type).add(log);
            }

            for (ArrayList<Log> list : map.values()){
                GeneralHelper.quickSort(list, (log1, log2) ->
                        log1.getTime().compareTo(log2.getTime()));
            }

            ArrayList<LogType> types = new ArrayList<>(map.keySet());
            GeneralHelper.quickSort(types, (type1, type2) ->
                    GeneralHelper.stringCompare(type1.getName(), type2.getName()));

            for (LogType type : types){
                createLogTypeLayout(type, map.get(type));
            }
        }

        //Move the button to the bottom
        tbl_types.removeView(btn_add);
        tbl_types.addView(btn_add);
        btn_add.setOnClickListener(this::btn_add_onClick);
    }
}
