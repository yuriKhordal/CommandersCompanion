package com.yuri.commanderscompanion.api;

import java.util.ArrayList;

import dbAPI.Column;
import dbAPI.Constraint;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.DatabaseValue;
import dbAPI.ForeignKeyConstraint;
import dbAPI.IColumn;
import dbAPI.IRow;
import dbAPI.SingularPrimaryKey;

/**Represents a soldier and a database row*/
public class Soldier extends SQLiteRow{
	/**The column of the soldier's service number*/
	public static final Column ID = new Column("id", 0, DatabaseDataType.INTEGER,
			Constraint.BASIC_PRIMARY_KEY_CONSTRAINT);
	/**The column of the soldier's unit*/
	public static final Column UNIT_ID = new Column("unit_id", 1, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("unit_id", OrganisationalUnits.NAME + '(' + OrganisationalUnit.ID.getName() + ')'),
			Constraint.NOT_NULL);
	/**The column that tells whether it is a commander or not*/
	public static final Column IS_COMMANDER = new Column("is_commander", 2, DatabaseDataType.INTEGER,
			Constraint.NOT_NULL);
	/**The column of the soldier's name*/
	public static final Column NAME = new Column("name", 3, DatabaseDataType.STRING,
			Constraint.NOT_NULL);
	/**The column of the soldier's rank*/
	public static final Column RANK = new Column("rank", 4, DatabaseDataType.STRING);
	/**The column of the soldier's weapon serial*/
	//column constructor sets the constraint's column to itself so null is acceptable
	public static final Column WEAPON_SERIAL = new Column("weapon_serial", 5, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("weapon_serial", Weapons.NAME + '(' + Weapon.SERIAL.getName() + ')'));
	
	/**This soldier's service number*/
	protected int id;
	/**This soldier's name*/
	protected String name;
	/**This soldier's rank*/
	protected String rank;
	/**This soldier's weapon*/
	protected Weapon weapon;
	/**The unit the soldier is in*/
	protected OrganisationalUnit unit;
	/**Whether the soldier is a commander*/
	protected boolean isCommander;
	
	/**This soldier's equipment*/
	protected ArrayList<Equipment> equipment;
	/**The soldier's notes*/
	protected ArrayList<Note> notes;
	/**The soldier's notices*/
	protected ArrayList<Notice> notices;
	
	/**Initialize a new soldier with cells
	 * @param cells The cells of the row
	 */
	protected Soldier(DatabaseCell...cells) {
		super(cells);
		this.equipment = new ArrayList<Equipment>();
		this.notes = new ArrayList<Note>();
		this.notices = new ArrayList<Notice>();
		isCommander = false;
	}
	
	/**Initialize a new soldier with a unit, name, rank and id
	 * @param unit The soldier's unit
	 * @param name The soldier's name
	 * @param rank The soldier's rank
	 * @param id The soldier's service number
	 */
	public Soldier(OrganisationalUnit unit, String name, String rank, int id){
		this(
			new DatabaseCell(ID, id, DatabaseDataType.INTEGER),
			new DatabaseCell(UNIT_ID, unit.id, DatabaseDataType.INTEGER),
			new DatabaseCell(IS_COMMANDER, 0, DatabaseDataType.INTEGER),
			new DatabaseCell(NAME, name, DatabaseDataType.STRING),
			new DatabaseCell(RANK, rank, DatabaseDataType.STRING),
			new DatabaseCell(WEAPON_SERIAL, null, DatabaseDataType.INTEGER)
		);
		this.unit = unit;
		this.name = name;
		this.rank = rank;
		this.id = id;
	}

	/**Initialize a new soldier with a unit, name, rank, weapon and id
	 * @param unit The soldier's unit
	 * @param name The soldier's name
	 * @param rank The soldier's rank
	 * @param weapon The soldier's weapon
	 * @param id The soldier's service number
	 */
	public Soldier(OrganisationalUnit unit, String name, String rank, Weapon weapon, int id){
		this(
			new DatabaseCell(ID, id, DatabaseDataType.INTEGER),
			new DatabaseCell(UNIT_ID, unit.id, DatabaseDataType.INTEGER),
			new DatabaseCell(IS_COMMANDER, 0, DatabaseDataType.INTEGER),
			new DatabaseCell(NAME, name, DatabaseDataType.STRING),
			new DatabaseCell(RANK, rank, DatabaseDataType.STRING),
			new DatabaseCell(WEAPON_SERIAL, weapon.getSerial(), DatabaseDataType.INTEGER)
		);
		this.unit = unit;
		this.name = name;
		this.rank = rank;
		this.weapon = weapon;
		this.id = id;
	}
	
	/**Initialize a new soldier from a soldier row
	 * @param row The soldier row
	 */
	public Soldier(IRow row) {
//		this(row.getCell(ID), row.getCell(UNIT_ID), row.getCell(IS_COMMANDER), row.getCell(NAME),
//				row.getCell(RANK), row.getCell(WEAPON_SERIAL));
		this(GeneralHelper.getCells(row, Soldiers.COLUMNS));
		DatabaseValue temp;
		this.id = row.getCell(ID).Value.getInt();
		this.unit = Database.UNITS.getRow(new SingularPrimaryKey(row.getCell(UNIT_ID)));
		this.isCommander = row.getCell(IS_COMMANDER).Value.getInt() == 1;
		if (isCommander) {
			this.unit.commander = this;
			Database.COMMANDERS.addFromIRow(this);
		} else {
			this.unit.soldiers.add(this);
		}
		this.name = row.getCell(NAME).Value.getString();
		if ((temp = row.getCell(RANK).Value).Value == null){
			rank = "";
		} else {
			this.rank = temp.getString();
		}

		if ((temp = row.getCell(WEAPON_SERIAL).Value).Value == null){
			weapon = null;
		} else {
			this.weapon = Database.WEAPONS.getRow(new SingularPrimaryKey(WEAPON_SERIAL, temp));
		}
	}
	
	/**Get all the columns in the row
	 * @return All the columns in the row
	 */
	public static IColumn[] getStaticColumns() {
		return new IColumn[] { ID, UNIT_ID, IS_COMMANDER, NAME, RANK, WEAPON_SERIAL };
	}
	
	/**Checks whether the soldier is from the commanders table
	 * @return True if soldier is a commander, false otherwise
	 */
	public boolean isCommander() {
		return isCommander;
	}
	
	/**Get this soldier's id
	 * @return The id of this soldier
	 */
	public int getID(){
		return this.id;
	}
	
	/**Get this soldier's name
	 * @return The name of this soldier
	 */
	public String getName() {
		return this.name;
	}

	/**Set this soldier's name
	 * @param name The name for this soldier
	 */
	public void setName(String name) {
		this.name = name;
		this.setValue(NAME.getIndex(), new DatabaseValue(name, DatabaseDataType.STRING));
	}

	/**Get this soldier's rank
	 * @return The rank of this soldier
	 */
	public String getRank() {
		return rank;
	}

	/**Set this soldier's rank
	 * @param rank The rank for this soldier
	 */
	public void setRank(String rank) {
		this.rank = rank;
		this.setValue(RANK.getIndex(), new DatabaseValue(rank, DatabaseDataType.STRING));
	}

	/**Get this soldier's unit
	 * @return The unit of this soldier
	 */
	public OrganisationalUnit getUnit() {
		return unit;
	}

	/**Get this soldier's weapon
	 * @return The weapon of this soldier
	 */
	public Weapon getWeapon() {
		return weapon;
	}

	/**Set this soldier's weapon
	 * @param weapon The weapon for this soldier
	 */
	public void setWeapon(Weapon weapon) {
		this.weapon = weapon;
		this.setValue(WEAPON_SERIAL.getIndex(), new DatabaseValue(weapon.getSerial(), DatabaseDataType.INTEGER));
	}

	/**Get the soldier's notes
	 * @return An array of notes
	 */
	public ArrayList<Note> getNotes() {
		return notes;
	}

	/**Get the soldier's equipment
	 * @return An array of equipment
	 */
	public ArrayList<Equipment> getEquipment() {
		return equipment;
	}

	/**Get the soldier's notices
	 * @return An array of notices
	 */
	public ArrayList<Notice> getNotices() {
		return notices;
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
	public Soldier clone() {
		return new Soldier(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) { return false; }
		Soldier soldier = (Soldier)obj;
		return  soldier.id == id;
	}

	@Override
	public String toString(){
		return this.id + ": " + rank + name;
	}
	
	
}
