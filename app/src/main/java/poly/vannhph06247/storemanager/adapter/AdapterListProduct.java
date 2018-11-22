package poly.vannhph06247.storemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.listener.OnDelete;
import poly.vannhph06247.storemanager.model.ListProduct;

public class AdapterListProduct extends RecyclerView.Adapter<AdapterListProduct.ViewHolder> {

    private List<ListProduct> listProducts;
    private final Context context;
    private int lastPosition = -1;
    private final OnDelete onDelete;

    public AdapterListProduct(Context context, List<ListProduct> listProducts, OnDelete onDelete) {
        this.listProducts = listProducts;
        this.context = context;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_layout_list_product, parent, false);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);
        ListProduct listProduct = listProducts.get(position);
        holder.tvQuantity.setText("Số lượng: " + String.valueOf(listProduct.getQuantity()));
        holder.tvID.setText("Mã: "+listProduct.getId());
        holder.tvName.setText("Tên: "+listProduct.getName());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDelete.onDelete(position);
            }
        });
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
        final ImageView imgDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            imgDelete = itemView.findViewById(R.id.imgDelete);
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
