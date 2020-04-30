package com.yuri.commanderscompanion.api;

import dbAPI.Column;
import dbAPI.Constraint;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.DatabaseValue;
import dbAPI.IColumn;
import dbAPI.IRow;
import dbAPI.SingularPrimaryKey;

/**Represents a type or a category of notes*/
public class NoteType extends SQLiteRow {
	/**The column of the note type id*/
	public static final IColumn ID = new Column("id", 0, DatabaseDataType.INTEGER,
			Constraint.BASIC_PRIMARY_KEY_CONSTRAINT/*, Constraint.AUTO_INCREMENT*/);
	/**The column of the note type's owner id*/
	public static final IColumn OWNER_ID = new Column("owner_id", 1, DatabaseDataType.INTEGER,
			Constraint.NOT_NULL);
	/**The column of whether the owner is a soldier(true) or a unit(false)*/
	public static final IColumn OF_SOLDIER = new Column("of_soldier", 2, DatabaseDataType.INTEGER,
			Constraint.NOT_NULL);
	/**The column of the note type name)*/
	public static final IColumn NAME = new Column("name", 3, DatabaseDataType.STRING,
			Constraint.NOT_NULL);

	/**The id of the last note type*/
	protected static int last_id = 1;
	
	/**The id of the note*/
	protected int id;
	/**Whether the note type belongs to a soldier or a unit*/
	protected boolean ofSoldier;
	/**The soldier the note type belongs to, null if the note type is a unit's*/
	protected Soldier ownerS;
	/**The unit the note type belongs to, null if the note type is a soldier's*/
	protected OrganisationalUnit ownerOU;
	/**The type of the note*/
	protected String name;
	
	/**Initialize a new note type with cells
	 * @param cells The cells of this row
	 */
	protected NoteType(DatabaseCell... cells) { super(cells); }
	
	/**Initialize a new Note type with an owner and a name
	 * @param owner The soldier that the note type is attached to
	 * @param name The name of the type of note
	 */
	public NoteType(Soldier owner, String name){
		this(
				new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(OWNER_ID, owner.id, DatabaseDataType.INTEGER),
				new DatabaseCell(OF_SOLDIER, "1", DatabaseDataType.INTEGER),
				new DatabaseCell(NAME, name, DatabaseDataType.STRING)
		);
		id = last_id++;
		this.ofSoldier = true;
		ownerS = owner;
		ownerOU = null;
		this.name = name;
	}
	
	/**Initialize a new Note type with an owner and a name
	 * @param owner The unit that the note type is attached to
	 * @param name The name of the type of note
	 */
	public NoteType(OrganisationalUnit owner, String name){
		this(
				new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(OWNER_ID, owner.id, DatabaseDataType.INTEGER),
				new DatabaseCell(OF_SOLDIER, "0", DatabaseDataType.INTEGER),
				new DatabaseCell(NAME, name, DatabaseDataType.STRING)
		);
		id = last_id++;
		this.ofSoldier = false;
		ownerS = null;
		ownerOU = owner;
		this.name = name;
	}
	
	public NoteType(IRow row) {
//		this(row.getCell(ID), row.getCell(OWNER_ID), row.getCell(OF_SOLDIER), row.getCell(NAME));
		this(GeneralHelper.getCells(row, NoteTypes.COLUMNS));
		DatabaseValue temp;
		if ((temp = row.getCell(ID).Value).Value == null){
			id = last_id++;
		} else {
			id = temp.getInt();
			last_id = id > last_id ? id+1 : last_id;
		}
		ofSoldier = row.getCell(OF_SOLDIER).Value.getInt() == 1;
		/*if (ofSoldier && ownerS.isCommander) {
			ownerS = Database.COMMANDERS.getRow(new SingularPrimaryKey(row.getCell(OWNER_ID)));
		} else */if (ofSoldier) {
			ownerS = Database.SOLDIERS.getRow(new SingularPrimaryKey(row.getCell(OWNER_ID)));
		} else {
			ownerOU = Database.UNITS.getRow(new SingularPrimaryKey(row.getCell(OWNER_ID)));
		}
		name = row.getCell(NAME).Value.getString();
	}
	
	/**Get the name of this note type
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**Set the name of this note type
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
		this.setValue(NAME, new DatabaseValue(name, DatabaseDataType.STRING));
	}

	/**Get the id of this note type
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**Check whether the note type is attached to a soldier
	 * @return True if the owner is a soldier, false if unit
	 */
	public boolean ofSoldier() {
		return ofSoldier;
	}

	/**Get the owner if it is a soldier
	 * @return the owner, if {@link #ofSoldier !ofSoldier} then null
	 */
	public Soldier getOwnerSoldier() {
		return ownerS;
	}

	/**Get the owner if it is a unit
	 * @return the owner if {@link #ofSoldier} then null
	 */
	public OrganisationalUnit getOwnerUnit() {
		return ownerOU;
	}

	/**Get the columns in the row
	 * @return All the columns in the row
	 */
	public static IColumn[] getStaticColumns() {
		return new IColumn[] {ID, OWNER_ID, OF_SOLDIER, NAME};
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
	public NoteType clone() {
		return new NoteType(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) { return false; }
		NoteType noteType = (NoteType)obj;
		if (noteType.ofSoldier != ofSoldier) { return false; }
		if (ofSoldier){
			if (noteType.ownerS.equals(ownerS)) { return false; }
		} else {
			if (noteType.ownerOU.equals(ownerOU)) { return false; }
		}
		return noteType.id == id && noteType.name == name;
	}
	
	@Override
	public String toString() {
		return  "Owner:\t" + (ofSoldier ? ownerS : ownerOU) + "\n" +
				"Name:\t" + name;
	}
}
