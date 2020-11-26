package com.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notes.models.Image;
import com.example.notes.models.Note;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class DaoImages {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoImages(Context ctx) {
        this.context = ctx;
        db = new DB(ctx);
        ad = db.getWritableDatabase();
    }

    public void insertImage(long id, ArrayList<Image> images) {
        ContentValues cv;
        for (int i = 0; i < images.size(); i++) {
            cv = new ContentValues();
            cv.put(DB.COLUMNS_TABLEIMAGES[1], id);
            cv.put(DB.COLUMNS_TABLEIMAGES[2], images.get(i).getSrcImage());
            ad.insert(DB.TABLE_IMAGES_NAME, null, cv);
        }
    }

    public ArrayList<Image> getAll(long id) {
        ArrayList<Image> lst = new ArrayList<>();
        Image image;
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_IMAGES_NAME + " where "+ DB.COLUMNS_TABLEIMAGES[1]  + " = ?", new String[]{String.valueOf(id)});

        if (cursor.getCount() >= 0) {
            while (cursor.moveToNext()) {
                image = new Image(cursor.getLong(0), cursor.getLong(1), cursor.getString(2));
                lst.add(image);
            }
        }
        return lst;
    }

    public Note getOneById(long id) {
        Cursor cursor = null;
        Note note = null;
        cursor = ad.rawQuery("select * from " + DB.TABLE_NOTES_NAME + " where " + DB.COLUMS_TABLENOTES[0] + "=?",
                new String[]{String.valueOf(id)});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
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

    public boolean update(Note note) {
        ContentValues cv = new ContentValues();
        cv.put(DB.COLUMS_TABLENOTES[1], note.getTitle());
        cv.put(DB.COLUMS_TABLENOTES[2], note.getContent());
        cv.put(DB.COLUMS_TABLENOTES[3], note.isReminder());
        cv.put(DB.COLUMS_TABLENOTES[4], "");

        return ad.update(
                DB.TABLE_NOTES_NAME, cv, "id=?",
                new String[]{String.valueOf(note.getId())}
        ) > 0;
    }

    public boolean delete(long id) {
        return ad.delete(DB.TABLE_NOTES_NAME, "id=?", new String[]{String.valueOf(id)}) > 0;
    }
}
