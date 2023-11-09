package com.example.musicapp.Activity;

import static com.example.musicapp.Application.MyApplication.CHANNEL_ID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.media.session.MediaSessionCompat;
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
import com.example.musicapp.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMusicActivity extends AppCompatActivity {
    private ConstraintLayout playMusicLayout;
    private TextView musicName, runTime, totalTime, txtLyric;
    private CircleImageView musicImage;
    private SeekBar seekBar;
    private ImageButton playPauseButton, preButton, nextButton, replayButton, favBtn, shuffleBtn, btnOpenLyric, backBtn;
    private MediaPlayer mediaPlayer;
    private Animation animation;
    private static List<Music> arrayMusic;
    private int position;
    private boolean replay = false;
    private GestureDetector gestureDetector;
    private NotificationCompat.Builder notificationBuilder;
    private PendingIntent pendingPreviousIntent, pendingPlayPauseIntent,pendingNextIntent;
    private NotificationManager notificationManager;
    private int SWIPE_THRESHOLD = 300;
    private int SWIPE_VELOCITY_THRESHOLD = 100;

    // Phương thức để thiết lập danh sách nhạc
    public static void setArrayMusic(List<Music> arrayMusic) {
        PlayMusicActivity.arrayMusic = arrayMusic;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        AnhXa();
        setNotification();
        setAnimation();
        getData();
        setMusicPlayImage();
        setMusicName();
        khoiTaoMediaPlayer();
        setTimeTotal();
        setFavButton();
        sendNotificationMedia();

        // Xử lý sự kiện khi nút Play/Pause được nhấn
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPauseButtonClick();
            }
        });

        // Xử lý sự kiện khi nút Next được nhấn
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextSong();
            }
        });

        // Xử lý sự kiện khi nút Previous được nhấn
        preButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preSong();
            }
        });

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
                    updateNotification(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });

        // Xử lý sự kiện khi nút Replay được nhấn
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (replay) {
                    replayButton.setImageResource(R.drawable.ic_replay_1);
                    replay = false;
                } else {
                    replayButton.setImageResource(R.drawable.ic_replay_2);
                    replay = true;
                }
            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favBtnClick();
            }
        });
        shuffleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shuffleMusic();
            }
        });
        btnOpenLyric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openLyricPage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Thiết lập Gesture Detector để nhận diện cử chỉ vuốt trên màn hình
        gestureDetector = new GestureDetector(this, new MyGesture());
        playMusicLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
    }

    private void setNotification() {
        PlayMusicActivity.MyReceiver receiver = new PlayMusicActivity.MyReceiver(); //this save my entire life
        IntentFilter filter = new IntentFilter();
        filter.addAction("PreviousButtonClicked");
        filter.addAction("PlayPauseButtonClicked");
        filter.addAction("NextButtonClicked");
        registerReceiver(receiver, filter);
        Intent previousIntent = new Intent("PreviousButtonClicked");
        pendingPreviousIntent = PendingIntent.getBroadcast(this, 0, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent playPauseIntent = new Intent("PlayPauseButtonClicked");
        pendingPlayPauseIntent = PendingIntent.getBroadcast(this, 0, playPauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Intent nextIntent = new Intent("NextButtonClicked");
        pendingNextIntent = PendingIntent.getBroadcast(this, 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
    }

    private void playPauseButtonClick() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            animation.cancel();
            musicImage.setAnimation(null);
            playPauseButton.setImageResource(R.drawable.ic_play);
            updateNotification(R.drawable.ic_play);
        } else {
            mediaPlayer.start();
            musicImage.startAnimation(animation);
            playPauseButton.setImageResource(R.drawable.ic_pause);
            updateNotification(R.drawable.ic_pause);
            addHistory();
        }
        updateRunTime();
    }
    private void updateNotification(int icon) {
        String tenNhac = arrayMusic.get(position).getTenNhac();
        String caSi = arrayMusic.get(position).getCaSi();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), arrayMusic.get(position).getHinhNen());

        notificationBuilder.setContentTitle(tenNhac)
                .setContentText(caSi)
                .setLargeIcon(bitmap)
                .clearActions()
                .addAction(R.drawable.ic_pre, "Previous", pendingPreviousIntent)
                .addAction(icon, "PlayPause", pendingPlayPauseIntent)
                .addAction(R.drawable.ic_next, "Next", pendingNextIntent);
        notificationManager.notify(1, notificationBuilder.build());
    }
    private void sendNotificationMedia() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), arrayMusic.get(position).getHinhNen());
        MediaSessionCompat mediaSession = new MediaSessionCompat(this, "tag");
        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_note)
                .setSubText("Music App")
                .setContentTitle(arrayMusic.get(position).getTenNhac())
                .setContentText(arrayMusic.get(position).getCaSi())
                .setLargeIcon(bitmap)
                .addAction(R.drawable.ic_pre, "Previous", pendingPreviousIntent)
                .addAction(R.drawable.ic_play, "PlayPause", pendingPlayPauseIntent)
                .addAction(R.drawable.ic_next, "Next", pendingNextIntent)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(1)
                        .setMediaSession(mediaSession.getSessionToken()));
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        managerCompat.notify(1, notificationBuilder.build());
    }

    private void openLyricPage() throws IOException {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_lyric);
        LyricsAdapter lyricsAdapter = new LyricsAdapter(this,arrayMusic.get(position).getFileSource());
        RecyclerView rcvLyric = dialog.findViewById(R.id.lyrics_activity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rcvLyric.setLayoutManager(linearLayoutManager);
        rcvLyric.setAdapter(lyricsAdapter);
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,1000);
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
    private void nextSong(){
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
        updateNotification(R.drawable.ic_pause);
        mediaPlayer.start();
    }

    // Phương thức để chuyển đến bài hát trước đó trong danh sách
    private void preSong(){
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
        updateNotification(R.drawable.ic_pause);
        mediaPlayer.start();
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
                        updateNotification(R.drawable.ic_pause);
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
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        notificationManager.cancel(1);
        super.onDestroy();
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
    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("PreviousButtonClicked".equals(intent.getAction())) {
                preSong();
            }
            else if ("PlayPauseButtonClicked".equals(intent.getAction())) {
                playPauseButtonClick();
            }
            else if ("NextButtonClicked".equals(intent.getAction())) {
                nextSong();
            }
        }
    }
    public MediaPlayer getMediaPlayer(){
        return this.mediaPlayer;
    }
}