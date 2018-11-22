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
import poly.vannhph06247.storemanager.dao.ProductDAO;
import poly.vannhph06247.storemanager.dao.ProductTypeDAO;
import poly.vannhph06247.storemanager.listener.OnClick;
import poly.vannhph06247.storemanager.model.Information;
import poly.vannhph06247.storemanager.model.ProductType;

public class ProductTypeInforActivity extends Library implements OnClick {
    private List<Information> informationList;
    private RecyclerView lvListProductTypeInfor;
    private ProductTypeDAO productTypeDAO;
    private String productTypeID;
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type_infor);

        initViews();

        informationList = new ArrayList<>();
        AdapterInformation adapterInformation = new AdapterInformation(this, informationList, this);
        lvListProductTypeInfor.setAdapter(adapterInformation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListProductTypeInfor.setLayoutManager(manager);
        productTypeDAO = new ProductTypeDAO(this);
        productDAO = new ProductDAO(this);
        fakeData();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListProductTypeInfor = findViewById(R.id.lvList);
    }


    private void fakeData() {
        productTypeID = getIntent().getStringExtra("productTypeID");
        ProductType productType = productTypeDAO.getProductTypeByID(productTypeID);

        String name, des, add_day, edited_day;
        if (productType != null) {
            name = productType.getProductTypeName();
            des = productType.getProductTypeDes();
            add_day = new Date(productType.getAddedDay()).toString();
            informationList.add(new Information("Tên nhóm hàng", name, ""));
            informationList.add(new Information("Mã nhóm hàng", productTypeID, ""));
            informationList.add(new Information("Mô tả", des, ""));
            informationList.add(new Information("Ngày thêm ", add_day, ""));
            if (productType.getEditedDay() == 0) {
                informationList.add(new Information("Ngày sửa gần nhất", "", ""));
            } else {
                edited_day = new Date(productType.getEditedDay()).toString();
                informationList.add(new Information("Ngày sửa gần nhất", edited_day, "Chi tiết"));
            }
            if (productDAO.getAllProductByProductType(productTypeID).size() != 0)
                informationList.add(new Information("Số sản phẩm trong nhóm", String.valueOf(productDAO.getAllProductByProductType(productTypeID).size() + " sản phẩm"), "Chi tiết"));
            else
                informationList.add(new Information("Số sản phẩm trong nhóm", String.valueOf(productDAO.getAllProductByProductType(productTypeID).size() + " sản phẩm"), ""));
        }


    }

    @Override
    protected void onResume() {
        informationList.clear();
        fakeData();
        runLayoutAnimationLeft(lvListProductTypeInfor);
        super.onResume();
    }

    @Override
    public void onClick(int position) {
        if (position == 4) {
            Intent intent = new Intent(this, EditedProductTypeInforActivity.class);
            intent.putExtra("productTypeID", productTypeID);
            startActivity(intent);
        } else if (position == 5) {
            Intent intent = new Intent(this, ProductListOfProductTypeActivity.class);
            intent.putExtra("productTypeID", productTypeID);
            startActivity(intent);
        }
    }
}
