package com.example.austin.walktothemoon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class PowerupsDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;
    private Context context;

    public PowerupsDataSource(Context context) {
        this.context = context;
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
        values.put(MySQLiteHelper.COLUMN_IN_USE, powerup.getInUse());
        values.put(MySQLiteHelper.COLUMN_EXPIRATION, powerup.getExpirationDate());

        if (getPowerup(powerup.getName()) != null) {
            updatePowerup(powerup);
        }
        else {
            db.insert(MySQLiteHelper.TABLE_POWERUPS, null, values);
        }
    }

    public void updatePowerup(Powerups powerup) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_PNAME, powerup.getName());
        values.put(MySQLiteHelper.COLUMN_IN_USE, powerup.getInUse());
        values.put(MySQLiteHelper.COLUMN_EXPIRATION, powerup.getExpirationDate());

        db.update(MySQLiteHelper.TABLE_POWERUPS, values, MySQLiteHelper.COLUMN_PNAME + "=?",
                new String[]{String.valueOf(powerup.getName())});
    }

    public Powerups getPowerup(String name) {
        String query = "SELECT * FROM " + MySQLiteHelper.TABLE_POWERUPS + " WHERE "
                + MySQLiteHelper.COLUMN_PNAME + " =  \"" + name + "\"";

        Cursor cursor = db.rawQuery(query, null);

        Powerups powerup = new Powerups();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            powerup.setName(cursor.getString(0));
            powerup.setInUse(Integer.parseInt(cursor.getString(1)));
            powerup.setExpirationDate(cursor.getString(2));
            cursor.close();
        }
        else {
            powerup = null;
        }
        return powerup;
    }

    public ArrayList<String> getActivePowerups() {
        ArrayList<String> activePowerups = new ArrayList<>();

        String query = "SELECT " + MySQLiteHelper.COLUMN_PNAME + " FROM "
                + MySQLiteHelper.TABLE_POWERUPS + " WHERE " + MySQLiteHelper.COLUMN_IN_USE + " =  1";

        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            activePowerups.add(cursor.getString(0));
        }

        cursor.close();

        if (activePowerups.size() == 0)
            activePowerups = null;

        return activePowerups;
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
