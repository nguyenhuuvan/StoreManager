package poly.project.storemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import poly.project.storemanager.R;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.listener.OnEdit;
import poly.project.storemanager.model.Bill;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Product;

public class AdapterBill extends RecyclerView.Adapter<AdapterBill.ViewHolder> {
    private List<Bill> billList;
    private final Context context;
    private final OnClick onClick;
    private final OnDelete onDelete;
    private final OnEdit onEdit;
    private int lastPosition = -1;
    private ProductDAO productDAO;
    private BillDetailDAO billDetailDAO;

    public AdapterBill(Context context, List<Bill> billList, OnClick onClick, OnDelete onDelete, OnEdit onEdit) {
        this.billList = billList;
        this.context = context;
        this.onClick = onClick;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_layout_bill, parent, false);
        productDAO = new ProductDAO(context);
        billDetailDAO = new BillDetailDAO(context);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        Bill bill = billList.get(position);
        List<BillDetail> billDetail = billDetailDAO.getAllBillDetailsByBillID(bill.getBillID());
        if (bill.getStatus().equalsIgnoreCase("Chưa thanh toán")) {
            holder.tvMoney.setText(formatVnCurrence(getSum(billDetail).toString())+"*");
            holder.tvMoney.setTextColor(Color.RED);
        } else {
            holder.tvMoney.setText(formatVnCurrence(getSum(billDetail).toString()));
            holder.tvMoney.setTextColor(Color.BLUE);
        }
        holder.tvID.setText("Mã: " + bill.getBillID());
        holder.tvDate.setText("Ngày: " + simpleDateFormat.format(new Date(bill.getBillDate())));
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    PopupMenu popup = new PopupMenu(context, holder.imgMenu);
                    popup.inflate(R.menu.option_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            switch (item.getItemId()) {
                                case R.id.delete:
                                    onDelete.onDelete(position);
                                    return true;
                                case R.id.update:
                                    onEdit.onEdit(position);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popup.show();
                } catch (Exception e) {
                    Log.e("lỗi onclick","lỗi onclick");
                }


            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onClick.onClick(position);
                } catch (Exception e) {
                    Log.e("lỗi onclick","lỗi onclick");
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        if (billList == null) return 0;
        return billList.size();
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

    public void changeDataset(List<Bill> items) {
        this.billList = items;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        billList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgMenu;
        final TextView tvID;
        final TextView tvDate;
        final TextView tvMoney;


        ViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvMoney = itemView.findViewById(R.id.tvMoney);
            imgMenu = itemView.findViewById(R.id.imgMenu);
        }


    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private Double getSum(List<BillDetail> billDetailList) {
        double sum = 0;
        for (int i = 0; i < billDetailList.size(); i++) {
            Product product = productDAO.getProductByID(billDetailList.get(i).getProductID());
            sum = sum + ((double) product.getOutputPrice() * (double) billDetailList.get(i).getQuantity());
        }
        return sum;
    }
}
