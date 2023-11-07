package com.example.musicapp.Class;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "item")
public class Item {
    @PrimaryKey
    @NonNull
    private String id;
    private boolean chosen;

    public Item(String id, boolean chosen) {
        this.id = id;
        this.chosen = chosen;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChosen() {
        return chosen;
    }

    public void setChosen(boolean chosen) {
        this.chosen = chosen;
    }
}
