package com.yuri.commanderscompanion.api;

import java.util.Arrays;
import java.util.Date;

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

    // ---- Shared static variables ----

    /**The current unit being worked on*/
    public static OrganisationalUnit currentUnit;

    // ---- static functions ----

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

    public static boolean equals(Object obj1, Object obj2){
        if (obj1 == null){
            return obj2 == null;
        }
        if (obj2 == null) { return false; }
        return obj1.equals(obj2);
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

    /**Insert data into database to test all tables*/
    public static void testDataInsert(){
        Sight s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
        Weapon w1, w2, w3, w4, w5, w6, w7, w8, w9, w10;
        OrganisationalUnit u1, u2, u3;
        Soldier so1, so2, so3, so4, so5, so6, so7, so8, so9, so10;
        Equipment eq1, eq2, eq3, eq4, eq5, eq6, eq7, eq8, eq9, eq10;
        Notice nc1, nc2, nc3;
        NoteType nt1, nt2, nt3;
        Note n1, n2, n3, n4, n5, n6, n7, n8, n9, n10;
        LogType lt1, lt2, lt3, lt4;
        Log l1, l2, l3, l4;
        LogEntry en1, en2, en3, en4, en5, en6, en7, en8, en9, en10;
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
            Database.UNITS.insert(u1 = new OrganisationalUnit("Squad", "1A"));
            Database.UNITS.insert(u2 = new OrganisationalUnit("Squad", "1B"));
            Database.UNITS.insert(u3 = new OrganisationalUnit("Platoon", "1"));
            u3.addSubUnit(u1);
            u3.addSubUnit(u2);
        } {
            Database.SOLDIERS.insert(so1 = new Soldier(u1, "name1", "Private", w1, 1020));
            Database.SOLDIERS.insert(so2 = new Soldier(u1, "name2", "Private", w2, 1021));
            Database.SOLDIERS.insert(so3 = new Soldier(u1, "name3", "Private", w3, 1022));
            Database.SOLDIERS.insert(so4 = new Commander(1023, u1, "comA4", "Sergeant", w4));
            Database.SOLDIERS.insert(so5 = new Commander(1024, u2, "comB5", "Sergeant", w6));
            Database.SOLDIERS.insert(so6 = new Soldier(u2, "name6", "Private", w7, 1025));
            Database.SOLDIERS.insert(so7 = new Soldier(u2, "name7", "Private", w8, 1026));
            Database.SOLDIERS.insert(so8 = new Soldier(u2, "name8", "Private", w9, 1027));
            Database.SOLDIERS.insert(so9 = new Soldier(u3, "name9", "SSG", w10, 1028));
            Database.SOLDIERS.insert(so10 = new Commander(1029, u3, "name10", "Lieutenant", w5));
        } {
            Database.EQUIPMENT.insert(eq1 = new Equipment(so4.id, 1237, "Pillow", Equipment.EquipmentStatus.WORKING));
            Database.EQUIPMENT.insert(eq2 = new Equipment(so4.id, 1238, "Bottle", Equipment.EquipmentStatus.WORKING));
            Database.EQUIPMENT.insert(eq3 = new Equipment(so4.id, 1239, "Vest", Equipment.EquipmentStatus.DEFICIENT));
            Database.EQUIPMENT.insert(eq4 = new Equipment(so4.id, 1240, "Mag", Equipment.EquipmentStatus.BROKEN));
            Database.EQUIPMENT.insert(eq5 = new Equipment(so5.id, 1237, "Pillow", Equipment.EquipmentStatus.LOST_OR_STOLEN));
            Database.EQUIPMENT.insert(eq6 = new Equipment(so5.id, 1238, "Bottle", Equipment.EquipmentStatus.WORKING));
            Database.EQUIPMENT.insert(eq7 = new Equipment(so5.id, 1239, "Vest", Equipment.EquipmentStatus.WORKING));
            Database.EQUIPMENT.insert(eq8 = new Equipment(so5.id, 1240, "Mag", Equipment.EquipmentStatus.BROKEN));
            Database.EQUIPMENT.insert(eq9 = new Equipment(so9.id, 1241, "Chest", Equipment.EquipmentStatus.WORKING));
            Database.EQUIPMENT.insert(eq10 = new Equipment(so9.id, 1238, "Bottle", Equipment.EquipmentStatus.WORKING));
        } {
            Database.NOTICES.insert(nc1 = new Notice(so1, new Date(), "Broke Mag", "Shabbat"));
            Database.NOTICES.insert(nc2 = new Notice(so6, new Date(), "Broke Vest", "Sue"));
            Database.NOTICES.insert(nc3 = new Notice(so6, new Date(), "Lie", "2 Hours"));
        } {
            Database.NOTE_TYPES.insert(nt1 = new NoteType(u1, "Squad Notes"));
            Database.NOTE_TYPES.insert(nt2 = new NoteType(so9, "Personal Notes"));
            Database.NOTE_TYPES.insert(nt3 = new NoteType(so9, "Time Tables"));
        } {
            Database.NOTES.insert(n1 = new Note(nt1, "Get equipment", ""));
            Database.NOTES.insert(n2 = new Note(nt1, "Do Lesson", ""));
            Database.NOTES.insert(n3 = new Note(nt2, "Go Do", ""));
            Database.NOTES.insert(n4 = new Note(nt2, "Write Code", ""));
            Database.NOTES.insert(n5 = new Note(nt2, "", "blah"));
            Database.NOTES.insert(n6 = new Note(nt2, "", "blah"));
            Database.NOTES.insert(n7 = new Note(nt3, "Sunday", "body"));
            Database.NOTES.insert(n8 = new Note(nt3, "Monday", "body"));
            Database.NOTES.insert(n9 = new Note(nt3, "Tuesday", "body"));
            Database.NOTES.insert(n10 = new Note(nt3, "Wednesday", "body"));
        } {
            Database.LOG_TYPES.insert(lt1 = new LogType(u3, "Log 1", true));
            Database.LOG_TYPES.insert(lt2 = new LogType(u3, "Closed", false));
            Database.LOG_TYPES.insert(lt3 = new LogType(u1, "Lesson", false));
            Database.LOG_TYPES.insert(lt4 = new LogType(u2, "Lesson", true));
        } {
            Database.LOGS.insert(l1 = new Log(lt1, new Date()));
            Database.LOGS.insert(l2 = new Log(lt1, new Date()));
            Database.LOGS.insert(l3 = new Log(lt1, new Date()));
            Database.LOGS.insert(l4 = new Log(lt3, new Date()));
        } {
            Database.ENTRIES.insert(en1 = new LogEntry(l4, so9, "Understood"));
            Database.ENTRIES.insert(en2 = new LogEntry(l3, so1, "Base"));
            Database.ENTRIES.insert(en3 = new LogEntry(l3, so2, "Base"));
            Database.ENTRIES.insert(en4 = new LogEntry(l3, so3, "Home"));
            Database.ENTRIES.insert(en5 = new LogEntry(l3, so4, "Base"));
            Database.ENTRIES.insert(en6 = new LogEntry(l1, so5, "Sick"));
            Database.ENTRIES.insert(en7 = new LogEntry(l1, so6, "Base"));
            Database.ENTRIES.insert(en8 = new LogEntry(l1, so7, "On Way"));
            Database.ENTRIES.insert(en9 = new LogEntry(l1, so8, "Base"));
            Database.ENTRIES.insert(en10 = new LogEntry(l1, so9, "Base"));
        }
    }
}
