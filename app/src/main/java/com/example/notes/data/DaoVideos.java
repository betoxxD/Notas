package com.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notes.models.Image;

import java.util.ArrayList;

public class DaoVideos {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoVideos(Context ctx) {
        this.context = ctx;
        db = new DB(ctx);
        ad = db.getWritableDatabase();
    }

    public void insertVideo(long id, ArrayList<String> images) {
        ContentValues cv;
        for (int i = 0; i < images.size(); i++) {
            cv = new ContentValues();
            cv.put(DB.COLUMNS_TABLEVIDEOS[1], id);
            cv.put(DB.COLUMNS_TABLEVIDEOS[2], images.get(i));
            ad.insert(DB.TABLE_VIDEOS_NAME, null, cv);
        }
    }

    public ArrayList<String> getAll(long id) {
        ArrayList<String> lst = new ArrayList<>();
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_VIDEOS_NAME + " where "+ DB.COLUMNS_TABLEVIDEOS[1]  + " = ?", new String[]{String.valueOf(id)});

        if (cursor.getCount() >= 0) {
            while (cursor.moveToNext()) {
                lst.add(cursor.getString(2));
            }
        }
        return lst;
    }
}
