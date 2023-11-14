package com.example.musicapp.Fragment

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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
    private val viewmodel : FindSongViewModel by lazy {
        ViewModelProvider(this)[FindSongViewModel::class.java]
    }
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

        val textView: TextView = view.findViewById(R.id.neh)
        val loginAPI: Button = view.findViewById(R.id.ApiLogin)
        loginAPI.setOnClickListener(){
            ApiLogin()
        }
        val recordButton: ImageButton = view.findViewById(R.id.recordButton)
        recordButton.setOnClickListener {
            if (isRecording) {
                recordButton.setImageResource(R.mipmap.ic_launcher)
                stopRecording()
                val handler = Handler()
                val text = IdentifyProtocolV1.execute(filePath,viewmodel.host, viewmodel.accessKey,viewmodel.secretKey)
                textView.text = "Wait a second, PMusic is looking"
                handler.postDelayed(Runnable {
                    try {
                        metadata = fetchDataAPI(text)
                        val status = metadata.status.msg
                        if (status.equals("Success")) {
                            try {
                                textView.text = status
                                textView.setTextColor(Color.Green.toArgb())
                                var nameMusic = metadata.metadata.music[0].title
                                Log.i("debug", nameMusic)
                                if (nameMusic == null) {
                                    nameMusic = "Unknown"
                                }
                                var text1: TextView = view.findViewById(R.id.textView)
                                var text2: TextView = view.findViewById(R.id.textView2)
                                var text3: TextView = view.findViewById(R.id.textView3)
                                text1.text = "Title: " + nameMusic
                                var artist = metadata.metadata.music[0].artists[0].name
                                if (artist == null) {
                                    artist = "Unknown"
                                }
                                text2.text = "Artist: " + artist
                                var ytLink: String
                                try {
                                    ytLink = metadata.metadata.music[0].externalMetadata.youtube.vid
                                } catch (e: Exception) {
                                    ytLink = "Unknown"
                                }

                                text3.text = "Youtube: " + ytLink
                                var ytbIcon: ImageButton = view.findViewById(R.id.imageButton)
                                ytbIcon.setOnClickListener {
                                    if (!ytLink.equals("Unknown")) {
                                        val browserIntent =
                                            Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://www.youtube.com/watch?v=$ytLink")
                                            )
                                        startActivity(browserIntent)
                                    }
                                }
                            } catch (e: Exception) {
                                textView.text = "Lost Connection"
                                textView.setTextColor(Color.Yellow.toArgb())
                                var text1: TextView = view.findViewById(R.id.textView)
                                var text2: TextView = view.findViewById(R.id.textView2)
                                var text3: TextView = view.findViewById(R.id.textView3)
                                text1.text = "Title: NULL"
                                text2.text = "Artist: NULL"
                                text3.text = "Youtube: NULL"
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
                    }catch (e: Exception){
                        textView.text = "Wrong API id"
                        textView.setTextColor(Color.Red.toArgb())
                    }


                }, 1000)

            } else {
                if(viewmodel.host.isEmpty() || viewmodel.accessKey.isEmpty() || viewmodel.secretKey.isEmpty()){
                    Toast.makeText(context, "Check your API identify", Toast.LENGTH_SHORT).show()
                }
                else{
                    startRecording()
                    recordButton.setImageResource(R.drawable.ic_launcher)
                }
            }
        }

        return view
    }
    private fun fetchDataAPI(text: String): MusicMetadata {
        val gson = Gson()
        return gson.fromJson(text, MusicMetadata::class.java)
    }
    fun ApiLogin(){
        var host: String = ""
        var key: String = ""
        var password :  String = ""
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setTitle("API token")
        var hostText: TextView = TextView(context)
        hostText.text = "Host"
        val keyAccessStr: TextView = TextView(context)
        keyAccessStr.text = "keyAccess"
        val secretKey: TextView = TextView(context)
        secretKey.text = "secretKey"
        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        val input2 = EditText(context)
        input2.inputType = InputType.TYPE_CLASS_TEXT
        val input3 = EditText(context)
        input3.inputType = InputType.TYPE_CLASS_TEXT
        val layoutName = LinearLayout(context)
        layoutName.orientation = LinearLayout.VERTICAL
        layoutName.addView(hostText)
        layoutName.addView(input) // displays the user input bar
        layoutName.addView(keyAccessStr)
        layoutName.addView(input2)
        layoutName.addView(secretKey)
        layoutName.addView(input3)
        builder.setView(layoutName)
        builder.setPositiveButton("OK",
            DialogInterface.OnClickListener{
                    _, i ->
                viewmodel.setHost(input.text.toString())
                viewmodel.setAKey(input2.text.toString())
                viewmodel.setSKey(input3.text.toString())

            }
        )
        builder.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, which -> dialog.cancel() })

        builder.show()
    }
    private fun startRecording() {
        mediaRecorder = MediaRecorder()
        mediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
        mediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        var fileName  = requireContext().filesDir.toString() + "youraudiofile.amr"
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

