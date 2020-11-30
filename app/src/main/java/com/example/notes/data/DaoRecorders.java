package com.example.notes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notes.models.Image;

import java.util.ArrayList;

public class DaoRecorders {
    Context context;
    DB db;
    SQLiteDatabase ad;

    public DaoRecorders(Context ctx) {
        this.context = ctx;
        db = new DB(ctx);
        ad = db.getWritableDatabase();
    }

    public void insertRecorder(long id, ArrayList<String> recorders) {
        ContentValues cv;
        for (int i = 0; i < recorders.size(); i++) {
            cv = new ContentValues();
            cv.put(DB.COLUMNS_TABLERECORDERS[1], id);
            cv.put(DB.COLUMNS_TABLERECORDERS[2], recorders.get(i));
            ad.insert(DB.TABLE_RECORDERS_NAME, null, cv);
        }
    }

    public ArrayList<String> getAll(long id) {
        ArrayList<String> lst = new ArrayList<>();
        Cursor cursor = ad.rawQuery("select * from " + DB.TABLE_RECORDERS_NAME + " where "+ DB.COLUMNS_TABLERECORDERS[1]  + " = ?", new String[]{String.valueOf(id)});

        if (cursor.getCount() >= 0) {
            while (cursor.moveToNext()) {
                lst.add(cursor.getString(2));
            }
        }
        return lst;
    }
}
