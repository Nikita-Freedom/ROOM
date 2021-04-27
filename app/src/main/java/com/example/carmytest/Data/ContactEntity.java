package com.example.carmytest.Data;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

@androidx.room.Entity(tableName = "contacts")
public class ContactEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "contacts_id")
    private long id;

    @ColumnInfo(name = "contacts_name")
    private String name;

    @ColumnInfo(name = "contacts_surname")
    private String surname;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "phone_number")
    private String phone_number;

    public ContactEntity(long id, String name, String surname, String email, String phone_number) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone_number = phone_number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
