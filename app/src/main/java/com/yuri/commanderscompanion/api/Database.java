package com.yuri.commanderscompanion.api;

import dbAPI.AbstractDatabase;
import dbAPI.ITable;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Represents the database, */
public class Database extends AbstractDatabase {
    /**The name of the database file*/
    public static final String FILE_NAME = "soldiers.db";
    /**The path to the database file*/
	public static String PATH;
	/**The helper for the database*/
	public static SQLiteDatabaseHelper HELPER;
	/**The updates of te database*/
	public static Updates UPDATES;
	/**The sights table*/
	public static Sights SIGHTS;
	/**The weapons table*/
	public static Weapons WEAPONS;
	/**The units table*/
	public static OrganisationalUnits UNITS;
	/**The Unit parents*/
	public static UnitParents UNIT_PARENTS;
	/**The commanders table*/
	public static Commanders COMMANDERS;
	/**The soldiers table*/
	public static Soldiers SOLDIERS;
	/**The equipment table*/
	public static EquipmentTable EQUIPMENT;
	/**The notices table*/
	public static Notices NOTICES;
	/**The note types table*/
	public static NoteTypes NOTE_TYPES;
	/**The notes table*/
	public static Notes NOTES;
	/**The log types table*/
	public static LogTypes LOG_TYPES;
	/**The logs table*/
	public static Logs LOGS;
	/**The log entries table*/
	public static LogEntries ENTRIES;
	/**All the tables*/
	public static SinglePrimaryKeyCacheTable[] TABLES;

	/**A database object*/
	public static Database db;

	// ---- static methods ----

    /**Initialize the database*/
    public static void init(String path){
		PATH = path;
		HELPER = new SQLiteDatabaseHelper(PATH);
		UPDATES = new Updates(HELPER);

		SIGHTS = new Sights(HELPER);
		WEAPONS = new Weapons(HELPER);
		UNITS = new OrganisationalUnits(HELPER);
		UNIT_PARENTS = new UnitParents(HELPER);
		COMMANDERS = new Commanders(HELPER);
		SOLDIERS = new Soldiers(HELPER);
		EQUIPMENT = new EquipmentTable(HELPER);
		NOTICES = new Notices(HELPER);
		NOTE_TYPES = new NoteTypes(HELPER);
		NOTES = new Notes(HELPER);
		LOG_TYPES = new LogTypes(HELPER);
		LOGS = new Logs(HELPER);
		ENTRIES = new LogEntries(HELPER);
		TABLES = new SinglePrimaryKeyCacheTable[]{SIGHTS, WEAPONS, UNITS, UNIT_PARENTS, COMMANDERS, SOLDIERS, EQUIPMENT, NOTICES,
				NOTE_TYPES, NOTES, LOG_TYPES, LOGS, ENTRIES};

        db = new Database();
    }

	// ---- Database implementation ----

	/**Initialize a new database*/
	protected Database() { super(HELPER, TABLES); }

	@Override
	public ITable[] getTables() {
		return TABLES;
	}

	@Override
	public Database clone() {
		return new Database();
	}

	@Override
	public ITable getTable(String name) {
		switch (name) {
			case Sights.NAME:
				return SIGHTS;
			case Weapons.NAME:
				return WEAPONS;
			case OrganisationalUnits.NAME:
				return UNITS;
			case UnitParents.NAME:
				return UNIT_PARENTS;
			/*case Commanders.NAME:
				return COMMANDERS;*/
			case Soldiers.NAME:
				return SOLDIERS;
			case EquipmentTable.NAME:
				return EQUIPMENT;
			case Notices.NAME:
				return NOTICES;
			case NoteTypes.NAME:
				return NOTE_TYPES;
			case Notes.NAME:
				return NOTES;
			case LogTypes.NAME:
				return LOG_TYPES;
			case Logs.NAME:
				return LOGS;
			case LogEntries.NAME:
				return ENTRIES;
			default:
				throw new IllegalArgumentException("No table with the name '" + name + "' in the database!");
		}
	}
}
