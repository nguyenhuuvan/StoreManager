package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterInformation;
import poly.project.storemanager.dao.BillDAO;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.model.Bill;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Customer;
import poly.project.storemanager.model.Information;
import poly.project.storemanager.model.Product;

public class BillInforActivity extends Library implements OnClick {
    private List<Information> informationList;
    private RecyclerView lvListBillInfor;
    private String billID;
    private BillDAO billDAO;
    private CustomerDAO customerDAO;
    private BillDetailDAO billDetailDAO;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_infor);
        initViews();
        informationList = new ArrayList<>();
        AdapterInformation adapterInformation = new AdapterInformation(this, informationList, this);
        lvListBillInfor.setAdapter(adapterInformation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListBillInfor.setLayoutManager(manager);
        billDAO = new BillDAO(this);
        customerDAO = new CustomerDAO(this);
        billDetailDAO = new BillDetailDAO(this);
        productDAO = new ProductDAO(this);
        fakeData();
    }


    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListBillInfor = findViewById(R.id.lvList);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    protected void onResume() {
        informationList.clear();
        fakeData();
        runLayoutAnimationLeft(lvListBillInfor);
        super.onResume();
    }

    private void fakeData() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        billID = getIntent().getStringExtra("billID");
        Bill bill = billDAO.getBillByID(billID);
        if (bill != null) {
            Customer customer = customerDAO.getCustomerByID(bill.getCustomerPhone());
            if (customer != null) {
                informationList.add(new Information("Tên khách hàng", customer.getCustomerName(), ""));
            } else {
                informationList.add(new Information("Khách hàng", getString(R.string.customer_deleted), ""));
            }
            informationList.add(new Information("Số điện thoại", bill.getCustomerPhone(), ""));
            informationList.add(new Information("Thời gian tạo", simpleDateFormat.format(new Date(bill.getBillDate())), ""));
            if (bill.getEditedDay() == 0) {
                informationList.add(new Information("Ngày sửa gần nhất", "", ""));
            } else {
                informationList.add(new Information("Ngày sửa gần nhất", simpleDateFormat.format(new Date(bill.getEditedDay())), "Chi tiết"));
            }
            List<BillDetail> billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
            informationList.add(new Information("Tổng tiền hàng", formatVnCurrence(getSum(billDetailList).toString()), ""));
            if (billDetailList.size() == 0) {
                informationList.add(new Information("Số sản phẩm mua", String.valueOf(billDetailList.size()), ""));
            } else {
                informationList.add(new Information("Số sản phẩm mua", String.valueOf(billDetailList.size()), "Chi tiết"));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setMessage(getString(R.string.delete_message_bill));
                builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<BillDetail> billDetail = billDetailDAO.getAllBillDetailsByBillID(billID);
                        if (billDetail.size() > 0) {
                            Toast.makeText(BillInforActivity.this, getString(R.string.cannot_delete_bill), Toast.LENGTH_SHORT).show();
                        } else {
                            long result = billDAO.deleteBill(billID);
                            if (result > 0) {
                                Toast.makeText(BillInforActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(BillInforActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final Dialog dialog = builder.create();
                Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.show();
                break;
        }
        return false;
    }

    @Override
    public void onClick(int position) {
        if (position == 3) {
            Intent intent = new Intent(this, EditedBillInforActivity.class);
            intent.putExtra("billID", billID);
            startActivity(intent);
        } else if (position == 5) {
            Intent intent = new Intent(this, ProductListOfBillActivity.class);
            intent.putExtra("billID", billID);
            startActivity(intent);
        }
    }

    private String formatVnCurrence(String price) {

        NumberFormat format =
                new DecimalFormat("#,##0.00");
        format.setCurrency(Currency.getInstance(Locale.US));

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%s VNĐ", price);
        return price;
    }

    private Double getSum(List<BillDetail> billDetailList) {
        double sum = 0;
        for (int i = 0; i < billDetailList.size(); i++) {
            Product product = productDAO.getProductByID(billDetailList.get(i).getProductID());
            sum = sum + ((double) product.getOutputPrice() * (double) billDetailList.get(i).getQuantity());
        }
        return sum;
    }

}
