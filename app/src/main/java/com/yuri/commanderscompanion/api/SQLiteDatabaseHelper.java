package com.yuri.commanderscompanion.api;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.yuri.commanderscompanion.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import dbAPI.CheckConstraint;
import dbAPI.Constraint;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseValue;
import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IRow;
import dbAPI.ITable;

/**Represents a helper to an sqlite database*/
public class SQLiteDatabaseHelper implements IDatabaseHelper { //TODO: create row that
    /**The tag for log*/
    public static final String TAG = "SQLiteDatabaseHelper";
    /*public static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm");*/ //API level 26 needed
    public static final SimpleDateFormat SIMPLE_DATE_FORMATTER = new SimpleDateFormat("yyyy/MM/dd kk:mm");
    /**The sqlite database*/
    protected SQLiteDatabase db;
    /**The path to the database*/
    protected String path;

    protected IRow last_inserted_row;

    /**Initialize a new sql database helper with a path
     * @param path The path to the database file
     */
    public SQLiteDatabaseHelper(String path){
        Log.v(TAG, "Initialized a new SQLDatabaseHelper with path: '" + path + '\'');
        this.path = path;
        db = SQLiteDatabase.openOrCreateDatabase(path, null);
        last_inserted_row = null;

        if (BuildConfig.DEBUG){
            db.disableWriteAheadLogging();
        }
    }

    /**Get the last row that was inserted
     * @return The last inserted row
     */
    public IRow getLastInsertedRow(){
        return this.last_inserted_row;
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
    public void create(final ITable table) {
        Log.v(TAG, "Creating table " + table.getName());
        StringBuilder str = new StringBuilder();
        StringBuilder endStr = new StringBuilder();//foreign keys
        StringBuilder indices = new StringBuilder();//indices after creating table

        str.append("CREATE TABLE ").append(table.getName()).append('(').append('\n');

        if (table.getTableChecks() != null){
            CheckConstraint[] checks = table.getTableChecks();
            for (int i = 0; i < checks.length; i++){
                CheckConstraint check = checks[i];
                endStr.append(check.getSqlString()).append(',').append('\n');
            }
        }

        for (IColumn column : table.getColumns()){
            str.append(column.getName());
            switch (column.getType()){
                case INTEGER: case BOOLEAN: str.append(" INTEGER"); break;
                case DOUBLE: str.append(" REAL"); break;
                case STRING: case DATETIME: str.append(" TEXT"); break;
                case BYTEARRAY: str.append(" BLOB"); break;
            }
            if (column.getConstraints() != null) {
                for (Constraint constraint : column.getConstraints()) {
                    switch (constraint.getType()) {
                    /*case AUTO_INCREMENT: str.append(" AUTOINCREMENT"); break;
                    case CHECK:
                        str.append(" CHECK(").append(constraint.getInfo()).append(")");
                        break;
                    case DEFAULT:
                        str.append(" DEFAULT ").append(constraint.getInfo());
                        break;*/
                        case FOREIGN_KEY:
                        /*endStr.append("\nFOREIGN KEY (").append(column.getName()).append(
                                ") REFERENCES ").append(constraint.getInfo()).append(',');*/
                            endStr.append(' ').append(constraint.getSqlString()).append(',').append('\n');
                            break;
                        case INDEX:
                            //indices.append("CREATE INDEX ").append(constraint.getInfo()).append(';');
                            indices.append("CREATE ").append(constraint.getSqlString()).append(';').append('\n');
                            break;
                    /*case NOT_NULL: str.append(" NOT NULL"); break;
                    case PRIMARY_KEY: str.append(" PRIMARY KEY"); break;
                    case UNIQUE: str.append(" UNIQUE"); break;*/
                        default:
                            str.append(' ').append(constraint.getSqlString());
                            break;
                    }
                }
            }
            str.append(',').append('\n');
        }
        //                  delete last ','
        str.append(endStr).deleteCharAt(str.length() - 2).append(')').append(';');
        if (indices.length() != 0){
            str.append("\n\n").append(indices);
        }

        Log.d(TAG, "create SQL string: {\n" + str.toString() + "\n}");

        db.execSQL(str.toString());
        Log.v(TAG, "Created table " + table.getName() + " successfully");
    }

    @Override
    public void insert(final ITable table, final IColumn[] columns, final DatabaseValue[] values) {
        Log.v(TAG, "Inserting into table " + table.getName());
        if (columns.length != values.length){
                    IllegalArgumentException exception =
                    new IllegalArgumentException("The arrays 'columns' and 'values' are different lengths!");
            Log.wtf(TAG, "The arrays 'columns' and 'values' are different lengths!", exception);
            throw exception;
        }
        StringBuilder str = new StringBuilder();
        str.append("INSERT INTO ").append(table.getName()).append('(');

        for (IColumn column : columns){
            str.append(column.getName()).append(", ");
        }
        str.delete(str.length() - 2, str.length()).append(")\nVALUES(");
        for(DatabaseValue value : values){
            str.append(DatabaseValueToString(value)).append(", ");
        }
        str.delete(str.length() - 2, str.length()).append(");");
        String sql = str.toString();
        Log.d(TAG, "insert SQL string: {\n" + sql + "\n}");
        db.execSQL(sql);
        Log.v(TAG, "Finished inserting into " + table.getName());

        last_inserted_row = new SQLiteDataReader(db.rawQuery(
                "SELECT * FROM " + table.getName() + " WHERE rowid=last_insert_rowid()",
                null)).next();
    }

    @Override
    public void update(final ITable table, final IRow row) {
        Log.v(TAG, "Updating table " + table.getName());
        StringBuilder str = new StringBuilder();
        str.append("UPDATE ").append(table.getName()).append(" SET \n");

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

        Log.v(TAG, "Finished updating " + table.getName());
    }

    @Override
    public void update(final ITable table, final IRow row, final String whereCondition) {
        Log.v(TAG, "Updating table " + table.getName());
        StringBuilder str = new StringBuilder();
        //DatabaseCell keyCell = null;
        str.append("UPDATE ").append(table.getName()).append(" SET \n");

        for(int i = 0; i < row.getCellsCount(); i++){
            DatabaseCell cell = row.getCell(i);
            /*if (cell.getColumn().hasConstraint(ConstraintsEnum.PRIMARY_KEY)){
                keyCell = cell;
            }*/
            str.append(cell.getColumn().getName()).append(" = ").append(cell.Value.toString());
            if (i != row.getColumns().length - 1){
                str.append(",");
            }
            str.append("\n");
        }
        str.append("WHERE ").append(whereCondition);

        Log.d(TAG, "update SQL string: {\n" + str.toString() + "\n}");
        db.execSQL(str.toString());

        Log.v(TAG, "Finished updating " + table.toString());
    }

    @Override
    public void executeSql(String sql) {
        Log.d(TAG, "Executing sql string: {\n" + sql + "\n}");
        db.execSQL(sql);
    }

    @Override
    public SQLiteDataReader select(final ITable table, final IColumn[] columns) {
        Log.v(TAG, "Selecting from " + table.getName());
        StringBuilder str = new StringBuilder();
        SQLiteDataReader reader;

        str.append("SELECT ");
        for (int i = 0; i < columns.length; i++){
            str.append(columns[i].getName());
            if (i < columns.length - 1) { //if i is not last
                str.append(',').append(' ');
            }
        }
        str.append("\nFROM ").append(table.getName());

        Log.d(TAG, "select SQL string: {\n" + str.toString() + "\n}");
        reader = new SQLiteDataReader(db.rawQuery(str.toString(), null));

        Log.v(TAG, "Finished selecting from " + table.getName());
        return reader;
    }

    @Override
    public SQLiteDataReader select(final ITable table, final IColumn[] columns, final String whereCondition) {
        Log.v(TAG, "Selecting from " + table.getName());
        StringBuilder str = new StringBuilder();
        SQLiteDataReader reader;

        str.append("SELECT ");
        for (int i = 0; i < columns.length; i++){
            str.append(columns[i].getName());
            if (i < columns.length - 1) { //if i is not last
                str.append(',').append(' ');
            }
        }
        str.append("\nFROM ").append(table.getName());
        str.append("\nWHERE ").append(whereCondition);

        Log.d(TAG, "select SQL string: {\n" + str.toString() + "\n}");
        reader = new SQLiteDataReader(db.rawQuery(str.toString(), null));

        Log.v(TAG, "Finished selecting from " + table.getName());
        return reader;
    }

    @Override
    public SQLiteDataReader select(final ITable[] tables, final IColumn[] columns) {
        Log.v(TAG, "Selecting from multiple tables");
        StringBuilder str = new StringBuilder();
        SQLiteDataReader reader;

        str.append("SELECT ");
        for (int i = 0; i < columns.length; i++){
            str.append(columns[i].getName());
            if (i < columns.length - 1) { //if i is not last
                str.append(',').append(' ');
            }
        }

        str.append("\nFROM ");
        for (int i = 0; i < tables.length ; i++){
            str.append(tables[i].getName());
            if (i < tables.length - 1) { //if i is not last
                str.append(',').append(' ');
            }
        }

        Log.d(TAG, "select SQL string: {\n" + str.toString() + "\n}");
        reader = new SQLiteDataReader(db.rawQuery(str.toString(), null));

        Log.v(TAG, "Finished selecting from multiple tables");
        return reader;
    }

    @Override
    public SQLiteDataReader select(final ITable[] tables, final IColumn[] columns, final String whereCondition) {
        Log.v(TAG, "Selecting from multiple tables");
        StringBuilder str = new StringBuilder();
        SQLiteDataReader reader;

        str.append("SELECT ");
        for (int i = 0; i < columns.length; i++){
            str.append(columns[i].getName());
            if (i < columns.length - 1) { //if i is not last
                str.append(',').append(' ');
            }
        }

        str.append("\nFROM ");
        for (int i = 0; i < tables.length ; i++){
            str.append(tables[i].getName());
            if (i < tables.length - 1) { //if i is not last
                str.append(',').append(' ');
            }
        }
        str.append("\nWHERE ").append(whereCondition);

        Log.d(TAG, "select SQL string: {\n" + str.toString() + "\n}");
        reader = new SQLiteDataReader(db.rawQuery(str.toString(), null));

        Log.v(TAG, "Finished selecting from multiple tables");
        return reader;
    }

    @Override
    public SQLiteDataReader selectAll(final ITable table){
        Log.v(TAG, "Selecting from " + table.getName());
        StringBuilder str = new StringBuilder();
        SQLiteDataReader reader;

        str.append("SELECT * FROM ").append(table.getName());

        Log.d(TAG, "select SQL string: {\n" + str.toString() + "\n}");
        reader = new SQLiteDataReader(db.rawQuery(str.toString(), null));

        Log.v(TAG, "Finished selecting from " + table.getName());
        return reader;
    }

    @Override
    public SQLiteDataReader selectAll(final ITable table, final String whereCondition) {
        Log.v(TAG, "Selecting from " + table.getName());
        StringBuilder str = new StringBuilder();
        SQLiteDataReader reader;

        str.append("SELECT * FROM ").append(table.getName());
        str.append("\nWHERE ").append(whereCondition);

        Log.d(TAG, "select SQL string: {\n" + str.toString() + "\n}");
        reader = new SQLiteDataReader(db.rawQuery(str.toString(), null));

        Log.v(TAG, "Finished selecting from " + table.getName());
        return reader;
    }

    @Override
    public SQLiteDataReader selectAll(final ITable[] tables) {
        Log.v(TAG, "Selecting from multiple tables");
        StringBuilder str = new StringBuilder();
        SQLiteDataReader reader;

        str.append("SELECT * FROM ");
        for (int i = 0; i < tables.length ; i++){
            str.append(tables[i].getName());
            if (i < tables.length - 1) { //if i is not last
                str.append(',').append(' ');
            }
        }

        Log.d(TAG, "select SQL string: {\n" + str.toString() + "\n}");
        reader = new SQLiteDataReader(db.rawQuery(str.toString(), null));

        Log.v(TAG, "Finished selecting from multiple tables");
        return reader;
    }

    @Override
    public SQLiteDataReader selectAll(final ITable[] tables, final String whereCondition) {
        Log.v(TAG, "Selecting from multiple tables");
        StringBuilder str = new StringBuilder();
        SQLiteDataReader reader;

        str.append("SELECT * FROM ");
        for (int i = 0; i < tables.length ; i++){
            str.append(tables[i].getName());
            if (i < tables.length - 1) { //if i is not last
                str.append(',').append(' ');
            }
        }
        str.append("\nWHERE ").append(whereCondition);

        Log.d(TAG, "select SQL string: {\n" + str.toString() + "\n}");
        reader = new SQLiteDataReader(db.rawQuery(str.toString(), null));

        Log.v(TAG, "Finished selecting from multiple tables");
        return reader;
    }

    @Override
    public SQLiteDataReader readSql(final String sql) {
        return new SQLiteDataReader(db.rawQuery(sql, null));
    }

    @Override
    public String DatabaseValueToString(DatabaseValue value) {
        if (value.Value == null){
            return "NULL";
        }
        switch (value.getType()){
            //case BOOLEAN: value.getBoolean().toString(); break;
            case BYTEARRAY:
                final char[] HEX_STRING = "0123456789ABCDEF".toCharArray();
                byte[] bytes = value.getByteArray();
                char[] hexString = new char[bytes.length*2];
                for (int i = 0; i < bytes.length; i++){
                    int v = bytes[i] & 0xFF;
                    hexString[i*2] = HEX_STRING[v >>> 4];
                    hexString[i*2+1] = HEX_STRING[v & 0x0F];
                }
                return new String(hexString);
            case DATETIME:
                Date date = (Date)value.Value;
                return "'" + SIMPLE_DATE_FORMATTER.format(date) + "'";
            /*case DOUBLE: value.getDouble().toString(); break;
            case INTEGER: value.getInteger().toString(); break;*/
            case STRING: return "'" + value.getString() + "'";
            case BOOLEAN: return value.getBoolean() ? "1" : "0";
            default: return value.Value.toString();
        }
    }

    @Override
    public void delete(final ITable table, final String whereCondition) {
        db.execSQL("DELETE FROM " + table.getName() + " WHERE " + whereCondition);
    }

    @Override
    public void drop(final ITable table) {
        db.execSQL("DROP TABLE " + table.getName());
    }

    @Override
    public void close() { }

    @Override
    public IDatabaseHelper clone() {
        return new SQLiteDatabaseHelper(path);
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) { return true; }
        if (obj == null) { return false; }
        if (!(obj instanceof SQLiteDatabaseHelper)){ return false; }

        SQLiteDatabaseHelper helper = (SQLiteDatabaseHelper)obj;
        return helper.db.equals(db) && path.equals(helper.path);
    }

    @Override
    public int hashCode() {
        return GeneralHelper.hash(db, path);
    }

    @Override
    public String toString() {
        return "SQLite Database Helper: path: '" + path + "'\nDatabase: " + db + ";";
    }
}
