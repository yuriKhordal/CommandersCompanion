package com.yuri.commanderscompanion.api;

import java.time.LocalDateTime;

import dbAPI.Column;
import dbAPI.Constraint;
import dbAPI.ConstraintsEnum;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.DatabaseValue;
import dbAPI.IColumn;
import dbAPI.IRow;
import dbAPI.Row;

/**Represents a disciplinary notice and a row in a table*/
public class Notice extends Row {
	/**The column of the id of the notice*/
	public static final IColumn ID = new Column("id", 0, DatabaseDataType.INTEGER,
			ConstraintsEnum.AUTO_INCREMENT, ConstraintsEnum.PRIMERY_KEY);
	/**The column of the id of the notice*/
	public static final IColumn SOLDIER_ID = new Column("soldier_id", 1, DatabaseDataType.INTEGER,
			new Constraint(ConstraintsEnum.FOREIGN_KEY, null).setInfo(Soldiers.NAME),
			new Constraint(ConstraintsEnum.NOT_NULL, null));
	/**The column of the time of the notice*/
	public static final IColumn DATE = new Column("date", 2, DatabaseDataType.DATETIME,
			ConstraintsEnum.NOT_NULL);
	/**The column of the summary of the act*/
	public static final IColumn SUMMARY = new Column("summary", 3, DatabaseDataType.STRING,
			ConstraintsEnum.NOT_NULL);
	/**The column of the punishment*/
	public static final IColumn PUNISHMENT = new Column("punishment", 4, DatabaseDataType.STRING);
	
	/**The id of the last notice*/
	protected static int last_id = 0;
	
	/**The id of this notice*/
	protected int id;
	/**The soldier that the notice was given to*/
	protected Soldier soldier;
	/**The date of this notice*/
	protected LocalDateTime date;
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
	
	public Notice(Soldier soldier, LocalDateTime date, String summary, String punishment) {
		this(
				new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(SOLDIER_ID, soldier.id, DatabaseDataType.INTEGER),
				new DatabaseCell(DATE, date, DatabaseDataType.DATETIME),
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
		this(row.getCell(ID), row.getCell(SOLDIER_ID),row.getCell(DATE),row.getCell(SUMMARY),row.getCell(PUNISHMENT));
		this.id = row.getCell(ID).Value.getInteger();
		this.soldier = Database.SOLDIERS.getByPrimeryKey( row.getCell(SOLDIER_ID).Value );
		this.soldier.notices.add(this);
		this.date = row.getCell(DATE).Value.getDateTime();
		this.summary = row.getCell(SUMMARY).Value.getString();
		this.punishment = row.getCell(PUNISHMENT).Value.getString();
	}

	/**Get the date of this notice
	 * @return The date of this notice
	 */
	public LocalDateTime getDate() {
		return date;
	}

	/**Set the date of this notice
	 * @param date The date to set
	 */
	public void setDate(LocalDateTime date) {
		this.date = date;
		setValue(DATE, new DatabaseValue(date, DatabaseDataType.DATETIME));
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
	public IColumn[] getColumns() {
		return getStaticColumns();
	}
	
	@Override
	public String toString() {
		return  "Soldier:\t" + soldier + "\n" +
				"Date:\t" + date.toString() + "\n" +
				"Summary:\t" + summary + "\n" +
				"Punishment:\t" + punishment;
	}
}
