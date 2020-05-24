package com.yuri.commanderscompanion.api;

import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.IRow;
import dbAPI.SingularPrimaryKey;

/**Represents a commander of a unit and a row*/
public class Commander extends Soldier{
	
	/**The unit under the commander's command*/
	public OrganisationalUnit Unit;
	
	/**Initialize a new commander with cells
	 * @param cells The cells of the row
	 */
	protected Commander(DatabaseCell... cells) {
		super(cells);
		isCommander = true;
	}

	/** Initialize a new commander from a commander row
	 * @param row The commander row
	 */
	public Commander(IRow row) {
//		this(row.getCell(ID), row.getCell(UNIT_ID), row.getCell(IS_COMMANDER), row.getCell(NAME),
//				row.getCell(RANK), row.getCell(WEAPON_SERIAL));
		this(GeneralHelper.getCells(row, Commanders.COLUMNS));
//		this(
//				row.getCell(ID).Value.getInt(),
//				Database.UNITS.getRow(new SingularPrimaryKey(row.getCell(UNIT_ID))),
//				row.getCell(NAME).Value.getString(),
//				row.getCell(RANK).Value.getString(),
//				Database.WEAPONS.getRow(new SingularPrimaryKey(row.getCell(WEAPON_SERIAL)))
//		);
		this.id = row.getCell(ID).Value.getInt();
		this.name = row.getCell(NAME).Value.getString();
		this.rank = row.getCell(RANK).Value.getString();
		this.role = row.getCell(ROLE).Value.getString();
		this.unit = Database.UNITS.getRow(new SingularPrimaryKey(row.getCell(UNIT_ID)));
		this.Unit = this.unit;
		Unit.commander = this;
		this.weapon = Database.WEAPONS.getRow(new SingularPrimaryKey(row.getCell(WEAPON_SERIAL)));
		//pkey = new SingularPrimaryKey(ID, row.getCell(ID).Value);
	}

	/**Initialize a new commander with an id, unit, name, rank, and weapon
	 * @param name The name of this commander
	 * @param rank The rank of this commander
	 * @param role The role of this commander
	 * @param weapon The weapon of this commander
	 * @param id The ID of this commander
	 */
	protected Commander(int id, OrganisationalUnit unit, String name, String rank, String role, Weapon weapon) {
		this(
			new DatabaseCell(ID, id, DatabaseDataType.INTEGER),
			new DatabaseCell(UNIT_ID, unit.id, DatabaseDataType.INTEGER),
			new DatabaseCell(IS_COMMANDER, true, DatabaseDataType.BOOLEAN),
			new DatabaseCell(NAME, name, DatabaseDataType.STRING),
			new DatabaseCell(RANK, rank, DatabaseDataType.STRING),
			new DatabaseCell(ROLE, role, DatabaseDataType.STRING),
			new DatabaseCell(WEAPON_SERIAL, weapon.getSerial(), DatabaseDataType.INTEGER)
		);
		this.id = id;
		this.name = name;
		this.rank = rank;
		this.unit = unit;
		Unit = this.unit;
		Unit.commander = this;
		this.weapon = Database.WEAPONS.getRow(new SingularPrimaryKey(weapon.getCell(Weapon.SERIAL)));
	}

	@Override
	public Commander clone() {
		return new Commander(this);
	}
}
