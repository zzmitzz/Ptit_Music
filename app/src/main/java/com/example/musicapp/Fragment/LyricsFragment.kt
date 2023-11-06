//package com.example.musicapp.Fragment
//
//import android.os.Bundle
//import androidx.fragment.app.Fragment
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.musicapp.Adapter.LyricsAdapter
//import com.example.musicapp.R
//
//
//class LyricsFragment : Fragment() {
//    private lateinit var rcview: RecyclerView
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//
//        rcview = findViewById(R.id.lyrics_activity)
//        rcview.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
//        rcview.adapter = LyricsAdapter(context)
//        return inflater.inflate(R.layout.fragment_lyrics, container, false)
//    }
//}