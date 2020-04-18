package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Represents a units table*/
public class OrganisationalUnits extends SinglePrimaryKeyCacheTable<OrganisationalUnit> {
	/**The name of this table*/
	public static final String NAME = "OrganisationalUnits";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(OrganisationalUnit.ID);
	/**All the columns in the table*/
	public static final IColumn[] COLUMNS = OrganisationalUnit.getStaticColumns();

	/**Initialize a new units table*/
	protected OrganisationalUnits() {
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}
	
	/**Initialize a new units table with a helper
	 * @param helper The helper for the database
	 */
	public OrganisationalUnits(IDatabaseHelper helper) {
		super(helper, OrganisationalUnits::convert, NAME, PRIMARY_KEY_CONSTRAINT, null ,null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static OrganisationalUnit convert(IRow row){
		return new OrganisationalUnit(row);
	}

	@Override
	public String getName() {
		return NAME;
	}
}
