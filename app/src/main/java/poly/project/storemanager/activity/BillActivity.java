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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterBill;
import poly.project.storemanager.dao.BillDAO;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.listener.OnEdit;
import poly.project.storemanager.model.Bill;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Customer;
import poly.project.storemanager.model.Product;

public class BillActivity extends Library implements OnDelete, OnEdit, OnClick {
    private TextView tvTotal;
    private RecyclerView lvListBill;
    private AdapterBill adapterBill;
    private List<Bill> billList;
    private FloatingActionButton fabAddBill;
    private ImageView imgSort, imgCalender;
    private BillDAO billDAO;
    private CustomerDAO customerDAO;
    private String text = "";
    private BillDetailDAO billDetailDAO;
    private ProductDAO productDAO;
    private TextView tvCalender;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        billDAO = new BillDAO(this);
        customerDAO = new CustomerDAO(this);
        billList = billDAO.getAllBill();
        billDetailDAO = new BillDetailDAO(this);
        productDAO = new ProductDAO(this);
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
        tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
        lvListBill = findViewById(R.id.lvList);
        fabAddBill = findViewById(R.id.fabAdd);
        imgSort = findViewById(R.id.imgSort);
        imgCalender = findViewById(R.id.imgCalender);
        tvCalender = findViewById(R.id.tvCalender);
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
                                Collections.sort(billList, new Comparator<Bill>() {
                                    @Override
                                    public int compare(Bill pb1, Bill pb2) {
                                        int a = getSum(pb1.getBillID()).compareTo(getSum(pb2.getBillID()));
                                        if (a == 0) {
                                            return getSum(pb1.getBillID()).compareTo(getSum(pb2.getBillID()));
                                        }
                                        return a;
                                    }
                                });
                                break;
                            case R.id.down:
                                runLayoutAnimationRight(lvListBill);
                                Collections.sort(billList, new Comparator<Bill>() {
                                    @Override
                                    public int compare(Bill pb1, Bill pb2) {
                                        int a = getSum(pb2.getBillID()).compareTo(getSum(pb1.getBillID()));
                                        if (a == 0) {
                                            return getSum(pb2.getBillID()).compareTo(getSum(pb1.getBillID()));
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
        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(BillActivity.this, imgCalender);
                getMenuInflater().inflate(R.menu.menu_calender, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.day:
                                billList = billDAO.getAllBillOfDay();
                                adapterBill.changeDataset(billList);
                                runLayoutAnimationLeft(lvListBill);
                                tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
                                tvCalender.setText(getString(R.string.day));
                                break;
                            case R.id.week:
                                billList = billDAO.getAllBillOfWeek();
                                adapterBill.changeDataset(billList);
                                runLayoutAnimationRight(lvListBill);
                                tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
                                tvCalender.setText(getString(R.string.week));
                                break;
                            case R.id.month:
                                billList = billDAO.getAllBillOfMonth();
                                adapterBill.changeDataset(billList);
                                runLayoutAnimationLeft(lvListBill);
                                tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
                                tvCalender.setText(getString(R.string.month));
                                break;
                            case R.id.year:
                                billList = billDAO.getAllBillOfYear();
                                adapterBill.changeDataset(billList);
                                runLayoutAnimationRight(lvListBill);
                                tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
                                tvCalender.setText(getString(R.string.year));
                                break;
                            case R.id.all:
                                billList = billDAO.getAllBill();
                                adapterBill.changeDataset(billList);
                                runLayoutAnimationLeft(lvListBill);
                                tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
                                tvCalender.setText(getString(R.string.all));
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    private Double getSum(String billID) {
        List<BillDetail> billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
        double sum = 0;
        for (int i = 0; i < billDetailList.size(); i++) {
            Product product = productDAO.getProductByID(billDetailList.get(i).getProductID());
            sum = sum + ((double) product.getOutputPrice() * (double) billDetailList.get(i).getQuantity());
        }
        return sum;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        setLvBill(text);
        runLayoutAnimationLeft(lvListBill);
        tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
        tvCalender.setText(getString(R.string.all));
        super.onResume();
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
                setLvBill(text);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                text = newText;
                setLvBill(text);
                return true;
            }
        });
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void setLvBill(String text) {
        billList = billDAO.getAllBillByLike(text.toUpperCase());
        if (billList.size() > 0) {
            adapterBill.changeDataset(billList);
            tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
        } else {
            billList = billDAO.getAllBill();
            adapterBill.changeDataset(billList);
            tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
            if (billList.size() != 0)
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
        if (billList.get(position).getStatus().equalsIgnoreCase("Chưa thanh toán")) {
            Intent intent = new Intent(this, BillDetailActivity.class);
            intent.putExtra("billID", billList.get(position).getBillID());
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, BillInforActivity.class);
            intent.putExtra("billID", billList.get(position).getBillID());
            startActivity(intent);
        }
    }

    @Override
    public void onDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_bill));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<BillDetail> billDetail = billDetailDAO.getAllBillDetailsByBillID(billList.get(position).getBillID());
                if (billDetail.size() > 0) {
                    Toast.makeText(BillActivity.this, getString(R.string.cannot_delete_bill), Toast.LENGTH_SHORT).show();
                } else {
                    long result = billDAO.deleteBill(billList.get(position).getBillID());
                    if (result > 0) {
                        adapterBill.removeItem(position);
                        tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
                        Toast.makeText(BillActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BillActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
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
    public void onEdit(final int position) {
        if (billList.get(position).getStatus().equalsIgnoreCase("Chưa thanh toán")) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            @SuppressLint("InflateParams") View dialogView = Objects.requireNonNull(inflater).inflate(R.layout.dialog_edit_bill, null);
            builder.setView(dialogView);
            final Dialog dialog = builder.create();
            Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();
            final EditText edBillID = dialogView.findViewById(R.id.edBillID);
            final EditText edPhoneCustomer = dialogView.findViewById(R.id.edCustomerPhone);
            final EditText edCustomerName = dialogView.findViewById(R.id.edCustomerName);

            String oldBillID = billList.get(position).getBillID();
            final String oldPhoneCustomer = billList.get(position).getCustomerPhone();

            edBillID.setText(oldBillID);
            edPhoneCustomer.setText(oldPhoneCustomer);
            Customer oldcustomer = customerDAO.getCustomerByID(oldPhoneCustomer);
            if (oldcustomer != null) {
                edCustomerName.setText(oldcustomer.getCustomerName());
                edCustomerName.setEnabled(false);
            } else {
                Toast.makeText(this, getString(R.string.customer_deleted), Toast.LENGTH_SHORT).show();
            }
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
            Button edit = dialogView.findViewById(R.id.btnEdit);
            Button cancel = dialogView.findViewById(R.id.btnCancel);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phoneCustomer = edPhoneCustomer.getText().toString().trim();
                    String customerName = edCustomerName.getText().toString().trim();

                    if (phoneCustomer.isEmpty() || customerName.isEmpty()) {
                        if (phoneCustomer.isEmpty())
                            edPhoneCustomer.setError(getString(R.string.notify_empty_phone));
                        if (customerName.isEmpty())
                            edCustomerName.setError(getString(R.string.notify_empty_name));
                    } else if (customerName.length() > 30) {
                        edCustomerName.setError(getString(R.string.notify_length_name));
                    } else if (checkString(customerName) || !phoneCustomer.matches(format_phone)) {
                        if (checkString(customerName))
                            edCustomerName.setError(getString(R.string.string_exist_2_space));
                        if (!phoneCustomer.matches(format_phone))
                            edPhoneCustomer.setError(getString(R.string.notify_same_sdt));
                    } else {
                        Date date = new Date();
                        Bill bill1 = new Bill(billList.get(position).getBillID(), phoneCustomer, oldPhoneCustomer, date.getTime(), date.getTime(), "Chưa thanh toán");
                        long result = billDAO.updateBill(bill1);
                        if (result > 0) {
                            billList = billDAO.getAllBill();
                            adapterBill.changeDataset(billList);
                            Toast.makeText(BillActivity.this, getString(R.string.edited), Toast.LENGTH_SHORT).show();
                            if (customerDAO.getCustomerByID(phoneCustomer) == null) {
                                Customer customer = new Customer(phoneCustomer, changeString(customerName), "", "", "", "", "", "", "", "", "", 0, 0, date.getTime(), 0,null,null);
                                customerDAO.insertCustomer(customer);
                            }
                        } else {
                            Toast.makeText(BillActivity.this, getString(R.string.edit_fail), Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();

                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            Toast.makeText(this, getString(R.string.cannot_editbill), Toast.LENGTH_SHORT).show();
        }
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
                        Bill bill1 = new Bill(billID, phoneCustomer, "", date.getTime(), 0, "Chưa thanh toán");
                        long result = billDAO.insertBill(bill1);
                        if (result > 0) {
                            billList = billDAO.getAllBill();
                            adapterBill.changeDataset(billList);
                            Toast.makeText(BillActivity.this, getString(R.string.added), Toast.LENGTH_SHORT).show();
                            tvTotal.setText(getString(R.string.total) + " " + billList.size() + " " + getString(R.string.bill_));
                            if (customerDAO.getCustomerByID(phoneCustomer) == null) {
                                Customer customer = new Customer(phoneCustomer, changeString(customerName), "", "", "", "", "", "", "", "", "", 0, 0, date.getTime(), 0,null,null);
                                customerDAO.insertCustomer(customer);
                            }
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
