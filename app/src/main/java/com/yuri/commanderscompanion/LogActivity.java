package com.yuri.commanderscompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.yuri.commanderscompanion.api.AppSettings;
import com.yuri.commanderscompanion.api.Database;
import com.yuri.commanderscompanion.api.GeneralHelper;
import com.yuri.commanderscompanion.api.Log;
import com.yuri.commanderscompanion.api.LogEntry;
import com.yuri.commanderscompanion.api.LogType;
import com.yuri.commanderscompanion.api.OrganisationalUnit;
import com.yuri.commanderscompanion.api.SQLiteDatabaseHelper;
import com.yuri.commanderscompanion.api.Soldier;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.IntDef;
import androidx.core.widget.TextViewCompat;
import dbAPI.IDatabaseReader;
import dbAPI.IRow;

public class LogActivity extends BaseActivity {
    /**The tag of the class for logging*/
    public static final String TAG = "LogActivity";
    /**The name of the extra rowid value in the intent*/
    public static final String INTENT_EXTRA_ROWID_NAME = "rowid";
    /**The name of the extra mode value in the intent*/
    public static final String INTENT_EXTRA_MODE_NAME = "mode";

    @IntDef({MODE_UPDATE, MODE_CREATE})
    @interface Mode{};

    /**Represents that the activity is in update mode*/
    public static final int MODE_UPDATE = 0;
    /**Represents that the activity is in creation mode*/
    public static final int MODE_CREATE = 1;

    TextView lbl_id, lbl_type, lbl_date;
    EditText txt_id, txt_date;
    Spinner cmb_type;
    ImageButton btn_add_type;
    TableLayout tbl_entries;
    Button btn_update;

    OrganisationalUnit unit;
    Log log;
    @Mode int mode;
    LogType selected = null;

    ArrayList<LogType> types;
    ArrayList<String> typeNames;

    /**Make an intent for starting this activity
     * @param context The context of the activity calling this method
     * @param rowid The rowid of the log for update or unit for creation
     * @param mode The mode of the activity
     * @return An intent for creating the activity
     */
    public static Intent makeIntent(Context context, int rowid, @Mode int mode){
        Intent intent = new Intent(context, LogActivity.class);
        intent.putExtra(INTENT_EXTRA_ROWID_NAME, rowid);
        intent.putExtra(INTENT_EXTRA_MODE_NAME, mode);
        return intent;
    }

    /**Get the extras values and init the activities with them*/
    private void getExtras(){
        Intent intent = getIntent();
        int rowid = intent.getIntExtra(INTENT_EXTRA_ROWID_NAME, -1);
        mode = intent.getIntExtra(INTENT_EXTRA_MODE_NAME, -1);
        if (rowid == -1 || mode == -1){
            throw new RuntimeException("Intent's rowid(" + rowid + ") or mode(" + mode + ")" +
                    " are missing");
        }

        if (mode == MODE_UPDATE){
            log = Database.LOGS.getRowById(rowid);
        } else {
            unit = Database.UNITS.getRowById(rowid);
        }
    }

    /**Populate the entries table with entries*/
    private void populateTable(){
        tbl_entries.removeAllViews();

        if (mode == MODE_UPDATE){
            for (LogEntry entry : log.entries){
                createRow(entry.getSoldier(), entry.getText());
            }
            return;
        }

        if (selected == null){ return; }

        LogType type = selected;
        List<Soldier> soldiers;
        if (type.isRecursive()){
            soldiers = GeneralHelper.getAllSoldiersFromUnit(unit);
        } else {
            soldiers = unit.getSoldiers();
        }

        for (Soldier soldier : soldiers) {
            createRow(soldier, "");
        }
    }

    /**Initialize the types and typeNames lists and add all the unit's log types to them*/
    private void initTypes(){
        types = new ArrayList<>();
        typeNames = new ArrayList<>();

        if (mode == MODE_UPDATE){
            types.add(log.getType());
            typeNames.add(log.getType().getName());
            return;
        }

        /*HashSet<LogType> hashSet = new HashSet<>();
        for (Log log : unit.getLogs()){
            LogType type = log.getType();

            if (hashSet.contains(type)){ continue; }
            hashSet.add(type);
            types.add(type);
            typeNames.add(type.getName());
        }*/

        String where = LogType.UNIT_ID.getName() + " = " + unit.getRowId();
        IDatabaseReader reader = Database.LOG_TYPES.select(
                new dbAPI.IColumn[]{LogType.LOG_TYPE_ID}, where);
        for (IRow row : reader){
            LogType type = Database.LOG_TYPES.getRowById(row.getValue(LogType.LOG_TYPE_ID).getInt());
            types.add(type);
            typeNames.add(type.getName());
        }
    }

    /**Create a table row from a soldier and text that represent an entry, and add it to the table
     * @param soldier The soldier of the entry
     * @param text The text for the entry
     */
    public void createRow(Soldier soldier, String text){
        TableLayout.LayoutParams row_params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams lbl_params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        TableRow row = new TableRow(this);
        row.setLayoutParams(row_params);
        row.setTag(soldier);
        tbl_entries.addView(row);

        TextView lbl_name = new TextView(this);
        lbl_name.setLayoutParams(lbl_params);
        lbl_name.setGravity(Gravity.CENTER);
        lbl_name.setBackgroundResource(R.drawable.table_cell);
        lbl_name.setText(soldier.getName());
        TextViewCompat.setTextAppearance(lbl_name, R.style.TextAppearance_AppCompat_Large);
        row.addView(lbl_name);

        EditText txt_text = new EditText(this);
        txt_text.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
        txt_text.setGravity(Gravity.CENTER);
        txt_text.setBackgroundResource(R.drawable.table_cell);
        txt_text.setText(text);
        TextViewCompat.setTextAppearance(txt_text, R.style.TextAppearance_AppCompat_Large);
        row.addView(txt_text);
    }

    /**The onClick event of the update/create button
     * @param view The update/create button
     */
    public void btn_update_onClick(View view){
        if (mode == MODE_UPDATE){
            updateLog();
        } else {
            LogType logType;
            String dateStr = txt_date.getText().toString();
            Date date;

            if (types.isEmpty()){
                Toast.makeText(this,
                        "אי אפשר ליצור דוח ללא קטגוריה", Toast.LENGTH_LONG).show();
                return;
            }

            if (dateStr.isEmpty()){
                Toast.makeText(this,
                        "אי אפשר ליצור דוח ללא תאריך", Toast.LENGTH_LONG).show();
                return;
            }

            try { date = SQLiteDatabaseHelper.SIMPLE_DATETIME_FORMATTER.parse(dateStr); }
            catch (ParseException e) {
                try { date = SQLiteDatabaseHelper.SIMPLE_DATE_FORMATTER.parse(dateStr); }
                catch (ParseException ex) {
                    Toast.makeText(this, "התאריך צריך להיות בתבנית dd/mm/yyyy(שנה/חודש/יום)" +
                            " או dd/mm/yyyy hh:mm(דקה:שעה שנה/חודש/יום)", Toast.LENGTH_LONG).show();
                    return;
                }
            }

            logType = types.get(cmb_type.getSelectedItemPosition());
            createLog(logType, date);
        }
    }

    /**Update the log from the table on MODE_UPDATE*/
    public void updateLog(){
        if (mode == MODE_CREATE) { return; }
        final int TEXT_COLUMN = 1;
        for (int i = 0; i < tbl_entries.getChildCount(); i++){
            TableRow row = (TableRow) tbl_entries.getChildAt(i);
            Soldier soldier = (Soldier) row.getTag();
            LogEntry entry = log.entries.get(i);
            String text = ((EditText)row.getChildAt(TEXT_COLUMN)).getText().toString();

            if (!entry.getSoldier().equals(soldier)){
                android.util.Log.wtf(TAG, "Something went wrong while updating log:\n" +
                        log.toString() + "\nWhile iterating the rows in tbl_entries at index " +
                        i + ", the soldier, in the row's tag was:\n" + soldier.toString() +
                        "\nAnd the soldier in the entry of the same index were not equal!");
                throw new RuntimeException("Soldier at row index '" + i + "' and at entry of " +
                        "the same index aren't equal");
            }


            entry.setText(text);
            Database.UPDATES.add(Database.ENTRIES, entry, entry.getCell(LogEntry.TEXT));
        }
        Database.UPDATES.update();
        Toast.makeText(this, "הדוח עודכן בהצלחה", Toast.LENGTH_LONG).show();
    }

    /**Create a new log from the activity and add to the database
     * @param type The type of log to create
     * @param date The date of the new log
     */
    public void createLog(LogType type, Date date){
        Log log = new Log(type, date);

        Database.LOGS.add(log);
        log = Database.LOGS.getLastRowAdded();

        final int TEXT_COLUMN = 1;
        for (int i = 0; i < tbl_entries.getChildCount(); i++){
            TableRow row = (TableRow) tbl_entries.getChildAt(i);
            Soldier soldier = (Soldier) row.getTag();
            String text = ((EditText)row.getChildAt(TEXT_COLUMN)).getText().toString();
            LogEntry entry = new LogEntry(log, soldier, text);

            Database.ENTRIES.add(entry);
            entry = Database.ENTRIES.getLastRowAdded();
            log.entries.set(i, entry);
        }

        Toast.makeText(this, "הדוח הוסף בהצלחה", Toast.LENGTH_LONG).show();
    }

    /**The onClick event of the plus button to add type
     * @param view The plus button
     */
    public void btn_add_type_onClick(View view){
        if (mode == MODE_UPDATE){ return; }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_add_type);
        EditText name = new EditText(this);
        CheckBox recursive = new CheckBox(this);
        recursive.setText(R.string.dialog_recursive);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        //layout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        layout.addView(name); layout.addView(recursive);
        builder.setView(layout);
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.setPositiveButton(R.string.dialog_add, (dialogInterface, i) -> {
           String typeName = name.getText().toString();
           boolean isRecursive = recursive.isChecked();
           LogType type = new LogType(unit, typeName, isRecursive);

           Database.LOG_TYPES.add(type);
           type = Database.LOG_TYPES.getLastRowAdded();

           types.add(type);
           typeNames.add(typeName);
           cmb_type.setSelection(typeNames.size() - 1);
           ((ArrayAdapter)cmb_type.getAdapter()).notifyDataSetChanged();
        });
        GeneralHelper.setDialogRTL(builder.create()).show();
    }

    /**The spinner's onItemSelected event
     * @param adapterView The spinner
     * @param view The view of the item that was clicked
     * @param i The position of the item
     * @param l The id of the row of the item
     */
    public void cmb_type_onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (selected == null){
            selected = types.get(i);
            populateTable();
            return;
        }

        LogType prev = selected;
        selected = types.get(i);

        if (selected.isRecursive() != prev.isRecursive()){
            populateTable();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        getExtras();
        setViews();
        configureViews();
    }

    @Override
    public void toolbar_menu_refresh_onClick() {
        getExtras();
        super.toolbar_menu_refresh_onClick();
    }

    @Override
    public void setViews() {
        super.setViews();

        lbl_id = findViewById(R.id.log_lbl_id);
        txt_id = findViewById(R.id.log_txt_id);
        lbl_type = findViewById(R.id.log_lbl_type);
        cmb_type = findViewById(R.id.log_cmb_type);
        btn_add_type = findViewById(R.id.log_btn_add_type);
        lbl_date = findViewById(R.id.log_lbl_date);
        txt_date = findViewById(R.id.log_txt_date);
        tbl_entries = findViewById(R.id.log_tbl_entries);
        btn_update = findViewById(R.id.log_btn_update);
    }

    @Override
    public void configureViews() {
        super.configureViews();
        if (mode == MODE_UPDATE){
            if (AppSettings.isDebugMode()){
                lbl_id.setVisibility(View.VISIBLE);
                txt_id.setVisibility(View.VISIBLE);
                txt_id.setText(String.valueOf(log.getId()));
            }

            //Hide settings options in toolbar menu
            toolbar_menu.getMenu().findItem(R.id.toolbar_menu_settings).setVisible(false);

            cmb_type.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                    new String[]{log.getType().getName()}));
            cmb_type.setEnabled(false);
            btn_add_type.setVisibility(View.GONE);

            String dateStr = SQLiteDatabaseHelper.SIMPLE_DATETIME_FORMATTER.format(log.getTime());
            txt_date.setText(dateStr);
            txt_date.setEnabled(false);

            btn_update.setText(R.string.update);
        } else {
            //Hide the menu button
            toolbar_btn_menu.setVisibility(View.GONE);

            initTypes();
            cmb_type.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, typeNames));
            cmb_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    cmb_type_onItemSelected(adapterView, view, i, l);
                } @Override public void onNothingSelected(AdapterView<?> adapterView) {} });

            String dateStr = SQLiteDatabaseHelper.SIMPLE_DATETIME_FORMATTER.format(new Date());
            txt_date.setText(dateStr);
        }

        populateTable();
    }
}
