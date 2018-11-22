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
import poly.vannhph06247.storemanager.model.Product;

public class ProductInforActivity extends Library implements OnClick {
    private List<Information> informationList;
    private RecyclerView lvListProductInfor;
    private String productID;
    private ProductDAO productDAO;
    private ProductTypeDAO productTypeDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_infor);

        initViews();
        informationList = new ArrayList<>();
        AdapterInformation adapterInformation = new AdapterInformation(this, informationList, this);
        lvListProductInfor.setAdapter(adapterInformation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListProductInfor.setLayoutManager(manager);
        productDAO = new ProductDAO(this);
        productTypeDAO = new ProductTypeDAO(this);
        fakeData();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListProductInfor = findViewById(R.id.lvList);
    }


    private void fakeData() {
        productID = getIntent().getStringExtra("productID");
        Product product = productDAO.getProductByID(productID);

        String  productName, description, productTypeID,productTypeName;
        int inputPrice, outputPrice, quantity;
        long add_day,edited_day;
        productName = product.getProductName();
        description = product.getDescription();
        productTypeID = product.getProductTypeID();
        inputPrice = product.getInputPrice();
        outputPrice = product.getOutputPrice();
        quantity = product.getQuantity();
        add_day = product.getAddedDay();
        edited_day = product.getEditedDay();

        productTypeName = productTypeDAO.getProductTypeByID(productTypeID).getProductTypeName();


        informationList.add(new Information("Mã sản phẩm", productID, ""));
        informationList.add(new Information("Tên sản phẩm", productName, ""));
        informationList.add(new Information("Nhóm hàng", productTypeName+"("+productTypeID+")", ""));
        informationList.add(new Information("Giá nhập", String.valueOf(inputPrice), ""));
        informationList.add(new Information("Giá bán", String.valueOf(outputPrice), ""));
        informationList.add(new Information("Số lượng ban đầu", String.valueOf(quantity), ""));
        informationList.add(new Information("Mô tả", description, ""));

        informationList.add(new Information("Ngày thêm", new Date(add_day).toString(), ""));
        if (edited_day == 0) {
            informationList.add(new Information("Ngày sửa gần nhất", "", ""));
        } else {
            informationList.add(new Information("Ngày sửa gần nhất", new Date(edited_day).toString(), "Chi tiết"));
        }
    }

    @Override
    protected void onResume() {
        informationList.clear();
        fakeData();
        runLayoutAnimationLeft(lvListProductInfor);
        super.onResume();
    }

    @Override
    public void onClick(int position) {
        if (position == 8) {
            Intent intent = new Intent(this, EditedProductInforActivity.class);
            intent.putExtra("productID", productID);
            startActivity(intent);
        }
    }
}