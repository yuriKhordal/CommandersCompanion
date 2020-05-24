package com.yuri.commanderscompanion.api;

import android.content.Context;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Stack;

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

    /**Return a given string repeated multiple times
     * @param string The string to repeat
     * @param times The amount of times to repeat a string
     * @return The string 'times' times repeated
     */
    public static String multiplyString(final String string, int times){
        StringBuilder str = new StringBuilder(string.length()*times);

        for (int i = 0; i < times; i++){
            str.append(string);
        }

        return str.toString();
    }

    public static int dpToPixels(Context context, int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return (int)(dp * density + 0.5f);
    }

    public static <T> void quickSort(List<T> list, Comparator<T> comparator){
        Stack<Integer> pivotIndex = new Stack<Integer>();
        Stack<Integer> startIndex = new Stack<Integer>();
        Stack<Integer> endIndex = new Stack<Integer>();
        pivotIndex.push(list.size() / 2);
        startIndex.push(0);
        endIndex.push(list.size() - 1);

        while (!pivotIndex.isEmpty()){
            int start, pivot, end;
            start = startIndex.pop();
            pivot = pivotIndex.pop();
            end = endIndex.pop();

            //if (end - start <= 0) { continue; }
            /*if (end - start == 1){
                T startVal = sorted.get(start);
                if (comparator.compare(startVal, sorted.get(end)) > 0){
                    sorted.remove(start);
                    sorted.add(end, startVal);
                }
            }*/

            T pivotVal = list.get(pivot);
            for (int i = start; i < pivot; i++){
                T value = list.get(i);
                if (comparator.compare(value, pivotVal) > 0){
                    list.remove(i);
                    list.add(pivot, value);
                    pivot--;
                    //since the value at i got deleted, i now points to the next value
                    i--;
                }
            }

            for (int i = pivot + 1; i <= end; i++){
                T value = list.get(i);
                if (comparator.compare(value, pivotVal) < 0){
                    list.remove(i);
                    list.add(start, value);
                    pivot++;
                    //since the value at i got deleted, i now points to the next value
                    i--;
                }
            }

            if (pivot > start) {
                //Add left side of the pivot
                startIndex.push(start);
                //basically start + (end - start)/2
                pivotIndex.push(start + (pivot - 1 - start) / 2);
                endIndex.push(pivot - 1);
            }

            if (pivot < end) {
                //Add right side of the pivot
                startIndex.push(pivot + 1);
                //basically start + (end - start)/2
                pivotIndex.push(pivot + 1 + (end - pivot - 1) / 2);
                endIndex.push(end);
            }
        }
    }

    /**Insert data into database to test all tables*/
    public static void testDataInsert(){
        Sight s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
        Weapon w1, w2, w3, w4, w5, w6, w7, w8, w9, w10;
        OrganisationalUnit u1, u2, u3;
        OrganisationalUnit a, a1, a1A, a1B, a2, a2A, a2B, b, b1, b1A, b1B, b2, b2A, b2B, C, CA, CB, CC;
        Soldier so1, so2, so3, so4, so5, so6, so7, so8, so9, so10;
        Equipment eq1, eq2, eq3, eq4, eq5, eq6, eq7, eq8, eq9, eq10;
        Notice nc1, nc2, nc3;
        NoteType nt1, nt2, nt3;
        Note n1, n2, n3, n4, n5, n6, n7, n8, n9, n10;
        LogType lt1, lt2, lt3, lt4;
        Log l1, l2, l3, l4;
        LogEntry en1, en2, en3, en4, en5, en6, en7, en8, en9, en10;
        {
            Database.SIGHTS.insert(s1 = new Sight("מפרו", 1234, 1000));
            Database.SIGHTS.insert(s2 = new Sight("מפרו", 1234, 1001));
            Database.SIGHTS.insert(s3 = new Sight("מפרו", 1234, 1002));
            Database.SIGHTS.insert(s4 = new Sight("מפרו", 1234, 1003));
            Database.SIGHTS.insert(s5 = new Sight("מפרו", 1234, 1004));
            Database.SIGHTS.insert(s6 = new Sight("אם5", 1235, 1005));
            Database.SIGHTS.insert(s7 = new Sight("אם5", 1235, 1006));
            Database.SIGHTS.insert(s8 = new Sight("אם5", 1235, 1007));
            Database.SIGHTS.insert(s9 = new Sight("טריג", 1236, 1008));
            Database.SIGHTS.insert(s10 = new Sight("טריג", 1236, 1009));
        } {
            Database.WEAPONS.insert(w1 = new Weapon("אם16 ארוך", 1010));
            Database.WEAPONS.insert(w2 =new Weapon("אם16", s1 , 1011));
            Database.WEAPONS.insert(w3 = new Weapon("אם16", s2, 1012));
            Database.WEAPONS.insert(w4 = new Weapon("אם16", s3, 1013));
            Database.WEAPONS.insert(w5 = new Weapon("אם4", s9, 1014));
            Database.WEAPONS.insert(w6 = new Weapon("טבור", s8, 1015));
            Database.WEAPONS.insert(w7 = new Weapon("טבור", s5, 1016));
            Database.WEAPONS.insert(w8 = new Weapon("טבור", s6, 1017));
            Database.WEAPONS.insert(w9 = new Weapon("טבור", s7, 1018));
            Database.WEAPONS.insert(w10 = new Weapon("טבור", s10, 1019));
        } {
            Database.UNITS.insert(u1 = new OrganisationalUnit("כיתה", "1א"));
            Database.UNITS.insert(u2 = new OrganisationalUnit("כיתה", "1ב"));
            Database.UNITS.insert(u3 = new OrganisationalUnit("מחלקה", "1"));
            u3.addSubUnit(u1);
            u3.addSubUnit(u2);
        } {
            Database.UNITS.insert(a1A = new OrganisationalUnit("כיתה", "א1א"));
            Database.UNITS.insert(a1B = new OrganisationalUnit("כיתה", "א1ב"));
            Database.UNITS.insert(a2A = new OrganisationalUnit("כיתה", "א2א"));
            Database.UNITS.insert(a2B = new OrganisationalUnit("כיתה", "א2ב"));
            Database.UNITS.insert(b1A = new OrganisationalUnit("כיתה", "ב1א"));
            Database.UNITS.insert(b1B = new OrganisationalUnit("כיתה", "ב1ב"));
            Database.UNITS.insert(b2A = new OrganisationalUnit("כיתה", "ב2א"));
            Database.UNITS.insert(b2B = new OrganisationalUnit("כיתה", "ב22"));
            Database.UNITS.insert(CA = new OrganisationalUnit("כיתה", "3א"));
            Database.UNITS.insert(CB = new OrganisationalUnit("כיתה", "3ב"));
            Database.UNITS.insert(CC = new OrganisationalUnit("כיתה", "3ג"));

            Database.UNITS.insert(a1 = new OrganisationalUnit("מחלקה", "א1"));
            a1.addSubUnit(a1A);
            a1.addSubUnit(a1B);
            Database.UNITS.insert(a2 = new OrganisationalUnit("מחלקה", "א2"));
            a2.addSubUnit(a2A);
            a2.addSubUnit(a2B);
            Database.UNITS.insert(b1 = new OrganisationalUnit("מחלקה", "ב1"));
            b1.addSubUnit(b1A);
            b1.addSubUnit(b1B);
            Database.UNITS.insert(b2 = new OrganisationalUnit("מחלקה", "ב2"));
            b2.addSubUnit(b2A);
            b2.addSubUnit(b2B);
            Database.UNITS.insert(C = new OrganisationalUnit("מחלקה", "3"));
            C.addSubUnit(CA);
            C.addSubUnit(CB);
            C.addSubUnit(CC);

            Database.UNITS.insert(a = new OrganisationalUnit("פלוגה", "א"));
            a.addSubUnit(a1);
            a.addSubUnit(a2);
            Database.UNITS.insert(b = new OrganisationalUnit("פלוגה", "ב"));
            b.addSubUnit(b1);
            b.addSubUnit(b2);

//            Database.UNITS.add(a1A, a1B, a1, a2A, a2B, a2, a, b1A, b1B, b1, b2A, b2B, b2, b, CA, CB, CC, C);
        } {
            Database.SOLDIERS.insert(so1 = new Soldier(u1, "חייל א", "טוראי", "לוחם", w1, 1020));
            Database.SOLDIERS.insert(so2 = new Soldier(u1, "חייל ב", "טוראי", "לוחם", w2, 1021));
            Database.SOLDIERS.insert(so3 = new Soldier(u1, "חייל ג", "טוראי", "לוחם", w3, 1022));
            Database.SOLDIERS.insert(so4 = new Commander(1023, u1, "מפקד ד", "סמל", "מ\"כ", w4));
            Database.SOLDIERS.insert(so5 = new Commander(1024, u2, "מפקד ה", "סמל", "מ\"כ", w6));
            Database.SOLDIERS.insert(so6 = new Soldier(u2, "חייל ו", "טור", "לוחם", w7, 1025));
            Database.SOLDIERS.insert(so7 = new Soldier(u2, "חייל ז", "טוראי", "לוחם", w8, 1026));
            Database.SOLDIERS.insert(so8 = new Soldier(u2, "חייל ח", "טוראי", "לוחם", w9, 1027));
            Database.SOLDIERS.insert(so9 = new Soldier(u3, "סמל מחלקה 3", "סמ\"ר", "סמל מחלקה", w10, 1028));
            Database.SOLDIERS.insert(so10 = new Commander(1029, u3, "ממ 3", "סג\"ם", "מ\"מ", w5));
        } {
            Database.EQUIPMENT.insert(eq1 = new Equipment(so4.id, 1237, "כרית", Equipment.EquipmentStatus.WORKING));
            Database.EQUIPMENT.insert(eq2 = new Equipment(so4.id, 1238, "מימיה", Equipment.EquipmentStatus.WORKING));
            Database.EQUIPMENT.insert(eq3 = new Equipment(so4.id, 1239, "ווסט", Equipment.EquipmentStatus.DEFICIENT));
            Database.EQUIPMENT.insert(eq4 = new Equipment(so4.id, 1240, "מחסנית", Equipment.EquipmentStatus.BROKEN));
            Database.EQUIPMENT.insert(eq5 = new Equipment(so5.id, 1237, "כרית", Equipment.EquipmentStatus.LOST_OR_STOLEN));
            Database.EQUIPMENT.insert(eq6 = new Equipment(so5.id, 1238, "מימיה", Equipment.EquipmentStatus.WORKING));
            Database.EQUIPMENT.insert(eq7 = new Equipment(so5.id, 1239, "ווסט", Equipment.EquipmentStatus.WORKING));
            Database.EQUIPMENT.insert(eq8 = new Equipment(so5.id, 1240, "מחסנית", Equipment.EquipmentStatus.BROKEN));
            Database.EQUIPMENT.insert(eq9 = new Equipment(so9.id, 1241, "ארונית", Equipment.EquipmentStatus.WORKING));
            Database.EQUIPMENT.insert(eq10 = new Equipment(so9.id, 1238, "מימיה", Equipment.EquipmentStatus.WORKING));
        } {
            Database.NOTICES.insert(nc1 = new Notice(so1, new Date(), "שבר מחסנית", "שבת"));
            Database.NOTICES.insert(nc2 = new Notice(so6, new Date(), "שבר ווסט", "משפט"));
            Database.NOTICES.insert(nc3 = new Notice(so6, new Date(), "שיקר", "שעתיים ביציאה"));
        } {
            Database.NOTE_TYPES.insert(nt1 = new NoteType(u1, "הערות כיתתיות"));
            Database.NOTE_TYPES.insert(nt2 = new NoteType(so9, "הערות אישיות"));
            Database.NOTE_TYPES.insert(nt3 = new NoteType(so9, "לו\"ז שבועי"));
        } {
            Database.NOTES.insert(n1 = new Note(nt1, "לקבל ציוד", ""));
            Database.NOTES.insert(n2 = new Note(nt1, "לעשות שיעור", ""));
            Database.NOTES.insert(n3 = new Note(nt2, "לעשות אבג", ""));
            Database.NOTES.insert(n4 = new Note(nt2, "לכתוב קוד", ""));
            Database.NOTES.insert(n5 = new Note(nt2, "", "בלה בלה בלה"));
            Database.NOTES.insert(n6 = new Note(nt2, "", "בלה בלה בלה"));
            Database.NOTES.insert(n7 = new Note(nt3, "ראשון", "גוף"));
            Database.NOTES.insert(n8 = new Note(nt3, "שני", "גוף"));
            Database.NOTES.insert(n9 = new Note(nt3, "שלישי", "גוף"));
            Database.NOTES.insert(n10 = new Note(nt3, "רביעי", "גוף"));
        } {
            Database.LOG_TYPES.insert(lt1 = new LogType(u3, "דוח 1", true));
            Database.LOG_TYPES.insert(lt2 = new LogType(u3, "סגור", false));
            Database.LOG_TYPES.insert(lt3 = new LogType(u1, "שיעור מאג", false));
            Database.LOG_TYPES.insert(lt4 = new LogType(u2, "שיעור בטיחות לפני מסע", true));
        } {
            Database.LOGS.insert(l1 = new Log(lt1, new Date()));
            Database.LOGS.insert(l2 = new Log(lt1, new Date()));
            Database.LOGS.insert(l3 = new Log(lt1, new Date()));
            Database.LOGS.insert(l4 = new Log(lt3, new Date()));
        } {
            Database.ENTRIES.insert(en1 = new LogEntry(l4, so9, "נוכח"));
            Database.ENTRIES.insert(en2 = new LogEntry(l3, so1, "ביחידה"));
            Database.ENTRIES.insert(en3 = new LogEntry(l3, so2, "ביחידה"));
            Database.ENTRIES.insert(en4 = new LogEntry(l3, so3, "בית"));
            Database.ENTRIES.insert(en5 = new LogEntry(l3, so4, "ביחידה"));
            Database.ENTRIES.insert(en6 = new LogEntry(l1, so5, "גימלים"));
            Database.ENTRIES.insert(en7 = new LogEntry(l1, so6, "ביחידה"));
            Database.ENTRIES.insert(en8 = new LogEntry(l1, so7, "בדרך ליחידה"));
            Database.ENTRIES.insert(en9 = new LogEntry(l1, so8, "ביחידה"));
            Database.ENTRIES.insert(en10 = new LogEntry(l1, so9, "ביחידה"));
        }
    }

    public static int stringCompare(String s1, String s2){
        //if s1 is empty and s2 is not, then s1 < s2
        if (s1 == null || s1.equals("")){
            return s2 == null || s2.equals("") ? 0 : -1;
        }
        //if s2 is empty and s1 is not then s1 > s2
        if (s2 == null || s2 == ""){
            return 1;
        }
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();
        if (s1.equals(s2)){
            return 0;
        }

        int i = 0;
        while (i < s1.length()){
            //if s1 is longer then s2 and all chars prior are equal, then s1 > s2
            if (i >= s2.length()){ return 1; }
            if (s1.charAt(i) == s2.charAt(i)) {
                i++;
            } else {
                return s1.charAt(i) > s2.charAt(i) ? 1 : -1;
            }
        }

        return 0;
    }
}
