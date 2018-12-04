package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterInformation;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.model.Customer;
import poly.project.storemanager.model.Information;

public class CustomerInforActivity extends Library implements OnClick {
    private List<Information> informationList;
    private RecyclerView lvListCustomerInfor;
    private String customerPhone;
    private CustomerDAO customerDAO;
    private ImageView imgCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_infor);

        initViews();
        informationList = new ArrayList<>();
        AdapterInformation adapterInformation = new AdapterInformation(this, informationList, this);
        lvListCustomerInfor.setAdapter(adapterInformation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListCustomerInfor.setLayoutManager(manager);
        customerDAO = new CustomerDAO(this);
        fakeData();
    }


    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListCustomerInfor = findViewById(R.id.lvList);
        imgCustomer = findViewById(R.id.imgCustomer);
    }

    private void fakeData() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        customerPhone = getIntent().getStringExtra("customerPhone");
        Customer customer = customerDAO.getCustomerByID(customerPhone);

        informationList.add(new Information("Tên khách hàng", customer.getCustomerName(), ""));
        informationList.add(new Information("Số điện thoại", customerPhone, ""));
        informationList.add(new Information("Giới tính", customer.getCustomerSex(), ""));
        if (customer.getCustomerAge() == 0) {
            informationList.add(new Information("Tuổi", "", ""));
        } else {
            informationList.add(new Information("Tuổi", String.valueOf(customer.getCustomerAge()), ""));
        }
        informationList.add(new Information("Email", customer.getCustomerEmail(), ""));
        informationList.add(new Information("Địa chỉ", customer.getCustomerAddress(), ""));
        informationList.add(new Information("Ghi chú", customer.getNote(), ""));
        informationList.add(new Information("Ngày thêm", simpleDateFormat.format(new Date(customer.getAddedDay())), ""));
        if (customer.getEditedDay() == 0) {
            informationList.add(new Information("Ngày sửa gần nhất", "", ""));
        } else {
            informationList.add(new Information("Ngày sửa gần nhất", simpleDateFormat.format(new Date(customer.getEditedDay())), "Chi tiết"));
        }
        if (customerDAO.getProductofCustomer(customerPhone).size() == 0) {
            informationList.add(new Information("Số sản phẩm đã mua", String.valueOf(customerDAO.getProductofCustomer(customerPhone).size()) + " sản phẩm", ""));
        } else {
            informationList.add(new Information("Số sản phẩm đã mua", String.valueOf(customerDAO.getProductofCustomer(customerPhone).size()) + " sản phẩm", "Chi tiết"));
        }
        try {
            if (customer.getImgcustomer() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(customer.getImgcustomer(), 0, customer.getImgcustomer().length);
                imgCustomer.setImageBitmap(bitmap);
            }else{
                imgCustomer.setImageResource(R.drawable.customer);
            }
        } catch (Exception e) {
            Log.e("Error", "Error");
        }
    }

    @Override
    protected void onResume() {
        informationList.clear();
        fakeData();
        runLayoutAnimationLeft(lvListCustomerInfor);
        super.onResume();
    }

    @Override
    public void onClick(int position) {
        if (position == 8) {
            Intent intent = new Intent(this, EditedCustomerInforActivity.class);
            intent.putExtra("customerPhone", customerPhone);
            startActivity(intent);
        } else if (position == 9) {
            Intent intent = new Intent(this, ProductListOfCustomerActivity.class);
            intent.putExtra("customerPhone", customerPhone);
            startActivity(intent);
        }
    }
}
