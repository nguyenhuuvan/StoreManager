package poly.project.storemanager.constant;

public interface Constant {
    boolean isDEBUG = true;

    // Table User

    String USER_TABLE = "USERS";
    String COLUMN_USERNAME = "Username";
    String COLUMN_PASSWORD = "Password";
    String COLUMN_NAME = "AdminName";
    String COLUMN_PHONE_NUMBER = "AdminPhone";
    String COLUMN_EMAIL = "AdminEmail";
    String COLUMN_AGE = "AdminAge";


    String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + "(" +
            COLUMN_USERNAME + " VARCHAR PRIMARY KEY," +
            COLUMN_PASSWORD + " VARCHAR," +
            COLUMN_NAME + " VARCHAR," +
            COLUMN_EMAIL + " VARCHAR," +
            COLUMN_AGE + " INTEGER," +
            COLUMN_PHONE_NUMBER + " VARCHAR" +
            ")";


    //Table ProductType

    String PRODUCT_TYPE_TABLE = "PRODUCTS_TYPE";
    String PRODUCT_TYPE_ID = "ProductTypeID";
    String PRODUCTS_TYPE_NAME = "ProductTypeName";
    String PRODUCT_TYPE_DES = "ProductTypeDes";
    String OLD_PRODUCTS_TYPE_NAME = "OldProductTypeName";
    String OLD_PRODUCT_TYPE_DES = "OldProductTypeDes";
    String ADDED_DAY = "AddedDay";
    String EDITED_DAY = "EditedDay";

    String CREATE_PRODUCT_TYPE_TABLE = "CREATE TABLE IF NOT EXISTS " + PRODUCT_TYPE_TABLE + "(" +
            PRODUCT_TYPE_ID + " VARCHAR PRIMARY KEY," +
            PRODUCTS_TYPE_NAME + " VARCHAR," +
            PRODUCT_TYPE_DES + " VARCHAR," +
            OLD_PRODUCTS_TYPE_NAME + " VARCHAR," +
            OLD_PRODUCT_TYPE_DES + " VARCHAR," +
            ADDED_DAY + " LONG," +
            EDITED_DAY + " LONG" +
            ")";

    //Table Product

    String PRODUCT_TABLE = "PRODUCTS";

    String PRODUCT_ID = "ProductID";
    String PRODUCT_NAME = "ProductName";
    String PRODUCT_TYPEID = "ProductTypeID";
    String INPUT_PRICE = "InputPrice";
    String OUTPUT_PRICE = "OutputPrice";
    String QUANTITY = "Quantity";
    String PRODUCT_DES = "Description";
    String OLD_PRODUCT_NAME = "OldProductName";
    String OLD_PRODUCT_TYPEID = "OldProductTypeID";
    String OLD_INPUT_PRICE = "OldInputPrice";
    String OLD_OUTPUT_PRICE = "OldOutputPrice";
    String OLD_QUANTITY = "OldQuantity";
    String OLD_PRODUCT_DES = "OldDescription";
    String PRODUCT_PHOTO = "ProductPhoto";
    String OLD_PRODUCT_PHOTO = "OldProductPhoto";

    String CREATE_PRODUCT_TABLE = "CREATE TABLE IF NOT EXISTS " + PRODUCT_TABLE + "(" +
            PRODUCT_ID + " VARCHAR PRIMARY KEY," +
            PRODUCT_NAME + " VARCHAR," +
            PRODUCT_TYPEID + " VARCHAR," +
            INPUT_PRICE + " INTEGER," +
            OUTPUT_PRICE + " INTEGER," +
            QUANTITY + " INTEGER," +
            PRODUCT_DES + " VARCHAR," +
            OLD_PRODUCT_NAME + " VARCHAR," +
            OLD_PRODUCT_TYPEID + " VARCHAR," +
            OLD_INPUT_PRICE + " INTEGER," +
            OLD_OUTPUT_PRICE + " INTEGER," +
            OLD_QUANTITY + " INTEGER," +
            OLD_PRODUCT_DES + " VARCHAR," +
            PRODUCT_PHOTO + " BLOG," +
            OLD_PRODUCT_PHOTO + " BLOG," +
            ADDED_DAY + " LONG," +
            EDITED_DAY + " LONG" +
            ")";

    //Table Bill

    String BILL_TABLE = "BILLS";

    String BILLID = "BillID";
    String CUSTOMER_PHONE ="CustomerPhone";
    String BILLDATE = "BillDate";
    String OLD_CUSTOMERPHONE = "OldCustomerPhone";
    String STATUS = "Status";
    String CREATE_BILL_TABLE = "CREATE TABLE IF NOT EXISTS " + BILL_TABLE + "(" +
            BILLID + " VARCHAR PRIMARY KEY," +
            CUSTOMER_PHONE + " VARCHAR," +
            BILLDATE + " LONG," +
            OLD_CUSTOMERPHONE + " VARCHAR," +
            STATUS + " VARCHAR," +
            EDITED_DAY + " LONG" +
            ")";

    //Table Customer

    String CUSTOMER_TABLE = "CUSTOMERS";

    String CUSTOMER_NAME = "CustomerName";
    String CUSTOMER_SEX = "CustomerSex";
    String CUSTOMER_EMAIL = "CustomerEmail";
    String CUSTOMER_ADDRESS = "CustomerAddress";
    String CUSTOMER_AGE = "CustomerAge";
    String NOTE = "Note";
    String OLD_CUSTOMER_NAME = "OldCustomerName";
    String OLD_CUSTOMER_SEX = "OldCustomerSex";
    String OLD_CUSTOMER_EMAIL = "OldCustomerEmail";
    String OLD_CUSTOMER_ADDRESS = "OldCustomerAddress";
    String OLD_CUSTOMER_AGE = "OldCustomerAge";
    String OLD_NOTE = "OldNote";

    String CUSTOMER_PHOTO = "CustomerPhoto";
    String OLD_CUSTOMER_PHOTO = "OldCustomerPhoto";

    String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS " + CUSTOMER_TABLE + "(" +
            CUSTOMER_PHONE + " VARCHAR PRIMARY KEY," +
            CUSTOMER_NAME + " VARCHAR," +
            CUSTOMER_SEX + " VARCHAR," +
            CUSTOMER_EMAIL + " VARCHAR," +
            CUSTOMER_ADDRESS + " VARCHAR," +
            CUSTOMER_AGE + " INTEGER," +
            NOTE + " VARCHAR," +
            CUSTOMER_PHOTO + " BLOG," +
            OLD_CUSTOMER_NAME + " VARCHAR," +
            OLD_CUSTOMER_SEX + " VARCHAR," +
            OLD_CUSTOMER_EMAIL + " VARCHAR," +
            OLD_CUSTOMER_ADDRESS + " VARCHAR," +
            OLD_CUSTOMER_AGE + " INTEGER," +
            OLD_NOTE + " VARCHAR," +
            OLD_CUSTOMER_PHOTO + " BLOG," +
            ADDED_DAY + " LONG," +
            EDITED_DAY + " LONG" +
            ")";

    String BILL_DETAIL_TABLE = "BillDetail";
    String BILL_DETAIL_ID = "_id";

    String CREATE_BILL_DETAIL_TABLE = "CREATE TABLE IF NOT EXISTS " + BILL_DETAIL_TABLE + "(" +
            "" + BILL_DETAIL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            "" + BILLID + " VARCHAR," +
            "" + PRODUCT_ID + " VARCHAR," +
            "" + QUANTITY + " INT" +
            ")";
}
