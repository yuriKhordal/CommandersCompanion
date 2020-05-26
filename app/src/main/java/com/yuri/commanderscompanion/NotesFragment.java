package com.yuri.commanderscompanion;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.yuri.commanderscompanion.api.Note;
import com.yuri.commanderscompanion.api.NoteType;
import com.yuri.commanderscompanion.api.OrganisationalUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import dbAPI.IDatabaseReader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {
    /**The name of the rowid in the bundle*/
    private static final String ROWID_BUNDLE_NAME = "rowid";

    LinearLayout tbl_notes;
    Button btn_add;

    /**The unit with all the notes*/
    OrganisationalUnit unit;

    public NotesFragment() {
        // Required empty public constructor
    }

    /**Use this factory method to create a new instance of
     * this fragment with the specified unit
     *
     * @param rowid The id of the notes' unit
     * @return A new instance of fragment NotesFragment.
     */
    public static NotesFragment newInstance(int rowid) {
        NotesFragment fragment = new NotesFragment();
        Bundle args = new Bundle();
        args.putInt(ROWID_BUNDLE_NAME, rowid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            int rowid = getArguments().getInt(ROWID_BUNDLE_NAME);
            unit = Database.UNITS.getRowById(rowid);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflated = inflater.inflate(R.layout.fragment_notes, container, false);
        setViews(inflated);
        configureViews();
        return inflated;
    }

    public void createNoteTypeLayout(List<Note> notes, String type, ViewGroup group) {
        Context context = getContext();
        LinearLayout parent = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parent.setLayoutParams(params);
        parent.setOrientation(LinearLayout.VERTICAL);

        LinearLayout typeRow = new LinearLayout(context);
        typeRow.setLayoutParams(params);

        TextView name = new TextView(context);
        params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(params);
        name.setBackgroundResource(R.drawable.table_cell);
        name.setText(type);
        TextViewCompat.setTextAppearance(name, R.style.TextAppearance_AppCompat_Large);
        typeRow.addView(name);

        ImageButton arrow = new ImageButton(context);
        int dp24 = GeneralHelper.dpToPixels(context, 24);
        params = new LinearLayout.LayoutParams(dp24, dp24);
        params.gravity = Gravity.CENTER;
        arrow.setLayoutParams(params);
        arrow.setBackgroundResource(R.drawable.button_pressed);
        arrow.setContentDescription(getString(R.string.fold_arrow));
        arrow.setTag(false);
        int dp5 = GeneralHelper.dpToPixels(context, 5);
        arrow.setPaddingRelative(dp5, 0, dp5, 0);
        arrow.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        arrow.setOnClickListener(this::btn_arrow_onClick);
        typeRow.addView(arrow);

        parent.addView(typeRow);

        for (Note note : notes) {
            LinearLayout row = new LinearLayout(context);
            params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(params);
            row.setTag(note);

            TextView head = new TextView(context);
            params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            head.setLayoutParams(params);
            head.setBackgroundResource(R.drawable.table_cell);
            head.setText(note.getHead());
            TextViewCompat.setTextAppearance(head, R.style.TextAppearance_AppCompat_Large);
            head.setOnClickListener(this::note_onClick);
            row.addView(head);

            TextView body = new TextView(context);
            params = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            body.setLayoutParams(params);
            body.setBackgroundResource(R.drawable.table_cell);
            body.setLines(1);
            body.setText(note.getBody());
            body.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            TextViewCompat.setTextAppearance(body, R.style.TextAppearance_AppCompat_Large);
            body.setOnClickListener(this::note_onClick);
            row.addView(body);

            ImageButton remove = new ImageButton(context);
            params = new LinearLayout.LayoutParams(dp24, dp24);
            params.gravity = Gravity.CENTER;
            remove.setLayoutParams(params);
            remove.setBackgroundResource(R.drawable.button_pressed);
            remove.setContentDescription(getString(R.string.remove_desc));
            remove.setPaddingRelative(dp5, 0, dp5, 0);
            remove.setImageResource(R.drawable.ic_remove_black_24dp);
            remove.setOnClickListener(this::btn_remove_onClick);
            row.addView(remove);

            parent.addView(row);
        }
        group.addView(parent);
    }

    public void btn_arrow_onClick(View view){
        ImageButton btn = (ImageButton)view;
        LinearLayout layout = (LinearLayout) btn.getParent().getParent();
        boolean fold = !(boolean)btn.getTag();
        float rotation;
        int visibility;

        if (fold) {
            rotation = 270;
            visibility = View.GONE;
        } else {
            rotation = 0;
            visibility = View.VISIBLE;
        }

        for(int i = 1; i < layout.getChildCount(); i++) {
            layout.getChildAt(i).setVisibility(visibility);
        }

        btn.setRotation(rotation);
        btn.setTag(fold);
    }

    public void btn_remove_onClick(View view){
        ImageButton btn = (ImageButton)view;
        LinearLayout layout = (LinearLayout)btn.getParent();
        Note note = (Note)layout.getTag();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_delete_title);
        builder.setMessage(getString(R.string.dialog_delete_msg, "ההערה"));
        builder.setNegativeButton(R.string.dialog_no, null);
        builder.setPositiveButton(R.string.dialog_yes, (dialogInterface, i) -> {
            LinearLayout parent = (LinearLayout)layout.getParent();
            Database.NOTES.removeRow(note);
            parent.removeView(layout);

            String where = Note.TYPE_ID.getName() + " = " + note.getType().getId();
            IDatabaseReader reader = Database.NOTES.selectAll(where);
            if (reader.hasNext()) { return; }

            AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
            builder2.setTitle(getString(R.string.dialog_note_type, note.getType().getName()));
            builder2.setNegativeButton(R.string.dialog_no, null);
            builder2.setPositiveButton(R.string.dialog_yes, (dialogInterface2, i2) -> {
//                LinearLayout parent2 = (LinearLayout)layout.getParent();
                tbl_notes.removeView(parent);
            Database.NOTE_TYPES.removeRow(note.getType());
            });
            GeneralHelper.setDialogRTL(builder2.create()).show();
        });
        GeneralHelper.setDialogRTL(builder.create()).show();
    }

    public void btn_add_onClick(View view){
        Intent intent = NoteActivity.makeIntent(getContext(), unit.getRowId(),
                NoteActivity.INTENT_EXTRA_MODE_CREATE_FOR_UNIT);
        startActivity(intent);
    }

    public void note_onClick(View view){
        LinearLayout row = (LinearLayout) view.getParent();
        Note note = (Note) row.getTag();
        Intent intent = NoteActivity.makeIntent(getContext(), note.getId(),
                NoteActivity.INTENT_EXTRA_MODE_UPDATE);
        startActivity(intent);
    }

    /**Set the view objects
     * @param fragment The fragment view
     */
    public void setViews(View fragment){
        tbl_notes = (LinearLayout)fragment.findViewById(R.id.notes_tbl);
        btn_add = (Button) fragment.findViewById(R.id.notes_btn_add);
    }

    /**Configure the views after setting them*/
    public void configureViews() {
        List<Note> notes = unit.getNotes();

        if (!notes.isEmpty()) {
            HashMap<NoteType, ArrayList<Note>> map = new HashMap<>();
            for (Note note : notes) {
                NoteType type = note.getType();
                if (!map.containsKey(type)) {
                    map.put(type, new ArrayList<>());
                }
                map.get(type).add(note);
            }

            ArrayList<NoteType> types = new ArrayList<>(map.keySet());
            GeneralHelper.quickSort(types, (type1, type2) ->
                    GeneralHelper.stringCompare(type1.getName(), type2.getName()));


            for (NoteType type : types) {
                /*ArrayList<Note> */notes = map.get(type);
                createNoteTypeLayout(notes, type.getName(), tbl_notes);
            }
        }

        //move button to bottom
        tbl_notes.removeView(btn_add);
        tbl_notes.addView(btn_add);
        btn_add.setOnClickListener(this::btn_add_onClick);
    }
}
