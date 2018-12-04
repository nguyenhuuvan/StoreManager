package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterInformation;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.dao.ProductTypeDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Information;
import poly.project.storemanager.model.Product;

public class ProductInforActivity extends Library implements OnClick {
    private List<Information> informationList;
    private RecyclerView lvListProductInfor;
    private String productID;
    private ProductDAO productDAO;
    private ProductTypeDAO productTypeDAO;
    private BillDetailDAO billDetailDAO;
    private ImageView imgAvatar;
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
        billDetailDAO = new BillDetailDAO(this);
        fakeData();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListProductInfor = findViewById(R.id.lvList);
        imgAvatar = findViewById(R.id.imgProduct);
    }


    private void fakeData() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
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
        if(getAllQuantity()==0){
            informationList.add(new Information("Số lượng đã bán", "", ""));
        }else {
            informationList.add(new Information("Số lượng đã bán", String.valueOf(getAllQuantity()), ""));
        }
        informationList.add(new Information("Số lượng ban đầu", String.valueOf(quantity), ""));
        informationList.add(new Information("Mô tả", description, ""));

        informationList.add(new Information("Ngày thêm", simpleDateFormat.format(new Date(add_day)), ""));
        if (edited_day == 0) {
            informationList.add(new Information("Ngày sửa gần nhất", "", ""));
        } else {
            informationList.add(new Information("Ngày sửa gần nhất", simpleDateFormat.format(new Date(edited_day)), "Chi tiết"));
        }
        try {
            if (product.getImgProduct() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImgProduct(), 0, product.getImgProduct().length);
                imgAvatar.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("Error", "Error");
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
        if (position == 9) {
            Intent intent = new Intent(this, EditedProductInforActivity.class);
            intent.putExtra("productID", productID);
            startActivity(intent);
        }
    }
    private int getAllQuantity() {
        List<BillDetail> billDetails = billDetailDAO.getAllBillDetailsByProductID(productID);
        int quantity = 0;
        for (int i = 0; i < billDetails.size(); i++) {
            quantity = quantity + billDetails.get(i).getQuantity();
        }
        return quantity;

    }
}