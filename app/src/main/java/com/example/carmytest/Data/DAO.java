package com.example.carmytest.Data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAO {

    @Insert
    long AddContact(ContactEntity contactEntity); // метод добавления контакта

    @Update
    void UpdateContact(ContactEntity contactEntity); // метод обновления данных в айтеме

    @Delete
    void DeleteContact(ContactEntity contactEntity); // метод удаления конакта. Вызывается при нажатии на кнопку Delete или свайпу по айтему

    @Query("SELECT * FROM contacts") // выбираем все контакты из БД
    List<ContactEntity> getAllContacts();

    @Query("SELECT * FROM contacts WHERE contacts_id==:contactID") // выбираем все контакты по заданному id
    ContactEntity getContact(long contactID);
}
