package com.example.musicapp.Activity

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.Fragment.PlayMusicFragment
import com.example.musicapp.R

class PlayMusicActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = PlayMusicFragment(mediaPlayer)
        fragmentTransaction.add(R.id.play_music_fragment, fragment)
        fragmentTransaction.commit()
        mediaPlayer = fragment.mediaPlayer
    }
}