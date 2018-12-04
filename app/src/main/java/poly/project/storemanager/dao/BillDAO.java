package poly.project.storemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.constant.Constant;
import poly.project.storemanager.database.DatabaseHelper;
import poly.project.storemanager.model.Bill;

public class BillDAO implements Constant {
    private final DatabaseHelper databaseHelper;

    public BillDAO(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public long insertBill(Bill bill) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BILLID, bill.getBillID());
        contentValues.put(CUSTOMER_PHONE, bill.getCustomerPhone());
        contentValues.put(BILLDATE, bill.getBillDate());
        contentValues.put(OLD_CUSTOMERPHONE, bill.getOldCustomerPhone());
        contentValues.put(STATUS, bill.getStatus());
        contentValues.put(EDITED_DAY, bill.getEditedDay());
        long result = db.insert(BILL_TABLE, null, contentValues);
        if (Constant.isDEBUG)
            Log.e("insertBill", "insertBill ID : " + bill.getBillID());
        db.close();
        return result;
    }

    public long deleteBill(String ID) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long result = db.delete(BILL_TABLE, BILLID + " = ?",
                new String[]{String.valueOf(ID)});
        db.close();
        return result;

    }

    public long updateBill(Bill bill) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUSTOMER_PHONE, bill.getCustomerPhone());
        contentValues.put(OLD_CUSTOMERPHONE, bill.getOldCustomerPhone());
        contentValues.put(EDITED_DAY, bill.getEditedDay());
        return db.update(BILL_TABLE, contentValues, BILLID + " = ?",
                new String[]{String.valueOf(bill.getBillID())});
    }

    public long updateStatus(String billID, String status) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(STATUS, status);
        return db.update(BILL_TABLE, contentValues, BILLID + " = ?",
                new String[]{String.valueOf(billID)});
    }

    public List<Bill> getAllBill() {

        List<Bill> billList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BILL_TABLE;

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String billID = cursor.getString(cursor.getColumnIndex(BILLID));
                String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));
                long billDate = cursor.getLong(cursor.getColumnIndex(BILLDATE));
                String old_CustomerPhone = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMERPHONE));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                String status = cursor.getString(cursor.getColumnIndex(STATUS));
                Bill bill = new Bill();
                bill.setBillID(billID);
                bill.setCustomerPhone(customerPhone);
                bill.setBillDate(billDate);
                bill.setOldCustomerPhone(old_CustomerPhone);
                bill.setStatus(status);
                bill.setEditedDay(edited_day);
                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return billList;

    }
    public List<Bill> getAllBillOfDay() {

        List<Bill> billList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BILL_TABLE+" WHERE strftime('%Y-%m-%d', " + BILLDATE + "/ 1000, 'unixepoch','localtime') like strftime('%Y-%m-%d','now','localtime')";

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String billID = cursor.getString(cursor.getColumnIndex(BILLID));
                String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));
                long billDate = cursor.getLong(cursor.getColumnIndex(BILLDATE));
                String old_CustomerPhone = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMERPHONE));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                String status = cursor.getString(cursor.getColumnIndex(STATUS));
                Bill bill = new Bill();
                bill.setBillID(billID);
                bill.setCustomerPhone(customerPhone);
                bill.setBillDate(billDate);
                bill.setOldCustomerPhone(old_CustomerPhone);
                bill.setStatus(status);
                bill.setEditedDay(edited_day);
                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return billList;

    }
    public List<Bill> getAllBillOfWeek() {

        List<Bill> billList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BILL_TABLE+" WHERE strftime('%Y-%W', " + BILLDATE + "/ 1000, 'unixepoch','localtime') like strftime('%Y-%W','now','localtime')";

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String billID = cursor.getString(cursor.getColumnIndex(BILLID));
                String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));
                long billDate = cursor.getLong(cursor.getColumnIndex(BILLDATE));
                String old_CustomerPhone = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMERPHONE));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                String status = cursor.getString(cursor.getColumnIndex(STATUS));
                Bill bill = new Bill();
                bill.setBillID(billID);
                bill.setCustomerPhone(customerPhone);
                bill.setBillDate(billDate);
                bill.setOldCustomerPhone(old_CustomerPhone);
                bill.setStatus(status);
                bill.setEditedDay(edited_day);
                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return billList;

    }
    public List<Bill> getAllBillOfMonth() {

        List<Bill> billList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BILL_TABLE+" WHERE strftime('%Y-%m', " + BILLDATE + "/ 1000, 'unixepoch','localtime') like strftime('%Y-%m','now','localtime')";

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String billID = cursor.getString(cursor.getColumnIndex(BILLID));
                String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));
                long billDate = cursor.getLong(cursor.getColumnIndex(BILLDATE));
                String old_CustomerPhone = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMERPHONE));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                String status = cursor.getString(cursor.getColumnIndex(STATUS));
                Bill bill = new Bill();
                bill.setBillID(billID);
                bill.setCustomerPhone(customerPhone);
                bill.setBillDate(billDate);
                bill.setOldCustomerPhone(old_CustomerPhone);
                bill.setStatus(status);
                bill.setEditedDay(edited_day);
                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return billList;

    }
    public List<Bill> getAllBillOfYear() {

        List<Bill> billList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BILL_TABLE+" WHERE strftime('%Y', " + BILLDATE + "/ 1000, 'unixepoch','localtime') like strftime('%Y','now','localtime')";

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String billID = cursor.getString(cursor.getColumnIndex(BILLID));
                String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));
                long billDate = cursor.getLong(cursor.getColumnIndex(BILLDATE));
                String old_CustomerPhone = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMERPHONE));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                String status = cursor.getString(cursor.getColumnIndex(STATUS));
                Bill bill = new Bill();
                bill.setBillID(billID);
                bill.setCustomerPhone(customerPhone);
                bill.setBillDate(billDate);
                bill.setOldCustomerPhone(old_CustomerPhone);
                bill.setStatus(status);
                bill.setEditedDay(edited_day);
                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return billList;

    }
    public List<Bill> getAllBillByLike(String ID) {

        List<Bill> billList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BILL_TABLE + " WHERE " + BILLID + " like '%" + ID + "%'";

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String billID = cursor.getString(cursor.getColumnIndex(BILLID));
                String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));
                long billDate = cursor.getLong(cursor.getColumnIndex(BILLDATE));
                String old_CustomerPhone = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMERPHONE));
                String status = cursor.getString(cursor.getColumnIndex(STATUS));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));

                Bill bill = new Bill();
                bill.setBillID(billID);
                bill.setCustomerPhone(customerPhone);
                bill.setBillDate(billDate);
                bill.setOldCustomerPhone(old_CustomerPhone);
                bill.setStatus(status);
                bill.setEditedDay(edited_day);
                billList.add(bill);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return billList;

    }

    public Bill getBillByID(String ID) {

        Bill bill = null;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(BILL_TABLE, new String[]{BILLID, CUSTOMER_PHONE, BILLDATE, OLD_CUSTOMERPHONE, EDITED_DAY, STATUS},
                BILLID + "=?", new String[]{ID},
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            String billID = cursor.getString(cursor.getColumnIndex(BILLID));
            String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));
            long billDate = cursor.getLong(cursor.getColumnIndex(BILLDATE));
            String old_CustomerPhone = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMERPHONE));
            long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
            String status = cursor.getString(cursor.getColumnIndex(STATUS));
            bill = new Bill();
            bill.setBillID(billID);
            bill.setCustomerPhone(customerPhone);
            bill.setBillDate(billDate);
            bill.setOldCustomerPhone(old_CustomerPhone);
            bill.setEditedDay(edited_day);
            bill.setStatus(status);
            bill.setEditedDay(edited_day);
        }
        Objects.requireNonNull(cursor).close();
        return bill;

    }
}
