package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterListProductOfCustomer;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.model.ListProductOfCustomer;

public class ProductListOfCustomerActivity extends Library {
    private RecyclerView lvListProductType;
    private List<ListProductOfCustomer> listProductList;
    private ImageView imgSort;
    private TextView tvTotal;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_of_customer);
        initViews();
        initAction();
        String customerPhone = getIntent().getStringExtra("customerPhone");
        CustomerDAO customerDAO = new CustomerDAO(this);
        listProductList = customerDAO.getProductofCustomer(customerPhone);
        AdapterListProductOfCustomer adapterListProduct = new AdapterListProductOfCustomer(this, listProductList);
        lvListProductType.setAdapter(adapterListProduct);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListProductType.setLayoutManager(manager);
        tvTotal.setText(getString(R.string.total)+" " + listProductList.size() + " "+getString(R.string.product_));
    }
    @SuppressLint("SetTextI18n")
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTotal= findViewById(R.id.tvTotal);

        lvListProductType = findViewById(R.id.lvList);
        imgSort = findViewById(R.id.imgSort);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initAction() {
        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(ProductListOfCustomerActivity.this, imgSort);
                getMenuInflater().inflate(R.menu.menu_product,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.sort_by_name:
                                Collections.sort(listProductList, new Comparator<ListProductOfCustomer>() {
                                    @Override
                                    public int compare(ListProductOfCustomer pb1, ListProductOfCustomer pb2) {
                                        int a = pb1.getProductName().compareToIgnoreCase(pb2.getProductName());
                                        if (a == 0) {
                                            return pb1.getProductName().compareToIgnoreCase(pb2.getProductName());
                                        }
                                        return a;
                                    }
                                });
                                runLayoutAnimationLeft(lvListProductType);
                                break;
                            case R.id.sort_by_id:
                                Collections.sort(listProductList, new Comparator<ListProductOfCustomer>() {
                                    @Override
                                    public int compare(ListProductOfCustomer pb1, ListProductOfCustomer pb2) {
                                        int a = pb1.getProductId().compareToIgnoreCase(pb2.getProductId());
                                        if (a == 0) {
                                            return pb1.getProductId().compareToIgnoreCase(pb2.getProductId());
                                        }
                                        return a;
                                    }
                                });
                                runLayoutAnimationRight(lvListProductType);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @Override
    protected void onResume() {
        runLayoutAnimationLeft(lvListProductType);
        super.onResume();
    }
}
