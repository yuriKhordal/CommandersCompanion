package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents a notes table*/
public class Notes extends Table<Note> {
	/**The name of the table*/
	public static final String NAME = "Notes";
	/**All the columns in the table*/
	public static final IColumn[] COLUMNS = Note.getStaticColumn();

	/**Initialize a new notes table with a helper to the database
	 * @param helper The helper for the database
	 */
	public Notes(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void addTFromIRow(IRow row) {
		Note note = new Note(row);
		rows.add(note);
		rowsMap.put(note.id, note);
	}

	@Override
	public IColumn getPrimeryKey() {
		return Note.ID;
	}
}
