package poly.project.storemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import poly.project.storemanager.R;
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Product;

public class AdapterBillDetail extends RecyclerView.Adapter<AdapterBillDetail.ViewHolder> {
    private List<BillDetail> billDetails;
    private final OnDelete onDelete;
    private int lastPosition = -1;
    private final Context context;
    private final OnClick onClick;
    private ProductDAO productDAO;

    public AdapterBillDetail(Context context, List<BillDetail> billDetails, OnDelete onDelete,OnClick onClick) {
        this.billDetails = billDetails;
        this.onDelete = onDelete;
        this.context = context;
        this.onClick=onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_layout_billdetail, parent, false);
        productDAO = new ProductDAO(context);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);

        BillDetail billDetail = billDetails.get(position);
        holder.tvQuantity.setText("SL: " + billDetail.getQuantity());
        Product product = productDAO.getProductByID(billDetail.getProductID());
        holder.tvName.setText("Tên: " + product.getProductName());
        holder.tvTotalMoney.setText("Tổng: " + formatVnCurrence(String.valueOf(((double) product.getOutputPrice() * (double) billDetail.getQuantity()))));
        holder.tvPrice.setText("Giá: " + formatVnCurrence(String.valueOf(product.getOutputPrice())));
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    onDelete.onDelete(position);
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
        try {
            if (product.getImgProduct() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImgProduct(), 0, product.getImgProduct().length);
                holder.imgProduct.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("Error", "Error");
        }
    }

    @Override
    public int getItemCount() {
        if (billDetails == null) return 0;
        return billDetails.size();
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

    public void changeDataset(List<BillDetail> items) {
        this.billDetails = items;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        billDetails.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgDelete,imgProduct;
        final TextView tvName;
        final TextView tvQuantity;
        final TextView tvPrice;
        final TextView tvTotalMoney;


        ViewHolder(View itemView) {
            super(itemView);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTotalMoney = itemView.findViewById(R.id.tvTotalMoney);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }

    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}

