package poly.project.storemanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import poly.project.storemanager.constant.Constant;

public class DatabaseHelper extends SQLiteOpenHelper implements Constant {
    private static final String DATABASE_NAME = "StoreManager";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(CREATE_PRODUCT_TYPE_TABLE);
        sqLiteDatabase.execSQL(CREATE_PRODUCT_TABLE);
        sqLiteDatabase.execSQL(CREATE_BILL_TABLE);
        sqLiteDatabase.execSQL(CREATE_CUSTOMER_TABLE);
        sqLiteDatabase.execSQL(CREATE_BILL_DETAIL_TABLE);
        if (isDEBUG) Log.e("CREATE_USER_TABLE", CREATE_USER_TABLE);
        if (isDEBUG) Log.e("CREATE_PRODUCT_TYPE", CREATE_PRODUCT_TYPE_TABLE);
        if (isDEBUG) Log.e("CREATE_PRODUCT_TABLE", CREATE_PRODUCT_TABLE);
        if (isDEBUG) Log.e("CREATE_BILL_TABLE", CREATE_BILL_TABLE);
        if (isDEBUG) Log.e("CREATE_CUSTOMER_TABLE", CREATE_CUSTOMER_TABLE);
        if (isDEBUG) Log.e("CREATE_BILL_DETAIL_TB", CREATE_BILL_DETAIL_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TYPE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PRODUCT_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BILL_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BILL_DETAIL_TABLE);
        onCreate(sqLiteDatabase);
    }
}
