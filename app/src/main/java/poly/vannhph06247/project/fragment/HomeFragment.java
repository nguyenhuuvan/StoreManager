package poly.project.storemanager.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.Date;

import poly.project.storemanager.R;
import poly.project.storemanager.activity.BillActivity;
import poly.project.storemanager.activity.CustomerActivity;
import poly.project.storemanager.activity.ProductActivity;
import poly.project.storemanager.activity.ProductTypeActivity;
import poly.project.storemanager.activity.SellingProductsActivity;
import poly.project.storemanager.activity.StatisticActivity;
import poly.project.storemanager.dao.BillDAO;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.dao.ProductTypeDAO;
import poly.project.storemanager.model.Bill;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Customer;
import poly.project.storemanager.model.Product;
import poly.project.storemanager.model.ProductType;

public class HomeFragment extends Fragment {
    private CustomerDAO customerDAO;
    private ProductTypeDAO productTypeDAO;
    private ProductDAO productDAO;
    private BillDAO billDAO;
    private BillDetailDAO billDetailDAO;
    private ImageView imgCustomer;
    private ImageView imgProduct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        imgCustomer = view.findViewById(R.id.imgCustomer);
        imgProduct = view.findViewById(R.id.imgProduct);
        ImageView imgProductType = view.findViewById(R.id.imgProduct_type);
        ImageView imgBill = view.findViewById(R.id.imgBill);
        ImageView imgSelling = view.findViewById(R.id.imgSelling);
        ImageView imgStatistic = view.findViewById(R.id.imgStatistic);

        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerActivity.class));
            }
        });
        imgProductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProductTypeActivity.class));
            }
        });
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProductActivity.class));
            }
        });
        imgBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BillActivity.class));
            }
        });
        imgSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SellingProductsActivity.class));
            }
        });
        imgStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StatisticActivity.class));
            }
        });
/*
        try {
            fakedDataSQL();
        } catch (Exception e) {
            Log.e("fakeDataSQL", e.toString());
        }
*/

        return view;
    }

    private void fakedDataSQL() {
        customerDAO = new CustomerDAO(getActivity());
        productTypeDAO = new ProductTypeDAO(getActivity());
        productDAO = new ProductDAO(getActivity());
        billDAO = new BillDAO(getActivity());
        billDetailDAO = new BillDetailDAO(getActivity());
        BitmapDrawable bitmapDrawable = (BitmapDrawable) imgCustomer.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
        byte[] imgCustomer = byteArray.toByteArray();

        BitmapDrawable bitmapDrawable1 = (BitmapDrawable) imgProduct.getDrawable();
        Bitmap bitmap1 = bitmapDrawable1.getBitmap();
        ByteArrayOutputStream byteArray1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArray1);
        byte[] imgProduct = byteArray1.toByteArray();
        Date date = new Date();
        Customer customer = new Customer("0329144446", "Nguyễn Minh Anh", "Nam", "Anh@fpt.com", "VN", "không", "Trần Nhật Minh", "Nam", "Minh@fpt.com", "CN", "Không", 20, 25, date.getTime(), date.getTime(), imgCustomer, imgCustomer);
        customerDAO.insertCustomer(customer);
        ProductType productType = new ProductType("ĐTDĐ", "Điện thoại di động", "Không", "", "", date.getTime(), 0);
        productTypeDAO.insertProductType(productType);

        Product product = new Product();
        product.setProductID("SP01");

        product.setProductName("SamSung");
        product.setProductTypeID("ĐTDĐ");
        product.setDescription("Không");
        product.setInputPrice(1000000);
        product.setOutputPrice(2000000);
        product.setQuantity(10000000);

        product.setImgProduct(imgProduct);
        product.setOldImgProduct(null);

        product.setOldProductName("");
        product.setOldProductTypeID("");
        product.setOldDescription("");
        product.setOldInputPrice(0);
        product.setOldOutputPrice(0);
        product.setOldQuantity(0);

        product.setAddedDay(date.getTime());
        product.setEditedDay(0);

        productDAO.insertProduct(product);
        billDAO.insertBill(new Bill("HD01", "0329144446", "", Long.parseLong("1543511032593"), 0, "Đã thanh toán"));
        billDAO.insertBill(new Bill("HD02", "0329144446", "", Long.parseLong("1543079136537"), 0, "Chưa thanh toán"));
        billDAO.insertBill(new Bill("HD03", "0329144446", "", Long.parseLong("1543165575773"), 0, "Đã thanh toán"));
        billDAO.insertBill(new Bill("HD04", "0329144446", "", Long.parseLong("1514740015276"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD05", "0329144446", "", Long.parseLong("1519751246091"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD06", "0329144446", "", Long.parseLong("1543597728857"), 0, "Chưa thanh toán"));
        billDAO.insertBill(new Bill("HD07", "0329144446", "", Long.parseLong("1543684141383"), 0, "Chưa thanh toán"));
        billDAO.insertBill(new Bill("HD08", "0329144446", "", Long.parseLong("1543770560887"), 0, "Chưa thanh toán"));
        billDAO.insertBill(new Bill("HD09", "0329144446", "", Long.parseLong("1544029778549"), 0, "Chưa thanh toán"));
        billDAO.insertBill(new Bill("HD10", "0329144446", "", Long.parseLong("1544375391634"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD11", "0329144446", "",Long.parseLong("1544721002157"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD12", "0329144446", "", Long.parseLong("1544980215158"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD13", "0329144446", "", Long.parseLong("1545239427988"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD14", "0329144446", "", Long.parseLong("1545498645327"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD15", "0329144446", "", Long.parseLong("1545757859941"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD16", "0329144446", "", Long.parseLong("1545930671746"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD17", "0329144446", "", Long.parseLong("1546189883284"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD18", "0329144446", "", Long.parseLong("1534353099553"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD19", "0329144446", "", Long.parseLong("1517332315904"), 0, "Chưa thanh toán"));
        billDAO.insertBill( new Bill("HD20", "0329144446", "", Long.parseLong("1554829931429"), 0, "Chưa thanh toán"));

        billDetailDAO.insertBillDetail("HD01", "SP01", 20);
        billDetailDAO.insertBillDetail("HD02", "SP01", 200);
        billDetailDAO.insertBillDetail("HD03", "SP01", 2000);
        billDetailDAO.insertBillDetail("HD04", "SP01", 500);
        billDetailDAO.insertBillDetail("HD05", "SP01", 700);
        billDetailDAO.insertBillDetail("HD06", "SP01", 1000);
        billDetailDAO.insertBillDetail("HD07", "SP01", 20);
        billDetailDAO.insertBillDetail("HD08", "SP01", 80);
        billDetailDAO.insertBillDetail("HD09", "SP01", 90);
        billDetailDAO.insertBillDetail("HD10", "SP01", 15);
        billDetailDAO.insertBillDetail("HD11", "SP01", 2500);
        billDetailDAO.insertBillDetail("HD12", "SP01", 600);
        billDetailDAO.insertBillDetail("HD13", "SP01", 155);
        billDetailDAO.insertBillDetail("HD14", "SP01", 78);
        billDetailDAO.insertBillDetail("HD15", "SP01", 65);
        billDetailDAO.insertBillDetail("HD16", "SP01", 189);
        billDetailDAO.insertBillDetail("HD17", "SP01", 200);
        billDetailDAO.insertBillDetail("HD18", "SP01", 208);
        billDetailDAO.insertBillDetail("HD19", "SP01", 1888);
        billDetailDAO.insertBillDetail("HD20", "SP01", 2563);
    }

}
