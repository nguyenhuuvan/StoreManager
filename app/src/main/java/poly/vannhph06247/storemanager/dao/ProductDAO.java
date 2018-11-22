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
import poly.vannhph06247.storemanager.model.ListProduct;
import poly.vannhph06247.storemanager.model.Product;

public class ProductDAO implements Constant {
    private final DatabaseHelper databaseHelper;

    public ProductDAO(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public long insertProduct(Product product) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PRODUCT_ID, product.getProductID());
        contentValues.put(PRODUCT_NAME, product.getProductName());
        contentValues.put(PRODUCT_TYPEID, product.getProductTypeID());
        contentValues.put(INPUT_PRICE, product.getInputPrice());
        contentValues.put(OUTPUT_PRICE, product.getOutputPrice());
        contentValues.put(QUANTITY, product.getQuantity());
        contentValues.put(PRODUCT_DES, product.getDescription());

        contentValues.put(OLD_PRODUCT_NAME, product.getOldProductName());
        contentValues.put(OLD_PRODUCT_TYPEID, product.getOldProductTypeID());
        contentValues.put(OLD_INPUT_PRICE, product.getOldInputPrice());
        contentValues.put(OLD_OUTPUT_PRICE, product.getOldOutputPrice());
        contentValues.put(OLD_QUANTITY, product.getOldQuantity());
        contentValues.put(OLD_PRODUCT_DES, product.getOldDescription());

        contentValues.put(ADDED_DAY, product.getAddedDay());
        contentValues.put(EDITED_DAY, product.getEditedDay());
        long result = db.insert(PRODUCT_TABLE, null, contentValues);
        if (Constant.isDEBUG)
            Log.e("insertProduct", "insertProduct ID : " + product.getProductID());
        db.close();
        return result;
    }

    public long deleteProduct(String ID) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long result = db.delete(PRODUCT_TABLE, PRODUCT_ID + " = ?",
                new String[]{String.valueOf(ID)});
        db.close();
        return result;

    }

    public long updateProduct(Product product) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PRODUCT_NAME, product.getProductName());
        contentValues.put(PRODUCT_TYPEID, product.getProductTypeID());
        contentValues.put(INPUT_PRICE, product.getInputPrice());
        contentValues.put(OUTPUT_PRICE, product.getOutputPrice());
        contentValues.put(QUANTITY, product.getQuantity());
        contentValues.put(PRODUCT_DES, product.getDescription());

        contentValues.put(OLD_PRODUCT_NAME, product.getOldProductName());
        contentValues.put(OLD_PRODUCT_TYPEID, product.getOldProductTypeID());
        contentValues.put(OLD_INPUT_PRICE, product.getOldInputPrice());
        contentValues.put(OLD_OUTPUT_PRICE, product.getOldOutputPrice());
        contentValues.put(OLD_QUANTITY, product.getOldQuantity());
        contentValues.put(OLD_PRODUCT_DES, product.getOldDescription());

        contentValues.put(EDITED_DAY, product.getEditedDay());
        return db.update(PRODUCT_TABLE, contentValues, PRODUCT_ID + " = ?",
                new String[]{String.valueOf(product.getProductID())});
    }

    public List<Product> getAllProduct() {

        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PRODUCT_TABLE;

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
                String productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                String productTypeID = cursor.getString(cursor.getColumnIndex(PRODUCT_TYPEID));
                String description = cursor.getString(cursor.getColumnIndex(PRODUCT_DES));
                int inputPrice = cursor.getInt(cursor.getColumnIndex(INPUT_PRICE));
                int outputPrice = cursor.getInt(cursor.getColumnIndex(OUTPUT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));

                String oldProductName = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_NAME));
                String oldProductTypeID = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_TYPEID));
                String oldDescription = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_DES));
                int oldInputPrice = cursor.getInt(cursor.getColumnIndex(OLD_INPUT_PRICE));
                int oldOutputPrice = cursor.getInt(cursor.getColumnIndex(OLD_OUTPUT_PRICE));
                int oldQuantity = cursor.getInt(cursor.getColumnIndex(OLD_QUANTITY));

                long added_day = cursor.getLong(cursor.getColumnIndex(ADDED_DAY));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                Product product = new Product();
                product.setProductID(productID);

                product.setProductName(productName);
                product.setProductTypeID(productTypeID);
                product.setDescription(description);
                product.setInputPrice(inputPrice);
                product.setOutputPrice(outputPrice);
                product.setQuantity(quantity);

                product.setOldProductName(oldProductName);
                product.setOldProductTypeID(oldProductTypeID);
                product.setOldDescription(oldDescription);
                product.setOldInputPrice(oldInputPrice);
                product.setOldOutputPrice(oldOutputPrice);
                product.setOldQuantity(oldQuantity);

                product.setAddedDay(added_day);
                product.setEditedDay(edited_day);

                productList.add(product);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;

    }

    public List<ListProduct> getAllProductByProductType(String productType) {

        List<ListProduct> listProducts = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PRODUCT_TABLE + " WHERE " + PRODUCT_TYPEID + "= ?";

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{productType});
        if (cursor.moveToFirst()) {
            do {
                String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
                String productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
                ListProduct product = new ListProduct(productID,productName,quantity);
                listProducts.add(product);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listProducts;

    }

    public List<Product> getAllProductByLike(String ID) {

        List<Product> productList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + PRODUCT_TABLE + " WHERE " + PRODUCT_ID + " like '%" + ID + "%'";

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
                String productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                String productTypeID = cursor.getString(cursor.getColumnIndex(PRODUCT_TYPEID));
                String description = cursor.getString(cursor.getColumnIndex(PRODUCT_DES));
                int inputPrice = cursor.getInt(cursor.getColumnIndex(INPUT_PRICE));
                int outputPrice = cursor.getInt(cursor.getColumnIndex(OUTPUT_PRICE));
                int quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));

                String oldProductName = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_NAME));
                String oldProductTypeID = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_TYPEID));
                String oldDescription = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_DES));
                int oldInputPrice = cursor.getInt(cursor.getColumnIndex(OLD_INPUT_PRICE));
                int oldOutputPrice = cursor.getInt(cursor.getColumnIndex(OLD_OUTPUT_PRICE));
                int oldQuantity = cursor.getInt(cursor.getColumnIndex(OLD_QUANTITY));

                long added_day = cursor.getLong(cursor.getColumnIndex(ADDED_DAY));
                long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
                Product product = new Product();
                product.setProductID(productID);

                product.setProductName(productName);
                product.setProductTypeID(productTypeID);
                product.setDescription(description);
                product.setInputPrice(inputPrice);
                product.setOutputPrice(outputPrice);
                product.setQuantity(quantity);

                product.setOldProductName(oldProductName);
                product.setOldProductTypeID(oldProductTypeID);
                product.setOldDescription(oldDescription);
                product.setOldInputPrice(oldInputPrice);
                product.setOldOutputPrice(oldOutputPrice);
                product.setOldQuantity(oldQuantity);

                product.setAddedDay(added_day);
                product.setEditedDay(edited_day);

                productList.add(product);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;

    }

    public Product getProductByID(String ID) {

        Product product = null;

        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.query(PRODUCT_TABLE, new String[]{PRODUCT_ID, PRODUCT_NAME, PRODUCT_TYPEID, INPUT_PRICE, OUTPUT_PRICE, QUANTITY, PRODUCT_DES, OLD_PRODUCT_NAME, OLD_PRODUCT_TYPEID, OLD_INPUT_PRICE, OLD_OUTPUT_PRICE, OLD_QUANTITY, OLD_PRODUCT_DES, ADDED_DAY, EDITED_DAY},
                PRODUCT_ID + "=?", new String[]{ID},
                null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {

            String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
            String productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
            String productTypeID = cursor.getString(cursor.getColumnIndex(PRODUCT_TYPEID));
            String description = cursor.getString(cursor.getColumnIndex(PRODUCT_DES));
            int inputPrice = cursor.getInt(cursor.getColumnIndex(INPUT_PRICE));
            int outputPrice = cursor.getInt(cursor.getColumnIndex(OUTPUT_PRICE));
            int quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));

            String oldProductName = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_NAME));
            String oldProductTypeID = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_TYPEID));
            String oldDescription = cursor.getString(cursor.getColumnIndex(OLD_PRODUCT_DES));
            int oldInputPrice = cursor.getInt(cursor.getColumnIndex(OLD_INPUT_PRICE));
            int oldOutputPrice = cursor.getInt(cursor.getColumnIndex(OLD_OUTPUT_PRICE));
            int oldQuantity = cursor.getInt(cursor.getColumnIndex(OLD_QUANTITY));

            long added_day = cursor.getLong(cursor.getColumnIndex(ADDED_DAY));
            long edited_day = cursor.getLong(cursor.getColumnIndex(EDITED_DAY));
            product = new Product();
            product.setProductID(productID);

            product.setProductName(productName);
            product.setProductTypeID(productTypeID);
            product.setDescription(description);
            product.setInputPrice(inputPrice);
            product.setOutputPrice(outputPrice);
            product.setQuantity(quantity);

            product.setOldProductName(oldProductName);
            product.setOldProductTypeID(oldProductTypeID);
            product.setOldDescription(oldDescription);
            product.setOldInputPrice(oldInputPrice);
            product.setOldOutputPrice(oldOutputPrice);
            product.setOldQuantity(oldQuantity);

            product.setAddedDay(added_day);
            product.setEditedDay(edited_day);
        }
        Objects.requireNonNull(cursor).close();
        return product;

    }
}
