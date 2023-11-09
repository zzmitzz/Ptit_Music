package com.example.musicapp.Activity;
//Giao diện chứa các bài nhạc của từng ca sỹ
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.Adapter.MusicAdapter;
import com.example.musicapp.Class.Book;
import com.example.musicapp.Class.Category;
import com.example.musicapp.Class.Item;
import com.example.musicapp.Class.Music;
import com.example.musicapp.Data.LibraryData;
import com.example.musicapp.Data.MusicData;
import com.example.musicapp.Class.Musician;
import com.example.musicapp.Data.MusicianData;
import com.example.musicapp.DataBase.ItemDataBase;
import com.example.musicapp.Fragment.LibraryFragment;
import com.example.musicapp.Fragment.SearchFragment;
import com.example.musicapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MusicianPlaylistActivity extends AppCompatActivity{
    // Khai báo các biến và thành phần giao diện
    private ImageView headerImg;
    private TextView name;
    private RecyclerView rcvMusic;
    private static List<Music> arrayMusic;
    private static MusicAdapter adapter;
    private static Context context;
    private ImageButton btnPlay,backBtn,favBtn;
    public static Context getContext(){
        return context;
    }
    public static List<Music> getArrayMusic() {
        return arrayMusic;
    }
    public static void setArrayMusic(List<Music>newArrayMusic){
        arrayMusic = newArrayMusic;
    }
    public static void setAdapter(List<Music>list){
        if (adapter != null ){
            adapter.setData(context,list);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musician_playlist_activity);
        context = this;
        // Ánh xạ các thành phần giao diện
        AnhXa();

        Intent it = getIntent();

        String id = it.getStringExtra("musician");
        String musicianName = it.getStringExtra("musician");

        Musician musician = MusicianData.getMusicianHashMap().get(id);
        arrayMusic = MusicData.musicianList(musicianName);
        Collections.sort(arrayMusic);
        headerImg.setImageResource(musician.getImageBg());
        headerImg.setSelected(true);
        name.setText(musician.getName());
        setFavButton(musician);


        adapter = new MusicAdapter(new MusicAdapter.ClickIcon() {
            @Override
            public void updateIcon(Music music) {
                clickUpdateIcon(music);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        rcvMusic.setLayoutManager(linearLayoutManager);
        adapter.setData(this,arrayMusic);
        rcvMusic.setAdapter(adapter);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrayMusic.size() != 0){
                    PlayMusicActivity.setArrayMusic(arrayMusic);
                    Intent intent = new Intent(MusicianPlaylistActivity.this,PlayMusicActivity.class);
                    intent.putExtra("position",0 + "");
                    startActivity(intent);
                }
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFavButton(musician);
            }
        });
    }

    private void setFavButton(Musician musician) {
        if(musician.isLove()){
            favBtn.setImageResource(R.drawable.ic_fixed_favorite);
        }
        else{
            favBtn.setImageResource(R.drawable.ic_fixed_favorite_border);
        }
    }
    private void clickFavButton(Musician musician){
        if(musician.isLove()){
            musician.setLove(false);
            Item item = ItemDataBase.getInstance(this).itemDao().find(musician.getName()).get(0);
            item.setChosen(false);
            ItemDataBase.getInstance(this).itemDao().updateItem(item);
            favBtn.setImageResource(R.drawable.ic_fixed_favorite_border);
            setLibAdapter();
            Toast.makeText(getContext(),"Đã xóa khỏi mục ca sĩ yêu thích",Toast.LENGTH_SHORT).show();
        }
        else{
            musician.setLove(true);
            Item item = ItemDataBase.getInstance(this).itemDao().find(musician.getName()).get(0);
            item.setChosen(true);
            ItemDataBase.getInstance(this).itemDao().updateItem(item);
            favBtn.setImageResource(R.drawable.ic_fixed_favorite);
            setLibAdapter();
            Toast.makeText(getContext(),"Đã thêm vào mục ca sĩ yêu thích",Toast.LENGTH_SHORT).show();
        }
    }

    private void setLibAdapter() {
        List<Book> musicianList = LibraryData.getMusicianData();
        List<Book>musicList = LibraryData.getFavlist();
        List<Book>historyList = LibraryData.getHisList();
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Ca sĩ yêu thích",musicianList,"library"));
        categories.add(new Category("Bài hát yêu thích",musicList,"library"));
        categories.add(new Category("Lịch sử phát",historyList,"library"));
        LibraryFragment.setAdapter(categories);
    }

    private void clickUpdateIcon(Music music) {
        if (music.isLove()){
            music.setLove(false);
            Item item = ItemDataBase.getInstance(this).itemDao().find(music.getId()).get(0);
            item.setChosen(false);
            ItemDataBase.getInstance(this).itemDao().updateItem(item);
            setAdapter(music);
            Toast.makeText(getContext(),"Đã xóa khỏi mục yêu thích",Toast.LENGTH_SHORT).show();
        }
        else{
            music.setLove(true);
            Item item = ItemDataBase.getInstance(this).itemDao().find(music.getId()).get(0);
            item.setChosen(true);
            ItemDataBase.getInstance(this).itemDao().updateItem(item);
            setAdapter(music);
            Toast.makeText(getContext(),"Đã thêm vào mục yêu thích",Toast.LENGTH_SHORT).show();
        }
    }
    private void setAdapter(Music music) {
        SearchFragment.setAdapter(MusicData.getArrayMusic());
        MusicianPlaylistActivity.setAdapter(MusicData.musicianList(music.getCaSi()));
        List<Book> musicianList = LibraryData.getMusicianData();
        List<Book>musicList = LibraryData.getFavlist();
        List<Book>historyList = LibraryData.getHisList();
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Ca sĩ yêu thích",musicianList,"library"));
        categories.add(new Category("Bài hát Yêu thích",musicList,"library"));
        categories.add(new Category("Lịch sử phát",historyList,"library"));
        LibraryFragment.setAdapter(categories);
    }
    // Ánh xạ các thành phần giao diện
    private void AnhXa() {
        headerImg = findViewById(R.id.mc_img);
        rcvMusic = findViewById(R.id.play_list);
        name = findViewById(R.id.text_name);
        btnPlay = findViewById(R.id.btn_mc_play);
        backBtn = findViewById(R.id.plalist_back);
        favBtn = findViewById(R.id.fav_musician);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}