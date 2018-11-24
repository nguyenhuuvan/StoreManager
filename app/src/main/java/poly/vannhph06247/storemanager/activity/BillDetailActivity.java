package poly.vannhph06247.storemanager.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import poly.vannhph06247.storemanager.Library;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterBillDetail;
import poly.vannhph06247.storemanager.listener.OnDelete;
import poly.vannhph06247.storemanager.model.BillDetail;

public class BillDetailActivity extends Library implements OnDelete{
    private RecyclerView lvListBillDetail;
    private List<BillDetail> billDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);
        initViews();
        billDetails = new ArrayList<>();
        AdapterBillDetail adapterBillDetail = new AdapterBillDetail(this, billDetails, this);
        lvListBillDetail.setAdapter(adapterBillDetail);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListBillDetail.setLayoutManager(manager);
        fakeData();
    }


    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView tvTotalMonney = findViewById(R.id.tvTotalMoney);
        tvTotalMonney.setText("");
        lvListBillDetail = findViewById(R.id.lvList);

    }

    private void fakeData() {
        for (int i = 0; i < 40; i++) {
            billDetails.add(new BillDetail("H30122 ", "10", "50000","500000"));
        }
    }

    @Override
    protected void onResume() {
        runLayoutAnimationLeft(lvListBillDetail);
        super.onResume();
    }
    public void test(View view) {
        Toast.makeText(this, "Pay", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDelete(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_bill));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
