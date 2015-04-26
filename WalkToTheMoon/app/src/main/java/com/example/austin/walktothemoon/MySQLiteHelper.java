package com.example.austin.walktothemoon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 19;

    private static final String DATABASE_NAME = "walkToTheMoonDB.db";

    public static final String TABLE_USER = "user";
    public static final String COLUMN_LICENSE_ID = "license_id";
    public static final String COLUMN_UNAME = "name";
    public static final String COLUMN_ADDRESS_STATE = "address_state";
    public static final String COLUMN_REAL_STEPS = "real_steps";
    public static final String COLUMN_BOOSTED_STEPS = "boosted_steps";

    public static final String TABLE_POWERUPS = "powerups";
    public static final String COLUMN_PNAME = "name";
    public static final String COLUMN_IN_USE = "in_use";
    public static final String COLUMN_EXPIRATION = "expiration";


    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER_TABLE = "CREATE TABLE " +
                TABLE_USER + "("
                + COLUMN_LICENSE_ID + " TEXT PRIMARY KEY NOT NULL," + COLUMN_UNAME
                + " TEXT NOT NULL," + COLUMN_ADDRESS_STATE + " TEXT NOT NULL," + COLUMN_REAL_STEPS
                + " INTEGER NOT NULL," + COLUMN_BOOSTED_STEPS + " INTEGER NOT NULL" + ")";
        String CREATE_POWERUPS_TABLE = "CREATE TABLE " +
                TABLE_POWERUPS + "("
                + COLUMN_PNAME + " TEXT PRIMARY KEY NOT NULL," + COLUMN_IN_USE
                + " INTEGER NOT NULL CHECK (in_use IN (-2,-1,0,1))," + COLUMN_EXPIRATION + " TEXT" + ")";

        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_POWERUPS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POWERUPS);
        onCreate(db);
    }
}
