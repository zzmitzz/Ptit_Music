package com.example.musicapp.Activity;

import static com.example.musicapp.Application.MyApplication.ACTION_NEXT;
import static com.example.musicapp.Application.MyApplication.ACTION_PLAY;
import static com.example.musicapp.Application.MyApplication.ACTION_PREV;
import static com.example.musicapp.Application.MyApplication.CHANNEL_ID_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.Adapter.LyricsAdapter;
import com.example.musicapp.Class.Book;
import com.example.musicapp.Class.Category;
import com.example.musicapp.Class.Item;
import com.example.musicapp.Class.Music;
import com.example.musicapp.Data.LibraryData;
import com.example.musicapp.Data.MusicData;
import com.example.musicapp.DataBase.HistoryDataBase;
import com.example.musicapp.DataBase.ItemDataBase;
import com.example.musicapp.Fragment.LibraryFragment;
import com.example.musicapp.Fragment.SearchFragment;
import com.example.musicapp.Function.ActionPlaying;
import com.example.musicapp.Function.NotificationReceiver;
import com.example.musicapp.R;
import com.example.musicapp.Service.MusicService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMusicActivity extends AppCompatActivity implements ServiceConnection, ActionPlaying {
    private ConstraintLayout playMusicLayout;
    private TextView musicName, runTime, totalTime;
    private CircleImageView musicImage;
    private SeekBar seekBar;
    private ImageButton playPauseButton, preButton, nextButton, replayButton, favBtn, shuffleBtn, btnOpenLyric, backBtn;
    private MediaPlayer mediaPlayer;
    private Animation animation;
    private static List<Music> arrayMusic;
    private int position;
    private boolean replay = false;
    private boolean isServiceBound = false;
    private GestureDetector gestureDetector;
    private MusicService musicService;
    private MediaSessionCompat mediaSession;
    private Dialog dialog;
    private int SWIPE_THRESHOLD = 300;
    private int SWIPE_VELOCITY_THRESHOLD = 100;

    // Phương thức để thiết lập danh sách nhạc
    public static void setArrayMusic(List<Music> arrayMusic) {
        PlayMusicActivity.arrayMusic = arrayMusic;
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder) service;
        musicService = binder.getService();
        musicService.setCallBack(PlayMusicActivity.this);
        isServiceBound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService = null;
        isServiceBound = false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isServiceBound) {
            unbindService(this);
            isServiceBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        if (isServiceBound) {
            unbindService(this);
            isServiceBound = false;
        }
        super.onDestroy();
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        AnhXa();
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
        mediaSession = new MediaSessionCompat(this,"PlayerAudio");
        setAnimation();
        getData();
        setMusicPlayImage();
        setMusicName();
        khoiTaoMediaPlayer();
        setTimeTotal();
        setFavButton();

        // Xử lý sự kiện khi nút Play/Pause được nhấn
        playPauseButton.setOnClickListener(view -> playPauseButtonClick());

        // Xử lý sự kiện khi nút Next được nhấn
        nextButton.setOnClickListener(view -> nextSong());

        // Xử lý sự kiện khi nút Previous được nhấn
        preButton.setOnClickListener(view -> preSong());

        // Xử lý sự kiện khi người dùng thay đổi vị trí trên SeekBar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Xử lý khi người dùng thay đổi vị trí SeekBar
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Xử lý khi người dùng bắt đầu chạm vào SeekBar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Xử lý khi người dùng kết thúc chạm vào SeekBar
                mediaPlayer.seekTo(seekBar.getProgress());
                if (!mediaPlayer.isPlaying()) {
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    musicImage.startAnimation(animation);
                    updateRunTime();
                    addHistory();
                    mediaPlayer.start();
                    showNotification(R.drawable.ic_pause);
                }
            }
        });

        // Xử lý sự kiện khi nút Replay được nhấn
        replayButton.setOnClickListener(view -> {
            if (replay) {
                replayButton.setImageResource(R.drawable.ic_replay_1);
                replay = false;
            } else {
                replayButton.setImageResource(R.drawable.ic_replay_2);
                replay = true;
            }
        });
        favBtn.setOnClickListener(v -> favBtnClick());

        shuffleBtn.setOnClickListener(v -> shuffleMusic());

        btnOpenLyric.setOnClickListener(v -> {
            try {
                openLyricPage();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        backBtn.setOnClickListener(v -> onBackPressed());

        // Thiết lập Gesture Detector để nhận diện cử chỉ vuốt trên màn hình
        gestureDetector = new GestureDetector(this, new MyGesture());
        playMusicLayout.setOnTouchListener((view, motionEvent) -> {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        });
    }
    public void showNotification(int icon){
        Intent prevIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PREV);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(this,0,prevIntent,PendingIntent.FLAG_IMMUTABLE);
        Intent playIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(this,0,playIntent,PendingIntent.FLAG_IMMUTABLE);
        Intent nextIntent = new Intent(this, NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(this,0,nextIntent,PendingIntent.FLAG_IMMUTABLE);
        Bitmap picture = BitmapFactory.decodeResource(getResources(),arrayMusic.get(position).getHinhNen());

        Notification notification = new NotificationCompat.Builder(this,CHANNEL_ID_2)
                .setSmallIcon(R.drawable.ic_music_note)
                .setLargeIcon(picture)
                .setContentTitle(arrayMusic.get(position).getTenNhac())
                .setContentText(arrayMusic.get(position).getCaSi())
                .addAction(R.drawable.ic_pre,"Previous",prevPendingIntent)
                .addAction(icon,"Play",playPendingIntent)
                .addAction(R.drawable.ic_next,"Next",nextPendingIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0,1,2)
                        .setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(null)
                .setOnlyAlertOnce(true)
                .setContentIntent(null)
                .build();
        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putString(MediaMetadata.METADATA_KEY_TITLE,arrayMusic.get(position).getTenNhac())
                .putString(MediaMetadata.METADATA_KEY_ARTIST,arrayMusic.get(position).getCaSi())
                .build());
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);

    }

    @Override
    public void playPauseButtonClick() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            animation.cancel();
            musicImage.setAnimation(null);
            playPauseButton.setImageResource(R.drawable.ic_play);
            showNotification(R.drawable.ic_play);
        } else {
            mediaPlayer.start();
            musicImage.startAnimation(animation);
            playPauseButton.setImageResource(R.drawable.ic_pause);
            showNotification(R.drawable.ic_pause);
            addHistory();
        }
        updateRunTime();
    }

    private void openLyricPage() throws IOException {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_lyric);
        LyricsAdapter lyricsAdapter = new LyricsAdapter(this,arrayMusic.get(position).getFileSource());
        RecyclerView rcvLyric = dialog.findViewById(R.id.lyrics_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rcvLyric.setLayoutManager(linearLayoutManager);
        rcvLyric.setAdapter(lyricsAdapter);
        dialog.show();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,displayMetrics.heightPixels*7/10);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
    private void shuffleMusic() {
        List<Music> resultList = new ArrayList<>();
        List<Music> tmpList = new ArrayList<>();
        for (int i = 0; i < arrayMusic.size(); i++){
            if(i != position){
                tmpList.add(arrayMusic.get(i));
            }
        }
        Collections.shuffle(tmpList);
        for (int i = 0 ; i < arrayMusic.size(); i++){
            if(i== position){
                resultList.add(arrayMusic.get(position));
            }
            if(i < tmpList.size()){
                resultList.add(tmpList.get(i));
            }
        }
        arrayMusic = resultList;
        Toast.makeText(this,"Đã trộn nhạc",Toast.LENGTH_SHORT).show();
    }


    private void addHistory() {
        Music music = arrayMusic.get(position);
        Book book = new Book(music.getId(),"hisMusic",music.getHinhNen(), music.getTenNhac(),System.currentTimeMillis());
        if(checkContains(book)) {
            HistoryDataBase.getInstance(this).historyDao().deleteBook(book);
        }
        HistoryDataBase.getInstance(this).historyDao().insertHistory(book);
        List<Book> list = HistoryDataBase.getInstance(this).historyDao().getBookArray();
        if (list.size() > 10){
            HistoryDataBase.getInstance(this).historyDao().deleteBook(list.get(list.size()-1));
        }
        setAdapter();
    }
    private boolean checkContains(Book book){
        List<Book> list = HistoryDataBase.getInstance(this).historyDao().checkExist(book.getId());
        return list != null && !list.isEmpty();
    }

    private void setFavButton() {
        Music music = arrayMusic.get(position);
        if (music.isLove()){
            favBtn.setImageResource(R.drawable.ic_favorite);
        }
        else{
            favBtn.setImageResource(R.drawable.ic_favorite_border);
        }
    }

    private void favBtnClick() {
        Music music = arrayMusic.get(position);
        if (music.isLove()){
            music.setLove(false);
            Item item = ItemDataBase.getInstance(this).itemDao().find(music.getId()).get(0);
            item.setChosen(false);
            ItemDataBase.getInstance(this).itemDao().updateItem(item);
            favBtn.setImageResource(R.drawable.ic_favorite_border);
            setAdapter();
            Toast.makeText(this,"Đã xóa khỏi mục yêu thích",Toast.LENGTH_SHORT).show();
        }
        else{
            music.setLove(true);
            Item item = ItemDataBase.getInstance(this).itemDao().find(music.getId()).get(0);
            item.setChosen(true);
            ItemDataBase.getInstance(this).itemDao().updateItem(item);
            favBtn.setImageResource(R.drawable.ic_favorite);
            setAdapter();
            Toast.makeText(this,"Đã thêm vào mục yêu thích",Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter() {
        SearchFragment.setAdapter(MusicData.getArrayMusic());
        MusicianPlaylistActivity.setAdapter(MusicData.musicianList(arrayMusic.get(position).getCaSi()));
        List<Book> musicianList = LibraryData.getMusicianData();
        List<Book>musicList = LibraryData.getFavlist();
        List<Book>historyList = LibraryData.getHisList();
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Ca sĩ yêu thích",musicianList,"library"));
        categories.add(new Category("Bài hát yêu thích",musicList,"library"));
        categories.add(new Category("Lịch sử phát",historyList,"library"));
        LibraryFragment.setAdapter(categories);
    }

    // Phương thức để chuyển đến bài hát kế tiếp trong danh sách
    @Override
    public void nextSong(){
        position++;
        if (position >= arrayMusic.size()){
            position = 0;
        }

        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
        khoiTaoMediaPlayer();
        setMusicPlayImage();
        setMusicName();
        setTimeTotal();
        updateRunTime();
        setFavButton();
        addHistory();
        musicImage.startAnimation(animation);
        playPauseButton.setImageResource(R.drawable.ic_pause);
        mediaPlayer.start();
        showNotification(R.drawable.ic_pause);
        if(dialog != null){
            dialog.cancel();
        }
    }

    // Phương thức để chuyển đến bài hát trước đó trong danh sách
    @Override
    public void preSong(){
        position--;
        if(position < 0){
            position = arrayMusic.size()-1;
        }

        if ((mediaPlayer.isPlaying())){
            mediaPlayer.stop();
        }
        khoiTaoMediaPlayer();
        setMusicPlayImage();
        setMusicName();
        setTimeTotal();
        updateRunTime();
        setFavButton();
        addHistory();
        musicImage.startAnimation(animation);
        playPauseButton.setImageResource(R.drawable.ic_pause);
        mediaPlayer.start();
        showNotification(R.drawable.ic_pause);
        if(dialog != null){
            dialog.cancel();
        }
    }

    // Phương thức để thiết lập rotate animation
    private void setAnimation() {
        animation = AnimationUtils.loadAnimation(this,R.anim.rotate);
    }

    // Phương thức để cập nhật thời gian hiện tại của bài hát đang phát
    private void updateRunTime(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat running = new SimpleDateFormat("mm:ss");
                runTime.setText(running.format(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // Xử lý sự kiện khi bài hát kết thúc
                        if (!replay){
                            position++;
                            if (position >= arrayMusic.size()){
                                position = 0;
                            }

                            if(mediaPlayer.isPlaying()){
                                mediaPlayer.stop();
                            }
                        }
                        setMusicPlayImage();
                        setMusicName();
                        khoiTaoMediaPlayer();
                        setTimeTotal();
                        updateRunTime();
                        playPauseButton.setImageResource(R.drawable.ic_pause);
                        mediaPlayer.start();
                    }
                });
                handler.postDelayed(this::run,500);
            }
        },100);
    }
    private void setTimeTotal(){
        // Phương thức để thiết lập thời gian tổng của bài hát
        SimpleDateFormat total = new SimpleDateFormat("mm:ss");
        totalTime.setText(total.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }
    private void khoiTaoMediaPlayer(){
        mediaPlayer = MediaPlayer.create(this,arrayMusic.get(position).getSourceMp3());
        // Khởi tạo MediaPlayer để phát nhạc từ tệp âm thanh tại vị trí hiện tại trong danh sách nhạc
        runTime.setText("00:00");
    }
    private void setMusicName() {
        if(!arrayMusic.get(position).getTenNhac().startsWith(arrayMusic.get(position).getCaSi())){
            musicName.setText( arrayMusic.get(position).getCaSi() + "  -  " + arrayMusic.get(position).getTenNhac());
        }
        else{
            musicName.setText(arrayMusic.get(position).getTenNhac());
        }
        musicName.setSelected(true);
    }

    private void setMusicPlayImage() {
        // Đặt hình ảnh của bài hát hiện tại lên giao diện
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),arrayMusic.get(position).getHinhNen());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int sumRed = 0;
        int sumGreen = 0;
        int sumBlue = 0;
        for (int pixel : pixels) {
            sumRed += Color.red(pixel);
            sumGreen += Color.green(pixel);
            sumBlue += Color.blue(pixel);
        }
        int averageRed = sumRed / pixels.length;
        int averageGreen = sumGreen / pixels.length;
        int averageBlue = sumBlue / pixels.length;

        int dominantColor = Color.rgb(averageRed, averageGreen, averageBlue);
        int referenceColor = Color.parseColor("#CCCCCC");

        float[] hsvA = new float[3];
        float[] hsvB = new float[3];
        Color.colorToHSV(dominantColor , hsvA);
        Color.colorToHSV(referenceColor, hsvB);

        if(hsvA[2] > 0.75 * hsvB[2]){
            float[] hsv = new float[3];
            Color.colorToHSV(dominantColor, hsv);
            hsv[2] *= 0.5;
            dominantColor = Color.HSVToColor(hsv);
        }
        playMusicLayout.setBackgroundColor(dominantColor);
        musicImage.setImageResource(arrayMusic.get(position).getHinhNen());
    }

    private void getData() {
        // Lấy dữ liệu vị trí bài hát được chọn từ Intent
        Intent it = getIntent();
        position = Integer.parseInt(it.getStringExtra("position"));
    }
    private void AnhXa() {
        // Ánh xạ các thành phần giao diện
        playMusicLayout = findViewById(R.id.layout_play_music);
        musicName = findViewById(R.id.headMusicName);
        runTime = findViewById(R.id.runTime);
        totalTime = findViewById(R.id.totalTime);
        musicImage = findViewById(R.id.musicPlayImage);
        seekBar = findViewById(R.id.seekBar);
        playPauseButton = findViewById(R.id.playPauseButton);
        preButton = findViewById(R.id.preButton);
        nextButton = findViewById(R.id.nextButton);
        replayButton = findViewById(R.id.replay_button);
        favBtn = findViewById(R.id.like_button);
        shuffleBtn = findViewById(R.id.shuffle_button);
        btnOpenLyric = findViewById(R.id.expand_lyrics);
        backBtn = findViewById(R.id.down_arrow);
    }

    class MyGesture extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {

            // Xử lý cử chỉ vuốt trái hoặc phải trên màn hình
            if(e2.getX() - e1.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                nextSong();// Chuyển đến bài hát tiếp theo khi vuốt sang phải
            }
            if(e1.getX() - e2.getX() > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD){
                preSong();// Chuyển đến bài hát trước đó khi vuốt sang trái
            }
            if(e1.getY() - e2.getY() > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD){
                try {
                    openLyricPage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    public MediaPlayer getMediaPlayer(){
        return this.mediaPlayer;
    }
}