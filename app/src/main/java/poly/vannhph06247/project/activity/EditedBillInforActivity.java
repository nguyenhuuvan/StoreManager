package poly.project.storemanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterInformation;
import poly.project.storemanager.dao.BillDAO;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.model.Bill;
import poly.project.storemanager.model.Customer;
import poly.project.storemanager.model.Information;

public class EditedBillInforActivity extends AppCompatActivity implements OnClick {
    private List<Information> informationList;
    private RecyclerView lvListEditedBillInfor;
    private CustomerDAO customerDAO;
    private String billID;
    private BillDAO billDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edited_bill_infor);
        initViews();
        informationList = new ArrayList<>();
        AdapterInformation adapterInformation = new AdapterInformation(this, informationList, this);
        lvListEditedBillInfor.setAdapter(adapterInformation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListEditedBillInfor.setLayoutManager(manager);
        customerDAO = new CustomerDAO(this);
        billDAO = new BillDAO(this);
        fakeData();
    }


    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListEditedBillInfor = findViewById(R.id.lvList);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void fakeData() {
        billID = getIntent().getStringExtra("billID");
        Bill bill = billDAO.getBillByID(billID);
        if(bill !=null){
            Customer customer = customerDAO.getCustomerByID(bill.getOldCustomerPhone());
            if(customer!=null){
                informationList.add(new Information("Tên khách hàng", customer.getCustomerName(), ""));
            }else{
                informationList.add(new Information("Khách hàng",getString(R.string.customer_deleted) , ""));
            }
            informationList.add(new Information("Số điện thoại", bill.getOldCustomerPhone(), ""));
        }


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
                Bill bill = billDAO.getBillByID(billID);
                Customer customer = customerDAO.getCustomerByID(bill.getOldCustomerPhone());
                if (customer!=null){
                    Date date = new Date();
                    Bill bill1 = new Bill(billID, bill.getOldCustomerPhone(),bill.getCustomerPhone(), date.getTime(), date.getTime(), "Chưa thanh toán");
                    long result = billDAO.updateBill(bill1);
                    if (result > 0) {
                        finish();
                        Toast.makeText(EditedBillInforActivity.this, getString(R.string.recovery_success), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EditedBillInforActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, getString(R.string.cannot_recovery_bill), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(int position) {

    }
}
