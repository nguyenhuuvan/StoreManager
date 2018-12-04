package poly.project.storemanager.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterInformation;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.dao.ProductTypeDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Information;
import poly.project.storemanager.model.Product;
import poly.project.storemanager.model.ProductType;

public class EditedProductInforActivity extends AppCompatActivity implements OnClick {
    private Toolbar toolbar;
    private List<Information> informationList;
    private RecyclerView lvListEditedProductInfor;
    private ProductTypeDAO productTypeDAO;
    private ProductDAO productDAO;
    private String productID;
    private BillDetailDAO billDetailDAO;
    private ImageView imgAvatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edited_product_infor);
        initViews();
        initAction();
        informationList = new ArrayList<>();
        AdapterInformation adapterInformation = new AdapterInformation(this, informationList, this);
        lvListEditedProductInfor.setAdapter(adapterInformation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListEditedProductInfor.setLayoutManager(manager);
        productDAO = new ProductDAO(this);
        productTypeDAO = new ProductTypeDAO(this);
        billDetailDAO = new BillDetailDAO(this);
        fakeData();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListEditedProductInfor = findViewById(R.id.lvList);
        imgAvatar = findViewById(R.id.imgProduct);
    }

    private void initAction() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                Product product = productDAO.getProductByID(productID);
                ProductType productType = productTypeDAO.getProductTypeByID(product.getOldProductTypeID());
                if (productType != null) {
                    Date date = new Date();
                    Product product1 = new Product();
                    product1.setProductID(productID);

                    product1.setProductName(product.getOldProductName());
                    product1.setProductTypeID(product.getOldProductTypeID());
                    product1.setDescription(product.getOldDescription());
                    product1.setInputPrice(product.getOldInputPrice());
                    product1.setOutputPrice(product.getOldOutputPrice());
                    product1.setQuantity(product.getOldQuantity());

                    product1.setImgProduct(product.getOldImgProduct());
                    product1.setOldImgProduct(product.getImgProduct());

                    product1.setOldProductName(product.getProductName());
                    product1.setOldProductTypeID(product.getProductTypeID());
                    product1.setOldDescription(product.getDescription());
                    product1.setOldInputPrice(product.getInputPrice());
                    product1.setOldOutputPrice(product.getOutputPrice());
                    product1.setOldQuantity(product.getQuantity());

                    product1.setAddedDay(product.getAddedDay());
                    product1.setEditedDay(date.getTime());
                    if (product.getOldQuantity() < getAllQuantity()) {
                        Toast.makeText(this, getString(R.string.cannot_edit_quantity)+" "+getAllQuantity(), Toast.LENGTH_SHORT).show();
                    } else {
                        long result = productDAO.updateProduct(product1);
                        if (result > 0) {
                            finish();
                            Toast.makeText(EditedProductInforActivity.this, getString(R.string.recovery_success), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(EditedProductInforActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(this, getString(R.string.cannot_recovery_product), Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return false;
    }

    private void fakeData() {
        productID = getIntent().getStringExtra("productID");
        Product product = productDAO.getProductByID(productID);
        ProductType productType = productTypeDAO.getProductTypeByID(product.getOldProductTypeID());
        informationList.add(new Information("Mã sản phẩm", product.getProductID(), ""));
        informationList.add(new Information("Tên sản phẩm", product.getOldProductName(), ""));
        if (productType == null) {
            informationList.add(new Information("Nhóm hàng", "Nhóm hàng đã bị xóa", ""));
        } else {
            informationList.add(new Information("Nhóm hàng", productType.getProductTypeName() + "(" + productType.getProductTypeID() + ")", ""));
        }
        informationList.add(new Information("Giá nhập", String.valueOf(product.getOldInputPrice()), ""));
        informationList.add(new Information("Giá bán", String.valueOf(product.getOldOutputPrice()), ""));
        informationList.add(new Information("Số lượng ban đầu", String.valueOf(product.getOldQuantity()), ""));
        informationList.add(new Information("Mô tả", product.getOldDescription(), ""));

        try {
            if (product.getOldImgProduct() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(product.getOldImgProduct(), 0, product.getOldImgProduct().length);
                imgAvatar.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("Error", "Error");
        }

    }

    private int getAllQuantity() {
        List<BillDetail> billDetails = billDetailDAO.getAllBillDetailsByProductID(productID);
        int quantity = 0;
        for (int i = 0; i < billDetails.size(); i++) {
            quantity = quantity + billDetails.get(i).getQuantity();
        }
        Log.e("quantity", quantity + "");
        return quantity;

    }

    @Override
    public void onClick(int position) {

    }
}
