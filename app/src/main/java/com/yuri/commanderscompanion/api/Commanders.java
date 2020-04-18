package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Represents a commanders table*/
public class Commanders extends SinglePrimaryKeyCacheTable<Commander> {
	/**The table's name*/
	public static final String NAME = "Commanders";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(Commander.ID);
	/**The table's columns*/
	public static final IColumn[] COLUMNS = Commander.getStaticColumns();

	/**Initialize a new commanders table*/
	protected Commanders() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**Initialize a new commanders table with a helper
	 * @param helper The helper for the database
	 */
	public Commanders(IDatabaseHelper helper) {
		super(helper, Commanders::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static Commander convert(IRow row){
		return new Commander(row);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Commanders clone() {
		Commanders commanders = new Commanders();
		commanders.helper = helper.clone();
		commanders.converter = converter;

		for (Commander commander : rows){
			Commander cloned = commander.clone();
			commanders.add(cloned);
		}

		return commanders;
	}
}
