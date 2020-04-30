package com.yuri.commanderscompanion.api;

import dbAPI.Column;
import dbAPI.Constraint;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.IColumn;
import dbAPI.IRow;

/**Represents a sight and a database row*/
public class Sight extends SQLiteRow {
	/**The column of the serial of the sight*/
	public static final IColumn SERIAL = new Column("serial", 0,
			DatabaseDataType.INTEGER, Constraint.BASIC_PRIMARY_KEY_CONSTRAINT);
	/**The column of the catalog id of the sight*/
	public static final IColumn CATALOG_ID = new Column("catalogID", 1,
			DatabaseDataType.INTEGER, Constraint.NOT_NULL);
	/**The column of the type of the sight*/
	public static final IColumn TYPE = new Column("type", 2,
			DatabaseDataType.STRING, Constraint.NOT_NULL);

	/**The type of this sight*/
	private String type;
	/**The catalogID of this sight*/
	private int catalogID;
	/**The serial number of this sight*/
	private int serial;

	/**Initialize a new sight with a type, catalog id, and serial
	 * @param type The type of the sight
	 * @param catalogID The catalog id of the sight
	 * @param serial The serial of the sight
	 */
	public Sight(String type, int catalogID, int serial) {
		super(
				new DatabaseCell(SERIAL, serial, DatabaseDataType.INTEGER),
				new DatabaseCell(CATALOG_ID, catalogID, DatabaseDataType.INTEGER),
				new DatabaseCell(TYPE, type, DatabaseDataType.STRING)
		);
		this.type = type;
		this.catalogID = catalogID;
		this.serial = serial;
	}
	
	/**Initialize a new sight from a sight row
	 * @param row The row of the sight
	 */
	public Sight(IRow row) {
//		super(row.getCell(SERIAL), row.getCell(CATALOG_ID), row.getCell(TYPE));
		super(GeneralHelper.getCells(row, Sights.COLUMNS));
		this.serial = row.getCell(SERIAL).Value.getInt();
		this.catalogID = row.getCell(CATALOG_ID).Value.getInt();
		this.type = row.getCell(TYPE).Value.getString();
	}
	
	/**Get all the columns of the row
	 * @return The columns of the row
	 */
	public static IColumn[] getStaticColumns() {
		return new IColumn[] {SERIAL, CATALOG_ID, TYPE};
	}

	/**Get this sight's type
	 * @return The type of this sight
	 */
	public String getType() {
		return type;
	}

	/**Get this sight's catalog id
	 * @return The catalog id of this sight
	 */
	public int getCatalogID() {
		return catalogID;
	}

	/**Get this sight's serial
	 * @return The serial of this sight
	 */
	public int getSerial() {
		return serial;
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
	public Sight clone() {
		//return new Sight(type, catalogID, serial);
		return new Sight(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (!super.equals(obj)) { return false; }
		return equals((Sight)obj);
	}

	/**Equals for {@link Sight}
	 * @see Object#equals(Object)
	 */
	public boolean equals(Sight s){
		if (this == s) { return true; }
		if (s == null) { return false; }

		return s.type == type && s.catalogID == catalogID && s.serial == serial;
	}

	@Override
	public String toString() {
		return "Type:\t" + type + "\n" +
				"Serial:\t" + serial + "\n" +
				"Catalog ID:\t" + catalogID + "\n";
	}

}
