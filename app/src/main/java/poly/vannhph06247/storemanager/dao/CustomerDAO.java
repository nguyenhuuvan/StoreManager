package poly.vannhph06247.storemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import poly.vannhph06247.storemanager.constant.Constant;
import poly.vannhph06247.storemanager.database.DatabaseHelper;
import poly.vannhph06247.storemanager.model.Customer;

public class CustomerDAO implements Constant {
    private final DatabaseHelper databaseHelper;

    public CustomerDAO(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public long insertCustomer(Customer customer) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMER_PHONE, customer.getCustomerPhone());

        contentValues.put(CUSTOMER_NAME, customer.getCustomerName());
        contentValues.put(CUSTOMER_SEX, customer.getCustomerSex());
        contentValues.put(CUSTOMER_EMAIL, customer.getCustomerEmail());
        contentValues.put(CUSTOMER_ADDRESS, customer.getCustomerAddress());
        contentValues.put(CUSTOMER_AGE, customer.getCustomerAge());
        contentValues.put(NOTE, customer.getNote());

        contentValues.put(OLD_CUSTOMER_NAME, customer.getOldCustomerName());
        contentValues.put(OLD_CUSTOMER_SEX, customer.getOldCustomerSex());
        contentValues.put(OLD_CUSTOMER_EMAIL, customer.getOldCustomerEmail());
        contentValues.put(OLD_CUSTOMER_ADDRESS, customer.getOldCustomerAddress());
        contentValues.put(OLD_CUSTOMER_AGE, customer.getOldCustomerAge());
        contentValues.put(OLD_NOTE, customer.getOldNote());

        contentValues.put(ADDED_DAY, customer.getAddedDay());
        contentValues.put(EDITED_DAY, customer.getEditedDay());
        long result = db.insert(CUSTOMER_TABLE, null, contentValues);
        if (Constant.isDEBUG)
            Log.e("insertCustomer", "insertCustomer ID : " + customer.getCustomerPhone());
        db.close();
        return result;
    }

    public long deleteCustomer(String ID) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long result = db.delete(CUSTOMER_TABLE, CUSTOMER_PHONE + " = ?",
                new String[]{String.valueOf(ID)});
        db.close();
        return result;

    }

    public long updateCustomer(Customer customer) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CUSTOMER_NAME, customer.getCustomerName());
        contentValues.put(CUSTOMER_SEX, customer.getCustomerSex());
        contentValues.put(CUSTOMER_EMAIL, customer.getCustomerEmail());
        contentValues.put(CUSTOMER_ADDRESS, customer.getCustomerAddress());
        contentValues.put(CUSTOMER_AGE, customer.getCustomerAge());
        contentValues.put(NOTE, customer.getNote());

        contentValues.put(OLD_CUSTOMER_NAME, customer.getOldCustomerName());
        contentValues.put(OLD_CUSTOMER_SEX, customer.getOldCustomerSex());
        contentValues.put(OLD_CUSTOMER_EMAIL, customer.getOldCustomerEmail());
        contentValues.put(OLD_CUSTOMER_ADDRESS, customer.getOldCustomerAddress());
        contentValues.put(OLD_CUSTOMER_AGE, customer.getOldCustomerAge());
        contentValues.put(OLD_NOTE, customer.getOldNote());

        contentValues.put(EDITED_DAY, customer.getEditedDay());
        return db.update(CUSTOMER_TABLE, contentValues, CUSTOMER_PHONE + " = ?",
                new String[]{String.valueOf(customer.getCustomerPhone())});
    }

    public List<Customer> getAllCustomer() {

        List<Customer> customerList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + CUSTOMER_TABLE;

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));

                String customerName = cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME));
                String customerSex = cursor.getString(cursor.getColumnIndex(CUSTOMER_SEX));
                String customerEmail = cursor.getString(cursor.getColumnIndex(CUSTOMER_EMAIL));
                String customerAddress = cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS));
                String note = cursor.getString(cursor.getColumnIndex(NOTE));
                int customerAge = cursor.getInt(cursor.getColumnIndex(CUSTOMER_AGE));

                String oldCustomerName = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_NAME));
                String oldCustomerSex = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_SEX));
                String oldCustomerEmail = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_EMAIL));
                String oldCustomerAddress = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_ADDRESS));
                String oldNote = cursor.getString(cursor.getColumnIndex(OLD_NOTE));
                int oldCustomerAge = cursor.getInt(cursor.getColumnIndex(OLD_CUSTOMER_AGE));

                long added_day = cursor.getLong(cursor.getColumnIndex(ADDED_DAY));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                Customer customer = new Customer();
                customer.setCustomerPhone(customerPhone);

                customer.setCustomerName(customerName);
                customer.setCustomerSex(customerSex);
                customer.setCustomerEmail(customerEmail);
                customer.setCustomerAddress(customerAddress);
                customer.setNote(note);
                customer.setCustomerAge(customerAge);

                customer.setOldCustomerName(oldCustomerName);
                customer.setOldCustomerSex(oldCustomerSex);
                customer.setOldCustomerEmail(oldCustomerEmail);
                customer.setOldCustomerAddress(oldCustomerAddress);
                customer.setOldNote(oldNote);
                customer.setOldCustomerAge(oldCustomerAge);

                customer.setAddedDay(added_day);
                customer.setEditedDay(edited_day);

                customerList.add(customer);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return customerList;

    }

    public List<Customer> getAllCustomerByLike(String ID) {

        List<Customer> customerList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + CUSTOMER_TABLE + " WHERE " + CUSTOMER_PHONE + " like '%" + ID + "%'";

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));

                String customerName = cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME));
                String customerSex = cursor.getString(cursor.getColumnIndex(CUSTOMER_SEX));
                String customerEmail = cursor.getString(cursor.getColumnIndex(CUSTOMER_EMAIL));
                String customerAddress = cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS));
                String note = cursor.getString(cursor.getColumnIndex(NOTE));
                int customerAge = cursor.getInt(cursor.getColumnIndex(CUSTOMER_AGE));

                String oldCustomerName = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_NAME));
                String oldCustomerSex = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_SEX));
                String oldCustomerEmail = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_EMAIL));
                String oldCustomerAddress = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_ADDRESS));
                String oldNote = cursor.getString(cursor.getColumnIndex(OLD_NOTE));
                int oldCustomerAge = cursor.getInt(cursor.getColumnIndex(OLD_CUSTOMER_AGE));

                long added_day = cursor.getLong(cursor.getColumnIndex(ADDED_DAY));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                Customer customer = new Customer();
                customer.setCustomerPhone(customerPhone);

                customer.setCustomerName(customerName);
                customer.setCustomerSex(customerSex);
                customer.setCustomerEmail(customerEmail);
                customer.setCustomerAddress(customerAddress);
                customer.setNote(note);
                customer.setCustomerAge(customerAge);

                customer.setOldCustomerName(oldCustomerName);
                customer.setOldCustomerSex(oldCustomerSex);
                customer.setOldCustomerEmail(oldCustomerEmail);
                customer.setOldCustomerAddress(oldCustomerAddress);
                customer.setOldNote(oldNote);
                customer.setOldCustomerAge(oldCustomerAge);

                customer.setAddedDay(added_day);
                customer.setEditedDay(edited_day);

                customerList.add(customer);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return customerList;

    }

    public Customer getCustomerByID(String ID) {

        Customer customer = null;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(CUSTOMER_TABLE, new String[]{CUSTOMER_PHONE, CUSTOMER_NAME, CUSTOMER_SEX, CUSTOMER_EMAIL, CUSTOMER_ADDRESS, NOTE, CUSTOMER_AGE,
                        OLD_CUSTOMER_NAME, OLD_CUSTOMER_SEX, OLD_CUSTOMER_EMAIL, OLD_CUSTOMER_ADDRESS, OLD_NOTE, OLD_CUSTOMER_AGE, ADDED_DAY, EDITED_DAY},
                CUSTOMER_PHONE + "=?", new String[]{ID},
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));

            String customerName = cursor.getString(cursor.getColumnIndex(CUSTOMER_NAME));
            String customerSex = cursor.getString(cursor.getColumnIndex(CUSTOMER_SEX));
            String customerEmail = cursor.getString(cursor.getColumnIndex(CUSTOMER_EMAIL));
            String customerAddress = cursor.getString(cursor.getColumnIndex(CUSTOMER_ADDRESS));
            String note = cursor.getString(cursor.getColumnIndex(NOTE));
            int customerAge = cursor.getInt(cursor.getColumnIndex(CUSTOMER_AGE));

            String oldCustomerName = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_NAME));
            String oldCustomerSex = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_SEX));
            String oldCustomerEmail = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_EMAIL));
            String oldCustomerAddress = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMER_ADDRESS));
            String oldNote = cursor.getString(cursor.getColumnIndex(OLD_NOTE));
            int oldCustomerAge = cursor.getInt(cursor.getColumnIndex(OLD_CUSTOMER_AGE));

            long added_day = cursor.getLong(cursor.getColumnIndex(ADDED_DAY));
            long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
            customer = new Customer();
            customer.setCustomerPhone(customerPhone);

            customer.setCustomerName(customerName);
            customer.setCustomerSex(customerSex);
            customer.setCustomerEmail(customerEmail);
            customer.setCustomerAddress(customerAddress);
            customer.setNote(note);
            customer.setCustomerAge(customerAge);

            customer.setOldCustomerName(oldCustomerName);
            customer.setOldCustomerSex(oldCustomerSex);
            customer.setOldCustomerEmail(oldCustomerEmail);
            customer.setOldCustomerAddress(oldCustomerAddress);
            customer.setOldNote(oldNote);
            customer.setOldCustomerAge(oldCustomerAge);

            customer.setAddedDay(added_day);
            customer.setEditedDay(edited_day);
        }
        Objects.requireNonNull(cursor).close();
        return customer;

    }
}
