package com.example.musicapp.Class;

import java.util.List;

public class Category {
    private String nameCategory;
    private List<Book>books;
    private String from;

    public Category(String nameCategory, List<Book> books,String from) {
        this.nameCategory = nameCategory;
        this.books = books;
        this.from = from;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
