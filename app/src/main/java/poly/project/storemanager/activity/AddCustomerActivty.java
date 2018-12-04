package poly.project.storemanager.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Objects;

import poly.project.storemanager.Library;
import poly.project.storemanager.R;
import poly.project.storemanager.dao.CustomerDAO;
import poly.project.storemanager.model.Customer;

public class AddCustomerActivty extends Library {
    private EditText edCustomerPhone;
    private EditText edCustomerName;
    private RadioButton radioMale;
    private RadioButton radioFeMale;
    private EditText edCustomerAge;
    private EditText edCustomerEmail;
    private EditText edCustomerAddress;
    private EditText edCustomerNote;
    private RadioGroup radioGroup;
    private CustomerDAO customerDAO;
    private ImageView imgCustomer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_activty);

        customerDAO = new CustomerDAO(this);
        initViews();
        initAction();
    }

    private void initAction() {
        imgCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        edCustomerPhone = findViewById(R.id.edCustomerPhone);
        edCustomerName = findViewById(R.id.edCustomerName);
        edCustomerAge = findViewById(R.id.edCustomerAge);
        edCustomerEmail = findViewById(R.id.edCustomerEmail);
        edCustomerAddress = findViewById(R.id.edCustomerAddress);
        edCustomerNote = findViewById(R.id.edCustomerNote);
        radioGroup = findViewById(R.id.radio_group);
        radioMale = findViewById(R.id.radio_male);
        radioFeMale = findViewById(R.id.radio2);
        imgCustomer = findViewById(R.id.imgAvatar);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (resultCode == RESULT_OK) {
                    Bitmap bit = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
                    imgCustomer.setImageBitmap(bit);
                }
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                hideSoftKeyboard(this);
                addCustomer();
                break;
        }
        return false;
    }

    private void addCustomer() {
        String customerPhone, customerName, customerSex, customerEmail, customerAddress, note, customerAge;

        customerPhone = edCustomerPhone.getText().toString().trim();
        customerName = edCustomerName.getText().toString().trim();
        customerSex = getTextRadioGroup(radioGroup,radioMale,radioFeMale);
        customerEmail = edCustomerEmail.getText().toString().trim();
        customerAddress = edCustomerAddress.getText().toString().trim();
        note = edCustomerNote.getText().toString().trim();
        customerAge = edCustomerAge.getText().toString().trim();

        if (customerPhone.isEmpty() || customerName.isEmpty()) {
            if (customerPhone.isEmpty())
                edCustomerPhone.setError(getString(R.string.notify_empty_phone));
            if (customerName.isEmpty())
                edCustomerName.setError(getString(R.string.notify_empty_name));
        } else if (customerName.length() > 30 || customerEmail.length() > 30 || customerAddress.length() > 50 || note.length() > 255) {
            if (customerName.length() > 30)
                edCustomerName.setError(getString(R.string.notify_length_name));
            if (customerEmail.length() > 30)
                edCustomerEmail.setError(getString(R.string.notify_length_email));
            if (customerAddress.length() > 50)
                edCustomerAddress.setError(getString(R.string.notify_length_address));
            if (note.length() > 255)
                edCustomerNote.setError(getString(R.string.notify_length_note));
        } else if ((checkAge(customerAge) == 1 && !customerAge.isEmpty()) ||
                (checkAge(customerAge) == 0 && !customerAge.isEmpty()) ||
                (!customerEmail.matches(format_email) && !customerEmail.isEmpty()) ||
                checkString(customerName) || (checkString(customerAddress) && !customerAddress.isEmpty()) ||
                (checkString(note) && !note.isEmpty()) || !customerPhone.matches(format_phone)) {
            if ((checkAge(customerAge) == 1 && !customerAge.isEmpty()))
                edCustomerAge.setError(getString(R.string.notify_length_age_max));
            if ((checkAge(customerAge) == 0 && !customerAge.isEmpty()))
                edCustomerAge.setError(getString(R.string.notify_length_age_min));
            if ((!customerEmail.matches(format_email) && !customerEmail.isEmpty()))
                edCustomerEmail.setError(getString(R.string.notify_same_email));
            if (checkString(customerName))
                edCustomerName.setError(getString(R.string.string_exist_2_space));
            if ((checkString(customerAddress) && !customerAddress.isEmpty()))
                edCustomerAddress.setError(getString(R.string.string_exist_2_space));
            if ((checkString(note) && !note.isEmpty()))
                edCustomerNote.setError(getString(R.string.string_exist_2_space));
            if (!customerPhone.matches(format_phone))
                edCustomerPhone.setError(getString(R.string.notify_same_sdt));
        } else {
            Customer customer = customerDAO.getCustomerByID(customerPhone);
            BitmapDrawable bitmapDrawable = (BitmapDrawable) imgCustomer.getDrawable();
            Bitmap bitmap = bitmapDrawable.getBitmap();
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);
            byte[] imgAvatar = byteArray.toByteArray();
            if (customer == null) {
                Date date = new Date();
                Customer customer1 = new Customer();
                customer1.setCustomerPhone(customerPhone);

                customer1.setCustomerName(changeString(customerName));
                customer1.setCustomerSex(customerSex);
                if (customerAddress.isEmpty()) {
                    customer1.setCustomerAddress(customerAddress);
                } else {
                    customer1.setCustomerAddress(changeString(customerAddress));
                }
                customer1.setCustomerEmail(customerEmail);
                if (note.isEmpty()) {
                    customer1.setNote(note);
                } else {
                    customer1.setNote(changeString(note));
                }
                if (customerAge.isEmpty()) {
                    customer1.setCustomerAge(0);
                } else {
                    customer1.setCustomerAge(Integer.parseInt(customerAge));
                }
                customer1.setImgcustomer(imgAvatar);
                customer1.setOldImgCustomer(null);

                customer1.setOldCustomerName("");
                customer1.setOldCustomerSex("");
                customer1.setOldCustomerEmail("");
                customer1.setOldCustomerAddress("");
                customer1.setOldNote("");
                customer1.setOldCustomerAge(0);

                customer1.setAddedDay(date.getTime());
                customer1.setEditedDay(0);
                long result = customerDAO.insertCustomer(customer1);
                if (result > 0) {
                    Toast.makeText(AddCustomerActivty.this, getString(R.string.added), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddCustomerActivty.this, R.string.add_fail, Toast.LENGTH_SHORT).show();
                }
            } else {
                edCustomerPhone.setError(getString(R.string.customer_id_exist));
            }
        }
    }


}
