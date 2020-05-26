package com.yuri.commanderscompanion.api;

import java.text.ParseException;
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

/**Represents a disciplinary notice and a row in a table*/
public class Notice extends SQLiteRow {
	/**The column of the id of the notice*/
	public static final IColumn ID = new Column("id", 0, DatabaseDataType.INTEGER,
			/*Constraint.AUTO_INCREMENT, */Constraint.BASIC_PRIMARY_KEY_CONSTRAINT);
	/**The column of the id of the notice*/
	public static final IColumn SOLDIER_ID = new Column("soldier_id", 1, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("soldier_id", Soldiers.NAME + '(' + Soldier.ID.getName() + ')'),
			Constraint.NOT_NULL);
	/**The column of the time of the notice*/
	public static final IColumn DATE = new Column("date", 2, DatabaseDataType.STRING,
			Constraint.NOT_NULL);
	/**The column of the summary of the act*/
	public static final IColumn SUMMARY = new Column("summary", 3, DatabaseDataType.STRING,
			Constraint.NOT_NULL);
	/**The column of the punishment*/
	public static final IColumn PUNISHMENT = new Column("punishment", 4, DatabaseDataType.STRING);
	
	/**The id of the last notice*/
	protected static int last_id = 1;
	
	/**The id of this notice*/
	protected int id;
	/**The soldier that the notice was given to*/
	protected Soldier soldier;
	/**The date of this notice*/
	protected Date date;
	/**A summary of what the soldier did*/
	protected String summary;
	/**The punishment*/
	protected String punishment;

	/**Initialize a new notice with cells
	 * @param cells The cells of the row
	 */
	protected Notice(DatabaseCell... cells) {
		super(cells);
	}
	
	public Notice(Soldier soldier, Date date, String summary, String punishment) {
		this(
				new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(SOLDIER_ID, soldier.id, DatabaseDataType.INTEGER),
				new DatabaseCell(DATE, SQLiteDatabaseHelper.SIMPLE_DATETIME_FORMATTER.format(date),
						DatabaseDataType.STRING),
				new DatabaseCell(SUMMARY, summary, DatabaseDataType.STRING),
				new DatabaseCell(PUNISHMENT, punishment, DatabaseDataType.STRING)
		);
		this.id = last_id++;
		this.soldier = soldier;
		this.soldier.notices.add(this);
		this.date = date;
		this.summary = summary;
		this.punishment = punishment;
	}

	public Notice(IRow row) {
//		this(row.getCell(ID), row.getCell(SOLDIER_ID),row.getCell(DATE),row.getCell(SUMMARY),row.getCell(PUNISHMENT));
		this(GeneralHelper.getCells(row, Notices.COLUMNS));
		DatabaseValue temp;
		if ((temp = row.getCell(ID).Value).Value == null){
			this.id = last_id++;
		} else {
			this.id = temp.getInt();
			last_id = id > last_id ? id+1 : last_id;
		}
		this.soldier = Database.SOLDIERS.getRow(new SingularPrimaryKey(row.getCell(SOLDIER_ID)));
		this.soldier.notices.add(this);
		try {
			this.date = SQLiteDatabaseHelper.SIMPLE_DATETIME_FORMATTER.parse(row.getCell(DATE).Value.getString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.summary = row.getCell(SUMMARY).Value.getString();
		this.punishment = row.getCell(PUNISHMENT).Value.getString();
	}

	/**Get the date of this notice
	 * @return The date of this notice
	 */
	public Date getDate() {
		return date;
	}

	/**Set the date of this notice
	 * @param date The date to set
	 */
	public void setDate(Date date) {
		this.date = date;
		setValue(DATE, new DatabaseValue(SQLiteDatabaseHelper.SIMPLE_DATETIME_FORMATTER.format(date),
				DatabaseDataType.STRING));
	}

	/**Get the summary of what happened
	 * @return The summary
	 */
	public String getSummary() {
		return summary;
	}

	/**Set the summary of what happened
	 * @param summary The summary to set
	 */
	public void setSummary(String summary) {
		this.summary = summary;
		setValue(SUMMARY, new DatabaseValue(summary, DatabaseDataType.STRING));
	}

	/**Get the punishment
	 * @return The punishment
	 */
	public String getPunishment() {
		return punishment;
	}

	/**Set the punishment
	 * @param punishment The punishment to set
	 */
	public void setPunishment(String punishment) {
		this.punishment = punishment;
		setValue(PUNISHMENT, new DatabaseValue(punishment, DatabaseDataType.STRING));
	}

	/**Get the id of the notice
	 * @return The id
	 */
	public int getId() {
		return id;
	}

	/**Get the soldier that misbehaved
	 * @return The soldier
	 */
	public Soldier getSoldier() {
		return soldier;
	}
	
	public static IColumn[] getStaticColumns() {
		return new IColumn[] { ID, SOLDIER_ID, DATE, SUMMARY, PUNISHMENT };
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
	public Notice clone() {
		return new Notice(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) { return false; }
		Notice notice = (Notice)obj;
		return notice.id == id && notice.soldier.equals(soldier) && notice.date.equals(date) &&
				notice.summary == summary && notice.punishment == punishment;
	}
	
	@Override
	public String toString() {
		return  "Soldier:\t" + soldier + "\n" +
				"Date:\t" + date.toString() + "\n" +
				"Summary:\t" + summary + "\n" +
				"Punishment:\t" + punishment;
	}
}
