package com.example.musicapp.Fragment;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.Activity.LyricsActivity;
import com.example.musicapp.Activity.MusicianPlaylistActivity;
import com.example.musicapp.Class.Book;
import com.example.musicapp.Class.Category;
import com.example.musicapp.Class.Item;
import com.example.musicapp.Class.Music;
import com.example.musicapp.Data.LibraryData;
import com.example.musicapp.Data.MusicData;
import com.example.musicapp.DataBase.HistoryDataBase;
import com.example.musicapp.DataBase.ItemDataBase;
import com.example.musicapp.R;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMusicFragment extends Fragment {
    private ConstraintLayout playMusicLayout;
    private TextView musicName, runTime, totalTime,txtLyric;
    private CircleImageView musicImage;
    private SeekBar seekBar;
    private ImageButton playPauseButton,preButton,nextButton,replayButton,favBtn,shuffleBtn,btnOpenLyric,backBtn;
    private MediaPlayer mediaPlayer;
    private Animation animation;
    private static List<Music> arrayMusic;
    private int position;
    private boolean replay = false;
    private GestureDetector gestureDetector;
    private int SWIPE_THRESHOLD = 300;
    private int SWIPE_VELOCITY_THRESHOLD = 100;

    // Phương thức để thiết lập danh sách nhạc
    public static void setArrayMusic(List<Music> arrayMusic) {
        PlayMusicFragment.arrayMusic = arrayMusic;
    }
    public PlayMusicFragment(MediaPlayer mediaPlayer){
        this.mediaPlayer = mediaPlayer;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_play_music,container,false);
        AnhXa(view);
        setAnimation();
        getData();
        setMusicPlayImage();
        setMusicName();
        khoiTaoMediaPlayer();
        setTimeTotal();
        setFavButton();

        // Xử lý sự kiện khi nút Play/Pause được nhấn
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    animation.cancel();
                    musicImage.setAnimation(null);
                    playPauseButton.setImageResource(R.drawable.ic_play);
                }
                else{
                    mediaPlayer.start();
                    musicImage.startAnimation(animation);
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    addHistory();
                }
                updateRunTime();
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
                if(!mediaPlayer.isPlaying()){
                    playPauseButton.setImageResource(R.drawable.ic_pause);
                    musicImage.startAnimation(animation);
                    updateRunTime();
                    addHistory();
                    mediaPlayer.start();
                }
            }
        });

        // Xử lý sự kiện khi nút Replay được nhấn
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (replay){
                    replayButton.setImageResource(R.drawable.ic_replay_1);
                    replay = false;
                }
                else{
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
                openLyricPage();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        // Thiết lập Gesture Detector để nhận diện cử chỉ vuốt trên màn hình
        gestureDetector = new GestureDetector(getContext(), new MyGesture());
        playMusicLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                return true;
            }
        });
        return view;
    }
    private void openLyricPage(){
        Intent it = new Intent(getContext(), LyricsActivity.class);
        it.putExtra("nameFile",arrayMusic.get(position).getFileSource());
        startActivity(it);
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
        Toast.makeText(getActivity(),"Đã trộn nhạc",Toast.LENGTH_SHORT).show();
    }


    private void addHistory() {
        Music music = arrayMusic.get(position);
        Book book = new Book(music.getId(),"hisMusic",music.getHinhNen(), music.getTenNhac(),System.currentTimeMillis());
        if(checkContains(book)) {
            HistoryDataBase.getInstance(getContext()).historyDao().deleteBook(book);
        }
        HistoryDataBase.getInstance(getContext()).historyDao().insertHistory(book);
        List<Book> list = HistoryDataBase.getInstance(getContext()).historyDao().getBookArray();
        if (list.size() > 10){
            HistoryDataBase.getInstance(getContext()).historyDao().deleteBook(list.get(0));
        }
        setAdapter();
    }
    private boolean checkContains(Book book){
        List<Book> list = HistoryDataBase.getInstance(getContext()).historyDao().checkExist(book.getId());
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
            Item item = ItemDataBase.getInstance(getActivity()).itemDao().find(music.getId()).get(0);
            item.setChosen(false);
            ItemDataBase.getInstance(getActivity()).itemDao().updateItem(item);
            favBtn.setImageResource(R.drawable.ic_favorite_border);
            setAdapter();
            Toast.makeText(getContext(),"Đã xóa khỏi mục yêu thích",Toast.LENGTH_SHORT).show();
        }
        else{
            music.setLove(true);
            Item item = ItemDataBase.getInstance(getActivity()).itemDao().find(music.getId()).get(0);
            item.setChosen(true);
            ItemDataBase.getInstance(getActivity()).itemDao().updateItem(item);
            favBtn.setImageResource(R.drawable.ic_favorite);
            setAdapter();
            Toast.makeText(getContext(),"Đã thêm vào mục yêu thích",Toast.LENGTH_SHORT).show();
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
        mediaPlayer.start();
    }

    // Phương thức để thiết lập rotate animation
    private void setAnimation() {
        animation = AnimationUtils.loadAnimation(getContext(),R.anim.rotate);
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
        mediaPlayer = MediaPlayer.create(getContext(),arrayMusic.get(position).getSourceMp3());
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
        Intent it = getActivity().getIntent();
        position = Integer.parseInt(it.getStringExtra("position"));
    }
    private void AnhXa(View view) {
        // Ánh xạ các thành phần giao diện
        playMusicLayout = view.findViewById(R.id.layout_play_music);
        musicName = view.findViewById(R.id.headMusicName);
        runTime = view.findViewById(R.id.runTime);
        totalTime = view.findViewById(R.id.totalTime);
        musicImage = view.findViewById(R.id.musicPlayImage);
        seekBar = view.findViewById(R.id.seekBar);
        playPauseButton = view.findViewById(R.id.playPauseButton);
        preButton = view.findViewById(R.id.preButton);
        nextButton = view.findViewById(R.id.nextButton);
        replayButton = view.findViewById(R.id.replay_button);
        favBtn = view.findViewById(R.id.like_button);
        shuffleBtn = view.findViewById(R.id.shuffle_button);
        btnOpenLyric = view.findViewById(R.id.expand_lyrics);
        backBtn = view.findViewById(R.id.down_arrow);
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
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
                openLyricPage();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    public MediaPlayer getMediaPlayer(){
        return this.mediaPlayer;
    }
}