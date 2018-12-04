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
import poly.project.storemanager.model.Customer;
import poly.project.storemanager.model.ListProduct;
import poly.project.storemanager.model.ListProductOfCustomer;

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

        contentValues.put(CUSTOMER_PHOTO,customer.getImgcustomer());
        contentValues.put(OLD_CUSTOMER_PHOTO,customer.getOldImgCustomer());

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

        contentValues.put(CUSTOMER_PHOTO,customer.getImgcustomer());
        contentValues.put(OLD_CUSTOMER_PHOTO,customer.getOldImgCustomer());

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

                byte[] imgCustomer = cursor.getBlob(cursor.getColumnIndex(CUSTOMER_PHOTO));
                byte[] oldImgCustomer = cursor.getBlob(cursor.getColumnIndex(OLD_CUSTOMER_PHOTO));

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


                customer.setImgcustomer(imgCustomer);
                customer.setOldImgCustomer(oldImgCustomer);


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

                byte[] imgCustomer = cursor.getBlob(cursor.getColumnIndex(CUSTOMER_PHOTO));
                byte[] oldImgCustomer = cursor.getBlob(cursor.getColumnIndex(OLD_CUSTOMER_PHOTO));

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

                customer.setImgcustomer(imgCustomer);
                customer.setOldImgCustomer(oldImgCustomer);

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
                        OLD_CUSTOMER_NAME, OLD_CUSTOMER_SEX, OLD_CUSTOMER_EMAIL, OLD_CUSTOMER_ADDRESS, OLD_NOTE, OLD_CUSTOMER_AGE, ADDED_DAY, EDITED_DAY,CUSTOMER_PHOTO,OLD_CUSTOMER_PHOTO},
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

            byte[] imgCustomer = cursor.getBlob(cursor.getColumnIndex(CUSTOMER_PHOTO));
            byte[] oldImgCustomer = cursor.getBlob(cursor.getColumnIndex(OLD_CUSTOMER_PHOTO));

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

            customer.setImgcustomer(imgCustomer);
            customer.setOldImgCustomer(oldImgCustomer);


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

    public List<ListProductOfCustomer> getProductofCustomer(String customerPhone) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List<ListProductOfCustomer> productList = new ArrayList<>();
        String selectQuery = "SELECT BillDetail.ProductID,ProductName,BillDetail.Quantity,BILLS.BillDate,PRODUCTS.OutputPrice,PRODUCTS.ProductPhoto from CUSTOMERS inner join " +
                "BILLS on CUSTOMERS.CustomerPhone = BILLS.CustomerPhone inner join BillDetail on BILLS.BillID= BillDetail.BillID" +
                " inner join PRODUCTS on PRODUCTS.ProductID = BillDetail.ProductID" + " WHERE " + "CUSTOMERS.CustomerPhone = ?";
        Cursor cursor = db.rawQuery(selectQuery, new String[]{customerPhone});

        if (cursor.moveToFirst()) {
            do {
                String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
                String productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndex(QUANTITY));
                long billdate = cursor.getLong(cursor.getColumnIndex(BILLDATE));
                int price = cursor.getInt(cursor.getColumnIndex(OUTPUT_PRICE));
                byte[] imgProduct = cursor.getBlob(cursor.getColumnIndex(PRODUCT_PHOTO));

                ListProductOfCustomer listProduct = new ListProductOfCustomer(productID, productName, quantity, billdate, price,imgProduct);
                productList.add(listProduct);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public List<ListProduct> getProductSelling() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List<ListProduct> productList = new ArrayList<>();
        String selectQuery = "SELECT BillDetail.ProductID,ProductName,PRODUCTS.ProductPhoto,SUM(BillDetail.Quantity) as quantity from CUSTOMERS inner join " +
                "BILLS on CUSTOMERS.CustomerPhone = BILLS.CustomerPhone inner join BillDetail on BILLS.BillID= BillDetail.BillID" +
                " inner join PRODUCTS on PRODUCTS.ProductID = BillDetail.ProductID"+" group by BillDetail.ProductID,ProductName,PRODUCTS.ProductPhoto";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
                String productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                byte[] imgProduct = cursor.getBlob(cursor.getColumnIndex(PRODUCT_PHOTO));
                ListProduct listProduct = new ListProduct(productID, productName, quantity,imgProduct);
                productList.add(listProduct);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }
    public List<ListProduct> getProductSellingByDay() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List<ListProduct> productList = new ArrayList<>();
        String selectQuery = "SELECT BillDetail.ProductID,ProductName,PRODUCTS.ProductPhoto,SUM(BillDetail.Quantity) as quantity from CUSTOMERS inner join " +
                "BILLS on CUSTOMERS.CustomerPhone = BILLS.CustomerPhone inner join BillDetail on BILLS.BillID= BillDetail.BillID" +
                " inner join PRODUCTS on PRODUCTS.ProductID = BillDetail.ProductID"+" WHERE strftime('%Y-%m-%d', " + BILLDATE + "/ 1000, 'unixepoch','localtime') like strftime('%Y-%m-%d','now','localtime')"+
                " group by BillDetail.ProductID,ProductName,PRODUCTS.ProductPhoto";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
                String productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                byte[] imgProduct = cursor.getBlob(cursor.getColumnIndex(PRODUCT_PHOTO));
                ListProduct listProduct = new ListProduct(productID, productName, quantity,imgProduct);
                productList.add(listProduct);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public List<ListProduct> getProductSellingByWeek() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List<ListProduct> productList = new ArrayList<>();
        String selectQuery = "SELECT BillDetail.ProductID,ProductName,PRODUCTS.ProductPhoto,SUM(BillDetail.Quantity) as quantity from CUSTOMERS inner join " +
                "BILLS on CUSTOMERS.CustomerPhone = BILLS.CustomerPhone inner join BillDetail on BILLS.BillID= BillDetail.BillID" +
                " inner join PRODUCTS on PRODUCTS.ProductID = BillDetail.ProductID"+" WHERE strftime('%Y-%W', " + BILLDATE + "/ 1000, 'unixepoch','localtime') like strftime('%Y-%W','now','localtime')"+
                " group by BillDetail.ProductID,ProductName,PRODUCTS.ProductPhoto";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
                String productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                byte[] imgProduct = cursor.getBlob(cursor.getColumnIndex(PRODUCT_PHOTO));
                ListProduct listProduct = new ListProduct(productID, productName, quantity,imgProduct);
                productList.add(listProduct);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }
    public List<ListProduct> getProductSellingByMonth(String month) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        List<ListProduct> productList = new ArrayList<>();
        String selectQuery = "SELECT BillDetail.ProductID,ProductName,PRODUCTS.ProductPhoto,SUM(BillDetail.Quantity) as quantity from CUSTOMERS inner join " +
                "BILLS on CUSTOMERS.CustomerPhone = BILLS.CustomerPhone inner join BillDetail on BILLS.BillID= BillDetail.BillID" +
                " inner join PRODUCTS on PRODUCTS.ProductID = BillDetail.ProductID WHERE strftime('%Y-%m', " + BILLDATE + "/ 1000, 'unixepoch','localtime')  like '" + month + "'"+
                " group by BillDetail.ProductID,ProductName,PRODUCTS.ProductPhoto";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String productID = cursor.getString(cursor.getColumnIndex(PRODUCT_ID));
                String productName = cursor.getString(cursor.getColumnIndex(PRODUCT_NAME));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
                byte[] imgProduct = cursor.getBlob(cursor.getColumnIndex(PRODUCT_PHOTO));
                ListProduct listProduct = new ListProduct(productID, productName, quantity,imgProduct);
                productList.add(listProduct);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }
}
