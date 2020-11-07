package com.example.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "dbNotes";
    public static  final int DATABASE_VERSION = 1;
    // Reminders table
    public static final String TABLE_REMINDERS_NAME = "reminders";
    public static final String[] COLUMS_TABLEREMINDERS = {
            "id", "title", "information","finishDate","idRemindersDate"
    };
    public static final String SCRIPT_TABLE_REMINDERS=
            "create table reminders(" +
                    "id integer primary key autoincrement," +
                    "title text not null," +
                    "information text not null," +
                    "finishDate text not null," +
                    "idRemindersDate int" +
                    ");";

    // Notes

    Context context;

    // DB creation
    public DB(@Nullable Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    // Place to execute te SQLite code
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCRIPT_TABLE_REMINDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
