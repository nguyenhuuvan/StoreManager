package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterBillDetail;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.model.BillDetail;

public class ProductListOfBillActivity extends Library implements OnClick, OnDelete {
    private RecyclerView lvListProduct;
    private List<BillDetail> billDetailList;
    private String billID;
    private BillDetailDAO billDetailDAO;
    private TextView tvTotal;
    private AdapterBillDetail adapterListProduct;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_of_bill);
        initViews();

        billDetailDAO = new BillDetailDAO(this);
        billID = getIntent().getStringExtra("billID");
        billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
        adapterListProduct = new AdapterBillDetail(this, billDetailList, this, this);
        lvListProduct.setAdapter(adapterListProduct);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListProduct.setLayoutManager(manager);
        tvTotal.setText(getString(R.string.total) + " " + billDetailList.size() + " " + getString(R.string.product_));
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText(getString(R.string.total_product));
        lvListProduct = findViewById(R.id.lvList);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onResume() {
        runLayoutAnimationLeft(lvListProduct);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                deleteAllProduct();
                break;
        }
        return false;
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    public void onDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_bill));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long result = billDetailDAO.deleteBillDetail(billDetailList.get(position).getBillDetailID());
                if (result > 0) {
                    adapterListProduct.removeItem(position);
                    tvTotal.setText(getString(R.string.total)+" " + billDetailList.size() + " "+getString(R.string.product_));
                    Toast.makeText(ProductListOfBillActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductListOfBillActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
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
    }
    private void deleteAllProduct(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_allproduct_of_bill));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0;i<billDetailList.size();i++){
                    billDetailDAO.deleteBillDetail(billDetailList.get(i).getBillDetailID());
                }
                billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
                adapterListProduct.changeDataset(billDetailList);
                tvTotal.setText(getString(R.string.total)+" " + billDetailList.size() + " "+getString(R.string.product_));
                Toast.makeText(ProductListOfBillActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
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
    }
}
