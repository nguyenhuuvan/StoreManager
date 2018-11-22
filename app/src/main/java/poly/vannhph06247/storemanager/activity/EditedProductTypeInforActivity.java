package poly.vannhph06247.storemanager.activity;

import android.os.Bundle;
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

import poly.vannhph06247.storemanager.Library;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterInformation;
import poly.vannhph06247.storemanager.dao.ProductTypeDAO;
import poly.vannhph06247.storemanager.listener.OnClick;
import poly.vannhph06247.storemanager.model.Information;
import poly.vannhph06247.storemanager.model.ProductType;

public class EditedProductTypeInforActivity extends Library implements OnClick {
    private Toolbar toolbar;
    private List<Information> informationList;
    private RecyclerView lvListEditedProductTypeInfor;
    private ProductTypeDAO productTypeDAO;
    private String productTypeID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edited_product_type_infor);
        initViews();
        initAction();
        informationList = new ArrayList<>();
        AdapterInformation adapterInformation = new AdapterInformation(this, informationList, this);
        lvListEditedProductTypeInfor.setAdapter(adapterInformation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListEditedProductTypeInfor.setLayoutManager(manager);
        productTypeDAO = new ProductTypeDAO(this);
        fakeData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListEditedProductTypeInfor = findViewById(R.id.lvList);
    }

    private void initAction() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void fakeData() {

        productTypeID = getIntent().getStringExtra("productTypeID");
        ProductType productType = productTypeDAO.getProductTypeByID(productTypeID);

        String name = "", des = "";
        if (productType != null) {
            name = productType.getOldProductTypeName();
            des = productType.getOldProductTypeDes();
        }
        informationList.add(new Information("Mã nhóm hàng", productTypeID, ""));
        informationList.add(new Information("Tên nhóm hàng", name, ""));
        informationList.add(new Information("Mô tả", des, ""));
    }

    @Override
    public void onClick(int position) {

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
                ProductType productType = productTypeDAO.getProductTypeByID(productTypeID);
                Date date = new Date();
                ProductType productType1 = new ProductType(productTypeID, productType.getOldProductTypeName(), productType.getOldProductTypeDes(), productType.getProductTypeName(), productType.getProductTypeDes(), productType.getAddedDay(), date.getTime());
                long result = productTypeDAO.updateProductType(productType1);
                if (result > 0) {
                    finish();
                    Toast.makeText(EditedProductTypeInforActivity.this, "Đã khôi phục thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditedProductTypeInforActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return false;
    }

}
