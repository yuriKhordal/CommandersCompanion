package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Represents note types table*/
public class NoteTypes extends SinglePrimaryKeyCacheTable<NoteType> {
	/**The name of the table*/
	public static final String NAME = "NoteTypes";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(NoteType.ID);
	/**All the columns of the table*/
	public static final IColumn[] COLUMNS = NoteType.getStaticColumns();

	/**Initialize a new note type table*/
	protected NoteTypes() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**Initialize a new note type table with a helper
	 * @param helper The helper to the database
	 */
	public NoteTypes(IDatabaseHelper helper) {
		super(helper, NoteTypes::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static NoteType convert(IRow row){
		return new NoteType(row);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public NoteTypes clone() {
		NoteTypes noteTypes = new NoteTypes();
		noteTypes.helper = helper.clone();
		noteTypes.converter = converter;

		for (NoteType noteType : rows){
			NoteType cloned = noteType.clone();
			noteTypes.add(cloned);
		}

		return noteTypes;
	}

}
