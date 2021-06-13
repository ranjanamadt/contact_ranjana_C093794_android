package com.madt.contact_ranjana_c093794_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.madt.contact_ranjana_c093794_android.model.Contact;
import com.madt.contact_ranjana_c093794_android.model.ContactViewModel;

import java.util.Arrays;

public class AddContactActivity extends AppCompatActivity {
    private ContactViewModel contactViewModel;
    private EditText edtFirstName, edtLastName, edtEmail, edtPhoneNumber, edtAddress;
    private boolean isEditing = false;
    private int contactId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);

        edtFirstName = findViewById(R.id.edt_first_name);
        edtLastName = findViewById(R.id.edt_last_name);
        edtEmail = findViewById(R.id.edt_email);
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        edtAddress = findViewById(R.id.edt_address);
        
        
        if (getIntent().hasExtra(ContactListingActivity.CONTACT_ID)) {
            contactId = getIntent().getIntExtra(ContactListingActivity.CONTACT_ID, 0);
            Log.d("TAG", "onCreate: " + contactId);

            contactViewModel.getContactById(contactId).observe(this, contact -> {
                if (contact != null) {
                    edtFirstName.setText(contact.getFirstName());
                    edtLastName.setText(contact.getLastName());
                    edtEmail.setText(contact.getEmail());
                    edtPhoneNumber.setText(contact.getPhoneNumber());
                    edtAddress.setText(contact.getAddress());
                }
            });

            //TextView label = findViewById(R.id.label);
            isEditing = true;
            //label.setText(R.string.update_label);
            //addUpdateButton.setText(R.string.update_contact_btn_text);
        }

        findViewById(R.id.btnAddContact).setOnClickListener(v -> {

            if (validateFields()){

                if (isEditing) {
                    Contact contact = new Contact();
                    contact.setId(contactId);
                    contact.setFirstName(edtFirstName.getText().toString().trim());
                    contact.setLastName(edtLastName.getText().toString().trim());
                    contact.setEmail(edtEmail.getText().toString().trim());
                    contact.setPhoneNumber(Integer.parseInt(edtFirstName.getText().toString().trim()));
                    contact.setAddress(edtAddress.getText().toString().trim());
                    contactViewModel.update(contact);
                } else {
                    
                    Contact contact = new Contact(
                            edtFirstName.getText().toString().trim(),
                            edtLastName.getText().toString().trim(),
                            edtEmail.getText().toString().trim(),
                            Integer.parseInt(edtPhoneNumber.getText().toString().trim()),
                            edtAddress.getText().toString().trim()
                    );
                    contactViewModel.insert(contact);
                    Toast.makeText(this, "Contact added", Toast.LENGTH_SHORT).show();
                }

                finish();
            }

        });

    }

    private Boolean validateFields() {

        Boolean isValid = true;

        String firstName = edtFirstName.getText().toString().trim();
        String lastName = edtLastName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phoneNumber = edtPhoneNumber.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();


        if (firstName.isEmpty()) {
            edtFirstName.setError("Please enter First Name");
            edtFirstName.requestFocus();
            isValid = false;
            return isValid;
        }
        if (lastName.isEmpty()) {
            edtLastName.setError("Please enter Last Name");
            edtLastName.requestFocus();
            isValid = false;
            return isValid;
        }
        if (email.isEmpty()) {
            edtEmail.setError("Please enter Email");
            edtEmail.requestFocus();
            isValid = false;
            return isValid;
        }
        if (phoneNumber.isEmpty()) {
            edtPhoneNumber.setError("Please enter Phone Number");
            edtPhoneNumber.requestFocus();
            isValid = false;
            return isValid;
        }
        if (address.isEmpty()) {
            edtAddress.setError("Please enter address");
            edtAddress.requestFocus();
            isValid = false;
            return isValid;
        }


        return isValid;
    }
}