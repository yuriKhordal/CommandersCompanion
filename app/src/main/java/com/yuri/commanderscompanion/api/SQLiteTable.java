package com.yuri.commanderscompanion.api;

import dbAPI.CheckConstraint;
import dbAPI.IColumn;
import dbAPI.IPrimaryKey;
import dbAPI.IRow;
import dbAPI.IRowConverter;
import dbAPI.IndexConstraint;
import dbAPI.PrimaryKeyConstraint;
import dbAPI.SinglePrimaryKeyCacheTable;

public class SQLiteTable<T extends IRow> extends SinglePrimaryKeyCacheTable<T> {

    protected T last_row_added;

    /**Initialize this table with a name, primary key/s, indices, and columns
     * @param name             The name of the table
     * @param pk               The primary key columns of this table
     * @param indices          The indices of this table
     * @param checkConstraints The table level check constraints
     * @param columns          An array of columns to put in this table, excluding the primary key/s
     */
    protected SQLiteTable(String name, PrimaryKeyConstraint pk, IndexConstraint[] indices, CheckConstraint[] checkConstraints, IColumn... columns) {
        super(name, pk, indices, checkConstraints, columns);
    }

    /**Initialize this table with a helper, a converting method, a name,<br>
     * primary key columns, indices, and the rest of the columns
     *
     * @param helper           The helper for the database
     * @param converter        A listener object for converting {@link IRow} rows to this table's rows
     * @param name             The name of the table
     * @param pk               The primary key columns of this table
     * @param indices          The indices of this table
     * @param checkConstraints The table level check constraints
     * @param columns          An array of columns to put in this table, excluding the primary key/s
     */
    public SQLiteTable(SQLiteDatabaseHelper helper, IRowConverter<T> converter, String name, PrimaryKeyConstraint pk, IndexConstraint[] indices, CheckConstraint[] checkConstraints, IColumn... columns) {
        super(helper, converter, name, pk, indices, checkConstraints, columns);
    }

    public T getLastRowAdded(){
        return last_row_added;
    }

    @Override
    public void insert(IRow newRow) {
        helper.insert(this, columns, newRow.getValues());
        newRow = ((SQLiteDatabaseHelper)helper).getLastInsertedRow();
        addFromIRow(newRow);
        last_row_added = rows.get(rows.size() - 1);
    }

    // ---- Conflict resolution ----

    @Override
    public T getRow(int index) {
        return super.getRow(index);
    }

    @Override
    public T getRow(IPrimaryKey key) {
        return super.getRow(key);
    }
}