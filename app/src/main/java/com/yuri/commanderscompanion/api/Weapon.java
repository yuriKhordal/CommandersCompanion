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

/**Represents a weapon and a database row*/
public class Weapon extends SQLiteRow{
	/**The column of the weapon's serial*/
	public static final IColumn SERIAL = new Column("serial", 0, DatabaseDataType.INTEGER,
			Constraint.BASIC_PRIMARY_KEY_CONSTRAINT);
	/**The column of the weapon's sight's serial*/
	public static final IColumn SIGHT_SERIAL = new Column("sight_serial", 1, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("sight_serial", Sights.NAME + '(' + Sight.SERIAL.getName() + ')'));
	/**The column of the weapon's type*/
	public static final IColumn TYPE = new Column("type", 2, DatabaseDataType.STRING, Constraint.NOT_NULL);
	
	/**The type of the weapon, eg M4, Tavor etc*/
	private String type;
	/**The sight on the weapon, optional*/
	private Sight sight;
	/**The serial number on the weapon*/
	private int serial;

	/**Initialize a new weapon with a type, and a serial
	 * @param type The type of the weapon,like M4 or Tavor
	 * @param serial The serial of the weapon
	 */
	public Weapon(String type, int serial){
		super(
				new DatabaseCell(SERIAL, serial, DatabaseDataType.INTEGER),
				new DatabaseCell(SIGHT_SERIAL, null, DatabaseDataType.INTEGER),
				new DatabaseCell(TYPE, type, DatabaseDataType.STRING)
		);
		
		this.type = type;
		this.sight = null;
		this.serial = serial;
	}

	/**Initialize a new weapon with a type, a sight, and a serial
	 * @param type The type of the weapon,like M4 or Tavor
	 * @param s The sight on the weapon
	 * @param serial The serial of the weapon
	 */
	public Weapon(String type, Sight s, int serial){
		super(
				new DatabaseCell(SERIAL, serial, DatabaseDataType.INTEGER),
				new DatabaseCell(SIGHT_SERIAL, s.getSerial(), DatabaseDataType.INTEGER),
				new DatabaseCell(TYPE, type, DatabaseDataType.STRING)
		);
		
		this.type = type;
		this.sight = s;
		this.serial = serial;
	}
	
	/**Initialize a new weapon from a row
	 * @param row The weapon row
	 */
	public Weapon(IRow row) {
//		super(row.getCell(SERIAL), row.getCell(SIGHT_SERIAL), row.getCell(TYPE));
		super(GeneralHelper.getCells(row, Weapons.COLUMNS));
		DatabaseValue temp;
		
		this.type = row.getCell(TYPE).Value.getString();
		this.serial = row.getCell(SERIAL).Value.getInt();
		if ((temp = row.getCell(SIGHT_SERIAL).Value).Value == null){
			sight = null;
		} else {
			this.sight = Database.SIGHTS.getRow(new SingularPrimaryKey(SIGHT_SERIAL, temp));
		}
	}

	/**Get all the columns in the table
	 * @return All the columns in weapon
	 */
	public static IColumn[] getStaticColumns() {
		return new IColumn[] {SERIAL, SIGHT_SERIAL, TYPE};
	}
	
	/**Check if this weapon has a sight
	 * @return True if has a sight, otherwise false
	 */
	public boolean hasSight() {
		return this.sight != null;
	}
	
	/**Get the type of this weapon
	 * @return The type of this weapon
	 */
	public String getType() {
		return type;
	}
	
	/**Get the sight of this weapon
	 * @return The sight of this weapon
	 */
	public Sight getSight() {
		return this.sight;
	}

	/**Get the serial of this weapon
	 * @return The serial of this weapon
	 */
	public int getSerial(){
		return serial;
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
	public Weapon clone() {
		/*if (hasSight()) {
			return new Weapon(type, sight, serial);
		} else {
			return  new Weapon(type, serial);
		}*/
		return new Weapon(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) { return false; }
		Weapon weapon = (Weapon)obj;
		if (weapon.hasSight() != hasSight() || (weapon.hasSight() && !weapon.sight.equals(sight))) { return false; }
		return weapon.serial == serial && weapon.type == type;
	}

	@Override
	public String toString(){
		String string = "Type: " + type + "\n";
		if (this.sight != null){
			string += "Sight: " + this.sight;
		}
		string += "Serial: " + serial + "\n";
		return string;
	}
}
