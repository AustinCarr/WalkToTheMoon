package com.example.austin.walktothemoon;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 10;
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
                + " INTEGER NOT NULL CHECK (in_use IN (0,1))," + COLUMN_EXPIRATION + " TEXT" + ")";
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

    /*public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LICENSE_ID, user.getLicenseId());
        values.put(COLUMN_UNAME, user.getName());
        values.put(COLUMN_ADDRESS_STATE, user.getAddressState());
        values.put(COLUMN_REAL_STEPS, user.getRealSteps());
        values.put(COLUMN_BOOSTED_STEPS, user.getBoostedSteps());

        if (getUser(user.getLicenseId()) != null) {
            updateUser(user, values);
        }
        else {
            SQLiteDatabase db = this.getWritableDatabase();
            db.insert(TABLE_USER, null, values);
            db.close();
        }
    }

    public void updateUser(User user, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.update(TABLE_USER, values, COLUMN_LICENSE_ID + "=?", new String[]{String.valueOf(user.getLicenseId())});

        db.close();
    }

    public User getUser(String license_id) {
        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_LICENSE_ID + " =  \"" + license_id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            user.setLicenseId(cursor.getString(0));
            user.setName(cursor.getString(1));
            user.setAddressState(cursor.getString(2));
            user.setRealSteps(Integer.parseInt(cursor.getString(3)));
            user.setBoostedSteps(Integer.parseInt(cursor.getString(4)));
            cursor.close();
        }
        else {
            user = null;
        }
        db.close();
        return user;
    }

    public boolean deleteUser(String license_id) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_USER + " WHERE " + COLUMN_LICENSE_ID + " =  \"" + license_id + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            user.setLicenseId(cursor.getString(0));
            db.delete(TABLE_USER, COLUMN_LICENSE_ID + " = ?",
                    new String[] { String.valueOf(user.getLicenseId()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }

    public void addPowerup(Powerups powerup) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PNAME, powerup.getName());
        values.put(COLUMN_COST, powerup.getCost());
        values.put(COLUMN_DESCRIPTION, powerup.getDescription());
        values.put(COLUMN_IS_UNLOCKED, powerup.getIsUnlocked());
        values.put(COLUMN_IN_USE, powerup.getInUse());
        values.put(COLUMN_EXPIRATION_DATE, powerup.getExpirationDate());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TABLE_POWERUPS, null, values);
        db.close();
    }

    public Powerups getPowerup(String name) {
        String query = "Select * FROM " + TABLE_POWERUPS + " WHERE " + COLUMN_PNAME + " =  \"" + name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Powerups powerup = new Powerups();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            powerup.setName(cursor.getString(0));
            powerup.setCost(Integer.parseInt(cursor.getString(1)));
            powerup.setDescription(cursor.getString(2));
            powerup.setIsUnlocked(Integer.parseInt(cursor.getString(3)));
            powerup.setInUse(Integer.parseInt(cursor.getString(4)));
            powerup.setExpirationDate(cursor.getString(5));
            cursor.close();
        }
        else {
            powerup = null;
        }
        db.close();
        return powerup;
    }

    public boolean deletePowerup(String name) {

        boolean result = false;

        String query = "Select * FROM " + TABLE_POWERUPS + " WHERE " + COLUMN_PNAME + " =  \"" + name + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        Powerups powerup = new Powerups();

        if (cursor.moveToFirst()) {
            powerup.setName(cursor.getString(0));
            db.delete(TABLE_USER, COLUMN_PNAME + " = ?",
                    new String[] { String.valueOf(powerup.getName()) });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }*/
}
