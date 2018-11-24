package poly.vannhph06247.storemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.dao.ProductDAO;
import poly.vannhph06247.storemanager.listener.OnClick;
import poly.vannhph06247.storemanager.listener.OnDelete;
import poly.vannhph06247.storemanager.listener.OnEdit;
import poly.vannhph06247.storemanager.model.ListProduct;
import poly.vannhph06247.storemanager.model.ProductType;

public class AdapterProductType extends RecyclerView.Adapter<AdapterProductType.ViewHolder>  {
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
        holder.tvID.setText("Mã: "+productType.getProductTypeID());
        holder.tvName.setText("Tên: "+productType.getProductTypeName());
        holder.imgMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onClick(position);
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

    }

}

