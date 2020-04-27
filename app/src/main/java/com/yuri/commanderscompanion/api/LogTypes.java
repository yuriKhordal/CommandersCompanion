package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;

/**Represents a table of log types*/
public class LogTypes extends SQLiteTable<LogType> {
	/**The name of the table*/
	public static final String NAME = "LogTypes";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(LogType.LOG_TYPE_ID);
	/**All the columns of the table*/
	public static final IColumn[] COLUMNS = LogType.getStaticColumns();

	/**Initialize a new log types table*/
	protected LogTypes() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**Initialize a new log types table with a helper
	 * @param helper The helper for the database
	 */
	public LogTypes(SQLiteDatabaseHelper helper) {
		super(helper, LogTypes::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static LogType convert(IRow row){
		return new LogType(row);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public LogTypes clone() {
		LogTypes logTypes = new LogTypes();
		logTypes.helper = helper.clone();
		logTypes.converter = converter;

		for (LogType logType : rows){
			LogType cloned = logType.clone();
			logTypes.addFromIRow(cloned);
		}

		return logTypes;
	}
}
