package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Represents a logs table*/
public class Logs extends SinglePrimaryKeyCacheTable<Log> {
	/**The name of the table*/
	public static final String NAME = "Logs";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(Log.ID);
	/**All the columns of the table*/
	public static final IColumn[] COLUMNS = Log.getStaticColumns();

	/**Initialize a new logs table*/
	protected Logs() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**Initialize a new logs table with a helper
	 * @param helper The helper for the database
	 */
	public Logs(IDatabaseHelper helper) {
		super(helper, Logs::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static Log convert(IRow row){
		return new Log(row);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Logs clone() {
		Logs logs = new Logs();
		logs.helper = helper.clone();
		logs.converter = converter;

		for (Log log : rows){
			Log cloned = log.clone();
			logs.addFromIRow(cloned);
		}

		return logs;
	}
}
