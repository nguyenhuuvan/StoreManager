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
import poly.project.storemanager.model.ProductType;

public class ProductTypeDAO implements Constant {
    private final DatabaseHelper databaseHelper;

    public ProductTypeDAO(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public long insertProductType(ProductType productType) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_TYPE_ID, productType.getProductTypeID());
        contentValues.put(PRODUCTS_TYPE_NAME, productType.getProductTypeName());
        contentValues.put(PRODUCT_TYPE_DES, productType.getProductTypeDes());
        contentValues.put(OLD_PRODUCTS_TYPE_NAME, productType.getOldProductTypeName());
        contentValues.put(OLD_PRODUCT_TYPE_DES, productType.getOldProductTypeDes());
        contentValues.put(ADDED_DAY, productType.getAddedDay());
        contentValues.put(EDITED_DAY, productType.getEditedDay());
        long result = db.insert(PRODUCT_TYPE_TABLE, null, contentValues);
        if (Constant.isDEBUG)
            Log.e("insertProductType", "insertProductType ID : " + productType.getProductTypeID());
        db.close();
        return result;
    }

    public long deleteProductType(String ID) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long result = db.delete(PRODUCT_TYPE_TABLE, PRODUCT_TYPE_ID + " = ?",
                new String[]{String.valueOf(ID)});
        db.close();
        return result;

    }

    public long updateProductType(ProductType productType) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCTS_TYPE_NAME, productType.getProductTypeName());
        contentValues.put(PRODUCT_TYPE_DES, productType.getProductTypeDes());
        contentValues.put(OLD_PRODUCTS_TYPE_NAME, productType.getOldProductTypeName());
        contentValues.put(OLD_PRODUCT_TYPE_DES, productType.getOldProductTypeDes());
        contentValues.put(EDITED_DAY, productType.getEditedDay());
        return db.update(PRODUCT_TYPE_TABLE, contentValues, PRODUCT_TYPE_ID + " = ?",
                new String[]{String.valueOf(productType.getProductTypeID())});
    }

    public List<ProductType> getAllProductType() {

        List<ProductType> productTypes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PRODUCT_TYPE_TABLE;

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(PRODUCT_TYPE_ID));
                String name = cursor.getString(cursor.getColumnIndex(PRODUCTS_TYPE_NAME));
                String description = cursor.getString(cursor.getColumnIndex(PRODUCT_TYPE_DES));
                String old_name = cursor.getString(cursor.getColumnIndex(OLD_PRODUCTS_TYPE_NAME));
                String old_description = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_TYPE_DES));
                long added_day = cursor.getLong(cursor.getColumnIndex(ADDED_DAY));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                ProductType productType = new ProductType();
                productType.setProductTypeID(id);
                productType.setProductTypeName(name);
                productType.setProductTypeDes(description);
                productType.setOldProductTypeName(old_name);
                productType.setOldProductTypeDes(old_description);
                productType.setAddedDay(added_day);
                productType.setEditedDay(edited_day);
                productTypes.add(productType);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productTypes;

    }

    public List<ProductType> getAllProductTypeByLike(String ID) {

        List<ProductType> productTypes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PRODUCT_TYPE_TABLE + " WHERE " + PRODUCT_TYPE_ID + " like '%" + ID + "%'";

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex(PRODUCT_TYPE_ID));
                String name = cursor.getString(cursor.getColumnIndex(PRODUCTS_TYPE_NAME));
                String description = cursor.getString(cursor.getColumnIndex(PRODUCT_TYPE_DES));
                String old_name = cursor.getString(cursor.getColumnIndex(OLD_PRODUCTS_TYPE_NAME));
                String old_description = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_TYPE_DES));
                long added_day = cursor.getLong(cursor.getColumnIndex(ADDED_DAY));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                ProductType productType = new ProductType();
                productType.setProductTypeID(id);
                productType.setProductTypeName(name);
                productType.setProductTypeDes(description);
                productType.setOldProductTypeName(old_name);
                productType.setOldProductTypeDes(old_description);
                productType.setAddedDay(added_day);
                productType.setEditedDay(edited_day);
                productTypes.add(productType);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productTypes;

    }

    public ProductType getProductTypeByID(String ID) {

        ProductType productType = null;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(PRODUCT_TYPE_TABLE, new String[]{PRODUCT_TYPE_ID, PRODUCTS_TYPE_NAME, PRODUCT_TYPE_DES, OLD_PRODUCTS_TYPE_NAME, OLD_PRODUCT_TYPE_DES, ADDED_DAY, EDITED_DAY},
                PRODUCT_TYPE_ID + "=?", new String[]{ID},
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            String id = cursor.getString(cursor.getColumnIndex(PRODUCT_TYPE_ID));
            String name = cursor.getString(cursor.getColumnIndex(PRODUCTS_TYPE_NAME));
            String description = cursor.getString(cursor.getColumnIndex(PRODUCT_TYPE_DES));
            String old_name = cursor.getString(cursor.getColumnIndex(OLD_PRODUCTS_TYPE_NAME));
            String old_description = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_TYPE_DES));
            long added_day = cursor.getLong(cursor.getColumnIndex(ADDED_DAY));
            long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
            productType = new ProductType();
            productType.setProductTypeID(id);
            productType.setProductTypeName(name);
            productType.setProductTypeDes(description);
            productType.setOldProductTypeName(old_name);
            productType.setOldProductTypeDes(old_description);
            productType.setAddedDay(added_day);
            productType.setEditedDay(edited_day);
        }
        Objects.requireNonNull(cursor).close();
        return productType;

    }
}
