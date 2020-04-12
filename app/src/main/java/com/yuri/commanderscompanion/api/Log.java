/**
 * 
 */
package com.yuri.commanderscompanion.api;

import java.time.LocalDateTime;
import java.util.ArrayList;

import dbAPI.Column;
import dbAPI.Constraint;
import dbAPI.ConstraintsEnum;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.IColumn;
import dbAPI.IRow;
import dbAPI.Row;

/**Represents a log and a row in the database*/
public class Log extends Row {
	/**The column of this log's id*/
	public static final IColumn ID = new Column("id", 0, DatabaseDataType.INTEGER,
			ConstraintsEnum.PRIMERY_KEY, ConstraintsEnum.AUTO_INCREMENT);
	/**The column of this log's log type id*/
	public static final IColumn LOG_TYPE_ID = new Column("log_type_id", 1, DatabaseDataType.INTEGER,
			new Constraint(ConstraintsEnum.FOREIGN_KEY, null).setInfo(LogTypes.NAME),
			new Constraint(ConstraintsEnum.NOT_NULL, null));
	/**The column of this log's unit id*/
	public static final IColumn UNIT_ID = new Column("unit_id", 2, DatabaseDataType.INTEGER,
			new Constraint(ConstraintsEnum.FOREIGN_KEY, null).setInfo(OrganisationalUnits.NAME),
			new Constraint(ConstraintsEnum.NOT_NULL, null));
	/**The column of this log's id*/
	public static final IColumn DATE = new Column("date", 3, DatabaseDataType.DATETIME,
			ConstraintsEnum.NOT_NULL);
	
	/**The last row's id*/
	protected static int last_id = 0;
	/**This row's id*/
	protected int id;
	/**This row's type*/
	protected LogType type;
	/**The unit that this log is attached to*/
	protected OrganisationalUnit unit;
	/**The date and time of this log*/
	protected LocalDateTime time;
	/**This row's entries*/
	public ArrayList<LogEntry> entries;
	

	/**Initialize a new log with cells
	 * @param cells The cells
	 */
	protected Log(final DatabaseCell... cells) {super(cells);}
	
	/**Initialize a new log with a type, unit, and time
	 * @param type This log's type
	 * @param unit This log's unit
	 * @param time The time of this log
	 */
	public Log(final LogType type, OrganisationalUnit unit, final LocalDateTime time) {
		this(
			new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
			new DatabaseCell(LOG_TYPE_ID, type.id, DatabaseDataType.INTEGER),
			new DatabaseCell(UNIT_ID, unit.id, DatabaseDataType.INTEGER),
			new DatabaseCell(DATE, time, DatabaseDataType.DATETIME)
		);
		this.id = last_id++;
		this.entries = new ArrayList<LogEntry>();
		this.time = time;
		this.type = type;
		this.unit = unit;
		this.unit.logs.add(this);
	}
	
	/**Initialize a new log from a log row
	 * @param row The log row
	 */
	public Log(IRow row) {
		this(row.getCell(ID), row.getCell(LOG_TYPE_ID), row.getCell(UNIT_ID), row.getCell(DATE));
		this.id = row.getCell(ID).Value.getInteger();
		if (id > last_id) {
			last_id = id;
		}
		this.time = row.getCell(DATE).Value.getDateTime();
		this.type = Database.LOG_TYPES.getByPrimeryKey(row.getCell(LOG_TYPE_ID).Value);
		this.unit = Database.UNITS.getByPrimeryKey(row.getCell(UNIT_ID).Value);
		this.unit.logs.add(this);
		this.entries = new ArrayList<LogEntry>();
	}

	/**Get all the row's columns
	 * @return The columns
	 */
	public static IColumn[] getStaticColumns() {
		return new IColumn[] {ID, LOG_TYPE_ID, UNIT_ID, DATE};
	}
	
	/**Get this log's id
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**Get this log's type
	 * @return the type
	 */
	public LogType getType() {
		return type;
	}

	/**Get this log's unit
	 * @return the unit
	 */
	public OrganisationalUnit getUnit() {
		return unit;
	}

	/**Get this log's time
	 * @return the time
	 */
	public LocalDateTime getTime() {
		return time;
	}

	@Override
	public IColumn[] getColumns() {
		return getStaticColumns();
	}
	
	@Override
	public String toString() {
		return time + " : Log No." + id + " of type " + type;
	}
}
