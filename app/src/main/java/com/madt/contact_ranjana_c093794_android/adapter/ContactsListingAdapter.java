package com.madt.contact_ranjana_c093794_android.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.madt.contact_ranjana_c093794_android.R;
import com.madt.contact_ranjana_c093794_android.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class ContactsListingAdapter extends RecyclerView.Adapter<ContactsListingAdapter.ViewHolder> {

    private List<Contact> contactList;
    private Context context;
    private OnContactLongPressListener onContactLongPressListener;
    private OnEditContactClickListener onEditContactClickListener;

    public ContactsListingAdapter(List<Contact> contactList, Context context, OnContactLongPressListener onContactLongPressListener, OnEditContactClickListener onEditContactClickListener) {
        this.contactList = contactList;
        this.context = context;
        this.onContactLongPressListener = onContactLongPressListener;
        this.onEditContactClickListener = onEditContactClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_contact, parent, false);
        return new ViewHolder(view);
    }
    // method for filtering our recyclerview items.
    public void filterList(ArrayList<Contact> filterList) {
       // add filtered list in adapter list
        contactList = filterList;
      // notify adapter changes in list
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        // Set data on recycler row according to position
        holder.txtFirstName.setText(contact.getFirstName()+" "+contact.getLastName());
        holder.txtPhoneNumber.setText(contact.getPhoneNumber()+"");
        holder.txtTag.setText(contact.getFirstName().charAt(0)+""+contact.getLastName().charAt(0));

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView txtFirstName, txtPhoneNumber,txtTag;
        private ImageView imgEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFirstName=itemView.findViewById(R.id.txt_name);
            txtPhoneNumber=itemView.findViewById(R.id.txt_phone_number);
            txtTag=itemView.findViewById(R.id.txtTag);
            imgEdit=itemView.findViewById(R.id.img_edit);

            // Long Press on recycler row
            itemView.setOnLongClickListener(this);

            // Click Listener on listing edit button
            imgEdit.setOnClickListener(v -> {
                onEditContactClickListener.onEditContactClick(getAdapterPosition());
            });
        }
    
        @Override
        public boolean onLongClick(View v) {
            onContactLongPressListener.onContactLongPress(getAdapterPosition());
            return true;
        }
    }

    public interface OnContactLongPressListener {
        void onContactLongPress(int position);
    }
    public interface OnEditContactClickListener {
        void onEditContactClick(int position);
    }
}










