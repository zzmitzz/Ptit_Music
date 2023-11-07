package com.example.musicapp.Fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activity.MusicianPlaylistActivity;
import com.example.musicapp.Class.Book;
import com.example.musicapp.Class.Category;
import com.example.musicapp.Class.Item;
import com.example.musicapp.Class.Music;
import com.example.musicapp.Adapter.MusicAdapter;
import com.example.musicapp.Activity.PlayMusicActivity;
import com.example.musicapp.Class.NlpUtils;
import com.example.musicapp.Data.LibraryData;
import com.example.musicapp.Data.MusicData;
import com.example.musicapp.DataBase.ItemDataBase;
import com.example.musicapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Fragment chứa chức năng tìm kiếm âm nhạc
public class SearchFragment extends Fragment{
    private static List<Music> arrayMusic;// Danh sách âm nhạc
    private Intent it;// Intent để mở trang chơi âm nhạc
    private static MusicAdapter adapter;// Adapter để quản lý danh sách âm nhạc
    private static SearchView searchView;// Thành phần tìm kiếm
    private RecyclerView rcvMusic;

    private static Context context;
    private EditText edtSearch;

    // Phương thức getter để lấy đối tượng SearchView
    public static SearchView getSearchView() {
        return searchView;
    }

    // Phương thức getter để lấy danh sách âm nhạc

    public static void setAdapter(List<Music>musicList) {
        if(adapter != null){
            adapter.setData(context, musicList);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Gắn layout fragment_search.xml vào fragment
        View view = inflater.inflate(R.layout.fragment_search,container,false);
        context = getActivity();
        AnhXa(view);// Ánh xạ các thành phần trong layout
        arrayMusic = MusicData.getArrayMusic();
        adapter = new MusicAdapter(new MusicAdapter.ClickIcon() {
            @Override
            public void updateIcon(Music music) {
                clickUpdateIcon(music);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
        rcvMusic.setLayoutManager(linearLayoutManager);
        adapter.setData(getActivity(),arrayMusic);
        rcvMusic.setAdapter(adapter);

        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    handleSearchMusic();
                }
                return false;
            }
        });

        return view;// Trả về giao diện fragment đã được tạo
    }

    private void clickUpdateIcon(Music music) {
        if (music.isLove()){
            music.setLove(false);
            Item item = ItemDataBase.getInstance(getActivity()).itemDao().find(music.getId()).get(0);
            item.setChosen(false);
            ItemDataBase.getInstance(getActivity()).itemDao().updateItem(item);
            setAdapter(music);
            Toast.makeText(getContext(),"Đã xóa khỏi mục yêu thích",Toast.LENGTH_SHORT).show();
        }
        else{
            music.setLove(true);
            Item item = ItemDataBase.getInstance(getActivity()).itemDao().find(music.getId()).get(0);
            item.setChosen(true);
            ItemDataBase.getInstance(getActivity()).itemDao().updateItem(item);
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
        categories.add(new Category("Bài hát yêu thích",musicList,"library"));
        categories.add(new Category("Lịch sử phát",historyList,"library"));
        LibraryFragment.setAdapter(categories);
    }
    private void handleSearchMusic() {
        String strKeyWord = edtSearch.getText().toString().trim();
        List<Music> musicList = new ArrayList<>();
        for(Music music : MusicData.getArrayMusic()){
            if( NlpUtils.removeAccent(music.getTenNhac()).toLowerCase().contains(NlpUtils.removeAccent(strKeyWord).toLowerCase())
               || NlpUtils.removeAccent(music.getCaSi()).toLowerCase().contains(NlpUtils.removeAccent(strKeyWord).toLowerCase())){
                musicList.add(music);
            }
        }
        adapter.setData(getActivity(),musicList);
        hideSoftKeyBoard();
    }

    // Phương thức để mở trang chơi âm nhạc
    private void openPlayPage(int i){
        PlayMusicFragment.setArrayMusic(arrayMusic);
        it = new Intent(getActivity(),PlayMusicActivity.class);
        it.putExtra("position",i + "");// Truyền vị trí âm nhạc được chọn cho trang chơi âm nhạc
        startActivity(it);
    }
    private void hideSoftKeyBoard(){
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
        }
        catch (NullPointerException ex){
            ex.printStackTrace();
        }
    }
    // Phương thức để ánh xạ các thành phần trong layout
    private void AnhXa(View view) {
        rcvMusic = view.findViewById(R.id.musicList);
        edtSearch = view.findViewById(R.id.search_box);
    }
}
