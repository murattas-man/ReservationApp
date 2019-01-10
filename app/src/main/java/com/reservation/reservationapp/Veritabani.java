package com.reservation.reservationapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Dominic on 07/04/2015.
 */
public class Veritabani extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "dbReservation";
    public static final String TABLE_NAME = "rezervastion_table";
    public static final String C_ID = "_id";
    public static final String FULLNAME = "fullname";
    public static final String PHONENUMBER = "phone";
    public static final String PARTY = "party";
    public static final String NOTES = "notes";
    public static final String TIME = "time";
    public static final String DATE = "date";
    public static final String ALARMKON ="kontrol";
    public static final String MASANO = "masa";
    public static final String R_YEAR = "yil";
    public static final String R_MONTH = "ay";
    public static final String R_DAY = "gun";
    public static final String R_HOUR = "saat";
    public static final String R_MINUTE = "dakika";
    public static final int VERSION = 1;

    private SQLiteDatabase db;

    private final String createDB = "create table if not exists " + TABLE_NAME + " ( "
            + C_ID + " integer primary key autoincrement, "
            + FULLNAME + " text, "
            + PARTY + " text, "
            + PHONENUMBER + " text, "
            +NOTES + " text,"
            + DATE + " text,"
            +MASANO+ " integer,"
            + R_YEAR + " integer,"
            + R_MONTH + " integer,"
            + R_DAY + " integer,"
            + R_HOUR + " integer,"
            +R_MINUTE + " integer,"
            + ALARMKON + " integer)";


    public Veritabani(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(createDB);
            // db.execSQL(createDBTWO);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table " + TABLE_NAME);

       // db.execSQL("drop table " + TABLE_ALARM);
    }
    public void open() {
        try {
            db = getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void close() {
        if (db != null && db.isOpen()) {
            try {
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public Cursor getAll(String sql) {
        open();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        close();
        return cursor;
    }
    public long insert(String table, ContentValues values) {
        open();
        long index = db.insert(table, null, values);
        close();
        return index;
    }
    public boolean update(String table, ContentValues values, String where) {
        open();
        long index = db.update(table, values, where, null);
        close();
        return index > 0;
    }
    public boolean delete(String table, String where) {
        open();
        long index = db.delete(table, where, null);
        close();
        return index > 0;
    }
    public Note getNote(String sql) {
        Note note = null;
        Cursor cursor = getAll(sql);
        if (cursor != null) {
            note = cursorToNote(cursor);
            cursor.close();
        }
        return note;
    }
    public ArrayList<Note> getListNote(String sql) {
        ArrayList<Note> list = new ArrayList<>();
        Cursor cursor = getAll(sql);

        while (!cursor.isAfterLast()) {
            list.add(cursorToNote(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }
    public long insertNote(Note note) {
        return insert(TABLE_NAME, noteToValues(note));
    }
    public boolean updateNote(Note note) {
        return update(TABLE_NAME, noteToValues(note), C_ID + " = " + note.getId());
    }
    public boolean deleteNote(String where) {
        return delete(TABLE_NAME, where);
    }
    private ContentValues noteToValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(FULLNAME, note.getFullname());
        values.put(PHONENUMBER, note.getPhone());
        values.put(PARTY, note.getParty());
        values.put(MASANO, note.getMasa());
        values.put(DATE, note.getDate());
        values.put(R_YEAR, note.getYil());
        values.put(R_MONTH, note.getAy());
        values.put(R_DAY, note.getGun());
        values.put(R_HOUR, note.getSaat());
        values.put(R_MINUTE, note.getDakika());
        values.put(NOTES, note.getNotes());
        values.put(ALARMKON,note.getKontrol());
        return values;

    }
    public Note cursorToNote(Cursor cursor) {
        Note note = new Note();
        note.setID(cursor.getInt(cursor.getColumnIndex(C_ID)))
                .setFullname(cursor.getString(cursor.getColumnIndex(FULLNAME)))
                .setPhone(cursor.getString(cursor.getColumnIndex(PHONENUMBER)))
                .setParty(cursor.getString(cursor.getColumnIndex(PARTY)))
                .setNotes(cursor.getString(cursor.getColumnIndex(NOTES)))
                .setDate(cursor.getString(cursor.getColumnIndex(DATE)))
                .setYil(cursor.getInt(cursor.getColumnIndex(R_YEAR)))
                .setAy(cursor.getInt(cursor.getColumnIndex(R_MONTH)))
                .setGun(cursor.getInt(cursor.getColumnIndex(R_DAY)))
                .setSaat(cursor.getInt(cursor.getColumnIndex(R_HOUR)))
                .setDakika(cursor.getInt(cursor.getColumnIndex(R_MINUTE)))
                .setMasa(cursor.getInt(cursor.getColumnIndex(MASANO)))
                .setKontrol(cursor.getInt(cursor.getColumnIndex(ALARMKON)));
        return note;
    }

}