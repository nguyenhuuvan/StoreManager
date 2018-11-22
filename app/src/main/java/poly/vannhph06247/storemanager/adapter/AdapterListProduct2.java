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
import android.widget.TextView;

import java.util.List;

import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.model.ListProduct;

public class AdapterListProduct2 extends RecyclerView.Adapter<AdapterListProduct2.ViewHolder> {

    private final List<ListProduct> listProducts;
    private final Context context;
    private int lastPosition = -1;

    public AdapterListProduct2(Context context, List<ListProduct> listProducts) {
        this.listProducts = listProducts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_layout_list_product2, parent, false);
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
    }

    @Override
    public int getItemCount() {
        if (listProducts == null) return 0;
        return listProducts.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvID;
        final TextView tvName;
        final TextView tvQuantity;

        ViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
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
