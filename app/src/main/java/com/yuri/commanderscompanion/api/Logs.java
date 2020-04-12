/**
 * 
 */
package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents a logs table*/
public class Logs extends Table<Log> {
	/**The name of the table*/
	public static final String NAME = "Logs";
	/**All the columns of the table*/
	public static final IColumn[] COLUMNS = Log.getStaticColumns();

	/**Initialize a new table with a helper
	 * @param helper The helper for the database
	 */
	public Logs(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void addTFromIRow(IRow row) {
		Log log = new Log(row);
		rows.add(log);
		rowsMap.put(log.id, log);
	}
	
	@Override
	protected IColumn getPrimeryKey() {
		return Log.ID;
	}
}
