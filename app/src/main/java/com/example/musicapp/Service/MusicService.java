package com.example.musicapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.musicapp.Function.ActionPlaying;

public class MusicService extends Service {
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREV = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";
    ActionPlaying actionPlaying;

    private IBinder mBinder = new MyBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    public class MyBinder extends Binder{
        public MusicService getService(){
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = intent.getStringExtra("actionName");
        if(actionName != null) {
            switch (actionName) {
                case ACTION_NEXT:
                    if (actionPlaying != null) {
                        actionPlaying.nextSong();
                    }
                    break;
                case ACTION_PREV:
                    if (actionPlaying != null) {
                        actionPlaying.preSong();
                    }
                    break;
                case ACTION_PLAY:
                    if (actionPlaying != null) {
                        actionPlaying.playPauseButtonClick();
                    }
                    break;
            }
        }
        return START_STICKY;
    }
    public void setCallBack(ActionPlaying actionPlaying){
        this.actionPlaying = actionPlaying;
    }
}
