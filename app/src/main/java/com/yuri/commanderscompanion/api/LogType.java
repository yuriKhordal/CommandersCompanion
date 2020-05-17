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

/***Represents a type of log of soldiers and a row in the database*/
public class LogType extends SQLiteRow {
	/**The column of this log's id*/
	public static final IColumn LOG_TYPE_ID = new Column("log_type_id", 0, DatabaseDataType.INTEGER,
			Constraint.BASIC_PRIMARY_KEY_CONSTRAINT/*, Constraint.AUTO_INCREMENT*/);
	/**The column of the unit of this log*/
	public static final IColumn UNIT_ID = new Column("unit_id", 1, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("unit_id", OrganisationalUnits.NAME + '(' + OrganisationalUnit.ID.getName() + ')'),
			Constraint.NOT_NULL);
	/**The column of this log's name*/
	public static final IColumn NAME = new Column("name", 2, DatabaseDataType.STRING,
			Constraint.NOT_NULL);
	/**The column that determines whether to also include soldiers in subunits*/
	public static final IColumn RECURSIVE = new Column("recursive", 3, DatabaseDataType.INTEGER,
			Constraint.NOT_NULL);
	
	/**The id of the last row*/
	protected static int last_id = 1;
	
	/**The id of this log type*/
	protected int id;
	/**The unit of this log type*/
	protected OrganisationalUnit unit;
	/**The name of this log type*/
	protected String name;
	/**Whether to include soldiers in subunits in this log*/
	protected boolean recursive;

	/**Initialize a new log type with cells*/
	protected LogType(DatabaseCell... cells) { super(cells); }
	
	public LogType(OrganisationalUnit unit, String name, boolean recursive) {
		this(
			new DatabaseCell(LOG_TYPE_ID, null, DatabaseDataType.INTEGER),
			new DatabaseCell(UNIT_ID, unit.id, DatabaseDataType.INTEGER),
			new DatabaseCell(NAME, name, DatabaseDataType.STRING),
			new DatabaseCell(RECURSIVE, recursive ? 1 : 0, DatabaseDataType.INTEGER)
		);
		this.id = last_id++;
		this.name = name;
		this.recursive = recursive;
		this.unit = unit;
	}
	
	/**Initialize a new log type from a log type row
	 * @param row The log type row
	 */
	public LogType(IRow row) {
//		this(row.getCell(LOG_TYPE_ID), row.getCell(UNIT_ID), row.getCell(NAME), row.getCell(RECURSIVE));
		this(GeneralHelper.getCells(row, LogTypes.COLUMNS));
		DatabaseValue temp;
		if  ((temp = row.getCell(LOG_TYPE_ID).Value).Value == null){
			this.id = last_id++;
		} else {
			this.id = temp.getInt();
			if (id > last_id) {
				last_id = id;
			}
		}
		this.name = row.getCell(NAME).Value.getString();
		this.recursive = row.getCell(RECURSIVE).Value.getInt() == 1;
		this.unit = Database.UNITS.getRow(new SingularPrimaryKey(row.getCell(UNIT_ID)));
	}
	
	/**Get the name of this log type
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**Set the name of this log type
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		setValue(NAME, new DatabaseValue(name, DatabaseDataType.STRING));
	}

	/**Check whether to include subunits' soldiers
	 * @return True if yes, otherwise false
	 */
	public boolean isRecursive() {
		return recursive;
	}

	/** Set the value of whether to include subunits' soldiers
	 * @param recursive whether to include subunits' soldiers
	 */
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
		setValue(RECURSIVE, new DatabaseValue(recursive ? 1 : 0, DatabaseDataType.INTEGER));
	}

	/**Get this log's id
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**Get the unit of this log's soldiers
	 * @return the unit
	 */
	public OrganisationalUnit getUnit() {
		return unit;
	}

	/**Get all the columns of this row
	 * @return The columns of this row
	 */
	public static IColumn[] getStaticColumns() {
		return new IColumn[] {LOG_TYPE_ID, UNIT_ID, NAME, RECURSIVE};
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
	public LogType clone() {
		return new LogType(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) { return false; }
		LogType logType = (LogType)obj;
		return logType.id == id && logType.recursive == recursive && logType.name == name
				&& logType.unit.equals(unit);
	}
	
	@Override
	public String toString() {
		return id + "\t" + name + " log for " + unit;
	}

}
