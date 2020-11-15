package com.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.notes.models.Note;

import java.util.ArrayList;

public class DaoNotes {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoNotes(Context ctx){
        this.context = ctx;
        db = new DB(ctx);
        ad = db.getWritableDatabase();
    }

    public long insertNote (Note note){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLENOTES[1],note.getTitle());
        cv.put(DB.COLUMS_TABLENOTES[2],note.getContent());
        cv.put(DB.COLUMS_TABLENOTES[3],note.isReminder());
        return  ad.insert(DB.TABLE_NOTES_NAME,null,cv);
    }

    public ArrayList<Note> getAllNotes(){
        Cursor cursor = ad.rawQuery("select * from "+DB.TABLE_NOTES_NAME + " where isReminder = ?",new String[]{String.valueOf(0)});
        ArrayList<Note> notes = new ArrayList<>();
        if(cursor.getCount() >= 0){
            while (cursor.moveToNext()){
                notes.add(new Note(
                        cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3)
                ));
            }
        }
        return notes;
    }

    public ArrayList<Note> getAll(){
        ArrayList<Note> lst = new ArrayList<>();
        Cursor cursor = ad.query(DB.TABLE_NOTES_NAME, DB.COLUMS_TABLENOTES,null,null,null,null,null,null);
        if(cursor.getCount() >= 0){
            while (cursor.moveToNext()){
                lst.add(new Note(
                        cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3)
                ));
            }
        }
        return lst;
    }

    public Note getOneById(long id){
        Cursor cursor = null;
        Note note = null;
        cursor = ad.rawQuery("select * from "+DB.TABLE_NOTES_NAME + " where "+DB.COLUMS_TABLENOTES[0]+"=?",
                new String[]{String.valueOf(id)});
        if(cursor != null){
            if(cursor.moveToFirst()){
                note = new Note(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3)
                );
            }
        }
        return note;
    }

    public boolean update(Note note){
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLENOTES[1],note.getTitle());
        cv.put(DB.COLUMS_TABLENOTES[2],note.getContent());
        cv.put(DB.COLUMS_TABLENOTES[3],note.isReminder());
        cv.put(DB.COLUMS_TABLENOTES[4], "");

        return ad.update(
                DB.TABLE_NOTES_NAME,cv,"id=?",
                new String[]{String.valueOf(note.getId())}
        )>0;
    }

    public boolean delete(long id){
        return ad.delete(DB.TABLE_NOTES_NAME,"id=?", new String[]{String.valueOf(id)})>0;
    }
}
