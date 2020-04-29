package com.yuri.commanderscompanion.api;

import dbAPI.Column;
import dbAPI.Constraint;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.DatabaseValue;
import dbAPI.ForeignKeyConstraint;
import dbAPI.IColumn;
import dbAPI.IRow;
import dbAPI.SingularPrimaryKey;

/**Represents a soldier's equipment and a row in a database*/
public class Equipment extends SQLiteRow {
	public static final IColumn SOLDIER_EQUIPMENT_ID = new Column("soldier_equipment_id", 0, DatabaseDataType.INTEGER,
			Constraint.BASIC_PRIMARY_KEY_CONSTRAINT/*, Constraint.AUTO_INCREMENT*/);
	/**The column of the equipment's owner id*/
	public static final IColumn SOLDIER_ID = new Column("soldier_id", 1, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("soldier_id", Soldiers.NAME + '(' + Soldier.ID.getName() + ')'),
			Constraint.NOT_NULL);
	/**The column of the equipment's serial*/
	public static final IColumn SERIAL = new Column("serial", 2, DatabaseDataType.INTEGER);
	/**The column of the equipment's name*/
	public static final IColumn NAME = new Column("name", 3, DatabaseDataType.STRING,
			Constraint.NOT_NULL);
	/**The column of the equipment's status*/
	public static final IColumn STATUS = new Column("status", 4, DatabaseDataType.STRING,
			Constraint.NOT_NULL);
	
	/**Equipment-Soldier id*/
	protected int soldier_equipment_id;
	/**Equipment's owner*/
	protected Soldier owner;
	/**Equipment's serial*/
	protected Integer serial;
	/**Equipment's name*/
	protected String name;
	/**Equipment's status*/
	protected EquipmentStatus status;

	/**Initialize a new equipment with owner id, serial, name, and status
	 * @param owner_id Equipment's owner
	 * @param serial Equipment's serial
	 * @param name Equipment's name
	 * @param status Equipment's status
	 */
	public Equipment(int owner_id, int serial, String name, EquipmentStatus status) {
		super(
				new DatabaseCell(SOLDIER_EQUIPMENT_ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(SOLDIER_ID, owner_id, DatabaseDataType.INTEGER),
				new DatabaseCell(SERIAL, serial, DatabaseDataType.INTEGER),
				new DatabaseCell(NAME, name, DatabaseDataType.STRING),
				new DatabaseCell(STATUS, status.toString(), DatabaseDataType.STRING)
		);
		this.soldier_equipment_id = OFFLINE_ROW_ID;
		this.owner = Database.SOLDIERS.getRow(new SingularPrimaryKey(SOLDIER_ID, owner_id, DatabaseDataType.INTEGER));
		this.owner.equipment.add(this);
		this.serial = serial;
		this.name = name;
		this.status = status;
	}
	
	/**Initialize a new equipment with owner id, name, and status
	 * @param owner_id Equipment's owner
	 * @param name Equipment's name
	 * @param status Equipment's status
	 */
	public Equipment(int owner_id, String name, EquipmentStatus status) {
		super(
				new DatabaseCell(SOLDIER_EQUIPMENT_ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(SOLDIER_ID, owner_id, DatabaseDataType.INTEGER),
				new DatabaseCell(SERIAL, null, DatabaseDataType.INTEGER),
				new DatabaseCell(NAME, name, DatabaseDataType.STRING),
				new DatabaseCell(STATUS, status.toString(), DatabaseDataType.STRING)
		);
		this.soldier_equipment_id = OFFLINE_ROW_ID;
		this.owner = Database.SOLDIERS.getRow(new SingularPrimaryKey(SOLDIER_ID, owner_id, DatabaseDataType.INTEGER));
		this.owner.equipment.add(this);
		this.name = name;
		this.status = status;
	}
	
	public Equipment(IRow row) {
//		super(row.getCell(SOLDIER_EQUIPMENT_ID), row.getCell(SOLDIER_ID), row.getCell(SERIAL),
//				row.getCell(NAME), row.getCell(STATUS));
		super(GeneralHelper.getCells(row, EquipmentTable.COLUMNS));
		DatabaseValue temp;
		if ((temp = row.getCell(SOLDIER_EQUIPMENT_ID).Value).Value == null) {
			this.soldier_equipment_id = OFFLINE_ROW_ID;
		} else {
			this.soldier_equipment_id = temp.getInt();
		}
		this.owner = Database.SOLDIERS.getRow(new SingularPrimaryKey(row.getCell(SOLDIER_ID)));
		this.owner.equipment.add(this);
		if ((temp = row.getCell(SERIAL).Value).Value == null){
			this.serial = null;
		} else {
			this.serial = temp.getInt();
		}
		this.name = row.getCell(NAME).Value.getString();
		this.status = EquipmentStatus.fromString(row.getCell(STATUS).Value.getString());
	}
	
	/**Get all the columns of the row
	 * @return All the columns
	 */
	public static IColumn[] getStaticColumns(){
		return new IColumn[] {SOLDIER_EQUIPMENT_ID, SOLDIER_ID, SERIAL, NAME, STATUS};
	}
	
	/**Get soldier-equipment unique id
	 * @return soldier-equipment id
	 */
	public int getSoldierEquipmentId() {
		return soldier_equipment_id;
	}

	/**Get equipment's serial
	 * @return the serial
	 */
	public int getSerial() {
		return serial;
	}

	/**Get equipment's name
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**Set equipment's name
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		this.setValue(NAME, new DatabaseValue(name, DatabaseDataType.STRING));
	}

	/**Get equipment's status
	 * @return the status
	 */
	public EquipmentStatus getStatus() {
		return status;
	}

	/**Set equipment's status
	 * @param status the status to set
	 */
	public void setStatus(EquipmentStatus status) {
		this.status = status;
		this.setValue(STATUS, new DatabaseValue(status.toString(), DatabaseDataType.STRING));
	}

	@Override
	public boolean hasPrimaryKey() {
		return true;
	}

	@Override
	public IColumn[] getColumns() {
		return getStaticColumns();
	}

	@Override
	public Equipment clone() {
		return new Equipment(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) { return false; }
		Equipment equipment = (Equipment)obj;
		return equipment.soldier_equipment_id == soldier_equipment_id && equipment.owner.equals(owner) &&
				equipment.serial == serial && equipment.name == name && equipment.status == status;
	}

	@Override
	public String toString() {
		return name + ":\t" + status + "\t" + serial != null ? serial.toString() : "";
	}

	/**Represents the status of the equipment*/
	public enum EquipmentStatus{
		WORKING, LOST_OR_STOLEN, DEFICIENT, BROKEN;
		
		public static EquipmentStatus fromString(String string) {
			switch(string) {
				case "WORKING":			return WORKING;
				case "LOST_OR_STOLEN":	return LOST_OR_STOLEN;
				case "DEFICIENT":		return DEFICIENT;
				case "BROKEN":			return BROKEN;
				default: throw new IllegalArgumentException("No status named: " + string);
			}
		}
	}
}
