package poly.project.storemanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.dao.ProductTypeDAO;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Product;
import poly.project.storemanager.model.ProductType;

public class EditProductActivity extends Library {
    private EditText edProductID;
    private EditText edProductName;
    private EditText edProductTypeID;
    private EditText edOutputPrice;
    private EditText edInputPrice;
    private EditText edProductDes;
    private EditText edQuantity;
    private ProductTypeDAO productTypeDAO;
    private ProductDAO productDAO;
    private String productID, oldProductName, oldProductTypeID, oldDescription;
    private int oldInputPrice, oldOutputPrice;
    private int oldQuantity;
    private BillDetailDAO billDetailDAO;
    private ImageView imgProduct;
    private byte[] oldImgAvatar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        productTypeDAO = new ProductTypeDAO(this);
        productDAO = new ProductDAO(this);
        billDetailDAO = new BillDetailDAO(this);

        productID = getIntent().getStringExtra("productID");
        getData();
        initView();
        initAction();

    }

    private void getData() {
        Product product = productDAO.getProductByID(productID);
        oldProductName = product.getProductName();
        oldProductTypeID = product.getProductTypeID();
        oldDescription = product.getDescription();
        oldInputPrice = product.getInputPrice();
        oldOutputPrice = product.getOutputPrice();
        oldQuantity = product.getQuantity();
        oldImgAvatar = product.getImgProduct();
        imgProduct = findViewById(R.id.imgProduct);
        try {
            if (product.getImgProduct() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImgProduct(), 0, product.getImgProduct().length);
                imgProduct.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("Error", "Error");
        }
    }

    private void initView() {
        edProductID = findViewById(R.id.edProductID);
        edProductName = findViewById(R.id.edProductName);
        edProductTypeID = findViewById(R.id.edProductTypeID);
        edOutputPrice = findViewById(R.id.edOutputPrice);
        edInputPrice = findViewById(R.id.edInputPrice);
        edProductDes = findViewById(R.id.edProductDes);
        edQuantity = findViewById(R.id.edQuantity);
        edProductID.setEnabled(false);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initAction() {
        edProductTypeID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditProductActivity.this, ProductTypeActivity.class);
                i.putExtra("check", "check");
                startActivityForResult(i, 1);
            }
        });
        edProductID.setText(productID);
        edProductName.setText(oldProductName);
        edProductTypeID.setText(oldProductTypeID);
        edInputPrice.setText(String.valueOf(oldInputPrice));
        edOutputPrice.setText(String.valueOf(oldOutputPrice));
        edProductDes.setText(oldDescription);
        edQuantity.setText(String.valueOf(oldQuantity));
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                edProductTypeID.setText(result);
                edProductTypeID.setTextSize(16);
            }
        } else if (requestCode == REQUEST_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bit = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                imgProduct.setImageBitmap(bit);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ProductType productType = productTypeDAO.getProductTypeByID(edProductTypeID.getText().toString().trim());
        if (productType == null) {
            edProductTypeID.setText("");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                hideSoftKeyboard(this);
                editProduct();
                break;
        }
        return false;
    }

    private void editProduct() {
        String productName, description, inputPrice, outputPrice, quantity;
        String productTypeID = edProductTypeID.getText().toString().trim();
        productName = edProductName.getText().toString().trim();
        description = edProductDes.getText().toString().trim();
        inputPrice = edInputPrice.getText().toString().trim();
        outputPrice = edOutputPrice.getText().toString().trim();
        quantity = edQuantity.getText().toString().trim();

        if (productName.isEmpty() || productTypeID.equals("") || description.isEmpty() || inputPrice.isEmpty() || outputPrice.isEmpty() || quantity.isEmpty()) {
            if (productName.isEmpty())
                edProductName.setError(getString(R.string.notify_empty_product_name));
            if (productTypeID.equals("")) {
                edProductTypeID.setText(getString(R.string.chooseProductType));
                edProductTypeID.setTextSize(12);
            }
            if (description.isEmpty())
                edProductDes.setError(getString(R.string.notify_empty_product_type_des));
            if (inputPrice.isEmpty())
                edInputPrice.setError(getString(R.string.notify_empty_product_input_price));
            if (outputPrice.isEmpty())
                edOutputPrice.setError(getString(R.string.notify_empty_product_output_price));
            if (quantity.isEmpty())
                edQuantity.setError(getString(R.string.notify_empty_quantity));
        } else if (productName.length() > 50 || description.length() > 255) {
            if (productName.length() > 50)
                edProductName.setError(getString(R.string.notify_length_product_name));
            if (description.length() > 255)
                edProductDes.setError(getString(R.string.notify_length_product_type_des));
        } else if (checkString(productName) || checkString(description) || checkInteger(quantity) || checkInteger(inputPrice) || checkInteger(outputPrice)) {
            if (checkString(productName))
                edProductName.setError(getString(R.string.string_exist_2_space));
            if (checkString(description))
                edProductDes.setError(getString(R.string.string_exist_2_space));
            if (checkInteger(quantity))
                edQuantity.setError(getString(R.string.number_too_large));
            if (checkInteger(inputPrice))
                edInputPrice.setError(getString(R.string.number_too_large));
            if (checkInteger(outputPrice))
                edOutputPrice.setError(getString(R.string.number_too_large));
        } else if (Integer.parseInt(quantity) <= 0 || Integer.parseInt(inputPrice) <= 0 || Integer.parseInt(outputPrice) <= 0 || Integer.parseInt(quantity) < getAllQuantity()) {
            if (Integer.parseInt(quantity) <= 0)
                edQuantity.setError(getString(R.string.notify_0));
            if (Integer.parseInt(inputPrice) <= 0)
                edInputPrice.setError(getString(R.string.notify_0));
            if (Integer.parseInt(outputPrice) <= 0)
                edOutputPrice.setError(getString(R.string.notify_0));
            if(Integer.parseInt(quantity) < getAllQuantity())
                edQuantity.setError(getString(R.string.cannot_edit_quantity)+" "+getAllQuantity());
        } else if (Integer.parseInt(inputPrice) >= Integer.parseInt(outputPrice))
            edInputPrice.setError(getString(R.string.price_error));
        else {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgProduct.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            byte[] imgAvatar = byteArray.toByteArray();
            Date date = new Date();
            Product product = new Product();
            product.setProductID(productID);

            product.setProductName(changeString(productName));
            product.setProductTypeID(productTypeID);
            product.setDescription(changeString(description));
            product.setInputPrice(Integer.parseInt(inputPrice));
            product.setOutputPrice(Integer.parseInt(outputPrice));
            product.setQuantity(Integer.parseInt(quantity));

            product.setImgProduct(imgAvatar);
            product.setOldImgProduct(oldImgAvatar);
            
            product.setOldProductName(oldProductName);
            product.setOldProductTypeID(oldProductTypeID);
            product.setOldDescription(oldDescription);
            product.setOldInputPrice(oldInputPrice);
            product.setOldOutputPrice(oldOutputPrice);
            product.setOldQuantity(oldQuantity);

            product.setAddedDay(product.getAddedDay());
            product.setEditedDay(date.getTime());
            long result = productDAO.updateProduct(product);
            if (result > 0) {
                Toast.makeText(EditProductActivity.this, getString(R.string.edited), Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditProductActivity.this, getString(R.string.edit_fail), Toast.LENGTH_SHORT).show();
            }
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
