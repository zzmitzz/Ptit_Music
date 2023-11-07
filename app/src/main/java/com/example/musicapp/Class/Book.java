package com.example.musicapp.Class;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "history")
public class Book implements Serializable,Comparable<Book> {

    @PrimaryKey
    @NonNull
    private String id ;
    private String category;
    private int resourceId;
    private String title;
    private long playHisTime;

    public Book(){

    }
    public Book(String id, String category, int resourceId, String title) {
        this.id = id;
        this.category = category;
        this.resourceId = resourceId;
        this.title = title;
    }
    public Book(String id, String category, int resourceId, String title,long playHisTime) {
        this.id = id;
        this.category = category;
        this.resourceId = resourceId;
        this.title = title;
        this.playHisTime = playHisTime;
    }

    public long getPlayHisTime() {
        return playHisTime;
    }

    public void setPlayHisTime(long playHisTime) {
        this.playHisTime = playHisTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
//    @Override
//    public boolean equals(Object obj){
//        if (!(obj instanceof Book))
//            return false;
//        if (obj == this)
//            return true;
//        Book rhs = (Book) obj;
//        return this.title.equals(((Book) obj).title);
//
//    }
    @Override
    public int compareTo(Book book) {
        return 0;
    }
}
