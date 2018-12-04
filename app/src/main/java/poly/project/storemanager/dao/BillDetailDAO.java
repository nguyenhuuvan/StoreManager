package poly.project.storemanager.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import poly.project.storemanager.constant.Constant;
import poly.project.storemanager.database.DatabaseHelper;
import poly.project.storemanager.model.BillDetail;

public class BillDetailDAO implements Constant {
    private final DatabaseHelper databaseHelper;

    public BillDetailDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);

    }

    public long insertBillDetail(String billID, String productID, int quantity) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BILLID,billID );
        contentValues.put(PRODUCT_ID, productID);
        contentValues.put(QUANTITY, quantity);
        long result = db.insert(BILL_DETAIL_TABLE, null, contentValues);
        if (Constant.isDEBUG) Log.e("insertBillDetail", "insertBillDetail ID : " +result);
        db.close();
        return result;
    }
    public long deleteBillDetail(int billDetailID) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long result = db.delete(BILL_DETAIL_TABLE, BILL_DETAIL_ID + " = ?",
                new String[]{String.valueOf(billDetailID)});
        db.close();
        return result;

    }
    public long updateBillDetail(int billDetailID,String billID, String productID, int quantity) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BILLID,billID );
        contentValues.put(PRODUCT_ID, productID);
        contentValues.put(QUANTITY, quantity);
        return db.update(BILL_DETAIL_TABLE, contentValues, BILL_DETAIL_ID + " = ?",
                new String[]{String.valueOf(billDetailID)});
    }
    public List<BillDetail> getAllBillDetailsByBillID(String ID) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List<BillDetail> billDetails = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BILL_DETAIL_TABLE+" WHERE "+BILLID+ " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ID});
        if (cursor.moveToFirst()) {
            do {
                Integer billDetailID = cursor.getInt(cursor.getColumnIndex(BILL_DETAIL_ID));
                String billID = cursor.getString(cursor.getColumnIndex(BILLID));
                Integer quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
                String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
                BillDetail billDetail = new BillDetail();
                billDetail.setBillDetailID(billDetailID);
                billDetail.setBillID(billID);
                billDetail.setProductID(productID);
                billDetail.setQuantity(quantity);
                billDetails.add(billDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return billDetails;
    }
    public List<BillDetail> getAllBillDetailsByProductID(String ID) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List<BillDetail> billDetails = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + BILL_DETAIL_TABLE+" WHERE "+PRODUCT_ID+ " = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{ID});
        if (cursor.moveToFirst()) {
            do {
                Integer billDetailID = cursor.getInt(cursor.getColumnIndex(BILL_DETAIL_ID));
                String billID = cursor.getString(cursor.getColumnIndex(BILLID));
                Integer quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
                String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
                BillDetail billDetail = new BillDetail();
                billDetail.setBillDetailID(billDetailID);
                billDetail.setBillID(billID);
                billDetail.setProductID(productID);
                billDetail.setQuantity(quantity);
                billDetails.add(billDetail);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return billDetails;
    }

}
