package com.example.musicapp.DataBase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.musicapp.Class.Book;
import com.example.musicapp.Class.Music;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    void insertHistory(Book book);

    @Query("SELECT * FROM history ORDER BY playHisTime DESC")
    List<Book> getBookArray();
    @Query("SELECT * FROM history WHERE id = :id")
    List<Book> checkExist(String id);
    @Delete
    void deleteBook(Book book);

}
