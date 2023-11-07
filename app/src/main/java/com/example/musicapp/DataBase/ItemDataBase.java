package com.example.musicapp.DataBase;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.musicapp.Class.Item;

@Database(entities = {Item.class},version = 1)
public abstract class ItemDataBase extends RoomDatabase {
    private static final String DATABASE_NAME = "item.db";
    private static ItemDataBase instance;

    public static synchronized ItemDataBase getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), ItemDataBase.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract ItemDao itemDao();
}
