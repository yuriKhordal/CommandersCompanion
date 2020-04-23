package com.yuri.commanderscompanion.api;

import java.util.ArrayList;

import dbAPI.DatabaseCell;
import dbAPI.IRow;
import dbAPI.ITable;

/**Represents all the rows that were updated*/
public class Updates {
    /**The list of all the updates*/
    protected ArrayList<Update> updates;
    /**The helper to this database*/
    protected SQLiteDatabaseHelper helper;

    /**Initialize a new updates list*/
    public Updates(SQLiteDatabaseHelper helper){
        updates = new ArrayList<Update>();
        this.helper = helper;
    }

    /**Add an updated row
     * @param table The table of the row
     * @param row The row that was updated
     */
    public void add(ITable table, IRow row){
        updates.add(new Update(table, row));
    }

    public void update(){
        for (Update update : updates){
            ITable table = update.table;
            IRow row = update.row;
            helper.update(update.table, update.row, getWhere(row));
        }
        updates.clear();
    }

    /**Get the condition for finding the row within the table
     * @param row The row to find
     * @return A string of the sql condition
     */
    private String getWhere(IRow row){
        StringBuilder str = new StringBuilder();
        int i = 0;

        for (DatabaseCell cell : row.getPrimaryKey().getCells()){
            str.append(cell.getColumn().getName()).append(" = ");
            str.append(helper.DatabaseValueToString(cell.Value));
            i++;
            if (i < row.getPrimaryKey().getKeysCount()){
                str.append(",\n");
            }
        }

        return str.toString();
    }

    /**Represents a row that was updated*/
    protected static class Update{
        /**The table that the row is a part of*/
        public ITable table;
        /**The row that was updated*/
        public IRow row;

        /**Initialize a new row update a row and it's table
         * @param table The row's table
         * @param row The updated row
         */
        public Update(ITable table, IRow row){
            this.table = table;
            this.row = row;
        }
    }
}
