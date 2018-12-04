package poly.project.storemanager.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import poly.project.storemanager.constant.Constant;
import poly.project.storemanager.database.DatabaseHelper;

public class StatisticDAO implements Constant {

    private final DatabaseHelper databaseHelper;

    public StatisticDAO(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public double getStatisticByDay(String day) {
        double statistic = 0;
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String sSQL = "SELECT SUM(statistic) from (SELECT SUM(PRODUCTS.OutputPrice * BillDetail.Quantity) as statistic " +
                "FROM BILLS INNER JOIN BillDetail on BILLS.BillID = BillDetail.BillID " +
                "INNER JOIN PRODUCTS on BillDetail.ProductID = PRODUCTS.ProductID WHERE strftime('%Y/%m/%d' , " + BILLDATE + "/1000, 'unixepoch','localtime') like '"+day+"')";
        Cursor c = db.rawQuery(sSQL, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            statistic = c.getDouble(0);
            c.moveToNext();
        }
        c.close();
        return statistic;
    }
}
