package poly.vannhph06247.storemanager.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import poly.vannhph06247.storemanager.Library;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.adapter.AdapterInformation;
import poly.vannhph06247.storemanager.dao.UserDAO;
import poly.vannhph06247.storemanager.listener.OnClick;
import poly.vannhph06247.storemanager.model.Information;
import poly.vannhph06247.storemanager.model.User;

public class UserActivity extends Library implements OnClick {
    private List<Information> informationList;
    private RecyclerView lvListInforUser;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initViews();
        informationList = new ArrayList<>();
        AdapterInformation adapterInformation = new AdapterInformation(this, informationList, this);
        lvListInforUser.setAdapter(adapterInformation);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        lvListInforUser.setLayoutManager(manager);
        userDAO = new UserDAO(this);
        fakeData();


    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        lvListInforUser = findViewById(R.id.lvList);
    }

    private void fakeData() {
        User user = userDAO.getUser("ADMIN");
        String age;
        if (user.getAdminAge() == 0) {
            age = "";
        } else {
            age = user.getAdminAge() + "";
        }
        informationList.add(new Information("Tên đăng nhập", user.getUsername(), ""));
        informationList.add(new Information("Họ tên", user.getAdminName(), ""));
        informationList.add(new Information("Tuổi", age, ""));
        informationList.add(new Information("SĐT", user.getAdminPhone(), ""));
        informationList.add(new Information("Email", user.getAdminEmail(), ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                startActivity(new Intent(this, EditUserActivity.class));
                break;
            case R.id.changepass:
                showDialogChangePass();
                break;
        }
        return false;
    }

    private void showDialogChangePass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View dialogView = Objects.requireNonNull(inflater).inflate(R.layout.dialog_change_pass, null);
        builder.setView(dialogView);
        final Dialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        Button change = dialogView.findViewById(R.id.btnChange);
        Button cancel = dialogView.findViewById(R.id.btnCancel);
        final TextView edpassOld = dialogView.findViewById(R.id.edPassWordOld);
        final TextView edpassNew = dialogView.findViewById(R.id.edPassWordNew);
        final TextView edpassNew2 = dialogView.findViewById(R.id.edPassWordNew2);
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passOld = edpassOld.getText().toString().trim();
                String passNew = edpassNew.getText().toString().trim();
                String passNew2 = edpassNew2.getText().toString().trim();

                if (passNew.isEmpty() || passNew2.isEmpty() || passOld.isEmpty()) {
                    if (passOld.isEmpty())
                        edpassOld.setError(getString(R.string.notify_empty_passold));
                    if (passNew.isEmpty())
                        edpassNew.setError(getString(R.string.notify_empty_passnew));
                    if (passNew.isEmpty())
                        edpassNew2.setError(getString(R.string.notify_empty_passnew2));
                } else if (passNew.length() < 6 || passNew2.length() < 6 || passOld.length() < 6 || passNew.length() > 50 || passNew2.length() > 50 || passOld.length() > 50) {
                    if (passNew.length() < 6)
                        edpassNew.setError(getString(R.string.notify_length_pass_min));
                    if (passNew2.length() < 6)
                        edpassNew2.setError(getString(R.string.notify_length_pass_min));
                    if (passOld.length() < 6)
                        edpassOld.setError(getString(R.string.notify_length_pass_min));
                    if (passOld.length() > 50)
                        edpassOld.setError(getString(R.string.notify_length_pass_max));
                    if (passNew.length() > 50)
                        edpassNew.setError(getString(R.string.notify_length_pass_max));
                    if (passNew2.length() > 50)
                        edpassNew2.setError(getString(R.string.notify_length_pass_max));
                } else if (checkPrimaryKey(passOld) || checkPrimaryKey(passNew) || checkPrimaryKey(passNew2)) {
                    if (checkPrimaryKey(passOld))
                        edpassOld.setError(getString(R.string.primarykey_exist_space));
                    if (checkPrimaryKey(passNew))
                        edpassNew.setError(getString(R.string.primarykey_exist_space));
                    if (checkPrimaryKey(passNew2))
                        edpassNew2.setError(getString(R.string.primarykey_exist_space));
                } else if (!passNew.equals(passNew2)) {
                    edpassNew2.setError(getString(R.string.notify_same_pass));
                } else {
                    User user = userDAO.getUser("ADMIN");
                    if (user.getPassword().equals(passOld)) {
                        long result = userDAO.updatePassWord("ADMIN", passNew2);
                        if (result > 0) {
                            Toast.makeText(UserActivity.this, getString(R.string.change_pass_success) + " Admin", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserActivity.this, getString(R.string.change_pass_failed), Toast.LENGTH_SHORT).show();
                        }
                        dialog.dismiss();
                    } else {
                        edpassOld.setError(getString(R.string.wrong_pass));
                    }
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(int position) {

    }

    @Override
    protected void onResume() {
        informationList.clear();
        fakeData();
        runLayoutAnimationLeft(lvListInforUser);
        super.onResume();

    }

}
