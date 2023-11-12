package com.example.musicapp.Fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.musicapp.Function.IdentifyProtocolV1
import com.example.musicapp.Function.MusicMetadata
import com.example.musicapp.R
import com.example.musicapp.R.layout
import com.google.gson.Gson
import java.io.IOException


class FinderFragment : Fragment() {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    private lateinit var metadata: MusicMetadata
    private val REQUEST_EXTERNAL_STORAGE = 1

    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var filePath = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Gắn layout fragment_home.xml vào fragment
        val view = inflater.inflate(layout.fragment_finder, container, false)
        val textView: TextView = view.findViewById(R.id.neh)
        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        // Request permission to record audio
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.RECORD_AUDIO),
                1
            )
        }
        val permission = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                requireActivity(),
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
        val recordButton: ImageButton = view.findViewById(R.id.recordButton)
        recordButton.setOnClickListener {
            if (isRecording) {
                recordButton.setImageResource(R.mipmap.ic_launcher)
                stopRecording()
                val handler = Handler()
                val text = IdentifyProtocolV1.execute(filePath)
                textView.text = "Wait a second, PMusic is looking"
                handler.postDelayed(Runnable {
                    metadata = fetchDataAPI(text)
                    val status = metadata.status.msg
                    if(status.equals("Success")){
                        textView.text = status
                        textView.setTextColor(Color.Green.toArgb())
                        val nameMusic = metadata.metadata.music[0].title
                        var text1: TextView = view.findViewById(R.id.textView)
                        var text2: TextView = view.findViewById(R.id.textView2)
                        var text3: TextView = view.findViewById(R.id.textView3)
                        text1.text = "Title: "+nameMusic
                        val artist = metadata.metadata.music[0].artists[0].name
                        text2.text = "Artist: "+artist
                        val ytLink = metadata.metadata.music[0].externalMetadata.youtube.vid
                        text3.text = "Youtube: "+ytLink
                        var ytbIcon : ImageButton = view.findViewById(R.id.imageButton)
                        ytbIcon.setOnClickListener{
                            val browserIntent =
                                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=$ytLink"))
                            startActivity(browserIntent)
                        }
                    }
                    else{
                        textView.text = status
                        textView.setTextColor(Color.Red.toArgb())
                        var text1: TextView = view.findViewById(R.id.textView)
                        var text2: TextView = view.findViewById(R.id.textView2)
                        var text3: TextView = view.findViewById(R.id.textView3)
                        text1.text = "Title: NULL"
                        text2.text = "Artist: NULL"
                        text3.text = "Youtube: NULL"
                    }

                }, 1000)

            } else {
                startRecording()
                recordButton.setImageResource(R.drawable.ic_launcher)
            }
        }

        return view
    }
    private fun fetchDataAPI(text: String): MusicMetadata {
        val gson = Gson()
        return gson.fromJson(text, MusicMetadata::class.java)
    }
    private fun startRecording() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        var fileName = Environment.getExternalStorageDirectory().absolutePath
        fileName += "/youraudiofile.amr";
        mediaRecorder?.setOutputFile(fileName)
        filePath = fileName

        try {
            mediaRecorder?.prepare()
            mediaRecorder?.start()
            isRecording = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopRecording() {
        mediaRecorder?.stop()
        mediaRecorder?.release()
        mediaRecorder = null
        isRecording = false
    }
}