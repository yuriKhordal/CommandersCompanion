package com.yuri.commanderscompanion.api;

import java.util.ArrayList;
import java.util.Stack;

import dbAPI.Column;
import dbAPI.Constraint;
import dbAPI.ConstraintsEnum;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.IColumn;
import dbAPI.IRow;
import dbAPI.Row;

/**Represents an organizational unit like a squad or a platoon and a row in the database*/
public class OrganisationalUnit extends Row  {
	/**The column of this unit's id*/
	public static final IColumn ID = new Column("id", 0, DatabaseDataType.INTEGER,
			ConstraintsEnum.PRIMERY_KEY, ConstraintsEnum.AUTO_INCREMENT);
	/**The column of this unit's commander id*/
	public static final IColumn COMMANDER_ID = new Column("commander_id", 1, DatabaseDataType.INTEGER,
			new Constraint(ConstraintsEnum.FOREIGN_KEY, null).setInfo(Commanders.NAME));
	/**The column of this unit's type*/
	public static final IColumn TYPE = new Column("type", 2, DatabaseDataType.STRING,
			ConstraintsEnum.NOT_NULL);
	/**The column of this unit's name*/
	public static final IColumn NAME = new Column("name", 3, DatabaseDataType.STRING,
			ConstraintsEnum.NOT_NULL);
	
	/**The last id of the unit*/
	protected static int last_id = 0;
	
	/**The id of this unit*/
	protected int id;
	/**The commander of this unit*/
	protected Soldier commander;
	/**The soldiers of this unit*/
	protected ArrayList<Soldier> soldiers;
	/**Sub-Units inside this unit*/
	protected ArrayList<OrganisationalUnit> subUnits;
	/**The type of this unit(eg squad, platoon, etc)*/
	protected String type;
	/**The name of the unit(not type,like platoon, but name, like Alpha team)*/
	protected String name;
	
	/**The logs for this unit*/
	protected ArrayList<Log> logs;
	/**The notes of this unit*/
	protected ArrayList<Note> notes;
	
	/**Initialize this unit with cells
	 * @param cells The cells
	 */
	protected OrganisationalUnit(final DatabaseCell... cells) {
		super(cells);
		logs = new ArrayList<Log>();
		notes = new ArrayList<Note>();
	}

	/**Initialize a new unit with a type, a name, and soldiers
	 * @param type The type of this unit
	 * @param name The name of this unit
	 * @param soldiers The soldiers in this unit
	 * @throws IllegalArgumentException If one of the soldiers is null
	 */
	public OrganisationalUnit(final String type, final String name, final Soldier...soldiers)
	throws IllegalArgumentException {
		this(
				new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(COMMANDER_ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(TYPE, type, DatabaseDataType.STRING),
				new DatabaseCell(NAME, name, DatabaseDataType.STRING)
		);
		this.id = last_id++;
		this.type = type;
		this.name = name;
		this.soldiers = new ArrayList<Soldier>(soldiers.length);
		this.subUnits = new ArrayList<OrganisationalUnit>(0);
		for (Soldier soldier : soldiers) {
			if (soldier == null) {
				throw new IllegalArgumentException("Soldier can't be null!");
			}
			this.soldiers.add(soldier);
		}
	}

	/**Initialize a new unit with a type, a name, and subunits
	 * @param type The type of this row
	 * @param name The name of this unit
	 * @param subUnits The sub-units in this unit
	 * @throws IllegalArgumentException If one of the subunits is null
	 */
	public OrganisationalUnit(final String type, final String name, final OrganisationalUnit...subUnits)
	throws IllegalArgumentException {
		this(
				new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(COMMANDER_ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(TYPE, type, DatabaseDataType.STRING),
				new DatabaseCell(NAME, name, DatabaseDataType.STRING)
		);
		this.id = last_id++;
		this.type = type;
		this.name = name;
		this.subUnits = new ArrayList<OrganisationalUnit>(subUnits.length);
		this.soldiers = new ArrayList<Soldier>(0);
		for (OrganisationalUnit subUnit : subUnits) {
			if (subUnit == null) {
				throw new IllegalArgumentException("subUnit can't be null!");
			}
			this.subUnits.add(subUnit);
		}
	}
	
	/**Initialize a new unit with a type, a name, soldiers, and subunits
	 * @param type The type of this row
	 * @param name The name of this unit
	 * @param subUnits The sub-units in this unit
	 * @param soldiers The soldiers in this unit
	 * @throws IllegalArgumentException If one of the subunits, soldiers, or commander are null
	 */
	public OrganisationalUnit(final String type, final String name, final OrganisationalUnit[] subUnits, Soldier...soldiers)
	throws IllegalArgumentException {
		this(
				new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(COMMANDER_ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(TYPE, type, DatabaseDataType.STRING),
				new DatabaseCell(NAME, name, DatabaseDataType.STRING)
		);
		this.id = last_id++;
		this.type = type;
		this.name = name;
		this.soldiers = new ArrayList<Soldier>(soldiers.length);
		this.subUnits = new ArrayList<OrganisationalUnit>(subUnits.length);
		for (OrganisationalUnit subUnit : subUnits) {
			if (subUnit == null) {
				throw new IllegalArgumentException("subUnit can't be null!");
			}
			this.subUnits.add(subUnit);
		}
		for (Soldier soldier : soldiers) {
			if (soldier == null) {
				throw new IllegalArgumentException("soldier can't be null!");
			}
			this.soldiers.add(soldier);
		}
	}
	
	/**Initialize a new unit from a unit row
	 * @param row The unit row
	 */
	public OrganisationalUnit(IRow row) {
		this(row.getCell(ID), row.getCell(COMMANDER_ID), row.getCell(TYPE), row.getCell(NAME));
		this.commander = null;
		this.id = row.getCell(ID).Value.getInteger();
		this.name = row.getCell(NAME).Value.getString();
		this.soldiers = new ArrayList<Soldier>();
		this.subUnits = new ArrayList<OrganisationalUnit>();
		this.type = row.getCell(TYPE).Value.getString();
	}
	
	public static IColumn[] getStaticColumns() {
		return new IColumn[] {ID, COMMANDER_ID, TYPE, NAME};
	}
	
	/**Get the number of soldiers in this unit
	 * @param recursive Whether to count the soldiers in subunits
	 * @return The number of soldiers in this unit
	 */
	public int getNumberOfSoldiers(boolean recursive) {
		if (!recursive) {
			return !soldiers.isEmpty() ? soldiers.size() : 0;
		}
		int size = !soldiers.isEmpty() ? soldiers.size() : 0;
		Stack<OrganisationalUnit> stack = new Stack<OrganisationalUnit>();
		stack.push(this);
		while (!stack.isEmpty()) {
			OrganisationalUnit next = stack.pop();
			if (!next.soldiers.isEmpty()) {
				size += next.soldiers.size();
			}
			if (!next.subUnits.isEmpty()) {
				for (OrganisationalUnit unit : next.subUnits) {
					stack.push(unit);
				}
			}
		}
		return size;
	}
	
	/**Get the number of sub-units in this unit
	 * @param recursive Whether to also count units in subunits
	 * @return The number of sub-units in this unit
	 */
	public int getNumberofUnits(boolean recursive) {
		if (!subUnits.isEmpty()) {
			return 0;
		}
		if (!recursive) {
			return subUnits.size();
		}
		int size = subUnits.size();
		Stack<OrganisationalUnit> stack = new Stack<OrganisationalUnit>();
		stack.push(this);
		while (!stack.isEmpty()) {
			OrganisationalUnit next = stack.pop();
			if (!next.subUnits.isEmpty()) {
				continue;
			}
			size += next.subUnits.size();
			for (OrganisationalUnit unit : next.subUnits) {
				stack.push(unit);
			}
		}
		return size;
	}
	
	/**Check if this unit has sub-units
	 * @return True if there are sub-units, otherwise false
	 */
	public boolean hasSubUnits() {
		return subUnits.isEmpty();
	}
	
	/**Check if this unit has soldiers
	 * @return True if there are soldiers, otherwise false
	 */
	public boolean hasSoldiers() {
		return soldiers.isEmpty();
	}
	
	/**Get a soldier based on index
	 * @param index The index of the soldier
	 * @return A soldier with the given index
	 */
	public Soldier getSoldier(int index) {
		return soldiers.get(index);
	}
	
	/**Get a soldier with a specified id
	 * @param id The id of the soldier to search
	 * @param recursive Whether to also search in sub-units
	 * @return The soldier with the given id
	 * @throws IllegalArgumentException If no soldier with the given id is found in this unit
	 */
	public Soldier getSoldier(int id, boolean recursive) 
	throws IllegalArgumentException {
		if (!recursive) {
			if (soldiers == null) {
				throw new IllegalArgumentException("No soldier with id('" + id + "') in '" + name + "'");
			}
			for (Soldier soldier : soldiers) {
				if (soldier.getID() == id) {
					return soldier;
				}
			}
		}
		
		Stack<OrganisationalUnit> units = new Stack<OrganisationalUnit>();
		units.push(this);
		while(!units.isEmpty()) {
			OrganisationalUnit unit = units.pop();
			for (Soldier soldier : unit.soldiers) {
				if (soldier.getID() == id) {
					return soldier;
				} 
			}
			for(OrganisationalUnit subUnit : unit.subUnits) {
				units.push(subUnit);
			}
		}
		throw new IllegalArgumentException("No soldier with id('" + id + "') in '" + name + "'");
	}
	
	/**Get a subunit based on index
	 * @param index The index of the subunit
	 * @return The subunit at the given index
	 */
	public OrganisationalUnit getOU(int index) {
		return subUnits.get(index);
	}
	
	/**Get this unit's commander
	 * @return The commander of this unit
	 */
	public Soldier getCommander() {
		return this.commander;
	}
	
	@Override
	public IColumn[] getColumns() {
		return getStaticColumns();
	}
	
	@Override
	public String toString() {
		return type + " " + name;
	}
}
