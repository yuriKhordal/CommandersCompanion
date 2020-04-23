package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Represents a notes table*/
public class Notes extends SinglePrimaryKeyCacheTable<Note> {
	/**The name of the table*/
	public static final String NAME = "Notes";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(Note.ID);
	/**All the columns in the table*/
	public static final IColumn[] COLUMNS = Note.getStaticColumns();

	/**Initialize a new notes table*/
	protected Notes() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**Initialize a new notes table with a helper
	 * @param helper The helper to the database
	 */
	public Notes(IDatabaseHelper helper) {
		super(helper, Notes::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static Note convert(IRow row){
		return new Note(row);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Notes clone() {
		Notes notes = new Notes();
		notes.helper = helper.clone();
		notes.converter = converter;

		for (Note note : rows){
			Note cloned = note.clone();
			notes.addFromIRow(cloned);
		}

		return notes;
	}
}
