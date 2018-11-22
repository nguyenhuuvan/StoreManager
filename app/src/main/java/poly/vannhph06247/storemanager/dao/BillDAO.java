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
import poly.vannhph06247.storemanager.model.Bill;

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

                Bill bill = new Bill();
                bill.setBillID(billID);
                bill.setCustomerPhone(customerPhone);
                bill.setBillDate(billDate);
                bill.setOldCustomerPhone(old_CustomerPhone);
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
        String selectQuery = "SELECT  * FROM " + BILL_TABLE+ " WHERE " + BILLID + " like '%" + ID + "%'";

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String billID = cursor.getString(cursor.getColumnIndex(BILLID));
                String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));
                long billDate = cursor.getLong(cursor.getColumnIndex(BILLDATE));
                String old_CustomerPhone = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMERPHONE));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));

                Bill bill = new Bill();
                bill.setBillID(billID);
                bill.setCustomerPhone(customerPhone);
                bill.setBillDate(billDate);
                bill.setOldCustomerPhone(old_CustomerPhone);
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

        Cursor cursor = db.query(BILL_TABLE, new String[]{BILLID, CUSTOMER_PHONE, BILLDATE, OLD_CUSTOMERPHONE, EDITED_DAY},
                BILLID + "=?", new String[]{ID},
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            String billID = cursor.getString(cursor.getColumnIndex(BILLID));
            String customerPhone = cursor.getString(cursor.getColumnIndex(CUSTOMER_PHONE));
            long billDate = cursor.getLong(cursor.getColumnIndex(BILLDATE));
            String old_CustomerPhone = cursor.getString(cursor.getColumnIndex(OLD_CUSTOMERPHONE));
            long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
            bill = new Bill();
            bill.setBillID(billID);
            bill.setCustomerPhone(customerPhone);
            bill.setBillDate(billDate);
            bill.setOldCustomerPhone(old_CustomerPhone);
            bill.setEditedDay(edited_day);
            bill.setEditedDay(edited_day);
        }
        Objects.requireNonNull(cursor).close();
        return bill;

    }
}
