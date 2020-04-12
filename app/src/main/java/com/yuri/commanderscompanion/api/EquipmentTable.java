package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents equipment table*/
public class EquipmentTable extends Table<Equipment> {
	/**The name of the table in the database*/
	public static final String NAME = "Equipment";
	/**All the columns of the table*/
	public static final IColumn[] COLUMNS = Equipment.getStaticColumns();
	
	/**Initialize a new table with a helper
	 * @param helper The helper for the database
	 */
	public EquipmentTable(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void addTFromIRow(IRow row) {
		Equipment e = new Equipment(row);
		this.rows.add(e);
		this.rowsMap.put(e.getSoldier_equipment_id(), e);
	}
	
	@Override
	protected IColumn getPrimeryKey() {
		return Equipment.SOLDIER_EQUIPMENT_ID;
	}

}
