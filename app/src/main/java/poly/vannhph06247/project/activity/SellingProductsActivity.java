package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterListProductSelling;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.model.ListProduct;

public class SellingProductsActivity extends Library {
    private RecyclerView lvListSeling;
    private List<ListProduct> listProductList;
    private ImageView imgSort, imgCalender;
    private CustomerDAO customerDAO;
    private TextView tvTotal;
    private TextView tvCalender;
    private AdapterListProductSelling adapterListProduct;
    private String yearNow;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_products);
        initViews();
        initAction();

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateYear = new SimpleDateFormat("yyyy");
        @SuppressLint("SimpleDateFormat") final SimpleDateFormat dateMonth = new SimpleDateFormat("MM");

        String monthNow = dateMonth.format(new Date());
        yearNow = dateYear.format(new Date());
        customerDAO = new CustomerDAO(this);
        listProductList = customerDAO.getProductSellingByMonth(yearNow + "-" + monthNow);
        adapterListProduct = new AdapterListProductSelling(this, listProductList);
        lvListSeling.setAdapter(adapterListProduct);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListSeling.setLayoutManager(manager);
        tvTotal.setText(getString(R.string.total) + " " + listProductList.size() + " " + getString(R.string.product_));
        tvCalender.setText(getString(R.string.month_) + " " + monthNow);
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTotal = findViewById(R.id.tvTotal);
        lvListSeling = findViewById(R.id.lvList);
        tvTotal.setTextColor(Color.BLACK);
        imgSort = findViewById(R.id.imgSort);
        tvCalender = findViewById(R.id.tvCalender);
        imgCalender = findViewById(R.id.imgCalender);
    }


    private void initAction() {
        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(SellingProductsActivity.this, imgSort);
                getMenuInflater().inflate(R.menu.menu_product2, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.up:
                                Collections.sort(listProductList, new Comparator<ListProduct>() {
                                    @Override
                                    public int compare(ListProduct pb1, ListProduct pb2) {
                                        int a = Integer.compare(pb1.getQuantity(), pb2.getQuantity());
                                        if (a == 0) {
                                            return Integer.compare(pb1.getQuantity(), pb2.getQuantity());
                                        }
                                        return a;
                                    }
                                });
                                runLayoutAnimationLeft(lvListSeling);
                                break;
                            case R.id.down:
                                Collections.sort(listProductList, new Comparator<ListProduct>() {
                                    @Override
                                    public int compare(ListProduct pb1, ListProduct pb2) {
                                        int a = Integer.compare(pb2.getQuantity(), pb1.getQuantity());
                                        if (a == 0) {
                                            return Integer.compare(pb2.getQuantity(), pb1.getQuantity());
                                        }
                                        return a;
                                    }
                                });
                                runLayoutAnimationRight(lvListSeling);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(SellingProductsActivity.this, imgCalender);
                getMenuInflater().inflate(R.menu.menu_month_all_day_week, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.all:
                                listProductList = customerDAO.getProductSelling();
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationLeft(lvListSeling);
                                tvCalender.setText(getString(R.string.all));
                                break;
                            case R.id.day:
                                listProductList = customerDAO.getProductSellingByDay();
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationRight(lvListSeling);
                                tvCalender.setText(getString(R.string.day));
                                break;
                            case R.id.week:
                                listProductList = customerDAO.getProductSellingByWeek();
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationLeft(lvListSeling);
                                tvCalender.setText(getString(R.string.week));
                                break;
                            case R.id.month1:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-01");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationRight(lvListSeling);
                                tvCalender.setText(getString(R.string.month1));
                                break;
                            case R.id.month2:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-02");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationLeft(lvListSeling);
                                tvCalender.setText(getString(R.string.month2));
                                break;
                            case R.id.month3:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-03");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationRight(lvListSeling);
                                tvCalender.setText(getString(R.string.month3));
                                break;
                            case R.id.month4:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-04");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationLeft(lvListSeling);
                                tvCalender.setText(getString(R.string.month4));
                                break;
                            case R.id.month5:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-05");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationRight(lvListSeling);
                                tvCalender.setText(getString(R.string.month5));
                                break;
                            case R.id.month6:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-06");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationLeft(lvListSeling);
                                tvCalender.setText(getString(R.string.month6));
                                break;
                            case R.id.month7:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-07");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationRight(lvListSeling);
                                tvCalender.setText(getString(R.string.month7));
                                break;
                            case R.id.month8:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-08");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationLeft(lvListSeling);
                                tvCalender.setText(getString(R.string.month8));
                                break;
                            case R.id.month9:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-09");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationRight(lvListSeling);
                                tvCalender.setText(getString(R.string.month9));
                                break;
                            case R.id.month10:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-10");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationLeft(lvListSeling);
                                tvCalender.setText(getString(R.string.month10));
                                break;
                            case R.id.month11:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-11");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationRight(lvListSeling);
                                tvCalender.setText(getString(R.string.month11));
                                break;
                            case R.id.month12:
                                listProductList = customerDAO.getProductSellingByMonth(yearNow + "-12");
                                adapterListProduct.changeDataset(listProductList);
                                runLayoutAnimationLeft(lvListSeling);
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
}
