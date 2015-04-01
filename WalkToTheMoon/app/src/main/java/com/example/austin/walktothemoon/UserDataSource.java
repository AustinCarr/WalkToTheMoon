package com.example.austin.walktothemoon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDataSource {
    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;

    public UserDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_LICENSE_ID, user.getLicenseId());
        values.put(MySQLiteHelper.COLUMN_UNAME, user.getName());
        values.put(MySQLiteHelper.COLUMN_ADDRESS_STATE, user.getAddressState());
        values.put(MySQLiteHelper.COLUMN_REAL_STEPS, user.getRealSteps());
        values.put(MySQLiteHelper.COLUMN_BOOSTED_STEPS, user.getBoostedSteps());

        //if (getUser(user.getLicenseId()) != null) {
        if (getUser() != null) {
            updateUser(user);
        }
        else {
            db.insert(MySQLiteHelper.TABLE_USER, null, values);
        }
    }

    public void updateUser(User user) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteHelper.COLUMN_LICENSE_ID, user.getLicenseId());
        values.put(MySQLiteHelper.COLUMN_UNAME, user.getName());
        values.put(MySQLiteHelper.COLUMN_ADDRESS_STATE, user.getAddressState());
        values.put(MySQLiteHelper.COLUMN_REAL_STEPS, user.getRealSteps());
        values.put(MySQLiteHelper.COLUMN_BOOSTED_STEPS, user.getBoostedSteps());

        db.update(MySQLiteHelper.TABLE_USER, values, MySQLiteHelper.COLUMN_LICENSE_ID + "=?",
                new String[]{String.valueOf(user.getLicenseId())});
    }

    public User getUser() {
        String query = "Select * FROM " + MySQLiteHelper.TABLE_USER;

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
        return user;
    }

    /*public User getUser(String license_id) {
        String query = "Select * FROM " + MySQLiteHelper.TABLE_USER + " WHERE "
                + MySQLiteHelper.COLUMN_LICENSE_ID + " =  \"" + license_id + "\"";

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
        return user;
    }*/

    public boolean deleteUser(String license_id) {

        boolean result = false;

        String query = "Select * FROM " + MySQLiteHelper.TABLE_USER + " WHERE "
                + MySQLiteHelper.COLUMN_LICENSE_ID + " =  \"" + license_id + "\"";

        Cursor cursor = db.rawQuery(query, null);

        User user = new User();

        if (cursor.moveToFirst()) {
            user.setLicenseId(cursor.getString(0));
            db.delete(MySQLiteHelper.TABLE_USER, MySQLiteHelper.COLUMN_LICENSE_ID + " = ?",
                    new String[] { String.valueOf(user.getLicenseId()) });
            cursor.close();
            result = true;
        }
        return result;
    }
}
