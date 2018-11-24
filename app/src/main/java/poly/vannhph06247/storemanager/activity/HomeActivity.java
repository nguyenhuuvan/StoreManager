package poly.vannhph06247.storemanager.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import poly.vannhph06247.storemanager.Library;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.dao.UserDAO;
import poly.vannhph06247.storemanager.fragment.HomeFragment;
import poly.vannhph06247.storemanager.fragment.AppInforFragment;
import poly.vannhph06247.storemanager.model.User;

public class HomeActivity extends Library
        implements NavigationView.OnNavigationItemSelectedListener {
    private AppInforFragment appInforFragment;
    private HomeFragment homeFragment;
    private Toolbar toolbar;
    private TextView tvN;
    private TextView tvName;
    private TextView tvEmail;
    private UserDAO userDAO;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private static final int REQUEST = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        userDAO = new UserDAO(this);
        appInforFragment = new AppInforFragment();
        homeFragment = new HomeFragment();
        initView();
        initAction();
        showFragmentHome();

        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST );
            } else {
                Log.e("","");
            }
        } else {
            Log.e("","");
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("","");
                } else {
                    Toast.makeText(this, "Ứng dụng chưa có quyền truy cập bộ nhớ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);
        tvN = headerView.findViewById(R.id.tvN);
    }

    private void initAction() {
        toolbar.setTitleTextColor(Color.WHITE);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        showFragmentHome();
        navigationView.getMenu().getItem(0).setChecked(true);
        User user = userDAO.getUser("ADMIN");
        if (user.getAdminName().equals("") || user.getAdminEmail().equals("")) {
            tvEmail.setText(getString(R.string.nav_header_subtitle));
            tvName.setText(getString(R.string.nav_header_title));
            tvN.setText(getString(R.string.V));
        } else {
            try {
                tvEmail.setText(user.getAdminEmail());
                tvName.setText(user.getAdminName());
                String lastName;
                if (user.getAdminName().split("\\w+").length > 1) {
                    lastName = user.getAdminName().substring(user.getAdminName().lastIndexOf(" ") + 1);
                } else {
                    lastName = user.getAdminName();
                }
                char N = lastName.charAt(0);
                tvN.setText(String.valueOf(N).toUpperCase());
            } catch (Exception e) {
                Log.e("Error Admin Name","Error Admin Name");
            }

        }
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_admin:
                startActivity(new Intent(this, UserActivity.class));
                break;
            case R.id.nav_back:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle(getString(R.string.back));
                alertDialogBuilder
                        .setMessage(getString(R.string.click))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                })

                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.nav_home:
                toolbar.setTitle("Chức năng");
                showFragmentHome();
                break;
            case R.id.nav_infor_app:
                toolbar.setTitle("Thông tin phần mềm");
                showFragmentInforApp();
                break;
            case R.id.nav_toexcel:
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);

                builder.setMessage(getString(R.string.message_toexcel));
                builder.setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                final Dialog dialog = builder.create();
                Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.show();
                break;
            case R.id.nav_logout:
                finish();
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragmentHome() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (homeFragment.isAdded()) {
            ft.show(homeFragment);
        } else {
            ft.add(R.id.container1, homeFragment);
        }
        if (appInforFragment.isAdded()) {
            ft.remove(appInforFragment);
        }
        ft.commit();
    }

    private void showFragmentInforApp() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (appInforFragment.isAdded()) {
            ft.show(appInforFragment);
        } else {
            ft.add(R.id.container1, appInforFragment);
        }
        if (homeFragment.isAdded()) {
            ft.hide(homeFragment);
        }
        ft.commit();
    }
    }
