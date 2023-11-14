package com.example.musicapp.Fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModel
import com.example.musicapp.R

class FindSongViewModel : ViewModel() {
    private var _host: String = ""
    private var _accessKey: String = ""
    private var _secretKey: String = ""
    val host: String
        get() = _host
    val accessKey: String
        get() = _accessKey
    val secretKey: String
        get() = _secretKey
    fun setHost(a : String){
        _host = a
    }
    fun setAKey(a : String){
        _accessKey = a
    }
    fun setSKey(a : String){
        _secretKey = a
    }
}