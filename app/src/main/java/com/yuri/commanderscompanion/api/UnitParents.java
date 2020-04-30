package com.yuri.commanderscompanion.api;

import dbAPI.Column;
import dbAPI.Constraint;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.ForeignKeyConstraint;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.Row;
import dbAPI.SingularPrimaryKey;

/**Represents a unit parents table*/
public class UnitParents extends SQLiteTable<SQLiteRow> {
    /**The name of this table*/
    public static final String NAME = "UnitParents";
    /**The primary key id column*/
    public static final Column UNIT_PARENT_ID = new Column("unit_parent_id", 0,
            DatabaseDataType.INTEGER, Constraint.BASIC_PRIMARY_KEY_CONSTRAINT);
    /**The unit id*/
    public static final Column UNIT_ID = new Column("unit_id", 1, DatabaseDataType.INTEGER,
            new ForeignKeyConstraint("unit_id", OrganisationalUnits.NAME +
                    '(' + OrganisationalUnit.ID.getName() + ')'), Constraint.NOT_NULL, Constraint.UNIQUE);
    public static final Column PARENT_ID = new Column("parent_id", 2, DatabaseDataType.INTEGER,
            new ForeignKeyConstraint("parent_id", OrganisationalUnits.NAME +
                    '(' + OrganisationalUnit.ID.getName() + ')'), Constraint.NOT_NULL);
    /**The primary key of the table*/
    public static final PrimaryKeyConstraint PRIMARY_KEY_CONSTRAINT = new PrimaryKeyConstraint(UNIT_PARENT_ID);
    /**All the columns in the table*/
    public static final Column[] COLUMNS = {UNIT_PARENT_ID, UNIT_ID, PARENT_ID};

    protected UnitParents(){
        super(NAME, PRIMARY_KEY_CONSTRAINT, null, null, COLUMNS);
    }

    public UnitParents(SQLiteDatabaseHelper helper){
        super(helper, UnitParents::convert, NAME, PRIMARY_KEY_CONSTRAINT, null,
                null, GeneralHelper.getNonPrimaryColumns(COLUMNS));
    }

    /**@see IRowConverter#convertFromIRow(IRow) */
    protected static SQLiteRow convert(IRow row){
        return new SQLiteRow(
                new DatabaseCell(UNIT_PARENT_ID, row.getValue(UNIT_PARENT_ID)),
                new DatabaseCell(UNIT_ID, row.getValue(UNIT_ID)),
                new DatabaseCell(PARENT_ID, row.getValue(PARENT_ID))
        );
    }

    public void add(OrganisationalUnit unit, OrganisationalUnit parent){
        Row row = new Row(
                new DatabaseCell(UNIT_PARENT_ID, null, DatabaseDataType.INTEGER),
                new DatabaseCell(UNIT_ID, unit.id, DatabaseDataType.INTEGER),
                new DatabaseCell(PARENT_ID, parent.id, DatabaseDataType.INTEGER)
        );
        insert(row);
    }

    @Override
    protected void addFromIRow(IRow row) {
        super.addFromIRow(row);
        OrganisationalUnit unit = Database.UNITS.getRow(new SingularPrimaryKey(
                UNIT_ID, row.getValue(UNIT_ID)));
        OrganisationalUnit parent = Database.UNITS.getRow(new SingularPrimaryKey(
                PARENT_ID, row.getValue(PARENT_ID)));
        parent.subUnits.add(unit);
    }

    @Override
    public String getName() {
        return NAME;
    }
}
