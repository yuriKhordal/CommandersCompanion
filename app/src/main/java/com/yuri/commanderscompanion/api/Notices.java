package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Represents a notices table*/
public class Notices extends SinglePrimaryKeyCacheTable<Notice> {
	/**The name of the table*/
	public static final String NAME = "Notices";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(Notice.ID);
	/**All the columns in the table*/
	public static final IColumn[] COLUMNS = Notice.getStaticColumns();

	/**Initialize a new notices table*/
	protected Notices() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}
	
	/**Initialize a new notices table with a helper to the database
	 * @param helper A helper for the database
	 */
	public Notices(IDatabaseHelper helper) {
		super(helper, Notices::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static Notice convert(IRow row){
		return new Notice(row);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Notices clone() {
		Notices notices = new Notices();
		notices.helper = helper.clone();
		notices.converter = converter;

		for (Notice notice : rows){
			Notice cloned = notice.clone();
			notices.addFromIRow(cloned);
		}

		return notices;
	}
}
