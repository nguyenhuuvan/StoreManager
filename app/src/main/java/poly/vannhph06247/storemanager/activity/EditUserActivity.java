package poly.vannhph06247.storemanager.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import poly.vannhph06247.storemanager.Library;
import poly.vannhph06247.storemanager.R;
import poly.vannhph06247.storemanager.dao.UserDAO;
import poly.vannhph06247.storemanager.model.User;

public class EditUserActivity extends Library {
    private EditText edAdminName;
    private EditText edAdminAge;
    private EditText edAdminPhone;
    private EditText edAdminEmail;
    private UserDAO userDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);


        userDAO = new UserDAO(this);
        initView();
        initAction();
    }

    private void initAction() {
        User user = userDAO.getUser("ADMIN");
        edAdminName.setText(user.getAdminName());
        if (user.getAdminAge() == 0) {
            edAdminAge.setText("");
        } else {
            edAdminAge.setText(String.valueOf(user.getAdminAge()));
        }

        edAdminEmail.setText(user.getAdminEmail());
        edAdminPhone.setText(user.getAdminPhone());
    }

    private void initView() {
        Toolbar toolbar;
        edAdminName = findViewById(R.id.edAdminName);
        edAdminAge = findViewById(R.id.edAdminAge);
        edAdminPhone = findViewById(R.id.edAdminPhone);
        edAdminEmail = findViewById(R.id.edAdminEmail);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                editUser();
                break;
        }
        return false;
    }

    private void editUser() {
        String adminName = edAdminName.getText().toString().trim();
        String adminAge = edAdminAge.getText().toString().trim();
        String adminPhone = edAdminPhone.getText().toString().trim();
        String adminEmail = edAdminEmail.getText().toString().trim();

        if (adminName.isEmpty() || adminEmail.isEmpty() || adminPhone.isEmpty() || adminAge.isEmpty()) {
            if (adminName.isEmpty())
                edAdminName.setError(getString(R.string.notify_empty_name));
            if (adminEmail.isEmpty())
                edAdminEmail.setError(getString(R.string.notify_empty_email));
            if (adminPhone.isEmpty())
                edAdminPhone.setError(getString(R.string.notify_empty_phone));
            if (adminAge.isEmpty())
                edAdminAge.setError(getString(R.string.notify_empty_age));
        } else if (adminName.length() > 30 || adminEmail.length() > 30) {
            if (adminName.length() > 30)
                edAdminName.setError(getString(R.string.notify_length_name));
            if (adminEmail.length() > 30)
                edAdminEmail.setError(getString(R.string.notify_length_email));

        } else if (!adminPhone.matches(format_phone) || !adminEmail.matches(format_email) || checkAge(adminAge) == 1 || checkAge(adminAge) == 0 || checkString(adminName)) {
            if (!adminPhone.matches(format_phone))
                edAdminPhone.setError(getString(R.string.notify_same_sdt));
            if (!adminEmail.matches(format_email))
                edAdminEmail.setError(getString(R.string.notify_same_email));
            if (checkAge(adminAge) == 1)
                edAdminAge.setError(getString(R.string.notify_length_age_max));
            if (checkAge(adminAge) == 0)
                edAdminAge.setError(getString(R.string.notify_length_age_min));
            if (checkString(adminName))
                edAdminName.setError(getString(R.string.string_exist_2_space));
        } else {
            hideSoftKeyboard(this);
            long result =userDAO.updateUser("ADMIN", changeString(adminName), adminPhone, adminEmail, Integer.parseInt(adminAge));
            if(result>0){
                Toast.makeText(this, getString(R.string.edited), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, getString(R.string.edit_fail), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

}
