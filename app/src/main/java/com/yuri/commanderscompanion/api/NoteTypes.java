package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents note types table*/
public class NoteTypes extends Table<NoteType> {
	/**The name of the table*/
	public static final String NAME = "NoteTypes";
	/**All the columns of the table*/
	public static final IColumn[] COLUMNS = NoteType.getStaticColumns();

	/**Initialize a new note type with a helper
	 * @param helper The helper to the database
	 */
	public NoteTypes(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void addTFromIRow(IRow row) {
		NoteType type = new NoteType(row);
		rows.add(type);
		rowsMap.put(type.id, type);
	}
	
	@Override
	public IColumn getPrimeryKey() {
		return NoteType.ID;
	}

}
