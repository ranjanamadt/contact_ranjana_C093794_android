package com.madt.contact_ranjana_c093794_android;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.madt.contact_ranjana_c093794_android.adapter.RecyclerViewAdapter;
import com.madt.contact_ranjana_c093794_android.model.Contact;
import com.madt.contact_ranjana_c093794_android.model.ContactViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class ContactListingActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {
    private ContactViewModel contactViewModel;

    private RecyclerView rcContacts;
    private RecyclerViewAdapter rcContactsAdapter;
    public static final String CONTACT_ID = "contact_id";
    private Contact deletedContact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);

        rcContacts = findViewById(R.id.rcContacts);
        rcContacts.setHasFixedSize(true);
        rcContacts.setLayoutManager(new LinearLayoutManager(this));

     
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ContactListingActivity.this, AddContactActivity.class);
            /*startActivityForResult(intent, ADD_EMPLOYEE_REQUEST_CODE);*/
            // the following approach as startActivityForResult is deprecated
            launcher.launch(intent);

        });

        // attach the itemTouchHelper to my recyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rcContacts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactViewModel.getAllContacts().observe(this, contacts -> {
            // set adapter
            rcContactsAdapter = new RecyclerViewAdapter(contacts, this, this);

            rcContacts.setAdapter(rcContactsAdapter);
        });
    }

    @Override
    public void onContactClick(int position) {
        Contact contact = contactViewModel.getAllContacts().getValue().get(position);
        Intent intent = new Intent(ContactListingActivity.this, AddContactActivity.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ContactListingActivity.this);
                    builder.setTitle("Are you sure?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        deletedContact = contact;
                        contactViewModel.delete(contact);
                        Snackbar.make(rcContacts, deletedContact.getFirstName() + " is deleted!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", v -> contactViewModel.insert(deletedContact)).show();
                    });
                    builder.setNegativeButton("No", (dialog, which) -> rcContactsAdapter.notifyDataSetChanged());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;
                case ItemTouchHelper.RIGHT:
                    Intent intent = new Intent(ContactListingActivity.this, AddContactActivity.class);
                    intent.putExtra(CONTACT_ID, contact.getId());
                    startActivity(intent);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .setIconHorizontalMargin(1, 1)
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .addSwipeRightActionIcon(R.drawable.ic_baseline_update_24)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                  //  String name = data.getStringExtra(AddContactActivity.NAME_REPLY);
                  //  String salary = data.getStringExtra(AddContactActivity.SALARY_REPLY);
                   // String department = data.getStringExtra(AddContactActivity.DEPARTMENT_REPLY);
                    // getting the current date
                  /*  Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
                    String joiningDate = sdf.format(cal.getTime());

                    Contact contact = new Contact(name, department, joiningDate, Double.parseDouble(salary));
                    contactViewModel.insert(contact);*/
                }
            });

}