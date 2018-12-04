package poly.project.storemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Objects;

import poly.project.storemanager.constant.Constant;
import poly.project.storemanager.database.DatabaseHelper;
import poly.project.storemanager.model.User;

public class UserDAO implements Constant {
    private final DatabaseHelper databaseHelper;

    public UserDAO(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void insertUser(String user, String pass, String name, String phone, String email, int age) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME, user);
        contentValues.put(COLUMN_PASSWORD, pass);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_PHONE_NUMBER, phone);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_AGE, age);
        db.insert(USER_TABLE, null, contentValues);
        if (Constant.isDEBUG) Log.e("insertUser", "insertUser ID : " + user);
        db.close();
    }

    public long updateUser(String user, String name, String phone, String email, int age) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_PHONE_NUMBER, phone);
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_AGE, age);
        return db.update(USER_TABLE, contentValues, COLUMN_USERNAME + " = ?",
                new String[]{String.valueOf(user)});
    }

    public long updatePassWord(String user, String pass) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PASSWORD, pass);
        return db.update(USER_TABLE, contentValues, COLUMN_USERNAME + " = ?",
                new String[]{String.valueOf(user)});
    }

    public User getUser(String username) {

        User user = null;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(USER_TABLE, new String[]{COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_NAME, COLUMN_PHONE_NUMBER, COLUMN_EMAIL, COLUMN_AGE},
                COLUMN_USERNAME + "=?", new String[]{username},
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            String user1 = cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME));
            String pass = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE_NUMBER));
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            int age = cursor.getInt(cursor.getColumnIndex(COLUMN_AGE));

            user = new User(user1, pass, name, phone, email, age);
        }
        Objects.requireNonNull(cursor).close();
        return user;

    }
}

