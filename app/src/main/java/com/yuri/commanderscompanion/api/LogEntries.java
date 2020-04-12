/**
 * 
 */
package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Representing a table of entries*/
public class LogEntries extends Table<LogEntry> {
	/**The name of the table*/
	public static final String NAME = "LogEntries";
	/**All the table's columns*/
	public static final IColumn[] COLUMNS = LogEntry.getStaticColumns();
	
	/**Initialize a new table with a helper
	 * @param helper The helper for the database
	 */
	public LogEntries(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	protected void addTFromIRow(IRow row) {
		LogEntry entry = new LogEntry(row);
		rows.add(entry);
		rowsMap.put(entry.id, entry);
	}
	
	@Override
	protected IColumn getPrimeryKey() {
		return LogEntry.ID;
	}
}
