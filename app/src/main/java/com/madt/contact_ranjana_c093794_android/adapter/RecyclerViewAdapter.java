package com.madt.contact_ranjana_c093794_android.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.madt.contact_ranjana_c093794_android.R;
import com.madt.contact_ranjana_c093794_android.model.Contact;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Contact> contactList;
    private Context context;
    private OnContactClickListener onContactClickListener;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, OnContactClickListener onContactClickListener) {
        this.contactList = contactList;
        this.context = context;
        this.onContactClickListener = onContactClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.txtFirstName.setText(contact.getFirstName()+" "+contact.getLastName());
        holder.txtPhoneNumber.setText(contact.getPhoneNumber()+" ");

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtFirstName, txtPhoneNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtFirstName=itemView.findViewById(R.id.txt_name);
            txtPhoneNumber=itemView.findViewById(R.id.txt_phone_number);

        }

        /* private TextView name;
                private TextView department;
                private TextView hireDate;

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);

                    name = itemView.findViewById(R.id.name_row);
                    department = itemView.findViewById(R.id.dept_row);
                    hireDate = itemView.findViewById(R.id.hire_date_row);

                    itemView.setOnClickListener(this);

                }
        */
        @Override
        public void onClick(View v) {
            onContactClickListener.onContactClick(getAdapterPosition());
        }
    }

    public interface OnContactClickListener {
        void onContactClick(int position);
    }
}










