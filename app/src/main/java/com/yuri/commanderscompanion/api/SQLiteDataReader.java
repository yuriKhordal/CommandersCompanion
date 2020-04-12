package com.yuri.commanderscompanion.api;

import android.database.Cursor;

import java.util.Iterator;

import androidx.annotation.NonNull;
import dbAPI.Column;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.DatabaseValue;
import dbAPI.IColumn;
import dbAPI.IDatabaseReader;
import dbAPI.IRow;
import dbAPI.Row;

/**Represents a reader for an SQLite database*/
public class SQLiteDataReader implements IDatabaseReader {
    /**The cursor given by the database*/
    protected Cursor cursor;

    /**Initialize the reader with a cursor
     * @param cursor The cursor given by the database
     */
    public SQLiteDataReader(Cursor cursor){
        this.cursor = cursor;
    }

    @Override
    public IRow getRow(int index) {
        int pos = cursor.getPosition();
        IRow row;
        cursor.moveToPosition(index);

        row = next();

        cursor.moveToPosition(pos);
        return row;
    }

    @Override
    public IRow[] getAllRows() {
        IRow rows[] = new IRow[cursor.getCount()];
        for(int i = 0; i < rows.length; i++){
            rows[i] = next();
        }
        return  rows;
    }

    @Override
    public boolean hasNext() {
        return !cursor.isAfterLast();
    }

    @Override
    public IRow next() {
        String names[] = cursor.getColumnNames();
        DatabaseCell cells[] = new DatabaseCell[names.length];
        for (int i = 0; i < names.length; i++){
            IColumn col;
            DatabaseValue val;
            switch(cursor.getType(i)){
                case Cursor.FIELD_TYPE_NULL:
                    val = new DatabaseValue(null, DatabaseDataType.INTEGER);
                    break;
                case Cursor.FIELD_TYPE_INTEGER:
                    val = new DatabaseValue(cursor.getLong(i), DatabaseDataType.INTEGER);
                    break;
                case Cursor.FIELD_TYPE_FLOAT:
                    val = new DatabaseValue(cursor.getDouble(i), DatabaseDataType.DOUBLE);
                    break;
                case Cursor.FIELD_TYPE_STRING:
                    val = new DatabaseValue(cursor.getString(i), DatabaseDataType.STRING);
                    break;
                case Cursor.FIELD_TYPE_BLOB:
                    val = new DatabaseValue(cursor.getBlob(i), DatabaseDataType.BYTEARRAY);
                    break;
                default:
                    val = new DatabaseValue(null, DatabaseDataType.INTEGER);
                    break;
            }
            col = new Column(names[i], i, val.getType());
            cells[i] = new DatabaseCell(col, val);
        }
        cursor.moveToNext();

        return new Row(cells);
    }

    @NonNull
    @Override
    public Iterator<IRow> iterator() {
        return this;
    }
}
