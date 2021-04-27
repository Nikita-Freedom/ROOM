package com.example.carmytest.Recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carmytest.Data.ContactEntity;
import com.example.carmytest.MainActivity;
import com.example.carmytest.R;
import java.util.ArrayList;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ContactEntity> contacts;
    private MainActivity mainActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView surnameTextView;
        public TextView emailTextView;
        public TextView PhoneTextView;

        public MyViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.nameTextView);
            surnameTextView = view.findViewById(R.id.surnameTextView);
            emailTextView = view.findViewById(R.id.emailTextView);
            PhoneTextView = view.findViewById(R.id.phoneTextView);

        }
    }


    public ContactsAdapter(Context context, ArrayList<ContactEntity> contacts, MainActivity mainActivity) {
        this.context = context;
        this.contacts = contacts;
        this.mainActivity = mainActivity;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);

        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        final ContactEntity contact = contacts.get(position);

        holder.nameTextView.setText(contact.getName());
        holder.surnameTextView.setText(contact.getSurname());
        holder.emailTextView.setText(contact.getEmail());
        holder.PhoneTextView.setText(contact.getPhone_number());

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mainActivity.EditContact(true, contact, position);
            }
        });

    }

    @Override
    public int getItemCount() {

        return contacts.size();
    }


}
