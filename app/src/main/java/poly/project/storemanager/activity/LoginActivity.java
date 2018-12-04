package poly.project.storemanager.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.dao.UserDAO;
import poly.project.storemanager.model.User;

public class LoginActivity extends Library {
    private EditText edUserName;
    private EditText edPassWord;
    private CheckBox chkSavePass;
    private UserDAO userDAO;
    private TextView tvHotline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initAction();
        userDAO = new UserDAO(this);
        User user = userDAO.getUser("ADMIN");
        if (user == null) {
            userDAO.insertUser("ADMIN", "123456", "", "", "", 0);
        }
    }

    private void initView() {
        edPassWord = findViewById(R.id.edPassWord);
        edUserName = findViewById(R.id.edUserName);
        chkSavePass = findViewById(R.id.chkSavePass);
        tvHotline = findViewById(R.id.hotline);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initAction() {
        chkSavePass.setChecked(true);
        tvHotline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0329144446"));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

    }

    public void login(View view) {
        final String user = edUserName.getText().toString().trim();
        String pass = edPassWord.getText().toString().trim();
        if (user.isEmpty() || pass.isEmpty()) {
            if (user.isEmpty()) {
                edUserName.setError(getString(R.string.notify_empty_user));
            }
            if (pass.isEmpty()) {
                edPassWord.setError(getString(R.string.notify_empty_pass));
            }
        } else if (pass.length() < 6 || pass.length() > 50) {
            if (pass.length() < 6)
                edPassWord.setError(getString(R.string.notify_length_pass_min));
            if (pass.length() > 50)
                edPassWord.setError(getString(R.string.notify_length_pass_max));
        } else if (checkPrimaryKey(user) || checkPrimaryKey(pass)) {
            if (checkPrimaryKey(user))
                edUserName.setError(getString(R.string.primarykey_exist_space));
            if (checkPrimaryKey(pass))
                edPassWord.setError(getString(R.string.primarykey_exist_space));
        } else {
            final User user2 = userDAO.getUser(edUserName.getText().toString().trim().toUpperCase());
            if (user2 == null) {
                edUserName.setError(getString(R.string.user_not_exitst));
            } else {
                if (user2.getPassword().equals(edPassWord.getText().toString().trim())) {
                    hideSoftKeyboard(this);
                    if (user2.getAdminAge() == 0 && user2.getAdminEmail().equals("") && user2.getAdminName().equals("") && user2.getAdminPhone().equals("")) {
                        reUser(edUserName.getText().toString().trim(), edPassWord.getText().toString().trim(), chkSavePass.isChecked());
                        startActivity(new Intent(LoginActivity.this, MoreInforUserActivity.class));
                    } else {
                        reUser(edUserName.getText().toString().trim(), edPassWord.getText().toString().trim(), chkSavePass.isChecked());
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                } else {
                    edPassWord.setError(getString(R.string.wrong_pass));
                }

            }
        }
    }


    public void forgetPass(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        @SuppressLint("InflateParams") View dialogView = Objects.requireNonNull(inflater).inflate(R.layout.dialog_fogetpass, null);
        builder.setView(dialogView);
        final Dialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();
        final EditText edAdminPhone;
        final EditText edAdminEmail;
        Button btnGetpass;
        Button btnCancel;

        edAdminPhone = dialogView.findViewById(R.id.edAdminPhone);
        edAdminEmail = dialogView.findViewById(R.id.edAdminEmail);
        btnGetpass = dialogView.findViewById(R.id.btnGetpass);
        btnCancel = dialogView.findViewById(R.id.btnCancel);


        btnGetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminPhone = edAdminPhone.getText().toString().trim();
                String adminEmail = edAdminEmail.getText().toString().trim();

                if (adminEmail.isEmpty() || adminPhone.isEmpty()) {
                    if (adminEmail.isEmpty())
                        edAdminEmail.setError(getString(R.string.notify_empty_email));
                    if (adminPhone.isEmpty())
                        edAdminPhone.setError(getString(R.string.notify_empty_phone));
                } else if (adminEmail.length() > 30) {
                    edAdminEmail.setError(getString(R.string.notify_length_email));

                } else if (!adminPhone.matches(format_phone) || !adminEmail.matches(format_email)) {
                    if (!adminPhone.matches(format_phone))
                        edAdminPhone.setError(getString(R.string.notify_same_sdt));
                    if (!adminEmail.matches(format_email))
                        edAdminEmail.setError(getString(R.string.notify_same_email));
                } else {
                    User user = userDAO.getUser("ADMIN");
                    if (user.getAdminEmail().equalsIgnoreCase(adminEmail) && user.getAdminPhone().equalsIgnoreCase(adminPhone)) {
                        edUserName.setText(user.getUsername());
                        edPassWord.setText(user.getPassword());
                        edPassWord.setInputType(InputType.TYPE_CLASS_TEXT);
                        Toast.makeText(LoginActivity.this, getString(R.string.getpass_success), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        if (!user.getAdminEmail().equalsIgnoreCase(adminEmail))
                            edAdminEmail.setError(getString(R.string.wrong_email));
                        if (!user.getAdminPhone().equalsIgnoreCase(adminPhone))
                            edAdminPhone.setError(getString(R.string.wrong_phone));
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void reUser(String u, String p, boolean status) {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        if (!status) {
            edit.clear();
        } else {
            edit.putString("UserName", u);
            edit.putString("PassWord", p);
        }
        edit.apply();
    }

    private void getUser() {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        if (pref != null) {
            String strUserName = pref.getString("UserName", "");
            String strPassWord = pref.getString("PassWord", "");
            edPassWord.setText(strPassWord);
            edUserName.setText(strUserName);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        getUser();
        edPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }
}
