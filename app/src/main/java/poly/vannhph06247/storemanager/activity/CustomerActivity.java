package poly.vannhph06247.storemanager.activity;

import android.annotation.SuppressLint;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import poly.vannhph06247.storemanager.Library;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterCustomer;
import poly.vannhph06247.storemanager.dao.CustomerDAO;
import poly.vannhph06247.storemanager.listener.OnClick;
import poly.vannhph06247.storemanager.listener.OnDelete;
import poly.vannhph06247.storemanager.listener.OnEdit;
import poly.vannhph06247.storemanager.model.Customer;

public class CustomerActivity extends Library implements OnEdit, OnClick, OnDelete {
    private RecyclerView lvListCustomer;
    private AdapterCustomer adapterCustomer;
    private List<Customer> customerList;
    private TextView tvTotal;
    private ImageView imgSort;
    private FloatingActionButton fabAddCustomer;
    private CustomerDAO customerDAO;
    private String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        initViews();
        initAction();
        customerDAO = new CustomerDAO(this);
        customerList = customerDAO.getAllCustomer();
        adapterCustomer = new AdapterCustomer(this, customerList, this, this, this);
        lvListCustomer.setAdapter(adapterCustomer);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListCustomer.setLayoutManager(manager);

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListCustomer = findViewById(R.id.lvList);
        tvTotal = findViewById(R.id.tvTotal);
        imgSort = findViewById(R.id.imgSort);
        fabAddCustomer = findViewById(R.id.fabAdd);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        setLvCustomer(text);
        Log.e("text", text);
        runLayoutAnimationLeft(lvListCustomer);
        tvTotal.setText(getString(R.string.total)+" " + customerList.size() + " "+getString(R.string.customer_));
        super.onResume();
    }

    @SuppressLint("SetTextI18n")
    private void setLvCustomer(String text) {
        customerList = customerDAO.getAllCustomerByLike(text.toUpperCase());
        if (customerList.size() > 0) {
            adapterCustomer.changeDataset(customerList);
            tvTotal.setText(getString(R.string.total)+" " + customerList.size() + " "+getString(R.string.customer_));
        } else {
            customerList = customerDAO.getAllCustomer();
            adapterCustomer.changeDataset(customerList);
            tvTotal.setText(getString(R.string.total)+" " + customerList.size() + " "+getString(R.string.customer_));
            if (customerList.size() != 0)
                Toast.makeText(this, getString(R.string.cannot_find_result), Toast.LENGTH_SHORT).show();
        }
    }

    private void initAction() {
        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(CustomerActivity.this, imgSort);
                getMenuInflater().inflate(R.menu.menu_customer, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.up:
                                runLayoutAnimationLeft(lvListCustomer);
                                break;
                            case R.id.down:
                                runLayoutAnimationRight(lvListCustomer);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        lvListCustomer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabAddCustomer.getVisibility() == View.VISIBLE) {
                    fabAddCustomer.hide();

                } else if (dy < 0 && fabAddCustomer.getVisibility() != View.VISIBLE) {
                    fabAddCustomer.show();

                }
            }
        });
        fabAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CustomerActivity.this, AddCustomerActivty.class));
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
                setLvCustomer(text);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                text = newText;
                setLvCustomer(text);
                return true;
            }
        });
        return true;
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
        Intent intent = new Intent(CustomerActivity.this,CustomerInforActivity.class);
        intent.putExtra("customerPhone",customerList.get(position).getCustomerPhone());
        startActivity(intent);
    }

    @Override
    public void onDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_customer));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long result = customerDAO.deleteCustomer(customerList.get(position).getCustomerPhone());
                if (result > 0) {
                    adapterCustomer.removeItem(position);
                    tvTotal.setText(getString(R.string.total)+" " + customerList.size() + " "+getString(R.string.customer_));
                    Toast.makeText(CustomerActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CustomerActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(CustomerActivity.this,EditCustomerActivity.class);
        intent.putExtra("customerPhone",customerList.get(position).getCustomerPhone());
        startActivity(intent);
    }
}
