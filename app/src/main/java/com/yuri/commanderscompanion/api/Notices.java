package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents a notices table*/
public class Notices extends Table<Notice> {
	/**The name of the table*/
	public static final String NAME = "Notices";
	/**All the columns in the table*/
	public static final IColumn[] COLUMNS = Notice.getStaticColumns();
	
	/**Initialize a new table with a helper to the database 
	 * @param helper A helper for the database
	 */
	public Notices(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void addTFromIRow(IRow row) {
		Notice notice = new Notice(row);
		rows.add(notice);
		rowsMap.put(notice.id, notice);
	}
	
	@Override
	protected IColumn getPrimeryKey() {
		return Notice.ID;
	}
}
