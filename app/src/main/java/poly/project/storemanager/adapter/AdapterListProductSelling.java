package poly.project.storemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import poly.project.storemanager.model.ListProduct;

public class AdapterListProductSelling extends RecyclerView.Adapter<AdapterListProductSelling.ViewHolder> {

    private List<ListProduct> listProducts;
    private final Context context;
    private int lastPosition = -1;

    public AdapterListProductSelling(Context context, List<ListProduct> listProducts) {
        this.listProducts = listProducts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_layout_list_product_selling, parent, false);
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
        if (listProducts == null) return 0;
        return listProducts.size();
    }
    public void changeDataset(List<ListProduct> items) {
        this.listProducts = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvID;
        final TextView tvName;
        final TextView tvQuantity;
        final ImageView imgProduct;

        ViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
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
