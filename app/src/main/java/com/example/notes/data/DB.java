package com.example.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dbNotes";
    public static final int DATABASE_VERSION = 1;
    // Notes table
    public static final String TABLE_NOTES_NAME = "notes";
    public static final String[] COLUMS_TABLENOTES = {
            "id", "title", "description", "isReminder", "finishDate"
    };
    public static final String SCRIPT_TABLE_NOTES =
            "create table notes(\n" +
                    "   id integer primary key autoincrement NOT NULL,\n" +
                    "   title text not null,\n" +
                    "   description text not null,\n" +
                    "   isReminder INTEGER NOT NULL,\n" +
                    "   finishDate text\n" +
                    ");";

    // remindersDate
    public static final String TABLE_REMINDERS_DATE = "remindersDate";
    public static final String[] COLUMS_TABLEREMINDERSDATE = {
            "id", "idNote", "dateReminder"
    };
    public static final String SCRIPT_TABLE_REMINDERS_DATE =
            " CREATE TABLE remindersDate(\n" +
                    "   id INTEGER PRIMARY KEY autoincrement NOT NULL,\n" +
                    "   idNote integer NOT NULL,\n" +
                    "   dateReminder TEXT NOT NULL,\n" +
                    "   FOREIGN KEY(idNote) REFERENCES Notes(id)\n" +
                    " );";

    // Images
    public static final String TABLE_IMAGES_NAME = "images";
    public static final String[] COLUMNS_TABLEIMAGES = {
            "id", "idNote", "srcImage"
    };
    public static final String SCRIPT_TABLE_IMAGES =
            "CREATE TABLE images (\n" +
                    "  id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                    "  idNote INTEGER NOT NULL,\n" +
                    "  srcImage TEXT NOT NULL,\n" +
                    "  FOREIGN KEY(idNote) REFERENCES Notes(id)\n" +
                    ");";


    Context context;

    // DB creation
    public DB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Place to execute te SQLite code
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_TABLE_NOTES);
        db.execSQL(SCRIPT_TABLE_IMAGES);
        db.execSQL(SCRIPT_TABLE_REMINDERS_DATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
