package poly.vannhph06247.storemanager.activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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

public class ProductListOfCustomerActivity extends AppCompatActivity {
    private RecyclerView lvListProductType;
    private List<ListProduct> listProductList;
    private ImageView imgSort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list_of_customer);
        initViews();
        initAction();
        listProductList = new ArrayList<>();
        AdapterListProduct2 adapterListProduct = new AdapterListProduct2(this, listProductList);
        lvListProductType.setAdapter(adapterListProduct);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListProductType.setLayoutManager(manager);
        fakeData();
    }
    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        TextView tvTotal = findViewById(R.id.tvTotal);
        tvTotal.setText(getString(R.string.total_product));
        lvListProductType = findViewById(R.id.lvList);
        imgSort = findViewById(R.id.imgSort);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                PopupMenu popupMenu=new PopupMenu(ProductListOfCustomerActivity.this, imgSort);
                getMenuInflater().inflate(R.menu.menu_product,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.sort_by_name:
                                runLayoutAnimation(lvListProductType);
                                break;
                            case R.id.sort_by_id:
                                runLayoutAnimation(lvListProductType);
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

    @Override
    protected void onResume() {
        runLayoutAnimation(lvListProductType);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_item).getActionView();
        searchView.setSearchableInfo(
                Objects.requireNonNull(searchManager).getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search_item:
                break;
            case R.id.search_item2:
                break;
        }
        return false;
    }
}
