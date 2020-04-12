package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;

/**Represents a units table*/
public class OrganisationalUnits extends Table<OrganisationalUnit> {
	/**The name of this table*/
	public static final String NAME = "OrganisationalUnits";
	/**All the columns in the table*/
	public static final IColumn[] COLUMNS = OrganisationalUnit.getStaticColumns();
	
	/**Initialize a new table with a helper
	 * @param helper The helper for the database
	 */
	public OrganisationalUnits(IDatabaseHelper helper) {
		super(COLUMNS, helper);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	protected void addTFromIRow(IRow row) {
		OrganisationalUnit unit = new OrganisationalUnit(row);
		rows.add(unit);
		rowsMap.put(unit.id, unit);
	}
	
	@Override
	protected IColumn getPrimeryKey() {
		return OrganisationalUnit.ID;
	}

}
