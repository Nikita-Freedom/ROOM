package com.example.carmytest.Data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {ContactEntity.class}, version = 1)
abstract public class DataBase extends RoomDatabase {
    public abstract DAO getContactsDAO();
}
