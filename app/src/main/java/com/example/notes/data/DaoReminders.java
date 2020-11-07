package com.example.notes.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notes.models.Reminders;

import java.util.ArrayList;
import java.util.List;

public class DaoReminders {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoReminders(Context ctx){
        this.context = ctx;
        db = new DB(ctx);
        ad = db.getWritableDatabase();
    }

    public Cursor getAllCursor(){
        Cursor cursor = ad.query(DB.TABLE_NOTES_NAME, DB.COLUMS_TABLENOTES,null,null,null,null,null,null);
        return cursor;
    }

    public ArrayList<Reminders> getAll(){
        ArrayList<Reminders> lst = new ArrayList<>();
        Cursor cursor = ad.query(DB.TABLE_NOTES_NAME, DB.COLUMS_TABLENOTES,null,null,null,null,null,null);
        if(cursor.getCount() >= 0){
            while (cursor.moveToNext()){
                lst.add(new Reminders(
                        cursor.getInt(0),cursor.getString(1),cursor.getString(2),new ArrayList<String>(),cursor.getString(3)
                ));
            }
        }
        return lst;
    }
}
