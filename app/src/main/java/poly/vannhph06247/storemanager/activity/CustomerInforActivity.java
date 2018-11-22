package poly.vannhph06247.storemanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import poly.vannhph06247.storemanager.Library;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterInformation;
import poly.vannhph06247.storemanager.dao.CustomerDAO;
import poly.vannhph06247.storemanager.listener.OnClick;
import poly.vannhph06247.storemanager.model.Customer;
import poly.vannhph06247.storemanager.model.Information;

public class CustomerInforActivity extends Library implements OnClick {
    private List<Information> informationList;
    private RecyclerView lvListCustomerInfor;
    private String customerPhone;
    private CustomerDAO customerDAO;

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
    }


    private void fakeData() {
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
        informationList.add(new Information("Ngày thêm", new Date(customer.getAddedDay()).toString(), ""));
        if (customer.getEditedDay() == 0) {
            informationList.add(new Information("Ngày sửa gần nhất", "", ""));
        } else {
            informationList.add(new Information("Ngày sửa gần nhất", new Date(customer.getEditedDay()).toString(), "Chi tiết"));
        }
        informationList.add(new Information("Số sản phẩm đã mua", "100 sản phẩm", "Chi tiết"));
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
            startActivity(new Intent(this, ProductListOfCustomerActivity.class));
        }
    }
}
