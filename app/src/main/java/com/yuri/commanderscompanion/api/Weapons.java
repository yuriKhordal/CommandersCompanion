package com.yuri.commanderscompanion.api;

import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

/**Represents a weapons Table*/
public class Weapons extends SinglePrimaryKeyCacheTable<Weapon> {
	/**The name of the table in the database*/
	public static final String NAME = "Weapons";
	/**The primary key of the table*/
	public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(Weapon.SERIAL);
	/**All of the columns in the table*/
	public static final IColumn[] COLUMNS = Weapon.getStaticColumns();

	/**Initialise a new weapons table*/
	protected Weapons(){
		super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**Initialize a new weapons table with a helper
	 * @param helper The helper for the database
	 */
	public Weapons(IDatabaseHelper helper) {
		super(helper, Weapons::convert, NAME, PRIMARY_KEY_CONSTRAINT, null, null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
	}

	/**@see IRowConverter#convertFromIRow(IRow) */
	protected static Weapon convert(IRow row){
		return new Weapon(row);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Weapons clone() {
		Weapons weapons = new Weapons();
		weapons.helper = helper;
		weapons.converter = converter;

		for (Weapon weapon: rows){
			Weapon copy = weapon.clone();
			weapons.addFromIRow(copy);
		}
		return  weapons;
	}
}
