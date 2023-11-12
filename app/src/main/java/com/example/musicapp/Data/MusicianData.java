package com.example.musicapp.Data;

import com.example.musicapp.Activity.FlashActivity;
import com.example.musicapp.Class.Item;
import com.example.musicapp.Class.Musician;
import com.example.musicapp.DataBase.ItemDataBase;
import com.example.musicapp.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MusicianData {

    private static List<Musician> musicianList;
    private static HashMap<String,Musician>musicianHashMap;
    public static void setListMusician() {
        musicianList = new ArrayList<>();
        musicianHashMap = new HashMap<>();

        musicianList.add(new Musician(R.drawable.mc01_binz,R.drawable.mc01_binz2,"Binz"));
        musicianList.add(new Musician(R.drawable.mc02_denvau,R.drawable.mc02_denvau2,"Đen Vâu"));
        musicianList.add(new Musician(R.drawable.mc03_imagedragons,R.drawable.mc03_imagedragons,"Imagine Dragons"));
        musicianList.add(new Musician(R.drawable.mc04_sontung,R.drawable.mc04_sontung,"Sơn Tùng M-TP"));
        musicianList.add(new Musician(R.drawable.mc05_bui_anh_tuan,R.drawable.mc05_bui_anh_tuan,"Bùi Anh Tuấn"));
        musicianList.add(new Musician(R.drawable.mc06_vu,R.drawable.mc06_vu,"Vũ"));


        for(Musician musician : musicianList){
            musician.setLove(false);
            musicianHashMap.put(musician.getName(),musician);
            if(!checkExist(musician.getName())){
                ItemDataBase.getInstance(FlashActivity.getContext()).itemDao().insertItem(new Item(musician.getName(), false));
            }
        }
        List<Item>list = ItemDataBase.getInstance(FlashActivity.getContext()).itemDao().getList();
        for(Item item : list){
            if (musicianHashMap.containsKey(item.getId())){
                Musician musician = musicianHashMap.get(item.getId());
                musician.setLove(item.isChosen());
            }
        }
    }
    public static boolean checkExist(String id){
        List<Item> list = ItemDataBase.getInstance(FlashActivity.getContext()).itemDao().find(id);
        return list != null && !list.isEmpty();
    }
    public static List<Musician> getRandomMusician(){
        List<Musician> list = musicianList;
        Collections.shuffle(list);
        List<Musician> resList = new ArrayList<>();
        for(int i = 0; i < Math.min(list.size(),6); i++){
            resList.add(list.get(i));
        }
        return resList;
    }
    public static List<Musician> getMusicianList() {
        return musicianList;
    }
    public static HashMap<String, Musician> getMusicianHashMap() {
        return musicianHashMap;
    }
}
