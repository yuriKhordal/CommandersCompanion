package com.yuri.commanderscompanion.api;

import dbAPI.DatabaseCell;
import dbAPI.Row;

/**Represents a row in an SQLite database*/
public class SQLiteRow extends Row {
    /**Represents the id of a row that was created in software and is not in an actual database*/
    public static final int OFFLINE_ROW_ID = -1;

    /**Initializes a new row with cells*/
    public SQLiteRow(DatabaseCell... cells) {
        super(cells);
    }

    /**Get the id of the the row
     * @return An int representing the id of the row
     */
    public int getRowId(){
        return getPrimaryKey().getValue().getInt();
    }
}
