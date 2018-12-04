package poly.project.storemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import poly.project.storemanager.R;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.ListProduct;

public class AdapterListProductOfProductType extends RecyclerView.Adapter<AdapterListProductOfProductType.ViewHolder> {

    private List<ListProduct> listProducts;
    private final Context context;
    private int lastPosition = -1;
    private final OnDelete onDelete;
    private BillDetailDAO billDetailDAO;
    public AdapterListProductOfProductType(Context context, List<ListProduct> listProducts, OnDelete onDelete) {
        this.listProducts = listProducts;
        this.context = context;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_layout_list_product_of_product_type, parent, false);
        billDetailDAO = new BillDetailDAO(context);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);
        ListProduct listProduct = listProducts.get(position);

        List<BillDetail> billDetailList = billDetailDAO.getAllBillDetailsByProductID(listProduct.getId());
        int quantity = listProduct.getQuantity() - getAllQuantity(billDetailList);
        if(quantity==0){
            holder.tvQuantity.setText("Hết hàng");
            holder.tvQuantity.setTextColor(Color.RED);
        }else{
            holder.tvQuantity.setText("Còn: " + String.valueOf(quantity) + " sp");
            holder.tvQuantity.setTextColor(Color.BLUE);
        }
        holder.tvID.setText("Mã: "+listProduct.getId());
        holder.tvName.setText("Tên: "+listProduct.getName());
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
        try {
            if (listProduct.getImgProduct() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(listProduct.getImgProduct(), 0, listProduct.getImgProduct().length);
                holder.imgProduct.setImageBitmap(bitmap);
            }
        } catch (Exception e) {
            Log.e("Error", "Error");
        }
    }

    @Override
    public int getItemCount() {
        if (listProducts == null) return 1;
        return listProducts.size();
    }

    public void changeDataset(List<ListProduct> items) {
        this.listProducts = items;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        listProducts.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvID;
        final TextView tvName;
        final TextView tvQuantity;
        final ImageView imgDelete,imgProduct;

        ViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
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
    private int getAllQuantity(List<BillDetail> billDetails) {
        int quantity = 0;
        for (int i = 0; i < billDetails.size(); i++) {
            quantity = quantity + billDetails.get(i).getQuantity();
        }
        return quantity;
    }
}
