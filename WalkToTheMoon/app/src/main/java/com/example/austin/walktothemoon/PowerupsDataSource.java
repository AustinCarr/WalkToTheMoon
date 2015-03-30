package com.example.austin.walktothemoon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Julianne on 3/30/2015.
 */
public class PowerupsDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    public PowerupsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addPowerup(Powerups powerup) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PNAME, powerup.getName());
        values.put(MySQLiteHelper.COLUMN_COST, powerup.getCost());
        values.put(MySQLiteHelper.COLUMN_DESCRIPTION, powerup.getDescription());
        values.put(MySQLiteHelper.COLUMN_IS_UNLOCKED, powerup.getIsUnlocked());
        values.put(MySQLiteHelper.COLUMN_IN_USE, powerup.getInUse());
        values.put(MySQLiteHelper.COLUMN_EXPIRATION_DATE, powerup.getExpirationDate());

        db.insert(MySQLiteHelper.TABLE_POWERUPS, null, values);
    }

    public Powerups getPowerup(String name) {
        String query = "Select * FROM " + MySQLiteHelper.TABLE_POWERUPS + " WHERE "
                + MySQLiteHelper.COLUMN_PNAME + " =  \"" + name + "\"";

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
        return powerup;
    }

    public boolean deletePowerup(String name) {

        boolean result = false;

        String query = "Select * FROM " + MySQLiteHelper.TABLE_POWERUPS + " WHERE "
                + MySQLiteHelper.COLUMN_PNAME + " =  \"" + name + "\"";

        Cursor cursor = db.rawQuery(query, null);

        Powerups powerup = new Powerups();

        if (cursor.moveToFirst()) {
            powerup.setName(cursor.getString(0));
            db.delete(MySQLiteHelper.TABLE_USER, MySQLiteHelper.COLUMN_PNAME + " = ?",
                    new String[] { String.valueOf(powerup.getName()) });
            cursor.close();
            result = true;
        }
        return result;
    }
}
