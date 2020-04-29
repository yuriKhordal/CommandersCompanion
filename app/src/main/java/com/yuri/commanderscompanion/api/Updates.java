package com.yuri.commanderscompanion.api;

import java.util.ArrayList;

import dbAPI.DatabaseCell;
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
    public void add(ITable table, SQLiteRow row, DatabaseCell...cells){
        updates.add(new Update(table.getName(), row.getRowId()));
    }

    public void update(){
        for (Update update : updates){
            helper.executeSql(update.getUpdateString(helper));
        }
        updates.clear();
    }

//    /**Get the condition for finding the row within the table
//     * @param row The row to find
//     * @return A string of the sql condition
//     */
//    private String getWhere(IRow row){
//        StringBuilder str = new StringBuilder();
//        int i = 0;
//
//        for (DatabaseCell cell : row.getPrimaryKey().getCells()){
//            str.append(cell.getColumn().getName()).append(" = ");
//            str.append(helper.DatabaseValueToString(cell.Value));
//            i++;
//            if (i < row.getPrimaryKey().getKeysCount()){
//                str.append(",\n");
//            }
//        }
//
//        return str.toString();
//    }

    /**Represents a row that was updated*/
    protected static class Update {
        /**The table that the row is a part of*/
        public String table;
        /**The row that was updated*/
        public int rowid;
        /**The cells that were updated*/
        public DatabaseCell[] updated;

        /**Initialize a new row update with a row id and it's table
         * @param table The row's table name
         * @param rowid The updated row's id
         */
        public Update(String table, int rowid, DatabaseCell...cells) {
            this.table = table;
            this.rowid = rowid;
            updated = cells;
        }

        /**Get the string for updating the row
         * @param helper A helper object
         * @return An sql string for updating the row
         */
        public String getUpdateString(SQLiteDatabaseHelper helper){
            StringBuilder str = new StringBuilder();

            str.append("UPDATE ").append(table).append(" SET\n");
            for (int i = 0; i < updated.length; i++){
                str.append(updated[i].getColumn().getName());
                str.append(" = ");
                str.append(helper.DatabaseValueToString(updated[i].Value));

                if (i < updated.length - 1){
                    str.append(',');
                }
                str.append('\n');
            }
            str.append("WHERE rowid = ").append(rowid);

            return str.toString();
        }
    }
}
