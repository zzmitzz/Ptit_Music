package com.example.musicapp.Data;

import com.example.musicapp.Activity.FlashActivity;
import com.example.musicapp.Activity.MainActivity;
import com.example.musicapp.Class.Book;
import com.example.musicapp.Class.Item;
import com.example.musicapp.Class.Music;
import com.example.musicapp.Class.NlpUtils;
import com.example.musicapp.DataBase.ItemDataBase;
import com.example.musicapp.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MusicData {
    private static List<Music> arrayMusic;
    private static HashMap<String,Music> musicMap;

    // Phương thức để lấy danh sách các bản nhạc
    public static void setArrayMusic() {
        arrayMusic = new ArrayList<>();
        musicMap = new HashMap<>();
        arrayMusic.add(new Music("ms01","All For Love", "Tungevaag & Raaban", R.drawable.ms01_all_for_love, R.raw.ms01_all_for_love, "ms01_all_for_love.txt"));
        arrayMusic.add(new Music("ms02","Sơn Tùng M-TP - Âm thầm bên em","Sơn Tùng M-TP",R.drawable.ms02_am_tham_ben_em,R.raw.ms02_am_tham_ben_em,"ms02_am_tham_ben_em.txt"));
        arrayMusic.add(new Music("ms03","Arcade", "Duncan Laurence", R.drawable.ms03_arcade, R.raw.ms03_arcade, "ms03_arcade.txt"));
        arrayMusic.add(new Music("ms04","Imagine Dragons - Bad Liar","Imagine Dragons",R.drawable.ms04_bad_liar,R.raw.ms04_bad_liar,"ms04_bad_liar.txt"));
        arrayMusic.add(new Music("ms05","Imagine Dragons - Believer","Imagine Dragons",R.drawable.ms05_believer,R.raw.ms05_believer,"ms05_believer.txt"));
        arrayMusic.add(new Music("ms06","Imagine Dragons - Bones","Imagine Dragons",R.drawable.ms06_bones,R.raw.ms06_bones,"ms06_bones.txt"));
        arrayMusic.add(new Music("ms07","Sơn Tùng M-TP - Có chắc yêu là đây","Sơn Tùng M-TP",R.drawable.ms07_co_chac_yeu_la_day,R.raw.ms07_co_chac_yeu_la_day,"ms07_co_chac_yeu_la_day.txt"));
        arrayMusic.add(new Music("ms08","Imagine Dragons - Demons","Imagine Dragons",R.drawable.ms08_demons,R.raw.ms08_demons,"ms08_demons.txt"));
        arrayMusic.add(new Music("ms09","Dù Cho Mai Về Sau", "Buitruonglinh", R.drawable.ms09_duchomaivesau, R.raw.ms09_duchomaivesau, "ms09_duchomaivesau.txt"));
        arrayMusic.add(new Music("ms10","Imagine Dragons - Enemy", "Imagine Dragons", R.drawable.ms10_enemy_arcane,R.raw.ms10_enemy_arcane,"ms10_enemy_arcane.txt"));
        arrayMusic.add(new Music("ms11","Sơn Tùng M-TP - Hãy trao cho anh","Sơn Tùng M-TP",R.drawable.ms11_hay_trao_cho_anh,R.raw.ms11_hay_trao_cho_anh,"ms11_hay_trao_cho_anh.txt"));
        arrayMusic.add(new Music("ms13","Coldplay - Hymn For The Weekend", "Bely Basarte", R.drawable.ms13_hymnfortheweekend, R.raw.ms13_hymn_for_the_weekend, "ms13_hymn_for_the_weekend.txt"));
        arrayMusic.add(new Music("ms14","Legends Never Die", "Against The Current and Mako", R.drawable.ms14_legend_never_die, R.raw.ms14_legend_never_die, "ms14_legend_never_die.txt"));
        arrayMusic.add(new Music("ms15","Imagine Dragons - Natural","Imagine Dragons",R.drawable.ms15_natural,R.raw.ms15_natural,"ms15_natural.txt"));
        arrayMusic.add(new Music("ms16","Sơn Tùng M-TP - Nơi này có anh", "Sơn Tùng M-TP", R.drawable.ms16_noi_nay_co_anh, R.raw.ms16_noi_nay_co_anh, "ms16_noi_nay_co_anh.txt"));
        arrayMusic.add(new Music("ms17","Imagine Dragons - Radioactive","Imagine Dragons",R.drawable.ms17_radioactive,R.raw.ms17_radioactive,"ms17_radioactive.txt"));
        arrayMusic.add(new Music("ms18","Seasons", "Rival, Cadmium, Harley Bird", R.drawable.ms18_seasons, R.raw.ms18_seasons, "ms18_seasons.txt"));
        arrayMusic.add(new Music("ms19","Imagine Dragons - Thunder","Imagine Dragons",R.drawable.ms19_thunder,R.raw.ms19_thunder,"ms19_thunder.txt"));
        arrayMusic.add(new Music("ms20","Waiting For Love", "Avicii", R.drawable.ms20_waittingforlove, R.raw.ms20_waittingforlove, "ms20_waitingfor_love.txt"));
        arrayMusic.add(new Music("ms21","Imagine Dragons - Warriors","Imagine Dragons",R.drawable.ms21_warriors,R.raw.ms21_warriors,"ms21_warriors.txt"));
        arrayMusic.add(new Music("ms22","Imagine Dragons - Whatever It Takes","Imagine Dragons",R.drawable.ms22_whatever_it_takes,R.raw.ms22_whatever_it_takes,"ms22_whatever_it_take.txt"));
        Collections.sort(arrayMusic);



        for(Music music : arrayMusic){
            music.setLove(false);
            musicMap.put(music.getId(),music);
            if(!checkExist(music.getId())){
                ItemDataBase.getInstance(FlashActivity.getContext()).itemDao().insertItem(new Item(music.getId(), false));
            }
        }
        List<Item>list = ItemDataBase.getInstance(FlashActivity.getContext()).itemDao().getList();
        for(Item item : list){
            if(musicMap.containsKey(item.getId())){
                Music music = musicMap.get(item.getId());
                music.setLove(item.isChosen());
            }
        }
    }
    public static boolean checkExist(String id){
        List<Item> list = ItemDataBase.getInstance(FlashActivity.getContext()).itemDao().find(id);
        return list != null && !list.isEmpty();
    }


    public static List<Music> getArrayMusic() {
        return arrayMusic;
    }
    public static HashMap<String, Music> getMusicMap() {
        return musicMap;
    }
    public static List<Music> musicianList(String musicianName){
        List<Music>list = new ArrayList<>();
        for(Music music : arrayMusic){
            if(NlpUtils.removeAccent(music.getCaSi()).compareToIgnoreCase(NlpUtils.removeAccent(musicianName)) == 0){
                list.add(music);
            }
        }
        return list;
    }
    public static List<Music> getMusicList(List<Book>books){
        List<Music>resultList = new ArrayList<>();
        for (Music music : arrayMusic){
            for(Book book: books){
                if(book.getId() == music.getId()){
                    resultList.add(music);
                }
            }
        }
        return resultList;
    }
    public static int getPosition(String id,List<Music>arrayList){
        int res = 0;
        for(int i = 0; i < arrayList.size(); i++){
            if (arrayList.get(i).getId().equals(id)){
                res = i;
                break;
            }
        }
        return res;
    }
}
