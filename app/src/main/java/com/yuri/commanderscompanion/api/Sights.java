package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents a sights Table*/
public class Sights extends Table<Sight> {
	/**The name of the table in the database*/
	public static final String NAME = "Sights";
	/**All of the columns in the table*/
	public static final IColumn[] COLUMNS = Sight.getStaticColumns();

	/**Initialize the table with a helper
	 * @param helper The helper for the database
	 */
	public Sights(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void addTFromIRow(IRow row) {
		Sight s = new Sight(row);
		rows.add(s);
		rowsMap.put(s.getSerial(), s);
	}

	@Override
	protected IColumn getPrimeryKey() {
		return Sight.SERIAL;
	}
}
