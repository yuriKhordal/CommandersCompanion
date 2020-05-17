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

/**Represents an entry in a log*/
public class LogEntry extends SQLiteRow {
	/**The column of this entry's id*/
	public static final IColumn ID = new Column("id", 0, DatabaseDataType.INTEGER,
			Constraint.BASIC_PRIMARY_KEY_CONSTRAINT/*, Constraint.AUTO_INCREMENT*/);
	/**The column of this entry's log id*/
	public static final IColumn LOG_ID = new Column("log_id", 1, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("log_id", Logs.NAME + '(' + Log.ID.getName() + ')'),
			Constraint.NOT_NULL);
	/**The column of this entry's id*/
	public static final IColumn SOLDIER_ID = new Column("soldier_id", 2, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("soldier_id", Soldiers.NAME + '(' + Soldier.ID.getName() + ')'),
			Constraint.NOT_NULL);
	/**The column of this entry's id*/
	public static final IColumn TEXT = new Column("text", 3, DatabaseDataType.STRING, Constraint.NOT_NULL);
	
	/**The id of the last row*/
	protected static int last_id = 1;
	/**This entry's id*/
	protected int id;
	/**The parent log of the entry*/
	protected Log log;
	/**The soldier this entry's about*/
	protected Soldier soldier;
	/**The entry's text*/
	protected String text;
	
	/**Initialize a new entry with cells
	 * @param cells The cells
	 */
	protected LogEntry(DatabaseCell... cells) { super(cells); }
	
	/**Initialize a new entry with a log, soldier and text
	 * @param log The container log for the entries
	 * @param soldier The soldier
	 * @param text The text of this entry
	 */
	public LogEntry(Log log, final Soldier soldier, final String text) {
		this(
			new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
			new DatabaseCell(LOG_ID, log.id, DatabaseDataType.INTEGER),
			new DatabaseCell(SOLDIER_ID, soldier.id, DatabaseDataType.INTEGER),
			new DatabaseCell(TEXT, text , DatabaseDataType.STRING)
		);
		
		this.id = last_id++;
		this.log = log;
		log.entries.add(this);
		this.soldier = soldier;
		this.text = text;
	}
	
	/**Initialize a new log from a row
	 * @param row The row
	 */
	public LogEntry(IRow row) {
//		this(row.getCell(ID), row.getCell(LOG_ID), row.getCell(SOLDIER_ID), row.getCell(TEXT));
		this(GeneralHelper.getCells(row, LogEntries.COLUMNS));
		DatabaseValue temp;
		if ((temp = row.getCell(ID).Value).Value == null){
			this.id = last_id++;
		} else {
			this.id = temp.getInt();
			if (id > last_id) {
				last_id = id;
			}
		}
		this.log = Database.LOGS.getRow(new SingularPrimaryKey(row.getCell(LOG_ID)));
		log.entries.add(this);
		this.soldier = Database.SOLDIERS.getRow(new SingularPrimaryKey(row.getCell(SOLDIER_ID)));
		this.text = row.getCell(TEXT).Value.getString();
	}
	
	/**Get this entry's text
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**Set this entry's text
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**Get this entry's id
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**Get the log of this entry's
	 * @return the log
	 */
	public Log getLog() {
		return log;
	}

	/**Get the soldier that the entry's about
	 * @return the soldier
	 */
	public Soldier getSoldier() {
		return soldier;
	}

	/**Get all the columns in the row
	 * @return All the columns
	 */
	public static IColumn[] getStaticColumns() {
		return new IColumn[] { ID, LOG_ID, SOLDIER_ID, TEXT };
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
	public LogEntry clone() {
		return new LogEntry(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) { return false; }
		LogEntry logEntry = (LogEntry)obj;
		return logEntry.id == id && logEntry.log.equals(log) && logEntry.soldier.equals(soldier)
				&& logEntry.text == text;
	}
	
	@Override
	public String toString() {
		return log.type.id + "-" + log.id + "-" + id + ":\t" + soldier + ":\t" + text;
	}
}
