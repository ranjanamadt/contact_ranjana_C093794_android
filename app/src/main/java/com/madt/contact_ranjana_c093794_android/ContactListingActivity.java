package com.madt.contact_ranjana_c093794_android;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.madt.contact_ranjana_c093794_android.adapter.ContactsListingAdapter;
import com.madt.contact_ranjana_c093794_android.model.Contact;
import com.madt.contact_ranjana_c093794_android.model.ContactViewModel;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ContactListingActivity extends AppCompatActivity implements ContactsListingAdapter.OnContactLongPressListener, ContactsListingAdapter.OnEditContactClickListener {
    private ContactViewModel contactViewModel;

    private RecyclerView rcContacts;
    private ContactsListingAdapter rcContactsAdapter;
    public static final String CONTACT_ID = "contact_id";
    public TextView txtNoContactFound;
    private Contact deletedContact;  // Variable to keep deleted contact
    private List<Contact> contactList = new ArrayList<>();
    public TextView txtContactsCount; //TextView to show count of contacts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);


        rcContacts = findViewById(R.id.rcContacts);
        txtNoContactFound = findViewById(R.id.txtNoFound);
        txtContactsCount = findViewById(R.id.txtContactCount);
        SearchView searchView = findViewById(R.id.searchView);
        FloatingActionButton fab = findViewById(R.id.fab);

        // Setup Recycler View Layout Manager
        rcContacts.setHasFixedSize(true);
        rcContacts.setLayoutManager(new LinearLayoutManager(this));


        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Calling a method to filter Contact List
                filter(newText);
                return false;
            }
        });



       // Click on Add contact Button
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ContactListingActivity.this, AddContactActivity.class);
            startActivity(intent);

        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcContacts);
    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<Contact> filteredList = new ArrayList<>();

        // running a for loop to compare elements.
        for (Contact item : contactList) {
            // checking if the entered string matched with first name or last name  of contact list
            if (item.getFirstName().toLowerCase().contains(text.toLowerCase())||item.getLastName().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched , adding it to filtered contact list
                filteredList.add(item);
            }
        }
        if (filteredList.isEmpty()) {
            // if no item is added in filtered list displaying  no contact found.
            txtNoContactFound.setVisibility(View.VISIBLE);
        } else {
            // if we have  contacts added in filtered list
            txtNoContactFound.setVisibility(View.INVISIBLE);
        }
        // displaying contacts count
        setContactsCount(filteredList.size());
        // passing that filtered list to adapter class.
        rcContactsAdapter.filterList(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // getting updated contacts on resume and updating list
        contactViewModel.getAllContacts().observe(this, contacts -> {
            contactList.clear();
            contactList.addAll(contacts);
            setContactsCount(contacts.size());

            // If contacts list is empty
            if (contacts.isEmpty()) {
                txtNoContactFound.setVisibility(View.VISIBLE);
                rcContacts.setVisibility(View.INVISIBLE);

            } else {
                txtNoContactFound.setVisibility(View.INVISIBLE);
                rcContacts.setVisibility(View.VISIBLE);
                rcContactsAdapter = new ContactsListingAdapter(contacts, this, this,this);
                rcContacts.setAdapter(rcContactsAdapter);
            }
        });
    }

    // Method to set contacts count
    private void setContactsCount(int size) {
        txtContactsCount.setText("(" + size + ")");
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Contact contact = contactViewModel.getAllContacts().getValue().get(position);
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    // confirmation dialog to ask user before delete contact
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactListingActivity.this);
                    builder.setTitle("Are you sure you want to delete this contact?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        deletedContact = contact;
                        contactViewModel.delete(contact);

                        // Display snackbar with deleted contact name and giving option to undo also
                        Snackbar.make(rcContacts, deletedContact.getFirstName() + deletedContact.getLastName()+ " is deleted!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", v -> contactViewModel.insert(deletedContact)).show();

                    });
                    builder.setNegativeButton("No", (dialog, which) -> rcContactsAdapter.notifyDataSetChanged());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .setIconHorizontalMargin(1, 1)
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };


    @Override
    public void onContactLongPress(int position) {
        String MAKE_CALL = "Make a Call";
        String SEND_SMS = "Send SMS";
        String SEND_EMAIL = "Send email";

        final CharSequence[] items = {MAKE_CALL, SEND_SMS, SEND_EMAIL};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        makeACall(contactList.get(position).getPhoneNumber());
                        break;
                    case 1:
                        sendSMS(contactList.get(position).getPhoneNumber());
                        break;
                    case 2:
                        sendEmail(contactList.get(position).getEmail());
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + items[item].toString());
                }

            }
        });
        builder.show();


    }

    private void sendEmail(String toEmail) {

        // Intent to send user to Email app
        Intent mailIntent = new Intent(Intent.ACTION_SEND);
        mailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ toEmail});
        mailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        mailIntent.putExtra(Intent.EXTRA_TEXT, "");

        mailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(mailIntent, "Choose an Email client :"));
    }

    private void sendSMS(long phoneNumber) {
        // Intent to send user to Message app
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
        smsIntent.putExtra("sms_body", "");
        startActivity(smsIntent);
    }

    private void makeACall(long phoneNumber) {
        // Intent to send user to Call app
            String dial = "tel:" + phoneNumber;
            startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(dial)));
    }

    @Override
    public void onEditContactClick(int position) {

        Contact contact = contactViewModel.getAllContacts().getValue().get(position);
        Intent intent = new Intent(ContactListingActivity.this, AddContactActivity.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);
    }
}