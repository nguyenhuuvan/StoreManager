package poly.vannhph06247.storemanager.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import poly.vannhph06247.storemanager.Library;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterListProduct;
import poly.vannhph06247.storemanager.dao.ProductDAO;
import poly.vannhph06247.storemanager.listener.OnDelete;
import poly.vannhph06247.storemanager.model.ListProduct;

public class ProductListOfProductTypeActivity extends Library implements OnDelete{
    private Toolbar toolbar;
    private TextView tvTotal;
    private RecyclerView lvListProductType;
    private AdapterListProduct adapterListProduct;
    private List<ListProduct> productList;
    private ImageView imgSort;
    private String productTypeID;
    private ProductDAO productDAO;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_of_product_type);

        initViews();
        initAction();
        productDAO = new ProductDAO(this);
        productTypeID = getIntent().getStringExtra("productTypeID");
        productList = productDAO.getAllProductByProductType(productTypeID);
        adapterListProduct = new AdapterListProduct(this, productList,this);
        lvListProductType.setAdapter(adapterListProduct);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListProductType.setLayoutManager(manager);
        tvTotal.setText(getString(R.string.total)+" " + productList.size() + " "+getString(R.string.product_));

    }
    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTotal = findViewById(R.id.tvTotal);
        lvListProductType = findViewById(R.id.lvList);
        imgSort = findViewById(R.id.imgSort);
    }


    private void initAction() {
        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(ProductListOfProductTypeActivity.this, imgSort);
                getMenuInflater().inflate(R.menu.menu_product,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.sort_by_name:
                                runLayoutAnimationLeft(lvListProductType);
                                Collections.sort(productList, new Comparator<ListProduct>() {
                                    @Override
                                    public int compare(ListProduct pb1, ListProduct pb2) {
                                        int a = pb1.getName().compareToIgnoreCase(pb2.getName());
                                        if (a == 0) {
                                            return pb1.getName().compareToIgnoreCase(pb2.getName());
                                        }
                                        return a;
                                    }
                                });
                                break;
                            case R.id.sort_by_id:
                                runLayoutAnimationRight(lvListProductType);
                                Collections.sort(productList, new Comparator<ListProduct>() {
                                    @Override
                                    public int compare(ListProduct pb1, ListProduct pb2) {
                                        int a = pb1.getId().compareToIgnoreCase(pb2.getId());
                                        if (a == 0) {
                                            return pb1.getId().compareToIgnoreCase(pb2.getId());
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    @Override
    protected void onResume() {
        runLayoutAnimationLeft(lvListProductType);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteAll:
                deleteAllProduct();
                break;
        }
        return false;
    }

    private void deleteAllProduct() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_allproduct));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0;i<productList.size();i++){
                    productDAO.deleteProduct(productList.get(i).getId());
                }
                productList = productDAO.getAllProductByProductType(productTypeID);
                adapterListProduct.changeDataset(productList);
                tvTotal.setText(getString(R.string.total)+" " + productList.size() + " "+getString(R.string.product_));
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
    public void onDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_product));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long result = productDAO.deleteProduct(productList.get(position).getId());
                if (result > 0) {
                    adapterListProduct.removeItem(position);
                    tvTotal.setText(getString(R.string.total)+" " + productList.size() + " "+getString(R.string.product_));
                    Toast.makeText(ProductListOfProductTypeActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProductListOfProductTypeActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
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
}
