package com.yuri.commanderscompanion.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import dbAPI.Column;
import dbAPI.Constraint;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.DatabaseValue;
import dbAPI.ForeignKeyConstraint;
import dbAPI.IColumn;
import dbAPI.IRow;
import dbAPI.SingularPrimaryKey;

/**Represents a log and a row in the database*/
public class Log extends SQLiteRow {
	/**The column of this log's id*/
	public static final IColumn ID = new Column("id", 0, DatabaseDataType.INTEGER,
			Constraint.BASIC_PRIMARY_KEY_CONSTRAINT/*, Constraint.AUTO_INCREMENT*/);
	/**The column of this log's log type id*/
	public static final IColumn LOG_TYPE_ID = new Column("log_type_id", 1, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("log_type_id", LogTypes.NAME + '(' + LogType.LOG_TYPE_ID.getName() + ')'),
			Constraint.NOT_NULL);
	/**The column of this log's unit id*/
	/*public static final IColumn UNIT_ID = new Column("unit_id", 2, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("unit_id", OrganisationalUnits.NAME + '(' + OrganisationalUnit.ID.getName() + ')'),
			Constraint.NOT_NULL);*/
	/**The column of this log's id*/
	public static final IColumn DATE = new Column("date", 2, DatabaseDataType.STRING,
			Constraint.NOT_NULL);
	
	/**The last row's id*/
	protected static int last_id = 1;
	/**This row's id*/
	protected int id;
	/**This row's type*/
	protected LogType type;
	/**The unit that this log is attached to*/
	protected OrganisationalUnit unit;
	/**The date and time of this log*/
	protected Date time;
	/**This row's entries*/
	public ArrayList<LogEntry> entries;
	

	/**Initialize a new log with cells
	 * @param cells The cells
	 */
	protected Log(final DatabaseCell... cells) {super(cells);}
	
	/**Initialize a new log with a type, unit, and time
	 * @param type This log's type
	 * @param time The time of this log
	 */
	public Log(final LogType type/*, OrganisationalUnit unit*/, final Date time) {
		this(
			new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
			new DatabaseCell(LOG_TYPE_ID, type.id, DatabaseDataType.INTEGER),
			//new DatabaseCell(UNIT_ID, unit.id, DatabaseDataType.INTEGER),
			new DatabaseCell(DATE, SQLiteDatabaseHelper.SIMPLE_DATETIME_FORMATTER.format(time),
					DatabaseDataType.STRING)
		);
		this.id = last_id++;
		this.entries = new ArrayList<LogEntry>();
		this.time = time;
		this.type = type;
		this.unit = type.unit;
		//this.unit.logs.add(this);
	}
	
	/**Initialize a new log from a log row
	 * @param row The log row
	 */
	public Log(IRow row) {
		//this(row.getCell(ID), row.getCell(LOG_TYPE_ID), row.getCell(UNIT_ID), row.getCell(DATE));
		this(GeneralHelper.getCells(row, Logs.COLUMNS));
		DatabaseValue temp;
		if ((temp = row.getCell(ID).Value).Value == null){
			this.id = last_id++;
		} else {
			this.id = temp.getInt();
			if (id > last_id) {
				last_id = id;
			}
		}
		try {
			this.time = SQLiteDatabaseHelper.SIMPLE_DATETIME_FORMATTER.parse(row.getCell(DATE).Value.getString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.type = Database.LOG_TYPES.getRow(new SingularPrimaryKey(row.getCell(LOG_TYPE_ID)));
		//this.unit = Database.UNITS.getRow(new SingularPrimaryKey(row.getCell(UNIT_ID)));
		this.unit = this.type.unit;
		this.unit.logs.add(this);
		this.entries = new ArrayList<LogEntry>();
	}

	/**Get all the row's columns
	 * @return The columns
	 */
	public static IColumn[] getStaticColumns() {
		return new IColumn[] {ID, LOG_TYPE_ID/*, UNIT_ID*/, DATE};
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
	public Date getTime() {
		return time;
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
	public Log clone() {
		return new Log(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) { return false; }
		Log log = (Log)obj;
		return log.id == id && log.time.equals(time) && log.type.equals(type) && log.unit.equals(unit);
	}
	
	@Override
	public String toString() {
		return time + " : Log No." + id + " of type " + type;
	}
}
