package poly.vannhph06247.storemanager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.activity.BillActivity;
import poly.vannhph06247.storemanager.activity.CustomerActivity;
import poly.vannhph06247.storemanager.activity.ProductActivity;
import poly.vannhph06247.storemanager.activity.ProductTypeActivity;
import poly.vannhph06247.storemanager.activity.SellingProductsActivity;
import poly.vannhph06247.storemanager.activity.StatisticActivity;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView imgCustomer = view.findViewById(R.id.imgCustomer);
        ImageView imgProductType = view.findViewById(R.id.imgProduct_type);
        ImageView imgProduct = view.findViewById(R.id.imgProduct);
        ImageView imgBill = view.findViewById(R.id.imgBill);
        ImageView imgSelling = view.findViewById(R.id.imgSelling);
        ImageView imgStatistic = view.findViewById(R.id.imgStatistic);

        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerActivity.class));
            }
        });
        imgProductType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProductTypeActivity.class));
            }
        });
        imgProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProductActivity.class));
            }
        });
        imgBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), BillActivity.class));
            }
        });
        imgSelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SellingProductsActivity.class));
            }
        });
        imgStatistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StatisticActivity.class));
            }
        });
        return view;
    }


}
