package com.yuri.commanderscompanion.api;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import dbAPI.Constraint;
import dbAPI.ConstraintsEnum;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseValue;
import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IDatabaseReader;
import dbAPI.IRow;

/**Represents a helper to an sqlite database*/
public class SQLiteDatabaseHelper implements IDatabaseHelper {
    /**The tag for log*/
    public static final String TAG = "SQLiteDatabaseHelper";
    /**The sqlite database*/
    protected SQLiteDatabase db;
    /**The path to the database*/
    protected String path;

    public SQLiteDatabaseHelper(String path){
        this.path = path;
        db = SQLiteDatabase.openOrCreateDatabase(path, null);
    }

    @Override
    public int getType() {
        return SQLITE;
    }

    @Override
    public void open() { }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public void create(String table, IColumn[] columns) {
        Log.v(TAG, "Creating table " + table);
        StringBuilder str = new StringBuilder();
        StringBuilder endStr = new StringBuilder();//foreign keys
        StringBuilder indices = new StringBuilder();//indices after creating table

        str.append("CREATE TABLE ").append(table).append('(');

        for (IColumn column : columns){
            str.append('\n').append(column.getName());
            switch (column.getType()){
                case INTEGER: case BOOLEAN: str.append(" INTEGER"); break;
                case DOUBLE: str.append(" REAL"); break;
                case STRING: case DATETIME: str.append(" TEXT"); break;
                case BYTEARRAY: str.append(" BLOB"); break;
            }
            for (Constraint constraint : column.getConstraints()){
                switch(constraint.getType()){
                    case AUTO_INCREMENT: str.append(" AUTOINCREMENT"); break;
                    case CHECK:
                        str.append(" CHECK(").append(constraint.getInfo()).append(")");
                        break;
                    case DEFAULT:
                        str.append(" DEFAULT ").append(constraint.getInfo());
                        break;
                    case FOREIGN_KEY:
                        endStr.append("\nFOREIGN KEY (").append(column.getName()).append(
                                ") REFERENCES ").append(constraint.getInfo()).append(',');
                        break;
                    case INDEX:
                        indices.append("CREATE INDEX ").append(constraint.getInfo()).append(';');
                        break;
                    case NOT_NULL: str.append(" NOT NULL"); break;
                    case PRIMERY_KEY: str.append(" PRIMARY KEY"); break;
                    case UNIQUE: str.append(" UNIQUE"); break;
                }
                str.append(',');
            }
        }

        str.append(endStr).deleteCharAt(str.length() - 1).append("\n);");
        if (indices.length() != 0){
            str.append("\n\n").append(indices);
        }

        Log.d(TAG, "create SQL string: {\n" + str.toString() + "\n}");

        db.execSQL(str.toString());
        Log.v(TAG, "Created table " + table + " successfully");
    }

    @Override
    public void insert(String table, String[] columns, String[] values) {
        Log.v(TAG, "Inserting into table " + table);
        if (columns.length != values.length){
            Log.e(TAG, "The arrays 'columns' and 'values' are different lengths!");
            throw new IllegalArgumentException("The arrays 'columns' and 'values' are different lengths!");
        }
        StringBuilder str = new StringBuilder();
        str.append("INSERT INTO ").append(table).append('(');

        for (String column : columns){
            str.append(column).append(", ");
        }
        str.delete(str.length() - 2, str.length() - 1).append(")\nVALUES(");
        for(String value : values){
            str.append(value).append(", ");
        }
        str.delete(str.length() - 2, str.length() - 1).append(");");
        String sql = str.toString();
        Log.d(TAG, "insert SQL string: {\n" + sql + "\n}");
        db.execSQL(sql);
        Log.v(TAG, "Finished inserting into " + table);
    }

    @Override
    public void update(String table, IRow row) {
        Log.v(TAG, "Updating table " + table);
        StringBuilder str = new StringBuilder();
        str.append("UPDATE ").append(table).append(" SET \n");

        for(int i = 0; i < row.getColumns().length; i++){
            DatabaseCell cell = row.getCell(i);
            str.append(cell.getColumn().getName()).append(" = ").append(cell.Value.toString());
            if (i != row.getColumns().length - 1){
                str.append(",");
            }
            str.append("\n");
        }

        Log.d(TAG, "update SQL string: {\n" + str.toString() + "\n}");
        db.execSQL(str.toString());

        Log.v(TAG, "Finished updating " + table);
    }

    @Override
    public void update(String table, IRow row, String whereCondition) {
        Log.v(TAG, "Updating table " + table);
        StringBuilder str = new StringBuilder();
        DatabaseCell keyCell = null;
        str.append("UPDATE ").append(table).append(" SET \n");

        for(int i = 0; i < row.getColumns().length; i++){
            DatabaseCell cell = row.getCell(i);
            if (cell.getColumn().hasConstraint(ConstraintsEnum.PRIMERY_KEY)){
                keyCell = cell;
            }
            str.append(cell.getColumn().getName()).append(" = ").append(cell.Value.toString());
            if (i != row.getColumns().length - 1){
                str.append(",");
            }
            str.append("\n");
        }
        str.append("WHERE ").append(keyCell.getColumn().getName()).append(" = ").append(keyCell.Value.toString());

        Log.d(TAG, "update SQL string: {\n" + str.toString() + "\n}");
        db.execSQL(str.toString());

        Log.v(TAG, "Finished updating " + table);
    }

    @Override
    public void executeSql(String sql) {
        Log.d(TAG, "Executing sql string: {\n" + sql + "\n}");
        db.execSQL(sql);
    }

    @Override
    public IDatabaseReader select(String table, String[] columns) {
        StringBuilder str = new StringBuilder();

        str.append("SELECT");
        return null;
    }

    @Override
    public IDatabaseReader select(String table, String[] columns, String whereCondition) {
        return null;
    }

    @Override
    public IDatabaseReader select(String[] tables, String[] columns) {
        return null;
    }

    @Override
    public IDatabaseReader select(String[] tables, String[] columns, String whereCondition) {
        return null;
    }

    @Override
    public IDatabaseReader selectAll(String table){
        StringBuilder str = new StringBuilder();
        //TODO: Finish

        str.append("SELECT FROM ").append(table);
        return null;
    }

    @Override
    public IDatabaseReader selectAll(String table, String whereCondition) {
        return null;
    }

    @Override
    public IDatabaseReader selectAll(String[] tables) {
        return null;
    }

    @Override
    public IDatabaseReader selectAll(String[] tables, String whereCondition) {
        return null;
    }

    @Override
    public IDatabaseReader readSql(String sql) {
        return null;
    }

    @Override
    public String DatabaseValueToString(DatabaseValue value) {
        return null;
    }

    @Override
    public void delete(String table, String whereCondition) {

    }

    @Override
    public void drop(String table) {

    }

    @Override
    public void close() {

    }
}
