package com.madt.contact_ranjana_c093794_android.data;

// DAO - provides API for reading and writing data to/from room db
// tak care of crud

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.madt.contact_ranjana_c093794_android.model.Contact;

import java.util.List;


@Dao
public interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contact);

    @Query("DELETE FROM Contact")
    void deleteAll();

    @Query("SELECT * FROM Contact ORDER BY first_name ASC")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM Contact WHERE id == :id")
    LiveData<Contact> getContact(int id);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);
}
