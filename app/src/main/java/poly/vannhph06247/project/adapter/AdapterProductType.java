package poly.project.storemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import poly.project.storemanager.dao.ProductDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.listener.OnEdit;
import poly.project.storemanager.model.ListProduct;
import poly.project.storemanager.model.ProductType;

public class AdapterProductType extends RecyclerView.Adapter<AdapterProductType.ViewHolder> {
    private List<ProductType> productTypeList;
    private final Context context;
    private final OnClick onClick;
    private final OnDelete onDelete;
    private final OnEdit onEdit;
    private int lastPosition = -1;
    private ProductDAO productDAO;

    public AdapterProductType(Context context, List<ProductType> productTypeList, OnClick onClick, OnDelete onDelete, OnEdit onEdit) {
        this.productTypeList = productTypeList;
        this.context = context;
        this.onClick = onClick;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_layout_producttype, parent, false);
        productDAO = new ProductDAO(context);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final AdapterProductType.ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);
        ProductType productType = productTypeList.get(position);
        List<ListProduct> productList = productDAO.getAllProductByProductType(productType.getProductTypeID());

        holder.tvTotalProduct.setText("Có " + productList.size() + " sản phẩm");
        holder.tvID.setText("Mã: " + productType.getProductTypeID());
        holder.tvName.setText("Tên: " + productType.getProductTypeName());
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
        if (productTypeList == null) return 0;
        return productTypeList.size();
    }

    public void changeDataset(List<ProductType> items) {
        this.productTypeList = items;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        productTypeList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgMenu;
        final TextView tvID;
        final TextView tvName;
        final TextView tvTotalProduct;


        ViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvTotalProduct = itemView.findViewById(R.id.tvTotalProduct);
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

}

