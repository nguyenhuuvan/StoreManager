package poly.vannhph06247.storemanager.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterInformation;
import poly.vannhph06247.storemanager.listener.OnClick;
import poly.vannhph06247.storemanager.model.Information;

public class EditedBillInforActivity extends AppCompatActivity implements OnClick {
    private List<Information> informationList;
    private RecyclerView lvListEditedBillInfor;
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
        fakeData();
    }
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListEditedBillInfor = findViewById(R.id.lvList);
    }


    private void fakeData() {
        informationList.add(new Information("Tên khách hàng", "Nguyễn Văn Hùng", ""));
        informationList.add(new Information("Số điện thoại", "01299094321", ""));
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
                break;
        }
        return false;
    }

    @Override
    public void onClick(int position) {

    }
}
