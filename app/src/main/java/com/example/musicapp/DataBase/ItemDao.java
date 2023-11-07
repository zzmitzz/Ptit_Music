package com.example.musicapp.DataBase;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.musicapp.Class.Item;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insertItem(Item item);
    @Query("SELECT * FROM item")
    List<Item> getList();

    @Query("SELECT * FROM item WHERE id = :id")
    List<Item>find(String id);

    @Update
    void updateItem(Item item);
}
