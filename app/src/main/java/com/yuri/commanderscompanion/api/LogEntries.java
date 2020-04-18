package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Representing a table of entries*/
public class LogEntries extends SinglePrimaryKeyCacheTable<LogEntry> {
	/**The name of the table*/
	public static final String NAME = "LogEntries";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(LogEntry.ID);
	/**All the table's columns*/
	public static final IColumn[] COLUMNS = LogEntry.getStaticColumns();

	/**Initialize a new log entries table*/
	public LogEntries() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}
	
	/**Initialize a new log entries table with a helper
	 * @param helper The helper for the database
	 */
	public LogEntries(IDatabaseHelper helper) {
		super(helper, LogEntries::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static LogEntry convert(IRow row){
		return new LogEntry(row);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public LogEntries clone() {
		LogEntries logEntries = new LogEntries();
		logEntries.helper = helper.clone();
		logEntries.converter = converter;

		for (LogEntry logEntry : rows){
			LogEntry cloned = logEntry.clone();
			logEntries.add(cloned);
		}

		return logEntries;
	}
}
