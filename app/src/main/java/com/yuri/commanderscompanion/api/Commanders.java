package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents a commanders table*/
public class Commanders extends Table<Commander> {
	/**The table's name*/
	public static final String NAME = "Commanders";
	/**The table's columns*/
	public static final IColumn[] COLUMNS = Commander.getStaticColumns();

	/**Initialize a new table with a helper
	 * @param helper The helper for the database
	 */
	public Commanders(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void addTFromIRow(IRow row) {
		Commander commander = new Commander(row);
		rows.add(commander);
		rowsMap.put(commander.id, commander);
	}
	
	@Override
	protected IColumn getPrimeryKey() {
		return Commander.ID;
	}

}
