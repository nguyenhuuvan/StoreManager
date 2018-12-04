package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.adapter.AdapterBillDetail;
import poly.project.storemanager.dao.BillDAO;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Product;

public class BillDetailActivity extends Library implements OnDelete, OnClick {
    private RecyclerView lvListBillDetail;
    private List<BillDetail> billDetailList;
    private List<BillDetail> billDetails;
    private String billID = null;
    private BillDetailDAO billDetailDAO;
    private ProductDAO productDAO;
    private LinearLayout pay;
    private EditText edProductID;
    private EditText edQuantity;
    private TextView tvTotalMonney;
    private Button btnBuy;
    private Button btnEdit;
    private AdapterBillDetail adapterBillDetail;
    private Double sum = 0.0;
    private int position = -1;
    private BillDAO billDAO;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_detail);

        initViews();
        initAction();
        billDetailDAO = new BillDetailDAO(this);
        productDAO = new ProductDAO(this);
        billDAO = new BillDAO(this);
        billID = getIntent().getStringExtra("billID");
        billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
        adapterBillDetail = new AdapterBillDetail(this, billDetailList, this, this);
        lvListBillDetail.setAdapter(adapterBillDetail);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListBillDetail.setLayoutManager(manager);
        tvTotalMonney.setText(" "+formatVnCurrence(getSum().toString()));
    }

    private void initAction() {
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(BillDetailActivity.this);
                pay();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(BillDetailActivity.this);
                editBillDetail();
            }
        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(BillDetailActivity.this);
                buyProduct();
            }
        });
        edProductID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BillDetailActivity.this, ProductActivity.class);
                i.putExtra("check", "check");
                startActivityForResult(i, 1);
            }
        });
    }

    private void pay() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_pay));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long result =  billDAO.updateStatus(billID,"Đã thanh toán");
                if (result > 0) {
                    Toast.makeText(BillDetailActivity.this, getString(R.string.paid), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(BillDetailActivity.this, getString(R.string.fail), Toast.LENGTH_SHORT).show();
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

    private void buyProduct() {
        String strQuantity = edQuantity.getText().toString().trim();
        String strProductID = edProductID.getText().toString().trim();
        if (strQuantity.isEmpty() || strProductID.isEmpty()) {
            if (strQuantity.isEmpty())
                edQuantity.setError(getString(R.string.notify_empty_quantity));
            if (strProductID.isEmpty()) {
                edProductID.setText(getString(R.string.chooseProduct));
                edProductID.setTextSize(12);
            }

        } else if (checkInteger(strQuantity)) {
            edQuantity.setError(getString(R.string.number_too_large));
        } else if (Integer.parseInt(strQuantity) <= 0) {
            edQuantity.setError(getString(R.string.notify_0));
        } else {
            Product product = productDAO.getProductByID(strProductID);
            if (product != null) {
                billDetails = billDetailDAO.getAllBillDetailsByProductID(strProductID);
                int quantity = getAllQuantity();
                if (Double.parseDouble(strQuantity) + (double) quantity > (double) product.getQuantity()) {
                    edQuantity.setError(getString(R.string.notify_Quantity) + " " + (product.getQuantity() - (quantity)) + " " + getString(R.string.product_));
                } else {
                    int checkProduct = checkProductID(strProductID);
                    // hàm check lấy vị trí xuất hiện của sản phẩm nếu có thì chỉ sửa nếu chưa có thì thêm vào
                    if (checkProduct >= 0) {
                        long result = billDetailDAO.updateBillDetail(billDetailList.get(checkProduct).getBillDetailID(), billID, strProductID, billDetailList.get(checkProduct).getQuantity() + Integer.parseInt(strQuantity));
                        if (result > 0) {
                            billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
                            adapterBillDetail.changeDataset(billDetailList);
                            Toast.makeText(BillDetailActivity.this, getString(R.string.added), Toast.LENGTH_SHORT).show();
                            changeSum();
                        } else {
                            Toast.makeText(BillDetailActivity.this, getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        // chưa có thì thêm vào
                        long result = billDetailDAO.insertBillDetail(billID, strProductID, Integer.parseInt(strQuantity));
                        if (result > 0) {
                            billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
                            adapterBillDetail.changeDataset(billDetailList);
                            Toast.makeText(BillDetailActivity.this, getString(R.string.added), Toast.LENGTH_SHORT).show();
                            changeSum();
                        } else {
                            Toast.makeText(BillDetailActivity.this, getString(R.string.add_fail), Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            } else {
                Toast.makeText(BillDetailActivity.this, getString(R.string.notify_productID_not_exists), Toast.LENGTH_SHORT).show();
            }


        }
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

        builder.setMessage(getString(R.string.delete_message_allproduct_of_bill));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0;i<billDetailList.size();i++){
                    billDetailDAO.deleteBillDetail(billDetailList.get(i).getBillDetailID());
                }
                billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
                adapterBillDetail.changeDataset(billDetailList);
                changeSum();
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

    private void editBillDetail() {
        String strQuantity = edQuantity.getText().toString().trim();
        String strProductID = edProductID.getText().toString().trim();
        if (position == -1) {
            Toast.makeText(this, getString(R.string.chooseBillDetail), Toast.LENGTH_SHORT).show();
        } else if (strQuantity.isEmpty() || strProductID.isEmpty()) {
            if (strQuantity.isEmpty())
                edQuantity.setError(getString(R.string.notify_empty_quantity));
            if (strProductID.isEmpty()) {
                edProductID.setText(getString(R.string.chooseProduct));
                edProductID.setTextSize(12);
            }
        } else if (checkInteger(strQuantity)) {
            edQuantity.setError(getString(R.string.number_too_large));
        } else if (Integer.parseInt(strQuantity) <= 0) {
            edQuantity.setError(getString(R.string.notify_0));
        } else {
            Product product = productDAO.getProductByID(strProductID);
            if (product != null) {

                billDetails = billDetailDAO.getAllBillDetailsByProductID(strProductID);
                int quantity = getAllQuantity();
                int checkProduct = checkProductID(strProductID);
                if (checkProduct >= 0) {
                    if (checkProduct != position) {
                        if (Double.parseDouble(strQuantity) + (double) quantity > (double) product.getQuantity()) {
                            edQuantity.setError(getString(R.string.notify_Quantity) + " " + (product.getQuantity() - (quantity)) + " " + getString(R.string.product_));
                        } else {
                            billDetailDAO.deleteBillDetail(billDetailList.get(position).getBillDetailID());
                            long result = billDetailDAO.updateBillDetail(billDetailList.get(checkProduct).getBillDetailID(), billID, strProductID, (billDetailList.get(checkProduct).getQuantity() + Integer.parseInt(strQuantity)));
                            if (result > 0) {
                                billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
                                adapterBillDetail.changeDataset(billDetailList);
                                Toast.makeText(BillDetailActivity.this, getString(R.string.edited), Toast.LENGTH_SHORT).show();
                                changeSum();
                                Log.e("TH1", "TH1: Có rồi thì ghi đè lên và xóa vị trí cũ");
                                edQuantity.setText("");
                                edProductID.setText("");
                                position = -1;
                            } else {
                                Toast.makeText(BillDetailActivity.this, getString(R.string.edit_fail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        if (Double.parseDouble(strQuantity) +(double) quantity - (double)billDetailList.get(position).getQuantity() >(double) product.getQuantity()) {
                            edQuantity.setError(getString(R.string.notify_Quantity) + " " + (product.getQuantity() - (quantity)) + " " + getString(R.string.product_));
                        } else {
                            long result = billDetailDAO.updateBillDetail(billDetailList.get(position).getBillDetailID(), billID, strProductID, Integer.parseInt(strQuantity));
                            if (result > 0) {
                                billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
                                adapterBillDetail.changeDataset(billDetailList);
                                Toast.makeText(BillDetailActivity.this, getString(R.string.edited), Toast.LENGTH_SHORT).show();
                                changeSum();
                                Log.e("TH2", "TH2: Sửa vị trí cũ cùng mã sản phẩm");
                                edQuantity.setText("");
                                edProductID.setText("");
                                position = -1;
                            } else {
                                Toast.makeText(BillDetailActivity.this, getString(R.string.edit_fail), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    if (Double.parseDouble(strQuantity) +(double) quantity >(double) product.getQuantity()) {
                        edQuantity.setError(getString(R.string.notify_Quantity) + " " + (product.getQuantity() - (quantity)) + " " + getString(R.string.product_));
                    } else {
                        long result = billDetailDAO.updateBillDetail(billDetailList.get(position).getBillDetailID(), billID, strProductID, Integer.parseInt(strQuantity));
                        if (result > 0) {
                            billDetailList = billDetailDAO.getAllBillDetailsByBillID(billID);
                            adapterBillDetail.changeDataset(billDetailList);
                            Toast.makeText(BillDetailActivity.this, getString(R.string.edited), Toast.LENGTH_SHORT).show();
                            changeSum();
                            Log.e("TH3", "TH3: Không cùng mã sản phẩm");
                            edQuantity.setText("");
                            edProductID.setText("");
                            position = -1;
                        } else {
                            Toast.makeText(BillDetailActivity.this, getString(R.string.edit_fail), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            } else {
                Toast.makeText(BillDetailActivity.this, getString(R.string.notify_productID_not_exists), Toast.LENGTH_SHORT).show();
            }

        }
    }


    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        tvTotalMonney = findViewById(R.id.tvTotalMoney);
        lvListBillDetail = findViewById(R.id.lvList);
        pay = findViewById(R.id.pay);
        edProductID = findViewById(R.id.edProductID);
        edQuantity = findViewById(R.id.edQuantity);
        btnBuy = findViewById(R.id.btnBuy);
        btnEdit = findViewById(R.id.btnEdit);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                edProductID.setText(result);
                edProductID.setTextSize(16);
            }
        }
    }

    @Override
    protected void onResume() {
        Product product = productDAO.getProductByID(edProductID.getText().toString().trim());
        if (product == null) {
            edProductID.setText("");
        }
        runLayoutAnimationLeft(lvListBillDetail);
        super.onResume();
    }

    @Override
    public void onDelete(final int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(getString(R.string.delete_message_bill));
        builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                long result = billDetailDAO.deleteBillDetail(billDetailList.get(position).getBillDetailID());
                if (result > 0) {
                    adapterBillDetail.removeItem(position);
                    Toast.makeText(BillDetailActivity.this, getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                    changeSum();
                } else {
                    Toast.makeText(BillDetailActivity.this, getString(R.string.deletefail), Toast.LENGTH_SHORT).show();
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

    private String formatVnCurrence(String price) {

        NumberFormat format =
                new DecimalFormat("#,##0.00");
        format.setCurrency(Currency.getInstance(Locale.US));

        price = (!TextUtils.isEmpty(price)) ? price : "0";
        price = price.trim();
        price = format.format(Double.parseDouble(price));
        price = price.replaceAll(",", "\\.");

        if (price.endsWith(".00")) {
            int centsIndex = price.lastIndexOf(".00");
            if (centsIndex != -1) {
                price = price.substring(0, centsIndex);
            }
        }
        price = String.format("%s VNĐ", price);
        return price;
    }

    private int checkProductID(String ID) {
        int position = -1;
        for (int i = 0; i < billDetailList.size(); i++) {
            if (ID.equalsIgnoreCase(billDetailList.get(i).getProductID())) {
                position = i;
            }
        }
        return position;
    }

    private int getAllQuantity() {
        int quantity = 0;
        for (int i = 0; i < billDetails.size(); i++) {
            quantity = quantity + billDetails.get(i).getQuantity();
        }
        return quantity;
    }

    private Double getSum() {
        for (int i = 0; i < billDetailList.size(); i++) {
            Product product = productDAO.getProductByID(billDetailList.get(i).getProductID());
            sum = sum + ((double) product.getOutputPrice() * (double) billDetailList.get(i).getQuantity());
        }
        return sum;
    }

    @SuppressLint("SetTextI18n")
    private void changeSum() {
        sum = 0.0;
        tvTotalMonney.setText(" "+formatVnCurrence(getSum().toString()));
    }

    @Override
    public void onClick(int position) {
        this.position = position;
        edProductID.setText(billDetailList.get(position).getProductID());
        edQuantity.setText(String.valueOf(billDetailList.get(position).getQuantity()));
    }
}
