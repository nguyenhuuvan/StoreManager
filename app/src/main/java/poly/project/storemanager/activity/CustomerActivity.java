package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
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
import poly.project.storemanager.adapter.AdapterCustomer;
import poly.project.storemanager.dao.BillDAO;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.listener.OnEdit;
import poly.project.storemanager.model.Bill;
import poly.project.storemanager.model.Customer;
import poly.project.storemanager.model.ListProductOfCustomer;

public class CustomerActivity extends Library implements OnEdit, OnClick, OnDelete {
    private RecyclerView lvListCustomer;
    private AdapterCustomer adapterCustomer;
    private List<Customer> customerList;
    private TextView tvTotal;
    private ImageView imgSort;
    private FloatingActionButton fabAddCustomer;
    private CustomerDAO customerDAO;
    private String text = "";
    private BillDAO billDAO;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        initViews();
        initAction();
        customerDAO = new CustomerDAO(this);
        billDAO = new BillDAO(this);
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
        runLayoutAnimationLeft(lvListCustomer);
        tvTotal.setText(getString(R.string.total) + " " + customerList.size() + " " + getString(R.string.customer_));
        super.onResume();
    }

    @SuppressLint("SetTextI18n")
    private void setLvCustomer(String text) {
        customerList = customerDAO.getAllCustomerByLike(text.toUpperCase());
        if (customerList.size() > 0) {
            adapterCustomer.changeDataset(customerList);
            tvTotal.setText(getString(R.string.total) + " " + customerList.size() + " " + getString(R.string.customer_));
        } else {
            customerList = customerDAO.getAllCustomer();
            adapterCustomer.changeDataset(customerList);
            tvTotal.setText(getString(R.string.total) + " " + customerList.size() + " " + getString(R.string.customer_));
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
                                Collections.sort(customerList, new Comparator<Customer>() {
                                    @Override
                                    public int compare(Customer pb1, Customer pb2) {
                                        int a = getTotalPriceOfCustomer(pb1.getCustomerPhone()).compareTo(getTotalPriceOfCustomer(pb2.getCustomerPhone()));
                                        if (a == 0) {
                                            return getTotalPriceOfCustomer(pb1.getCustomerPhone()).compareTo(getTotalPriceOfCustomer(pb2.getCustomerPhone()));
                                        }
                                        return a;
                                    }
                                });
                                runLayoutAnimationLeft(lvListCustomer);
                                break;
                            case R.id.down:
                                Collections.sort(customerList, new Comparator<Customer>() {
                                    @Override
                                    public int compare(Customer pb1, Customer pb2) {
                                        int a = getTotalPriceOfCustomer(pb2.getCustomerPhone()).compareTo(getTotalPriceOfCustomer(pb1.getCustomerPhone()));
                                        if (a == 0) {
                                            return getTotalPriceOfCustomer(pb2.getCustomerPhone()).compareTo(getTotalPriceOfCustomer(pb1.getCustomerPhone()));
                                        }
                                        return a;
                                    }
                                });
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

    private Double getTotalPriceOfCustomer(String customerPhone) {
        double total = 0;
        List<ListProductOfCustomer> listProductOfCustomer = customerDAO.getProductofCustomer(customerPhone);
        for (int i = 0; i < listProductOfCustomer.size(); i++) {
            total += (double) listProductOfCustomer.get(i).getQuantity() * (double) (listProductOfCustomer.get(i).getPrice());
        }
        return total;
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
                startVoiceRecognitionActivity("Tìm theo SĐT. Xin mời nói...");
                break;
        }
        return false;
    }

    @Override
    public void onClick(int position) {
        hideSoftKeyboard(this);
        Intent intent = new Intent(CustomerActivity.this, CustomerInforActivity.class);
        intent.putExtra("customerPhone", customerList.get(position).getCustomerPhone());
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
                if (checkBill(customerList.get(position).getCustomerPhone())) {
                    long result = customerDAO.deleteCustomer(customerList.get(position).getCustomerPhone());
                    if (result > 0) {
                        adapterCustomer.removeItem(position);
                        tvTotal.setText(getString(R.string.total) + " " + customerList.size() + " " + getString(R.string.customer_));
                        Toast.makeText(CustomerActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CustomerActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CustomerActivity.this, getString(R.string.cannot_delete_customer), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(CustomerActivity.this, EditCustomerActivity.class);
        intent.putExtra("customerPhone", customerList.get(position).getCustomerPhone());
        startActivity(intent);
    }

    private boolean checkBill(String customerPhone) {
        List<Bill> billList = billDAO.getAllBill();
        for (int i = 0; i < billList.size(); i++) {
            if (billList.get(i).getCustomerPhone().equalsIgnoreCase(customerPhone))
                return false;
        }
        return true;

    }
}
