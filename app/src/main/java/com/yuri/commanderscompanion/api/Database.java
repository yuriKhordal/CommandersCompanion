package com.yuri.commanderscompanion.api;

import dbAPI.AbstractDatabase;
import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IDatabaseReader;
import dbAPI.IRow;
import dbAPI.ITable;

/**Represents the database*/
public class Database {
	/**The helper for the database*/
	public static final IDatabaseHelper HELPER = null; //TODO: Replace with actual helper afterwards
	/**The sights table*/
	public static final Sights SIGHTS = new Sights(HELPER);
	/**The weapons table*/
	public static final Weapons WEAPONS = new Weapons(HELPER);
	/**The units table*/
	public static final OrganisationalUnits UNITS = new OrganisationalUnits(HELPER);
	/**The commanders table*/
	public static final Commanders COMMANDERS = new Commanders(HELPER);
	/**The soldiers table*/
	public static final Soldiers SOLDIERS = new Soldiers(HELPER);
	/**The equipment table*/
	public static final EquipmentTable EQUIPMENT = new EquipmentTable(HELPER);
	/**The notices table*/
	public static final Notices NOTICES = new Notices(HELPER);
	/**The note types table*/
	public static final NoteTypes NOTE_TYPES = new NoteTypes(HELPER);
	/**The notes table*/
	public static final Notes NOTES = new Notes(HELPER);
	/**The log types table*/
	public static final LogTypes LOG_TYPES = new LogTypes(HELPER);
	/**The logs table*/
	public static final Logs LOGS = new Logs(HELPER);
	/**The log entries table*/
	public static final LogEntries ENTRIES = new LogEntries(HELPER);
	/**All the tables*/
	public static final ITable[] TABLES = new ITable[] {
			SIGHTS, WEAPONS, UNITS, COMMANDERS, SOLDIERS, EQUIPMENT, NOTICES, NOTE_TYPES, NOTES, LOG_TYPES, LOGS, ENTRIES};
	
	/**A database object*/
	protected static NestedDatabase db;
	
	/**Get the database object
	 * @return The database object
	 */
	public static NestedDatabase getDatabase() {
		if (db == null) {
			Database temp = new Database();
			db = temp.new NestedDatabase();
		}
		return db;
	}
	
	/**Get all the tables in the database
	 * @return All the tables
	 */
	public static ITable[] getTables() {
		return TABLES;
	}
	
	/**Open the database*/
	public static void open(){
		getDatabase().open();
    }
	
	/**Check if the database is open
	 * @return True if the database is open, otherwise: False
	 */
	public static boolean isOpen(){
		return getDatabase().isOpen();
    }

	/**Create the specified table in the database
	 * @param table The table to create
	 */
	public static void create(final ITable table){
		getDatabase().create(table);
	}

	/**Insert a specified row in a specified table
	 * @param table The table in which to insert the row
	 * @param row The row to insert
	 */
	public static void insert(final ITable table, final IRow row){
		getDatabase().insert(table, row);
    }

	/**Insert specified rows in a specified table
	 * @param table The table in which to insert the row
	 * @param rows The rows to insert
	 */
	public static void insert(final ITable table, final IRow[] rows){
		getDatabase().insert(table, rows);
    }

	/**Update a specified table with values from a specified row
	 * @param table The table which is updated
	 * @param row The row to take values from
	 */
	public static void update(final ITable table, final IRow row){
		getDatabase().update(table, row);
    }
	
	/**Update a specified table with values from a specified row with a condition
	 * @param table The table which is updated
	 * @param row The row to take values from
	 * @param whereCondition The condition in sql format(without 'WHERE')
	 */
	public static void update(final ITable table, final IRow row, String whereCondition){
		getDatabase().update(table, row, whereCondition);
    }

	/**Select all the rows from a specified table and read the specified columns
	 * @param table The table from which to select
	 * @param columns The columns which to select
	 * @return A reader with the selects results
	 */
	public static IDatabaseReader select(final ITable table, final IColumn[] columns){
		return getDatabase().select(table, columns);
    }
	
	/**Select rows in a specified table and read the specified columns where the condition is met 
	 * @param table The table from which to select
	 * @param columns The columns which to select
	 * @param whereCondition The condition in sql format(without 'WHERE')
	 * @return A reader with the selects results
	 */
	public static IDatabaseReader select(final ITable table, final IColumn[] columns, String whereCondition){
		return getDatabase().select(table, columns, whereCondition);
    }
	
	/**Select all the rows from specified tables and read the specified columns
	 * @param tables The tables from which to select
	 * @param columns The columns which to select
	 * @return A reader with the selects results
	 */
	public static IDatabaseReader select(final ITable[] tables, final IColumn[] columns){
		return getDatabase().select(tables, columns);
    }
	
	/**Select rows in specified tables and read the specified columns where the condition is met 
	 * @param tables The tables from which to select
	 * @param columns The columns which to select
	 * @param whereCondition The condition in sql format(without 'WHERE')
	 * @return A reader with the selects results
	 */
	public static IDatabaseReader select(final ITable[] tables, final IColumn[] columns, String whereCondition){
		return getDatabase().select(tables, columns, whereCondition);
    }
	
	/**Select everything from the specified table
	 * @param table The table from which to select
	 * @return A reader with the selects results
	 */
	public static IDatabaseReader selectAll(final ITable table){
		return getDatabase().selectAll(table);
    }
	
	/**Select everything from the specified table where the condition is met
	 * @param table The table from which to select
	 * @param whereCondition The condition in sql format(without 'WHERE')
	 * @return A reader with the selects results
	 */
	public static IDatabaseReader selectAll(final ITable table, String whereCondition){
		return getDatabase().selectAll(table, whereCondition);
    }
	
	/**Select everything from the specified tables
	 * @param tables The tables from which to select
	 * @return A reader with the selects results
	 */
	public static IDatabaseReader selectAll(final ITable[] tables){
		return getDatabase().selectAll(tables);
    }
	
	/**Select everything from the specified tables where the condition is met
	 * @param tables The tables from which to select
	 * @param whereCondition The condition in sql format(without 'WHERE')
	 * @return A reader with the selects results
	 */
	public static IDatabaseReader selectAll(final ITable[] tables, String whereCondition){
		return getDatabase().selectAll(tables, whereCondition);
    }

	/**Delete from the specified table where the condition is met
	 * @param table The table from which to delete
	 * @param whereCondition The condition in sql format(without 'WHERE')
	 */
	public static void delete(final ITable table, String whereCondition){
		getDatabase().delete(table, whereCondition);
    }

	/**Drop the specified table
	 * @param table The table to drop
	 */
	public static void drop(final ITable table){
		getDatabase().drop(table);
	}

	/**Close the database*/
	public static void close(){
		getDatabase().close();
    }
	
	/**Represents the non-static database from the API*/
	protected class NestedDatabase extends AbstractDatabase{
		/**Initialize a new database*/
		public NestedDatabase() {
			super(HELPER, TABLES);
		}
		
		@Override
		public ITable[] getTables() {
			return TABLES;
		}

		@Override
		public ITable getTable(String name) {
			switch(name) {
				case Sights.NAME:				return SIGHTS;
				case Weapons.NAME:				return WEAPONS;
				case OrganisationalUnits.NAME:	return UNITS;
				case Commanders.NAME:			return COMMANDERS;
				case Soldiers.NAME:				return SOLDIERS;
				case EquipmentTable.NAME:		return EQUIPMENT;
				case Notices.NAME:				return NOTICES;
				case NoteTypes.NAME:			return NOTE_TYPES;
				case Notes.NAME:				return NOTES;
				case LogTypes.NAME:				return LOG_TYPES;
				case Logs.NAME:					return LOGS;
				case LogEntries.NAME:			return ENTRIES;
				default:
					throw new IllegalArgumentException("No table with the name '" + name + "' in the database!");
			}
		}
	}
}
