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
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Date;
import java.util.List;
import java.util.Objects;

import poly.vannhph06247.storemanager.Library;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterBill;
import poly.vannhph06247.storemanager.dao.BillDAO;
import poly.vannhph06247.storemanager.dao.CustomerDAO;
import poly.vannhph06247.storemanager.listener.OnClick;
import poly.vannhph06247.storemanager.listener.OnDelete;
import poly.vannhph06247.storemanager.listener.OnEdit;
import poly.vannhph06247.storemanager.model.Bill;
import poly.vannhph06247.storemanager.model.Customer;

public class BillActivity extends Library implements OnDelete, OnEdit, OnClick {
    private TextView tvTotal;
    private RecyclerView lvListBill;
    private AdapterBill adapterBill;
    private List<Bill> billList;
    private FloatingActionButton fabAddBill;
    private ImageView imgSort, imgCalender;
    private BillDAO billDAO;
    private CustomerDAO customerDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        billDAO = new BillDAO(this);
        customerDAO = new CustomerDAO(this);
        billList = billDAO.getAllBill();
        initViews();
        initAction();

        adapterBill = new AdapterBill(this, billList, this, this, this);
        lvListBill.setAdapter(adapterBill);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListBill.setLayoutManager(manager);
    }


    @SuppressLint("SetTextI18n")
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText(getString(R.string.total) +" "+ billList.size() + " "+getString(R.string.bill_));
        lvListBill = findViewById(R.id.lvList);
        fabAddBill = findViewById(R.id.fabAdd);
        imgSort = findViewById(R.id.imgSort);
        imgCalender = findViewById(R.id.imgCalender);

    }

    private void initAction() {
        lvListBill.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fabAddBill.getVisibility() == View.VISIBLE) {
                    fabAddBill.hide();

                } else if (dy < 0 && fabAddBill.getVisibility() != View.VISIBLE) {
                    fabAddBill.show();

                }
            }
        });
        fabAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddBill();
            }
        });

        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(BillActivity.this, imgSort);
                getMenuInflater().inflate(R.menu.menu_customer, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.up:
                                runLayoutAnimationLeft(lvListBill);
                                break;
                            case R.id.down:
                                runLayoutAnimationRight(lvListBill);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(BillActivity.this, imgCalender);
                getMenuInflater().inflate(R.menu.menu_calender, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.day:

                                break;
                            case R.id.week:

                                break;
                            case R.id.month:

                                break;
                            case R.id.year:

                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }


    @Override
    protected void onResume() {
        runLayoutAnimationLeft(lvListBill);
        super.onResume();
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
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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
        startActivity(new Intent(this, BillInforActivity.class));
    }

    @Override
    public void onDelete(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_bill));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View dialogView = Objects.requireNonNull(inflater).inflate(R.layout.dialog_edit_bill, null);
        builder.setView(dialogView);
        final Dialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        EditText edBillID = dialogView.findViewById(R.id.edBillID);
        edBillID.setEnabled(false);
        Button add = dialogView.findViewById(R.id.btnAdd);
        Button cancel = dialogView.findViewById(R.id.btnCancel);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showDialogAddBill() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View dialogView = Objects.requireNonNull(inflater).inflate(R.layout.dialog_add_bill, null);
        builder.setView(dialogView);
        final Dialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        Button add = dialogView.findViewById(R.id.btnAdd);
        Button cancel = dialogView.findViewById(R.id.btnCancel);
        final EditText edBillID = dialogView.findViewById(R.id.edBillID);
        final EditText edPhoneCustomer = dialogView.findViewById(R.id.edCustomerPhone);
        final EditText edCustomerName = dialogView.findViewById(R.id.edCustomerName);

        edPhoneCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Customer customer1 = customerDAO.getCustomerByID(edPhoneCustomer.getText().toString().trim());
                if (customer1 != null) {
                    edCustomerName.setText(customer1.getCustomerName());
                    edCustomerName.setEnabled(false);
                } else {
                    edCustomerName.setEnabled(true);

                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String billID = edBillID.getText().toString().trim().toUpperCase();
                String phoneCustomer = edPhoneCustomer.getText().toString().trim();
                String customerName = edCustomerName.getText().toString().trim();

                if (billID.isEmpty() || phoneCustomer.isEmpty() || customerName.isEmpty()) {
                    if (billID.isEmpty())
                        edBillID.setError(getString(R.string.notify_empty_bill_id));
                    if (phoneCustomer.isEmpty())
                        edPhoneCustomer.setError(getString(R.string.notify_empty_phone));
                    if (customerName.isEmpty())
                        edCustomerName.setError(getString(R.string.notify_empty_name));
                } else if (billID.length() > 10 || customerName.length() > 30) {
                    if (billID.length() > 10)
                        edBillID.setError(getString(R.string.notify_length_billID));
                    if (customerName.length() > 30)
                        edCustomerName.setError(getString(R.string.notify_length_name));
                } else if (checkPrimaryKey(billID) || checkString(customerName) || !phoneCustomer.matches(format_phone)) {
                    if (checkPrimaryKey(billID))
                        edBillID.setError(getString(R.string.primarykey_exist_space));
                    if (checkString(customerName))
                        edCustomerName.setError(getString(R.string.string_exist_2_space));
                    if (!phoneCustomer.matches(format_phone))
                        edPhoneCustomer.setError(getString(R.string.notify_same_sdt));
                } else {
                    Bill bill = billDAO.getBillByID(billID);
                    if (bill == null) {
                        Date date = new Date();
                        Bill bill1 = new Bill(billID, phoneCustomer, "", date.getTime(), 0);
                        long result = billDAO.insertBill(bill1);
                        if (result > 0) {
                            billList = billDAO.getAllBill();
                            adapterBill.changeDataset(billList);
                            Toast.makeText(BillActivity.this, getString(R.string.added), Toast.LENGTH_SHORT).show();
                            tvTotal.setText(getString(R.string.total) +" "+ billList.size() + " "+getString(R.string.bill_));
                            if (customerDAO.getCustomerByID(phoneCustomer) == null) {
                                Customer customer = new Customer(phoneCustomer, changeString(customerName), "", "", "", "", "", "", "", "", "", 0, 0, date.getTime(), 0);
                                customerDAO.insertCustomer(customer);
                            }
                            startActivity(new Intent(BillActivity.this,BillDetailActivity.class));
                        } else {
                            Toast.makeText(BillActivity.this, getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    } else {
                        edBillID.setError(getString(R.string.bill_id_exist));
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
}
