package com.yuri.commanderscompanion.api;

import dbAPI.Column;
import dbAPI.Constraint;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.DatabaseValue;
import dbAPI.ForeignKeyConstraint;
import dbAPI.IColumn;
import dbAPI.IRow;
import dbAPI.Row;
import dbAPI.SingularPrimaryKey;

/**Represents a note of either a soldier or a unit and a row in the database*/
public class Note extends Row {
	/**A column of the note's id*/
	public static final IColumn ID = new Column("id", 0, DatabaseDataType.INTEGER,
			Constraint.BASIC_PRIMARY_KEY_CONSTRAINT/*, Constraint.AUTO_INCREMENT*/);
	/**A column of the note's type id*/
	public static final IColumn TYPE_ID = new Column("type_id", 1, DatabaseDataType.INTEGER,
			new ForeignKeyConstraint("type_id", NoteTypes.NAME + '(' + NoteType.ID.getName() + ')'),
			Constraint.NOT_NULL);
	/**A column of the note's message's head*/
	public static final IColumn HEAD = new Column("head", 2, DatabaseDataType.STRING);
	/**A column of the note's message's body*/
	public static final IColumn BODY = new Column("body", 3, DatabaseDataType.STRING);

	/**The id of the last note*/
	protected static int last_id = 1;
	
	/**The id of this note*/
	protected int id;
	/**The type of this note*/
	protected NoteType type;
	/**The head message of the note(optional), "" if absent*/
	protected String head;
	/**The body message of the note(optional), "" if absent*/
	protected String body;
	
	/**Initialize a new Note with cells
	 * @param cells The cells of the row
	 */
	protected Note(DatabaseCell...cells) { super(cells); }
	
	/**Initializes a new Note with a type and a message head and body
	 * @param type The type of the note
	 * @param head The head message of the note, "" for empty head
	 * @param body The body message of the note, "" for empty body
	 */
	public Note(NoteType type, String head, String body) {
		this(
				new DatabaseCell(ID, null, DatabaseDataType.INTEGER),
				new DatabaseCell(TYPE_ID, type.id, DatabaseDataType.INTEGER),
				new DatabaseCell(HEAD, head, DatabaseDataType.STRING),
				new DatabaseCell(BODY, body, DatabaseDataType.STRING)
		);
		id = last_id++;
		this.type = type;
		if (type.ofSoldier) {
			type.ownerS.notes.add(this);
		} else {
			type.ownerOU.notes.add(this);
		}
		this.head = head;
		this.body = body;
	}
	
	public Note(IRow row) {
//		this(row.getCell(ID), row.getCell(TYPE_ID), row.getCell(HEAD), row.getCell(BODY));
		this(GeneralHelper.getCells(row, Notes.COLUMNS));
		DatabaseValue temp;
		if ((temp = row.getCell(ID).Value).Value == null){
			id = last_id++;
		} else {
			id = temp.getInt();
			last_id = id > last_id ? id+1 : last_id;
		}
		type = Database.NOTE_TYPES.getRow(new SingularPrimaryKey(row.getCell(TYPE_ID)));
		if (type.ofSoldier) {
			type.ownerS.notes.add(this);
		} else {
			type.ownerOU.notes.add(this);
		}

		if ((temp = row.getCell(HEAD).Value).Value == null){
			head = "";
		} else {
			head = temp.getString();
		}

		if ((temp = row.getCell(BODY).Value).Value == null) {
			body = "";
		} else {
			body = temp.getString();
		}
	}

	/**Get the head of the message
	 * @return the head of the massage
	 */
	public String getHead() {
		return head;
	}

	/**Set the head of the message
	 * @param head the message's head to set
	 */
	public void setHead(String head) {
		this.head = head;
		setValue(HEAD, new DatabaseValue(head, DatabaseDataType.STRING));
	}

	/**Get the body of the message
	 * @return the body of the massage
	 */
	public String getBody() {
		return body;
	}

	/**Set the body of the message
	 * @param body the message's body to set
	 */
	public void setBody(String body) {
		this.body = body;
		setValue(BODY, new DatabaseValue(body, DatabaseDataType.STRING));
	}

	/**Get the id of this note
	 * @return the id of this note
	 */
	public int getId() {
		return id;
	}

	/**Get the type of this note
	 * @return the type of this note
	 */
	public NoteType getType() {
		return type;
	}
	
	public static IColumn[] getStaticColumns() {
		return new IColumn[] { ID, TYPE_ID, HEAD, BODY };
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
	public Note clone() {
		return new Note(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) { return false; }
		Note note = (Note)obj;
		return note.id == id && note.type.equals(type) && note.head == head && note.body == body;
	}
	
	@Override
	public String toString() {
		return  "Type:\t" + type + "\n" +
				"Head:\t" + head + "\n" +
				"Body:\t" + body;
	}
}
