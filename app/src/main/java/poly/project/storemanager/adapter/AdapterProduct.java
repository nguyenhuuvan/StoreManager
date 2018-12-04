package poly.project.storemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import poly.project.storemanager.R;
import poly.project.storemanager.dao.BillDetailDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.listener.OnEdit;
import poly.project.storemanager.model.BillDetail;
import poly.project.storemanager.model.Product;

public class AdapterProduct extends RecyclerView.Adapter<AdapterProduct.ViewHolder> {
    private List<Product> productList;
    private final Context context;
    private final OnClick onClick;
    private final OnDelete onDelete;
    private final OnEdit onEdit;
    private int lastPosition = -1;
    private BillDetailDAO billDetailDAO;

    public AdapterProduct(Context context, List<Product> productList, OnClick onClick, OnDelete onDelete, OnEdit onEdit) {
        this.productList = productList;
        this.context = context;
        this.onClick = onClick;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_layout_product, parent, false);
        billDetailDAO = new BillDetailDAO(context);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);
        Product product = productList.get(position);
        List<BillDetail> billDetailList = billDetailDAO.getAllBillDetailsByProductID(product.getProductID());
        int quantity = product.getQuantity() - getAllQuantity(billDetailList);
        if (quantity == 0) {
            holder.tvQuantity.setText("Hết hàng");
            holder.tvQuantity.setTextColor(Color.RED);
        } else {
            holder.tvQuantity.setText("Còn: " + String.valueOf(quantity) + " sp");
            holder.tvQuantity.setTextColor(Color.BLUE);
        }

        holder.tvID.setText("Mã: " + product.getProductID());
        holder.tvName.setText("Tên: " + product.getProductName());
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
        if (productList == null) return 0;
        return productList.size();
    }

    public void changeDataset(List<Product> items) {
        this.productList = items;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgMenu, imgProduct;
        final TextView tvID;
        final TextView tvName;
        final TextView tvQuantity;


        ViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            imgMenu = itemView.findViewById(R.id.imgMenu);
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
