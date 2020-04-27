package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;

/**Represents equipment table*/
public class EquipmentTable extends SQLiteTable<Equipment> {
	/**The name of the table in the database*/
	public static final String NAME = "Equipment";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(Equipment.SOLDIER_EQUIPMENT_ID);
	/**All the columns of the table*/
	public static final IColumn[] COLUMNS = Equipment.getStaticColumns();

	/**Initialize a new equipment table*/
	protected EquipmentTable() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}
	
	/**Initialize a new equipment table with a helper
	 * @param helper The helper for the database
	 */
	public EquipmentTable(SQLiteDatabaseHelper helper) {
		super(helper, EquipmentTable::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static Equipment convert(IRow row){
		return new Equipment(row);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public EquipmentTable clone() {
		EquipmentTable equipmentTable = new EquipmentTable();
		equipmentTable.helper = helper.clone();
		equipmentTable.converter = converter;

		for (Equipment equipment : rows){
			Equipment cloned = equipment.clone();
			equipmentTable.add(cloned);
		}

		return equipmentTable;
	}
}
