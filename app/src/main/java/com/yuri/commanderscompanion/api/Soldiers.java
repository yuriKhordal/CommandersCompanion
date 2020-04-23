package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Represents a soldiers Table*/
public class Soldiers extends SinglePrimaryKeyCacheTable<Soldier> {
	/**The name of the table in the database*/
	public static final String NAME = "Soldiers";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(Soldier.ID);
	/**All of the columns in the table*/
	public static final IColumn[] COLUMNS = Soldier.getStaticColumns();

	/**Initialize the soldiers table*/
	protected Soldiers() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}
	
	/**Initialize the soldiers table with a helper
	 * @param helper The helper for the database
	 */
	public Soldiers(IDatabaseHelper helper) {
		super(helper, Soldiers::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static Soldier convert(IRow row){
		return new Soldier(row);
	}
	
	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Soldiers clone() {
		Soldiers soldiers = new Soldiers();
		soldiers.helper = helper.clone();
		soldiers.converter = converter;

		for (Soldier soldier : rows){
			Soldier cloned = soldier.clone();
			soldiers.addFromIRow(cloned);
		}

		return soldiers;
	}
}
