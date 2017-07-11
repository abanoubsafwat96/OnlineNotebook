package com.example.abanoub.onlinenotebook.localData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Abanoub on 2017-07-11.
 */

public class Helper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NotesDatabase";
    public static final String NOTES_TABLE_NAME = "NotesTable";
    public static final int DATABASE_VERSION = 1;

    //Columns of notes tables
    public static final String id = "id";
    public static final String title = "title";
    public static final String note = "note";
    public static final String pushId = "pushId";

    public static final String CREATE_NOTES_TABLE = "CREATE TABLE " + NOTES_TABLE_NAME + " (" + id +
            " VARCHAR(225) PRIMARY KEY, " + title + " VARCHAR(225), " + note + " VARCHAR(225), " + pushId + " VARCHAR(225) );";

    public static final String DROP_NOTES_TABLE = "DROP TABLE IF EXIST " + NOTES_TABLE_NAME;


    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_NOTES_TABLE);
        onCreate(db);

    }
}
