package com.example.musicapp.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.musicapp.Data.MusicData;
import com.example.musicapp.Data.MusicianData;
import com.example.musicapp.R;

import java.io.IOException;

public class FlashActivity extends AppCompatActivity {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash);
        context = this;

        try {
            MusicData.setArrayMusic();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        MusicianData.setListMusician();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent it = new Intent(FlashActivity.this,MainActivity.class);
                startActivity(it);
                finish();
            }
        },2000);
    }
}