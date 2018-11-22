package poly.vannhph06247.storemanager.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterInformation;
import poly.vannhph06247.storemanager.listener.OnClick;
import poly.vannhph06247.storemanager.model.Information;

public class BillInforActivity extends AppCompatActivity implements OnClick{
    private List<Information> informationList;
    private RecyclerView lvListBillInfor;
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
        fakeData();
    }
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListBillInfor = findViewById(R.id.lvList);
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_left);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    @Override
    protected void onResume() {
        runLayoutAnimation(lvListBillInfor);
        super.onResume();
    }
    private void fakeData() {
        informationList.add(new Information("Tên khách hàng", "Nguyễn Văn Hùng", ""));
        informationList.add(new Information("Số điện thoại", "01299094321", ""));
        informationList.add(new Information("Thời gian tạo", "10-10-2010 14:53", ""));
        informationList.add(new Information("Ngày sửa gần nhất", "20-10-2010 14:43", "Chi tiết"));
        informationList.add(new Information("Tổng tiền hàng", "5.000.000 VNĐ", ""));
        informationList.add(new Information("Giảm giá", "500.000 VNĐ", ""));
        informationList.add(new Information("Số tiền thanh toán", "4.500.000 VNĐ", ""));
        informationList.add(new Information("Số sản phẩm mua", "100 sản phẩm", "Chi tiết"));
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
                break;
        }
        return false;
    }

    @Override
    public void onClick(int position) {
        if (position==3){
           startActivity(new Intent(this,EditedBillInforActivity.class));
        }
        else if(position==7){
            startActivity(new Intent(this,ProductListOfBillActivity.class));
        }
    }
}
