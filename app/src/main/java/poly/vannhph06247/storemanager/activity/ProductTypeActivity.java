package poly.vannhph06247.storemanager.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;


import poly.vannhph06247.storemanager.Library;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterProductType;
import poly.vannhph06247.storemanager.dao.ProductDAO;
import poly.vannhph06247.storemanager.dao.ProductTypeDAO;
import poly.vannhph06247.storemanager.listener.OnClick;
import poly.vannhph06247.storemanager.listener.OnDelete;
import poly.vannhph06247.storemanager.listener.OnEdit;

import poly.vannhph06247.storemanager.model.Product;
import poly.vannhph06247.storemanager.model.ProductType;

public class ProductTypeActivity extends Library implements OnDelete, OnClick, OnEdit {
    private Toolbar toolbar;
    private TextView tvTotal;
    private RecyclerView lvListProductType;
    private AdapterProductType adapterProductType;
    private List<ProductType> productTypeList;
    private FloatingActionButton fabAddProductType;
    private ImageView imgSort;
    private ProductTypeDAO productTypeDAO;
    private String text = "";
    private ProductDAO productDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_type);

        productTypeDAO = new ProductTypeDAO(this);
        productTypeList = productTypeDAO.getAllProductType();
        productDAO = new ProductDAO(this);
        adapterProductType = new AdapterProductType(this, productTypeList, this, this, this);
        initViews();
        initAction();

        lvListProductType.setAdapter(adapterProductType);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListProductType.setLayoutManager(manager);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTotal = findViewById(R.id.tvTotal);
        lvListProductType = findViewById(R.id.lvList);
        fabAddProductType = findViewById(R.id.fabAdd);
        imgSort = findViewById(R.id.imgSort);
    }

    private void initAction() {
        lvListProductType.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabAddProductType.getVisibility() == View.VISIBLE) {
                    fabAddProductType.hide();

                } else if (dy < 0 && fabAddProductType.getVisibility() != View.VISIBLE) {
                    fabAddProductType.show();

                }
            }
        });
        fabAddProductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddProductType();
            }
        });

        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ProductTypeActivity.this, imgSort);
                getMenuInflater().inflate(R.menu.menu_product, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.sort_by_name:
                                runLayoutAnimationLeft(lvListProductType);
                                Collections.sort(productTypeList, new Comparator<ProductType>() {
                                    @Override
                                    public int compare(ProductType pb1, ProductType pb2) {
                                        int a = pb1.getProductTypeName().compareToIgnoreCase(pb2.getProductTypeName());
                                        if (a == 0) {
                                            return pb1.getProductTypeName().compareToIgnoreCase(pb2.getProductTypeName());
                                        }
                                        return a;
                                    }
                                });
                                break;
                            case R.id.sort_by_id:
                                runLayoutAnimationRight(lvListProductType);
                                Collections.sort(productTypeList, new Comparator<ProductType>() {
                                    @Override
                                    public int compare(ProductType pb1, ProductType pb2) {
                                        int a = pb1.getProductTypeID().compareToIgnoreCase(pb2.getProductTypeID());
                                        if (a == 0) {
                                            return pb1.getProductTypeID().compareToIgnoreCase(pb2.getProductTypeID());
                                        }
                                        return a;
                                    }
                                });
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        try {
            if (getIntent().getStringExtra("check").equals("check")) {
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        } catch (Exception e) {
            Log.e("", "");
        }


    }

    @Override
    protected void onResume() {
        setLvProductType(text);
        runLayoutAnimationLeft(lvListProductType);
        super.onResume();
    }

    private void showDialogAddProductType() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View dialogView = Objects.requireNonNull(inflater).inflate(R.layout.dialog_add_product_type, null);
        builder.setView(dialogView);
        final Dialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        Button btnAdd = dialogView.findViewById(R.id.btnAdd);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        final EditText edProductTypeID = dialogView.findViewById(R.id.edProductTypeID);
        final EditText edProductTypeName = dialogView.findViewById(R.id.edProductTypeName);
        final EditText edProductTypeDes = dialogView.findViewById(R.id.edProductTypeDes);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String productTypeID = edProductTypeID.getText().toString().trim().toUpperCase();
                String productTypeName = edProductTypeName.getText().toString().trim();
                String productTypeDes = edProductTypeDes.getText().toString().trim();

                if (productTypeID.isEmpty() || productTypeDes.isEmpty() || productTypeName.isEmpty()) {
                    if (productTypeID.isEmpty())
                        edProductTypeID.setError(getString(R.string.notify_empty_product_type_id));
                    if (productTypeName.isEmpty())
                        edProductTypeName.setError(getString(R.string.notify_empty_product_type_name));
                    if (productTypeDes.isEmpty())
                        edProductTypeDes.setError(getString(R.string.notify_empty_product_type_des));
                } else if (productTypeDes.length() > 255 || productTypeID.length() > 10 || productTypeName.length() > 50) {
                    if (productTypeDes.length() > 255)
                        edProductTypeDes.setError(getString(R.string.notify_length_product_type_des));

                    if (productTypeID.length() > 10)
                        edProductTypeID.setError(getString(R.string.notify_length_product_type_id));

                    if (productTypeName.length() > 50)
                        edProductTypeName.setError(getString(R.string.notify_length_product_type_name));
                } else if (checkPrimaryKey(productTypeID) || checkString(productTypeName) || checkString(productTypeDes)) {
                    if (checkPrimaryKey(productTypeID))
                        edProductTypeID.setError(getString(R.string.primarykey_exist_space));
                    if (checkString(productTypeName))
                        edProductTypeName.setError(getString(R.string.string_exist_2_space));
                    if (checkString(productTypeDes))
                        edProductTypeDes.setError(getString(R.string.string_exist_2_space));
                } else {
                    ProductType producttype = productTypeDAO.getProductTypeByID(productTypeID);
                    if (producttype == null) {
                        Date date = new Date();
                        ProductType productType = new ProductType(productTypeID, changeString(productTypeName), productTypeDes, "", "", date.getTime(), 0);
                        long result = productTypeDAO.insertProductType(productType);
                        if (result > 0) {
                            productTypeList = productTypeDAO.getAllProductType();
                            adapterProductType.changeDataset(productTypeList);
                            dialog.dismiss();
                            Toast.makeText(ProductTypeActivity.this, getString(R.string.added), Toast.LENGTH_SHORT).show();
                            tvTotal.setText(getString(R.string.total)+" " + productTypeList.size() + " "+getString(R.string.productType));
                        } else {
                            dialog.dismiss();
                            Toast.makeText(ProductTypeActivity.this, getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        edProductTypeID.setError(getString(R.string.product_type_id_exist));
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_item).getActionView();
        searchView.setSearchableInfo(
                Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                text = query;
                setLvProductType(text);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                text = newText;
                setLvProductType(text);
                return true;
            }
        });
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void setLvProductType(String text) {
        productTypeList = productTypeDAO.getAllProductTypeByLike(text.toUpperCase());
        if (productTypeList.size() > 0) {
            adapterProductType.changeDataset(productTypeList);
            tvTotal.setText(getString(R.string.total)+" " + productTypeList.size() + " "+getString(R.string.productType));
        } else {
            productTypeList = productTypeDAO.getAllProductType();
            adapterProductType.changeDataset(productTypeList);
            tvTotal.setText(getString(R.string.total)+" " + productTypeList.size() + " "+getString(R.string.productType));
            if (productTypeList.size() != 0)
                Toast.makeText(this, getString(R.string.cannot_find_result), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_item:
                break;
            case R.id.search_item2:
                break;
        }
        return false;
    }

    @Override
    public void onClick(int position) {
        hideSoftKeyboard(this);
        try {
            if (getIntent().getStringExtra("check").equals("check")) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", productTypeList.get(position).getProductTypeID());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        } catch (Exception e) {
            Intent intent = new Intent(this, ProductTypeInforActivity.class);
            intent.putExtra("productTypeID", productTypeList.get(position).getProductTypeID());
            startActivity(intent);
        }


    }

    @Override
    public void onDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_product_type));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(checkProduct(productTypeList.get(position).getProductTypeID())){
                    long result = productTypeDAO.deleteProductType(productTypeList.get(position).getProductTypeID());
                    if (result > 0) {
                        adapterProductType.removeItem(position);
                        tvTotal.setText(getString(R.string.total)+" " + productTypeList.size() + " "+getString(R.string.productType));
                        Toast.makeText(ProductTypeActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductTypeActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ProductTypeActivity.this, getString(R.string.cannot_delete_product_type), Toast.LENGTH_SHORT).show();
                }

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

    @Override
    public void onEdit(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View dialogView = Objects.requireNonNull(inflater).inflate(R.layout.dialog_edit_product_type, null);
        builder.setView(dialogView);
        final Dialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        final EditText edProductTypeID = dialogView.findViewById(R.id.edProductTypeID);
        final EditText edProductTypeName = dialogView.findViewById(R.id.edProductTypeName);
        final EditText edProductTypeDes = dialogView.findViewById(R.id.edProductTypeDes);


        String oldProductTypeID = productTypeList.get(position).getProductTypeID();
        final String oldProductTypeName = productTypeList.get(position).getProductTypeName();
        final String oldProductTypeDes = productTypeList.get(position).getProductTypeDes();
        edProductTypeID.setText(oldProductTypeID);
        edProductTypeID.setEnabled(false);
        edProductTypeName.setText(oldProductTypeName);
        edProductTypeDes.setText(oldProductTypeDes);

        Button btnEdit = dialogView.findViewById(R.id.btnEdit);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newProductTypeName = edProductTypeName.getText().toString().trim();
                String newProductTypeDes = edProductTypeDes.getText().toString().trim();

                if (newProductTypeDes.isEmpty() || newProductTypeName.isEmpty()) {
                    if (newProductTypeName.isEmpty())
                        edProductTypeName.setError(getString(R.string.notify_empty_product_type_name));
                    if (newProductTypeDes.isEmpty())
                        edProductTypeDes.setError(getString(R.string.notify_empty_product_type_des));
                } else if (newProductTypeDes.length() > 255 || newProductTypeName.length() > 50) {
                    if (newProductTypeDes.length() > 255)
                        edProductTypeDes.setError(getString(R.string.notify_length_product_type_des));
                    if (newProductTypeName.length() > 50)
                        edProductTypeName.setError(getString(R.string.notify_length_product_type_name));
                } else if (checkString(newProductTypeName) || checkString(newProductTypeDes)) {
                    if (checkString(newProductTypeName))
                        edProductTypeName.setError(getString(R.string.string_exist_2_space));
                    if (checkString(newProductTypeDes))
                        edProductTypeDes.setError(getString(R.string.string_exist_2_space));
                } else {
                    Date date = new Date();
                    ProductType productType = new ProductType(productTypeList.get(position).getProductTypeID(), newProductTypeName, newProductTypeDes, oldProductTypeName, oldProductTypeDes, productTypeList.get(position).getAddedDay(), date.getTime());
                    long result = productTypeDAO.updateProductType(productType);
                    if (result > 0) {
                        productTypeList = productTypeDAO.getAllProductType();
                        adapterProductType.changeDataset(productTypeList);
                        dialog.dismiss();
                        Toast.makeText(ProductTypeActivity.this, getString(R.string.edited), Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        Toast.makeText(ProductTypeActivity.this, getString(R.string.edit_fail), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private boolean checkProduct(String productTypeID) {
        List<Product> productList = productDAO.getAllProduct();
        for (int i = 0; i < productList.size(); i++) {
            if (productList.get(i).getProductTypeID().equalsIgnoreCase(productTypeID))
                return false;
        }
        return true;
    }
}
