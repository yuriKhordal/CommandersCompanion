package com.yuri.commanderscompanion.api;

import java.util.Arrays;

import dbAPI.Column;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseValue;
import dbAPI.IColumn;
import dbAPI.IRow;

/**A class for general hash functions*/
public final class GeneralHelper {
    /**The starting value of hash*/
    private static final int HASH_START = 7;
    /**The prime number to multiply the hash by for each object*/
    private static final int HASH_PRIME = 31;

    /**Hash an array of object
     * @param objects The objects to produce the hash code from
     * @return A hash code, produced from hashing all the given objects
     */
    public static int hash(Object...objects){
        int hash = HASH_START;
        for (Object object : objects){
            hash += HASH_PRIME * hash + (object == null ? 0 : object.hashCode());
        }
        return hash;
    }

    /**Get all the columns that are not primary(actually gets all columns except the first)
     * @param columns The columns to 'cut'
     * @return All the columns except the first
     */
    public static IColumn[] getNonPrimaryColumns(IColumn[] columns){
        return Arrays.copyOfRange(columns, 1, columns.length);
    }

    /**Get a row's cells, replaced with columns<br><br>
     * This method gets the values from row by the specified columns and puts the specified columns
     * in the cells instead of the row's columns<br><br>
     * The intended use is for getting a row from database and replacing the columns, with the same
     * columns but with more complex(like constraints, and stuff)
     * @param row The row
     * @param columns The columns
     * @return A new cells array with values from row, and column from columns argument
     */
    public static DatabaseCell[] getCells(IRow row, IColumn...columns){
        if (row == null){
            throw new NullPointerException("row is null");
        } if (columns == null) {
            throw new NullPointerException("columns is null");
        } if (row.getCellsCount() != columns.length){
            throw new IllegalArgumentException("row has " + row.getCellsCount() + " cells, but "
                    + columns.length + " columns were supplied");
        }
        DatabaseCell[] cells = new DatabaseCell[columns.length];
        for (int i = 0; i < columns.length; i++){
            IColumn column = columns[i];
            DatabaseValue value = row.getValue(column);
            if (column == null){
                throw new NullPointerException("column[" + i + "] is null");
            } if (value == null){
                throw new NullPointerException("value at " + i + " is null");
            }
            cells[i] = new DatabaseCell(column, value);
        }
        return cells;
    }

    public static void testDataInsert(){
        Sight s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
        Weapon w1, w2, w3, w4, w5, w6, w7, w8, w9, w10;
        Soldier so1, so2, so3, so4, so5, so6, so7, so8, so9, so10;
        OrganisationalUnit u1, u2, u3;
        {
            Database.SIGHTS.insert(s1 = new Sight("MeproLight", 1234, 1000));
            Database.SIGHTS.insert(s2 = new Sight("MeproLight", 1234, 1001));
            Database.SIGHTS.insert(s3 = new Sight("MeproLight", 1234, 1002));
            Database.SIGHTS.insert(s4 = new Sight("MeproLight", 1234, 1003));
            Database.SIGHTS.insert(s5 = new Sight("MeproLight", 1234, 1004));
            Database.SIGHTS.insert(s6 = new Sight("M5", 1235, 1005));
            Database.SIGHTS.insert(s7 = new Sight("M5", 1235, 1006));
            Database.SIGHTS.insert(s8 = new Sight("M5", 1235, 1007));
            Database.SIGHTS.insert(s9 = new Sight("ACOG", 1236, 1008));
            Database.SIGHTS.insert(s10 = new Sight("ACOG", 1236, 1009));
        } {
            Database.WEAPONS.insert(w1 = new Weapon("M16 Long", 1010));
            Database.WEAPONS.insert(w2 =new Weapon("M16", s1 , 1011));
            Database.WEAPONS.insert(w3 = new Weapon("M16", s2, 1012));
            Database.WEAPONS.insert(w4 = new Weapon("M16", s3, 1013));
            Database.WEAPONS.insert(w5 = new Weapon("M4", s9, 1014));
            Database.WEAPONS.insert(w6 = new Weapon("Tavor", s8, 1015));
            Database.WEAPONS.insert(w7 = new Weapon("Tavor", s5, 1016));
            Database.WEAPONS.insert(w8 = new Weapon("Tavor", s6, 1017));
            Database.WEAPONS.insert(w9 = new Weapon("Tavor", s7, 1018));
            Database.WEAPONS.insert(w10 = new Weapon("Tavor", s10, 1019));
        } {

        } {

        } {

        }
    }
}
