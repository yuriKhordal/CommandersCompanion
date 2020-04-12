package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents a soldiers Table*/
public class Soldiers extends Table<Soldier>{
	/**The name of the table in the database*/
	public static final String NAME = "Soldiers";
	/**All of the columns in the table*/
	public static final IColumn[] COLUMNS = Soldier.getStaticColumns();
	
	/**Initialize the table with a helper
	 * @param helper The helper for the database
	 */
	public Soldiers(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	protected void addTFromIRow(IRow row) {
		Soldier s = new Soldier(row);
		rows.add(s);
		rowsMap.put(s.getID(), s);
	}

	@Override
	protected IColumn getPrimeryKey() {
		return Soldier.ID;
	}
}
