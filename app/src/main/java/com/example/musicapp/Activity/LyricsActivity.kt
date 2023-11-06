package com.example.musicapp.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.Adapter.LyricsAdapter
import com.example.musicapp.R

class LyricsActivity : AppCompatActivity() {
    private lateinit var adapter: LyricsAdapter
    private lateinit var rcview: RecyclerView

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyrics)
        var it:Intent = getIntent()
        rcview = findViewById(R.id.lyrics_activity)
        rcview.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rcview.adapter = LyricsAdapter(this,it.getStringExtra("nameFile"))

    }

}