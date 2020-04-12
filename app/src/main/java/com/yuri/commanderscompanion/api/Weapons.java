package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents a weapons Table*/
public class Weapons extends Table<Weapon> {
	/**The name of the table in the database*/
	public static final String NAME = "Weapons";
	/**All of the columns in the table*/
	public static final IColumn[] COLUMNS = Weapon.getStaticColumns();

	/**Initialize the table with a helper
	 * @param helper The helper for the database
	 */
	public Weapons(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	protected void addTFromIRow(IRow row) {
		Weapon w = new Weapon(row);
		rows.add(w);
		rowsMap.put(w.getSerial(), w);
	}

	@Override
	protected IColumn getPrimeryKey() {
		return Weapon.SERIAL;
	}

}
