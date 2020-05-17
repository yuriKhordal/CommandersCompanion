package com.yuri.commanderscompanion;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yuri.commanderscompanion.api.AppSettings;
import com.yuri.commanderscompanion.api.Database;
import com.yuri.commanderscompanion.api.Note;
import com.yuri.commanderscompanion.api.NoteType;
import com.yuri.commanderscompanion.api.OrganisationalUnit;
import com.yuri.commanderscompanion.api.SQLiteDataReader;
import com.yuri.commanderscompanion.api.Soldier;

import java.util.ArrayList;

import androidx.appcompat.app.AlertDialog;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.IColumn;
import dbAPI.IRow;

public class NoteActivity extends BaseActivity {
    /**The name of the extra rowid value in the intent*/
    public static final String INTENT_EXTRA_ROWID_NAME = "rowid";
    /**The name of the extra mode value in the intent*/
    public static final String INTENT_EXTRA_MODE_NAME = "mode";
    /**Update note*/
    public static final int INTENT_EXTRA_MODE_UPDATE = 0;
    /**Create soldier note*/
    public static final int INTENT_EXTRA_MODE_CREATE_FOR_SOLDIER = 1;
    /**Create unit note*/
    public static final int INTENT_EXTRA_MODE_CREATE_FOR_UNIT = 2;
    /**Create note from type*/
    public static final int INTENT_EXTRA_MODE_CREATE_FOR_TYPE = 3;

    TextView lbl_id, lbl_type, lbl_head, lbl_body;
    EditText txt_id, txt_head, txt_body;
    Spinner cmb_type;
    ImageButton btn_add_type;
    Button btn_update;

    /**The mode of the activity*/
    int mode;
    /**The note of this activity, null when creating, other when updating*/
    Note note;
    /**The note's soldier*/
    Soldier soldier;
    /**The note's unit*/
    OrganisationalUnit unit;
    /**The note's type*/
    NoteType type;

    /**A list of types*/
    ArrayList<NoteType> types;
    /**A list of the names of the types, index of name is same as index of types such as
     * <code>types.get(index).getName.equals(typeNames.get(i))</code> is always true*/
    ArrayList<String> typeNames;
    /**The adapter for the spinner*/
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        setViews();
        int rowid = getIntent().getIntExtra(INTENT_EXTRA_ROWID_NAME, -1);
        mode = getIntent().getIntExtra(INTENT_EXTRA_MODE_NAME, -1);
        if (rowid == -1 || mode == -1){
            throw new RuntimeException("Intent's rowid(" + rowid + ") or mode(" + mode + ")" +
                    " are missing");
        }
        if (mode == INTENT_EXTRA_MODE_UPDATE){
            note = Database.NOTES.getRowById(rowid);
        } else if (mode == INTENT_EXTRA_MODE_CREATE_FOR_SOLDIER){
            soldier = Database.SOLDIERS.getRowById(rowid);
        } else if (mode == INTENT_EXTRA_MODE_CREATE_FOR_UNIT){
            unit = Database.UNITS.getRowById(rowid);
        } else if (mode == INTENT_EXTRA_MODE_CREATE_FOR_TYPE){
            type = Database.NOTE_TYPES.getRowById(rowid);
        }
        configureViews();
    }

    /**Make an intent for starting this activity
     * @param context The context of the activity calling this method
     * @param rowid The rowid of the note for update. For creation the id of the parent soldier,
     *              unit, or type
     * @param mode The mode of the activity
     * @return An intent for creating the activity
     */
    public static Intent makeIntent(Context context, int rowid, int mode){
        Intent intent = new Intent(context, NoteActivity.class);
        intent.putExtra(INTENT_EXTRA_ROWID_NAME, rowid);
        intent.putExtra(INTENT_EXTRA_MODE_NAME, mode);
        return intent;
    }

    /**The add type + button onClick event
     * @param view The button
     */
    public void btn_add_type_onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_add_note_type);
        EditText name = new EditText(this);
        name.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        builder.setView(name);
        builder.setPositiveButton(R.string.dialog_add, ((dialogInterface, i) -> {
            String type_name = name.getText().toString();
            NoteType type;
            if (mode == INTENT_EXTRA_MODE_CREATE_FOR_SOLDIER) {
                type = new NoteType(soldier, type_name);
            } else if (mode == INTENT_EXTRA_MODE_CREATE_FOR_UNIT) {
                type = new NoteType(unit, type_name);
            } else {
                throw new RuntimeException("NoteType type is null, mode is somehow not for " +
                        "soldiers or units");
            }
            Database.NOTE_TYPES.add(type);

            types.add(type);
            typeNames.add(type.getName());
            cmb_type.setSelection(typeNames.size() - 1);
            adapter.notifyDataSetChanged();
        }));
        builder.setNegativeButton(R.string.dialog_cancel, null);
        builder.create().show();
    }

    /**The update button onClick event
     * @param view The button
     */
    public void btn_update_onClick(View view){
        if (mode == INTENT_EXTRA_MODE_UPDATE){
            String head, body;
            head = txt_head.getText().toString();
            body = txt_body.getText().toString();
            Database.UPDATES.add(Database.NOTES, note,
                    new DatabaseCell(Note.HEAD, head, DatabaseDataType.STRING),
                    new DatabaseCell(Note.BODY, body, DatabaseDataType.STRING));
            Database.UPDATES.update();
            Toast.makeText(this , "ההערה " + head + " עודכנה בהצלחה",
                    Toast.LENGTH_LONG).show();
        } else {
            String head, body;
            head = txt_head.getText().toString();
            body = txt_body.getText().toString();
            if (types.isEmpty()){
                Toast.makeText(this,
                        "אי אפשר ליצור הערה ללא קטגוריה", Toast.LENGTH_LONG).show();
                return;
            }else if (mode != INTENT_EXTRA_MODE_CREATE_FOR_TYPE) {
                type = types.get(cmb_type.getSelectedItemPosition());
            }

            if (head.isEmpty() && body.isEmpty()){
                Toast.makeText(this,
                        "אי אפשר ליצור הערה ריקה, חייב להוסיף לפחות כותרת או את גוף ההערה",
                        Toast.LENGTH_LONG).show();
                return;
            }
            note = new Note(type, head, body);
            Database.NOTES.add(note);
            note = Database.NOTES.getLastRowAdded();

            mode = INTENT_EXTRA_MODE_UPDATE;
            Toast.makeText(this , "ההערה " + note.getHead() + " הוספה בהצלחה",
                    Toast.LENGTH_LONG).show();
            configureViews();
        }
    }

    @Override
    public void setViews(){
        super.setViews();
        lbl_id = findViewById(R.id.note_lbl_id);
        lbl_type = findViewById(R.id.note_lbl_type);
        lbl_head = findViewById(R.id.note_lbl_head);
        lbl_body = findViewById(R.id.note_lbl_body);
        txt_id = findViewById(R.id.note_txt_id);
        txt_head = findViewById(R.id.note_txt_head);
        txt_body = findViewById(R.id.note_txt_body);
        cmb_type = findViewById(R.id.note_cmb_type);
        btn_add_type = findViewById(R.id.note_btn_add_type);
        btn_update = findViewById(R.id.note_btn_update);
    }

    @Override
    public void configureViews() {
        super.configureViews();
        if (mode == INTENT_EXTRA_MODE_UPDATE) {
            if (AppSettings.isDebugMode()) {
                lbl_id.setVisibility(View.VISIBLE);
                txt_id.setVisibility(View.VISIBLE);
                txt_id.setText(String.valueOf(note.getId()));
            }
            cmb_type.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, new String[]{note.getType().getName()}));
            cmb_type.setSelection(0);
            cmb_type.setEnabled(false);
            btn_add_type.setVisibility(View.GONE);
            txt_head.setText(note.getHead());
            txt_body.setText(note.getBody());
            btn_update.setText(R.string.note_update);
            toolbar_lbl_title.setText(R.string.unit_update_title);
            return;
        }

        types = new ArrayList<>();
        typeNames = new ArrayList<>(types.size());
        /*HashSet<NoteType> hashSet = new HashSet<>();
        if (mode == INTENT_EXTRA_MODE_CREATE_FOR_SOLDIER) {
            for (Note note : soldier.getNotes()) {
                if (hashSet.contains(note.getType())){ continue; }
                hashSet.add(note.getType());
                types.add(note.getType());
                typeNames.add(note.getType().getName());
            }
        } else if (mode == INTENT_EXTRA_MODE_CREATE_FOR_UNIT) {
            for (Note note : unit.getNotes()) {
                if (hashSet.contains(note.getType())){ continue; }
                hashSet.add(note.getType());
                types.add(note.getType());
                typeNames.add(note.getType().getName());
            }
        } else if (mode == INTENT_EXTRA_MODE_CREATE_FOR_TYPE) {
            types.add(type);
            typeNames.add(type.getName());
        }
        hashSet.clear();*/

        if (mode != INTENT_EXTRA_MODE_CREATE_FOR_TYPE){
            int id = mode == INTENT_EXTRA_MODE_CREATE_FOR_SOLDIER ? soldier.getID() : unit.getRowId();
            int ofSoldier = mode == INTENT_EXTRA_MODE_CREATE_FOR_SOLDIER ? 1 : 0;
            String where = NoteType.OWNER_ID.getName() + " = " + id + " AND\n" +
                    NoteType.OF_SOLDIER.getName() + " = " + ofSoldier;
            SQLiteDataReader reader = (SQLiteDataReader) Database.NOTE_TYPES.select(
                    new IColumn[]{NoteType.ID},where);

            for (IRow row : reader){
                NoteType type = Database.NOTE_TYPES.getRowById(row.getValue(NoteType.ID).getInt());
                types.add(type);
                typeNames.add(type.getName());
            }
        } else {
            types.add(type);
            typeNames.add(type.getName());
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeNames);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        cmb_type.setAdapter(adapter);
        if (mode == INTENT_EXTRA_MODE_CREATE_FOR_TYPE) {
            cmb_type.setSelection(0);
            cmb_type.setEnabled(false);
            btn_add_type.setVisibility(View.GONE);
        }

        toolbar_lbl_title.setText(R.string.unit_create_title);
    }
}
