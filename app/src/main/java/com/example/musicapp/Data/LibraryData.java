package com.example.musicapp.Data;

import com.example.musicapp.Activity.MainActivity;
import com.example.musicapp.Class.Book;
import com.example.musicapp.Class.Music;
import com.example.musicapp.Class.Musician;
import com.example.musicapp.DataBase.HistoryDataBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LibraryData {

    public static List<Book> getFavlist(){
        List<Music>musicList = MusicData.getArrayMusic();
        List<Book>bookList = new ArrayList<>();
        for (Music music: musicList){
            if (music.isLove()){
                bookList.add(new Book(music.getId(),"favMusic",music.getHinhNen(), music.getTenNhac()));
            }
        }
        return bookList;
    }
    public static List<Book> getHisList(){
        List<Book>bookList = HistoryDataBase.getInstance(MainActivity.getContext()).historyDao().getBookArray();
        return bookList;
    }
    public static List<Book> getMusicianData(){
        List<Book>musicianList = new ArrayList<>();
        List<Musician>musicianListOld = MusicianData.getMusicianList();
        for(Musician musician :musicianListOld){
            if (musician.isLove()){
                musicianList.add(new Book(musician.getName(), "musician",musician.getImageId(), musician.getName()));
            }
        }
        return musicianList;
    }
}
