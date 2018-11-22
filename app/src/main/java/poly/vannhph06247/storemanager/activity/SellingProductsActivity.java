package poly.vannhph06247.storemanager.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterListProduct2;
import poly.vannhph06247.storemanager.model.ListProduct;

public class SellingProductsActivity extends AppCompatActivity {
    private RecyclerView lvListSeling;
    private List<ListProduct> listProductList;
    private ImageView imgSort, imgCalender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selling_products);
        initViews();
        initAction();
        listProductList = new ArrayList<>();
        AdapterListProduct2 adapterListProduct = new AdapterListProduct2(this, listProductList);
        lvListSeling.setAdapter(adapterListProduct);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListSeling.setLayoutManager(manager);
        fakeData();
    }
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText(getString(R.string.total_product));
        lvListSeling = findViewById(R.id.lvList);
        tvTotal.setTextColor(Color.BLACK);
        imgSort = findViewById(R.id.imgSort);
        imgCalender = findViewById(R.id.imgCalender);
    }

    private void fakeData() {
        for (int i = 0; i < 40; i++) {
            listProductList.add(new ListProduct("H30122 ", "Điện thoại di động", 5));
        }
    }

    private void initAction() {
        imgSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu=new PopupMenu(SellingProductsActivity.this, imgSort);
                getMenuInflater().inflate(R.menu.menu_product2,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.up:
                                runLayoutAnimation(lvListSeling);
                                break;
                            case R.id.down:
                                runLayoutAnimation(lvListSeling);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        imgCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(SellingProductsActivity.this, imgCalender);
                getMenuInflater().inflate(R.menu.menu_calender, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.day:

                                break;
                            case R.id.week:

                                break;
                            case R.id.month:

                                break;
                            case R.id.year:

                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }
    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_left);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
