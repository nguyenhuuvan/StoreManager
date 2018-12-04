package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterProduct;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.listener.OnEdit;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Product;

public class ProductActivity extends Library implements OnDelete, OnEdit, OnClick {
    private RecyclerView lvListProduct;
    private AdapterProduct adapterProduct;
    private List<Product> productList;
    private FloatingActionButton fabAddProduct;
    private ImageView imgSort;
    private ProductDAO productDAO;
    private String text = "";
    private TextView tvTotal;
    private Toolbar toolbar;
    private BillDetailDAO billDetailDAO;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        productDAO = new ProductDAO(this);
        billDetailDAO = new BillDetailDAO(this);
        initViews();
        initAction();
        productList = productDAO.getAllProduct();
        adapterProduct = new AdapterProduct(this, productList, this, this, this);
        lvListProduct.setAdapter(adapterProduct);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListProduct.setLayoutManager(manager);
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTotal = findViewById(R.id.tvTotal);
        lvListProduct = findViewById(R.id.lvList);
        fabAddProduct = findViewById(R.id.fabAdd);
        imgSort = findViewById(R.id.imgSort);
    }


    private void initAction() {
        lvListProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabAddProduct.getVisibility() == View.VISIBLE) {
                    fabAddProduct.hide();

                } else if (dy < 0 && fabAddProduct.getVisibility() != View.VISIBLE) {
                    fabAddProduct.show();

                }
            }
        });
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductActivity.this, AddProductActivity.class));
            }
        });
        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(ProductActivity.this, imgSort);
                getMenuInflater().inflate(R.menu.menu_product, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.sort_by_name:
                                runLayoutAnimationLeft(lvListProduct);
                                Collections.sort(productList, new Comparator<Product>() {
                                    @Override
                                    public int compare(Product pb1, Product pb2) {
                                        int a = pb1.getProductName().compareToIgnoreCase(pb2.getProductName());
                                        if (a == 0) {
                                            return pb1.getProductName().compareToIgnoreCase(pb2.getProductName());
                                        }
                                        return a;
                                    }
                                });
                                break;
                            case R.id.sort_by_id:
                                runLayoutAnimationRight(lvListProduct);
                                Collections.sort(productList, new Comparator<Product>() {
                                    @Override
                                    public int compare(Product pb1, Product pb2) {
                                        int a = pb1.getProductID().compareToIgnoreCase(pb2.getProductID());
                                        if (a == 0) {
                                            return pb1.getProductID().compareToIgnoreCase(pb2.getProductID());
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

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        setLvProduct(text);
        Log.e("text", text);
        runLayoutAnimationLeft(lvListProduct);
        tvTotal.setText(getString(R.string.total) + " " + productList.size() + " " + getString(R.string.product_));
        super.onResume();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            final ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (!matches.isEmpty()) {
                searchMenuItem.expandActionView();
                searchView.setQuery(matches.get(0),false);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem= menu.findItem(R.id.search_item);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(
                Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                text = query;
                setLvProduct(text);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                text = newText;
                setLvProduct(text);
                return true;
            }
        });
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void setLvProduct(String text) {
        productList = productDAO.getAllProductByLike(text.toUpperCase());
        if (productList.size() > 0) {
            adapterProduct.changeDataset(productList);
            tvTotal.setText(getString(R.string.total) + " " + productList.size() + " " + getString(R.string.product_));
        } else {
            productList = productDAO.getAllProduct();
            adapterProduct.changeDataset(productList);
            tvTotal.setText(getString(R.string.total) + " " + productList.size() + " " + getString(R.string.product_));
            if (productList.size() != 0)
                Toast.makeText(this, getString(R.string.cannot_find_result), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_item:
                break;
            case R.id.search_item2:
                startVoiceRecognitionActivity("Tìm theo mã. Xin mời nói...");
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
                returnIntent.putExtra("result", productList.get(position).getProductID());
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        } catch (Exception e) {
            Intent intent = new Intent(ProductActivity.this, ProductInforActivity.class);
            intent.putExtra("productID", productList.get(position).getProductID());
            startActivity(intent);
        }
    }

    @Override
    public void onDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_product));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<BillDetail> billDetail = billDetailDAO.getAllBillDetailsByProductID(productList.get(position).getProductID());
                if (billDetail.size() > 0)
                {
                    Toast.makeText(ProductActivity.this, getString(R.string.cannot_delete_product), Toast.LENGTH_SHORT).show();
                } else {
                    long result = productDAO.deleteProduct(productList.get(position).getProductID());
                    if (result > 0) {
                        adapterProduct.removeItem(position);
                        tvTotal.setText(getString(R.string.total) + " " + productList.size() + " " + getString(R.string.product_));
                        Toast.makeText(ProductActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
                    }
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
    public void onEdit(int position) {
        Intent intent = new Intent(ProductActivity.this, EditProductActivity.class);
        intent.putExtra("productID", productList.get(position).getProductID());
        startActivity(intent);
    }

}
