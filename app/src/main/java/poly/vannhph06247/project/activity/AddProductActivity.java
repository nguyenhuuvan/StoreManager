package poly.project.storemanager.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.dao.ProductTypeDAO;
import poly.project.storemanager.model.Product;
import poly.project.storemanager.model.ProductType;


public class AddProductActivity extends Library {

    private EditText edProductID;
    private EditText edProductName;
    private EditText edProductTypeID;
    private EditText edOutputPrice;
    private EditText edInputPrice;
    private EditText edProductDes;
    private EditText edQuantity;
    private ProductTypeDAO productTypeDAO;
    private ProductDAO productDAO;
    private ImageView imgProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_actitivy);

        productTypeDAO = new ProductTypeDAO(this);
        productDAO = new ProductDAO(this);
        initView();
        initAction();
    }

    private void initView() {
        edProductID = findViewById(R.id.edProductID);
        edProductName = findViewById(R.id.edProductName);
        edProductTypeID = findViewById(R.id.edProductTypeID);
        edOutputPrice = findViewById(R.id.edOutputPrice);
        edInputPrice = findViewById(R.id.edInputPrice);
        edProductDes = findViewById(R.id.edProductDes);
        edQuantity = findViewById(R.id.edQuantity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgProduct = findViewById(R.id.imgProduct);

    }

    private void initAction() {
        edProductTypeID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddProductActivity.this, ProductTypeActivity.class);
                i.putExtra("check", "check");
                startActivityForResult(i, 1);
            }
        });
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
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                hideSoftKeyboard(this);
                addProduct();
                break;
        }
        return false;
    }

    private void addProduct() {
        String productID, productName, description, inputPrice, outputPrice, quantity;
        String productTypeID = edProductTypeID.getText().toString().trim();
        productID = edProductID.getText().toString().trim().toUpperCase();
        productName = edProductName.getText().toString().trim();
        description = edProductDes.getText().toString().trim();
        inputPrice = edInputPrice.getText().toString().trim();
        outputPrice = edOutputPrice.getText().toString().trim();
        quantity = edQuantity.getText().toString().trim();


        if (productID.isEmpty() || productName.isEmpty() || productTypeID.isEmpty() || description.isEmpty() || inputPrice.isEmpty() || outputPrice.isEmpty() || quantity.isEmpty()) {
            if (productID.isEmpty())
                edProductID.setError(getString(R.string.notify_empty_product_id));
            if (productName.isEmpty())
                edProductName.setError(getString(R.string.notify_empty_product_name));
            if (productTypeID.isEmpty()) {
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
        } else if (productID.length() > 10 || productName.length() > 50 || description.length() > 255) {
            if (productID.length() > 10)
                edProductID.setError(getString(R.string.notify_length_product_id));
            if (productName.length() > 50)
                edProductName.setError(getString(R.string.notify_length_product_name));
            if (description.length() > 255)
                edProductDes.setError(getString(R.string.notify_length_product_type_des));
        } else if (checkPrimaryKey(productID) || checkString(productName) || checkString(description) || checkInteger(quantity) || checkInteger(inputPrice) || checkInteger(outputPrice)) {
            if (checkPrimaryKey(productID))
                edProductID.setError(getString(R.string.primarykey_exist_space));
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
        } else if (Integer.parseInt(quantity) <= 0 || Integer.parseInt(inputPrice) <= 0 || Integer.parseInt(outputPrice) <= 0) {
            if (Integer.parseInt(quantity) <= 0)
                edQuantity.setError(getString(R.string.notify_0));
            if (Integer.parseInt(inputPrice) <= 0)
                edInputPrice.setError(getString(R.string.notify_0));
            if (Integer.parseInt(outputPrice) <= 0)
                edOutputPrice.setError(getString(R.string.notify_0));
        } else if (Integer.parseInt(inputPrice) >= Integer.parseInt(outputPrice))
            edInputPrice.setError(getString(R.string.price_error));
        else {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgProduct.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            byte[] imgAvatar = byteArray.toByteArray();

            Product product1 = productDAO.getProductByID(productID);
            if (product1 == null) {
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
                product.setOldImgProduct(null);

                product.setOldProductName("");
                product.setOldProductTypeID("");
                product.setOldDescription("");
                product.setOldInputPrice(0);
                product.setOldOutputPrice(0);
                product.setOldQuantity(0);

                product.setAddedDay(date.getTime());
                product.setEditedDay(0);
                long result = productDAO.insertProduct(product);
                if (result > 0) {
                    Toast.makeText(AddProductActivity.this, getString(R.string.added), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddProductActivity.this, getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
                }
            } else {
                edProductID.setError(getString(R.string.product_id_exist));
            }
        }
    }
}

