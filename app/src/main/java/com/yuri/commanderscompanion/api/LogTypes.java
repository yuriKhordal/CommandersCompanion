/**
 * 
 */
package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents a table of log types*/
public class LogTypes extends Table<LogType> {
	/**The name of the table*/
	public static final String NAME = "LogTypes";
	/**All the columns of the table*/
	public static final IColumn[] COLUMNS = LogType.getStaticColumns();

	/**Initialize a new table with a helper
	 * @param helper The helper for the database
	 */
	public LogTypes(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void addTFromIRow(IRow row) {
		LogType log = new LogType(row);
		rows.add(log);
		rowsMap.put(log.id, log);
	}
}
