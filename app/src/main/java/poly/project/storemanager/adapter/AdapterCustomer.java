package poly.project.storemanager.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.Currency;
import java.util.List;

import poly.project.storemanager.R;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.listener.OnClick;
import poly.project.storemanager.listener.OnDelete;
import poly.project.storemanager.listener.OnEdit;
import poly.project.storemanager.model.Customer;
import poly.project.storemanager.model.ListProductOfCustomer;

import java.util.Locale;

public class AdapterCustomer extends RecyclerView.Adapter<AdapterCustomer.ViewHolder> {
    private List<Customer> customers;
    private final Context context;
    private final OnClick onClick;
    private final OnDelete onDelete;
    private final OnEdit onEdit;
    private int lastPosition = -1;
    private CustomerDAO customerDAO;

    public AdapterCustomer(Context context, List<Customer> customers, OnClick onClick, OnDelete onDelete, OnEdit onEdit) {
        this.customers = customers;
        this.context = context;
        this.onClick = onClick;
        this.onDelete = onDelete;
        this.onEdit = onEdit;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_layout_customer, parent, false);
        customerDAO = new CustomerDAO(context);
        return new ViewHolder(itemView);
    }

    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);
        Customer customer = customers.get(position);
        holder.tvTotalMoney.setText(formatVnCurrence(getTotalPriceOfCustomer(customer.getCustomerPhone()).toString()));
        holder.tvName.setText(customer.getCustomerName());
        holder.tvSDT.setText("SĐT: " + customer.getCustomerPhone());
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
            if (customer.getImgcustomer() != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(customer.getImgcustomer(), 0, customer.getImgcustomer().length);
                holder.imgCustomer.setImageBitmap(bitmap);
            }else {
                holder.imgCustomer.setImageResource(R.drawable.customer);
            }
        } catch (Exception e) {
            Log.e("Error", "Error");
        }

    }

    @Override
    public int getItemCount() {
        if (customers == null) return 0;
        return customers.size();
    }

    public void removeItem(int position) {
        customers.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
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
        price = String.format("%s", price);
        return price;
    }

    public void changeDataset(List<Customer> items) {
        this.customers = items;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imgMenu, imgCustomer;
        final TextView tvName;
        final TextView tvSDT;
        final TextView tvTotalMoney;


        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvSDT = itemView.findViewById(R.id.tvSDT);
            tvTotalMoney = itemView.findViewById(R.id.tvTotalMoney);
            imgMenu = itemView.findViewById(R.id.imgMenu);
            imgCustomer = itemView.findViewById(R.id.imgCustomer);

        }

    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private Double getTotalPriceOfCustomer(String customerPhone) {
        double total = 0;
        List<ListProductOfCustomer> listProductOfCustomer = customerDAO.getProductofCustomer(customerPhone);
        for (int i = 0; i < listProductOfCustomer.size(); i++) {
            total += (double) listProductOfCustomer.get(i).getQuantity() * (double) (listProductOfCustomer.get(i).getPrice());
        }
        return total;
    }
}
