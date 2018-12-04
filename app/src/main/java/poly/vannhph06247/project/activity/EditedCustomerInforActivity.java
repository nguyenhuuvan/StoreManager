package poly.project.storemanager.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterInformation;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.model.Customer;
import poly.project.storemanager.model.Information;

public class EditedCustomerInforActivity extends AppCompatActivity implements OnClick {
    private Toolbar toolbar;
    private List<Information> informationList;
    private RecyclerView lvListEditedCustomerInfor;
    private CustomerDAO customerDAO;
    private String customerPhone;
    private ImageView imgCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edited_customer_infor);
        initViews();
        initAction();
        informationList = new ArrayList<>();
        AdapterInformation adapterInformation = new AdapterInformation(this, informationList, this);
        lvListEditedCustomerInfor.setAdapter(adapterInformation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListEditedCustomerInfor.setLayoutManager(manager);
        customerDAO = new CustomerDAO(this);
        fakeData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListEditedCustomerInfor = findViewById(R.id.lvList);
        imgCustomer = findViewById(R.id.imgCustomer);
    }

    private void initAction() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recovery, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recovery:
                Customer customer = customerDAO.getCustomerByID(customerPhone);
                Date date = new Date();
                Customer customer1 = new Customer();
                customer1.setCustomerPhone(customerPhone);

                customer1.setCustomerName(customer.getOldCustomerName());
                customer1.setCustomerSex(customer.getOldCustomerSex());
                customer1.setCustomerAddress(customer.getOldCustomerAddress());
                customer1.setCustomerEmail(customer.getOldCustomerEmail());
                customer1.setNote(customer.getOldNote());
                customer1.setCustomerAge(customer.getOldCustomerAge());

                customer1.setImgcustomer(customer.getOldImgCustomer());
                customer1.setOldImgCustomer(customer.getImgcustomer());

                customer1.setOldCustomerName(customer.getCustomerName());
                customer1.setOldCustomerSex(customer.getCustomerSex());
                customer1.setOldCustomerEmail(customer.getCustomerEmail());
                customer1.setOldCustomerAddress(customer.getCustomerAddress());
                customer1.setOldNote(customer.getNote());
                customer1.setOldCustomerAge(customer.getCustomerAge());

                customer1.setAddedDay(customer.getAddedDay());
                customer1.setEditedDay(date.getTime());

                long result = customerDAO.updateCustomer(customer1);
                if (result > 0) {
                    finish();
                    Toast.makeText(EditedCustomerInforActivity.this, getString(R.string.recovery_success), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditedCustomerInforActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return false;
    }

    private void fakeData() {
        customerPhone = getIntent().getStringExtra("customerPhone");
        Customer customer = customerDAO.getCustomerByID(customerPhone);
        informationList.add(new Information("Tên khách hàng", customer.getOldCustomerName(), ""));
        informationList.add(new Information("Số điện thoại", customerPhone, ""));
        informationList.add(new Information("Giới tính", customer.getOldCustomerSex(), ""));
        if (customer.getOldCustomerAge() == 0) {
            informationList.add(new Information("Tuổi", "", ""));
        } else {
            informationList.add(new Information("Tuổi", String.valueOf(customer.getOldCustomerAge()), ""));
        }
        informationList.add(new Information("Email", customer.getOldCustomerEmail(), ""));
        informationList.add(new Information("Địa chỉ", customer.getOldCustomerAddress(), ""));
        informationList.add(new Information("Ghi chú", customer.getOldNote(), ""));
        try {
            if (customer.getOldImgCustomer() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(customer.getOldImgCustomer(), 0, customer.getOldImgCustomer().length);
                imgCustomer.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("Error", "Error");
        }
    }


    @Override
    public void onClick(int position) {

    }
}
