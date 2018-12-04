package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import poly.project.storemanager.R;
import poly.project.storemanager.dao.StatisticDAO;

public class StatisticActivity extends AppCompatActivity {
    private BarChart barChart;
    private ArrayList<String> dates;
    private ArrayList<BarEntry> barEntries;
    private String month;
    private String yearNow;
    private StatisticDAO statisticDAO;
    private ImageView imgCalender;
    private TextView tvCalender;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        statisticDAO = new StatisticDAO(this);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateYear = new SimpleDateFormat("yyyy");
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateMonth = new SimpleDateFormat("MM");
        month = dateMonth.format(new Date());

        yearNow = dateYear.format(new Date());
        initView();
        iniAction();
        tvCalender.setText(getString(R.string.month_) + " " + month);
        resetBarGraph();
    }
    private void resetBarGraph(){
        barChart.clear();
        switch (checkDayofMonth(Integer.parseInt(month))){
            case 31:
                creatBarGraph(yearNow + "/" + month + "/01", yearNow + "/" + month + "/31");
                break;
            case 30:
                creatBarGraph(yearNow + "/" + month + "/01", yearNow + "/" + month + "/30");
                break;
            case 29:
                creatBarGraph(yearNow + "/" + month + "/01", yearNow + "/" + month + "/29");
                break;
            case 28:
                creatBarGraph(yearNow + "/" + month + "/01", yearNow + "/" + month + "/28");
                break;
        }
    }
    private void iniAction() {
        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(StatisticActivity.this, imgCalender);
                getMenuInflater().inflate(R.menu.menu_month, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.month1:
                                month="01";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month1));
                                break;
                            case R.id.month2:
                                month="02";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month2));
                                break;
                            case R.id.month3:
                                month="03";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month3));
                                break;
                            case R.id.month4:
                                month="04";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month4));
                                break;
                            case R.id.month5:
                                month="05";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month5));
                                break;
                            case R.id.month6:
                                month="06";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month6));
                                break;
                            case R.id.month7:
                                month="07";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month7));
                                break;
                            case R.id.month8:
                                month="08";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month8));
                                break;
                            case R.id.month9:
                                month="09";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month9));
                                break;
                            case R.id.month10:
                                month="10";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month10));
                                break;
                            case R.id.month11:
                                month="11";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month11));
                                break;
                            case R.id.month12:
                                month="12";
                                resetBarGraph();
                                tvCalender.setText(getString(R.string.month12));
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        barChart = findViewById(R.id.bargraph);
        imgCalender = findViewById(R.id.imgCalender);
        tvCalender = findViewById(R.id.tvCalender);
    }

    private void creatBarGraph(String Date1, String Date2) {

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");

        try {
            Date date1 = simpleDateFormat.parse(Date1);
            Date date2 = simpleDateFormat.parse(Date2);

            Calendar mDate1 = Calendar.getInstance();
            Calendar mDate2 = Calendar.getInstance();
            mDate1.clear();
            mDate2.clear();

            mDate1.setTime(date1);
            mDate2.setTime(date2);

            dates = new ArrayList<>();
            dates = getList(mDate1, mDate2);
            //Lấy ngày

            barEntries = new ArrayList<>();

            for (int j = 0; j < dates.size(); j++) {
                String day;
                if (j < 9) {
                    day = "0" + (j + 1);
                } else {
                    day = String.valueOf((j + 1));
                }
                double value = statisticDAO.getStatisticByDay(yearNow + "/" + month + "/" + day);
                barEntries.add(new BarEntry((float) value, j));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        BarDataSet barDataSet = new BarDataSet(barEntries, getString(R.string.revenue));
        BarData barData = new BarData(dates, barDataSet);
        //Truyền ngày và doanh thu vào biểu đồ
        barChart.setData(barData);
        barChart.setDescription(getString(R.string.month_) + " " + month);

    }


    private ArrayList<String> getList(Calendar startDate, Calendar endDate) {
        ArrayList<String> list = new ArrayList<>();
        while (startDate.compareTo(endDate) <= 0) {
            // Vòng lặp đến khi startDate = endDate
            // Truyền vào list
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return list;
    }

    @SuppressLint("SimpleDateFormat")
    private String getDate(Calendar cld) {
        //Chuyển  Calender sang string
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/"
                + cld.get(Calendar.DAY_OF_MONTH);
        try {
            @SuppressLint("SimpleDateFormat") Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate = new SimpleDateFormat("dd").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return curDate;
    }

    private int checkDayofMonth(int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                if (Integer.parseInt(yearNow) % 4 == 0 && Integer.parseInt(yearNow) % 100 != 0) {
                    return 29;
                } else {
                    return 28;
                }
            default:
                return 0;

        }
    }
}
