package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Represents a sights Table*/
public class Sights extends SinglePrimaryKeyCacheTable<Sight> {
	/**The name of the table in the database*/
	public static final String NAME = "Sights";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(Sight.SERIAL);
	/**All of the columns in the table*/
	public static final IColumn[] COLUMNS = Sight.getStaticColumns();

	private static final IColumn[] NON_PRIMARY_COLUMNS = {Sight.CATALOG_ID, Sight.TYPE};

	/**Initialize a new sights table*/
	protected Sights() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**Initialize a new sights table with a helper
	 * @param helper The helper for the database
	 */
	public Sights(IDatabaseHelper helper) {
		super(helper, Sights::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static Sight convert(IRow row){
		return new Sight(row);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Sights clone() {
		Sights sights = new Sights();
		sights.helper = helper.clone();
		sights.converter = converter;

		for (Sight sight : rows){
			Sight cloned = sight.clone();
			sights.addFromIRow(cloned);
		}

		return sights;
	}
}
