package com.example.notes.data;

import android.content.ContentValues;
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

    public long insertReminder (Reminders reminders){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLENOTES[1],reminders.getTitle());
        cv.put(DB.COLUMS_TABLENOTES[2],reminders.getContent());
        cv.put(DB.COLUMS_TABLENOTES[3],reminders.isReminder());
        cv.put(DB.COLUMS_TABLENOTES[4],reminders.getFinishDate());
        return  ad.insert(DB.TABLE_NOTES_NAME,null,cv);
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

    public Reminders getOneById(long id){
        Cursor cursor = null;
        Reminders reminder = null;
        cursor = ad.rawQuery("select * from "+DB.TABLE_NOTES_NAME + " where "+DB.COLUMS_TABLENOTES[0]+"=?",
                new String[]{String.valueOf(id)});
        if(cursor != null){
            if(cursor.moveToFirst()){
                reminder = new Reminders(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Boolean.parseBoolean(cursor.getInt(3)+""),
                        cursor.getString(4)
                );
            }
        }
        return reminder;
    }

    public boolean update(Reminders reminder){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLENOTES[1],reminder.getTitle());
        cv.put(DB.COLUMS_TABLENOTES[2],reminder.getContent());
        cv.put(DB.COLUMS_TABLENOTES[3],reminder.isReminder());
        cv.put(DB.COLUMS_TABLENOTES[4], reminder.getFinishDate());

        return ad.update(
                DB.TABLE_NOTES_NAME,cv,"id=?",
                new String[]{String.valueOf(reminder.getId())}
        )>0;
    }

    public boolean delete(long id){
        return ad.delete(DB.TABLE_NOTES_NAME,"id=?", new String[]{String.valueOf(id)})>0;
    }
}
