package com.example.carmytest;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.carmytest.Data.ContactEntity;
import com.example.carmytest.Data.DataBase;
import com.example.carmytest.Recycler.ContactsAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ContactsAdapter contactsAdapter;
    private ArrayList<ContactEntity> entityArrayList = new ArrayList<>(); // создаем массив объектов контакта ContactEntity
    DataBase database; // объект главного класса базы данных
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // усатанавливаем разметку activity_main.xml
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);  // объект RecyclerView
        database = Room.databaseBuilder(getApplicationContext(), DataBase.class, "ContactsDB")
                .build();  // создаем подключение к БД, в котором указываем конект нашей активности, класс БД и имя БД


        new GetAllContactAsyncTask().execute();
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditContact(false, null, -1);
            }
        });
        contactsAdapter = new ContactsAdapter(this, entityArrayList, MainActivity.this); // создаем адаптер

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);


    }

    public void EditContact(final boolean isUpdate, final ContactEntity contactEntity, final int position){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.add_contact, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);
        TextView newContactTitle = view.findViewById(R.id.contactTextView);
        final EditText nameEditText = view.findViewById(R.id.nameTextView);
        final EditText surnameEditText = view.findViewById(R.id.surnameTextView);
        final EditText emailEditText = view.findViewById(R.id.emailTextView);
        final EditText phoneEditText = view.findViewById(R.id.phoneTextView);
        newContactTitle.setText(!isUpdate ? "Add Contact" : "Edit Contact");
        if(isUpdate && contactEntity != null){
            nameEditText.setText(contactEntity.getName());
            surnameEditText.setText(contactEntity.getSurname());
            emailEditText.setText(contactEntity.getEmail());
            phoneEditText.setText(contactEntity.getPhone_number());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton((isUpdate ? "Update" : "Save"),  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton(isUpdate ? "Delete" : "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isUpdate) {

                            DeleteContact(contactEntity, position);
                        } else {

                            dialog.cancel();

                        }
                    }
                });
        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(nameEditText.getText().toString())){
                    Toast.makeText(MainActivity.this, "Enter contact name!", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(surnameEditText.getText().toString())){
                    Toast.makeText(MainActivity.this, "Enter contact surname!", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(surnameEditText.getText().toString())){
                    Toast.makeText(MainActivity.this, "Enter contact email", Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(phoneEditText.getText().toString())){
                    Toast.makeText(MainActivity.this, "Enter contact phone", Toast.LENGTH_SHORT).show();
                }else {
                    alertDialog.dismiss();
                }
                if (isUpdate && contactEntity != null) {

                    UpdateContact(nameEditText.getText().toString(), surnameEditText.getText().toString(), emailEditText.getText().toString(),
                            phoneEditText.getText().toString(), position);
                } else {

                    CreateContact(nameEditText.getText().toString(), surnameEditText.getText().toString(), emailEditText.getText().toString(),
                            phoneEditText.getText().toString());
                }
            }
        });
    }


    private void CreateContact(String name, String surname, String email, String phone_number){
        new CreateContactAsyncTask().execute(new ContactEntity(0, name, surname, email, phone_number));
    }
    private void UpdateContact(String name, String surname, String email, String phone_number, int position){
        ContactEntity contactEntity = entityArrayList.get(position);
        contactEntity.setName(name);
        contactEntity.setSurname(surname);
        contactEntity.setEmail(email);
        contactEntity.setPhone_number(phone_number);
        new UpdateContactAsyncTask().execute(contactEntity);
        entityArrayList.set(position, contactEntity);
    }
    private void DeleteContact(ContactEntity contactEntity, int position){
        entityArrayList.remove(position);
        new DeleteContactAsyncTask().execute(contactEntity);
    }










    private class GetAllContactAsyncTask extends AsyncTask<Void, Void, Void>{ // AsyncTask для получаения всех контактов
        @Override
        protected void onPostExecute(Void aVoid) { // метод передающий действия в UI поток
            super.onPostExecute(aVoid);
            contactsAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... voids) { // метод в котором выполняются действия в побочном потоке
            entityArrayList.addAll(database.getContactsDAO().getAllContacts()); // получение всех контактов вызовом метода getAllContacts() методом getContactsDAO() класса DataBase
            return null;
        }
    }




    private class CreateContactAsyncTask extends AsyncTask<ContactEntity, Void, Void>{ // AsyncTask для добавления контакта
        @Override
        protected void onPostExecute(Void aVoid) { // метод передающий действия в UI поток
            contactsAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(ContactEntity... contactEntities) {
            long id = database.getContactsDAO().AddContact(contactEntities[0]);
            ContactEntity contactEntity = database.getContactsDAO().getContact(id);
            if(contactEntity != null){
                entityArrayList.add(0, contactEntity);
            }
            return null;
        }
    }




    private class UpdateContactAsyncTask extends AsyncTask<ContactEntity, Void, Void>{ // AsyncTask для обновления данных контакта
        @Override
        protected void onPostExecute(Void aVoid) { // метод передающий действия в UI поток
            contactsAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(ContactEntity... contactEntities) {
            database.getContactsDAO().UpdateContact(contactEntities[0]);
            return null;
        }
    }


    private class DeleteContactAsyncTask extends AsyncTask<ContactEntity, Void, Void>{ // AsyncTask для удаленяи контакта
        @Override
        protected void onPostExecute(Void aVoid) { // метод передающий действия в UI поток
            contactsAdapter.notifyDataSetChanged();
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(ContactEntity... contactEntities) {
            database.getContactsDAO().DeleteContact(contactEntities[0]);
            return null;
        }
    }
}