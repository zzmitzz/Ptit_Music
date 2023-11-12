package com.example.musicapp.Data;

import com.example.musicapp.Activity.FlashActivity;
import com.example.musicapp.Class.Book;
import com.example.musicapp.Class.Item;
import com.example.musicapp.Class.Music;
import com.example.musicapp.Function.NlpUtils;
import com.example.musicapp.DataBase.ItemDataBase;
import com.example.musicapp.R;

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
        arrayMusic.add(new Music("ms04","Bad Liar","Imagine Dragons",R.drawable.ms04_bad_liar,R.raw.ms04_bad_liar,"ms04_bad_liar.txt"));
        arrayMusic.add(new Music("ms05","Believer","Imagine Dragons",R.drawable.ms05_believer,R.raw.ms05_believer,"ms05_believer.txt"));
        arrayMusic.add(new Music("ms06","Bones","Imagine Dragons",R.drawable.ms06_bones,R.raw.ms06_bones,"ms06_bones.txt"));
        arrayMusic.add(new Music("ms07","Có chắc yêu là đây","Sơn Tùng M-TP",R.drawable.ms07_co_chac_yeu_la_day,R.raw.ms07_co_chac_yeu_la_day,"ms07_co_chac_yeu_la_day.txt"));
        arrayMusic.add(new Music("ms08","Demons","Imagine Dragons",R.drawable.ms08_demons,R.raw.ms08_demons,"ms08_demons.txt"));
        arrayMusic.add(new Music("ms09","Dù Cho Mai Về Sau", "Buitruonglinh", R.drawable.ms09_duchomaivesau, R.raw.ms09_duchomaivesau, "ms09_duchomaivesau.txt"));
        arrayMusic.add(new Music("ms10","Enemy", "Imagine Dragons", R.drawable.ms10_enemy_arcane,R.raw.ms10_enemy_arcane,"ms10_enemy_arcane.txt"));
        arrayMusic.add(new Music("ms11","Hãy trao cho anh","Sơn Tùng M-TP",R.drawable.ms11_hay_trao_cho_anh,R.raw.ms11_hay_trao_cho_anh,"ms11_hay_trao_cho_anh.txt"));
        arrayMusic.add(new Music("ms13","Hymn For The Weekend", "Bely Basarte", R.drawable.ms13_hymnfortheweekend, R.raw.ms13_hymn_for_the_weekend, "ms13_hymn_for_the_weekend.txt"));
        arrayMusic.add(new Music("ms14","Legends Never Die", "Against The Current and Mako", R.drawable.ms14_legend_never_die, R.raw.ms14_legend_never_die, "ms14_legend_never_die.txt"));
        arrayMusic.add(new Music("ms15","Natural","Imagine Dragons",R.drawable.ms15_natural,R.raw.ms15_natural,"ms15_natural.txt"));
        arrayMusic.add(new Music("ms16","Nơi này có anh", "Sơn Tùng M-TP", R.drawable.ms16_noi_nay_co_anh, R.raw.ms16_noi_nay_co_anh, "ms16_noi_nay_co_anh.txt"));
        arrayMusic.add(new Music("ms17","Radioactive","Imagine Dragons",R.drawable.ms17_radioactive,R.raw.ms17_radioactive,"ms17_radioactive.txt"));
        arrayMusic.add(new Music("ms18","Seasons", "Rival, Cadmium, Harley Bird", R.drawable.ms18_seasons, R.raw.ms18_seasons, "ms18_seasons.txt"));
        arrayMusic.add(new Music("ms19","Thunder","Imagine Dragons",R.drawable.ms19_thunder,R.raw.ms19_thunder,"ms19_thunder.txt"));
        arrayMusic.add(new Music("ms20","Waiting For Love", "Avicii", R.drawable.ms20_waittingforlove, R.raw.ms20_waittingforlove, "ms20_waitingfor_love.txt"));
        arrayMusic.add(new Music("ms21","Warriors","Imagine Dragons",R.drawable.ms21_warriors,R.raw.ms21_warriors,"ms21_warriors.txt"));
        arrayMusic.add(new Music("ms22","Whatever It Takes","Imagine Dragons",R.drawable.ms22_whatever_it_takes,R.raw.ms22_whatever_it_takes,"ms22_whatever_it_take.txt"));
        arrayMusic.add(new Music("ms23","Lạ Lùng","Vũ",R.drawable.ms23_la_lung,R.raw.ms23_la_lung,"ms23_La_Lung.txt"));
        arrayMusic.add(new Music("ms24","Mùa Đông Chưa Bao Giờ Tới","Vũ",R.drawable.ms24_mua_dong_chua_bao_gio_toi,R.raw.ms24_mua_dong_chua_bao_gio_toi,"ms24_Mua_ Dong_Chua_Bao_Gio_Toi.txt"));
        arrayMusic.add(new Music("ms25","Người Mai Dịch Cô Đơn","Vũ",R.drawable.ms25_nguoi_mai_dich_co_don,R.raw.ms25_nguoi_mai_dich_co_don,"ms25_Nguoi_Mai_Dich_Co_Don.txt"));
        arrayMusic.add(new Music("ms26","Khao Khát","Vũ",R.drawable.ms26_khao_khat,R.raw.ms26_khao_khat,"ms26_Khao_Khat.txt"));
        arrayMusic.add(new Music("ms27","Hai Người Xa","Vũ",R.drawable.ms27_hai_nguoi_xa,R.raw.ms27_hai_nguoi_xa,"ms27_Hai_Nguoi_Xa.txt"));
        arrayMusic.add(new Music("ms28","Nghe Nói","Vũ",R.drawable.ms28_nghe_noi,R.raw.ms28_nghe_noi,"ms28_Nghe_Noi.txt"));
        arrayMusic.add(new Music("ms29","Đông Kiếm Em","Vũ",R.drawable.ms29_dong_kiem_em,R.raw.ms29_dong_kiem_em,"ms29_Dong_Kiem_Em.txt"));
        arrayMusic.add(new Music("ms30","Cô Ta","Vũ",R.drawable.ms30_co_ta,R.raw.ms30_co_ta,"ms30_Co_Ta.txt"));
        arrayMusic.add(new Music("ms31","Người Duy Nhất","Bùi Anh Tuấn",R.drawable.ms31_nguoi_duy_nhat,R.raw.ms31_nguoi_duy_nhat,"ms31_Nguoi_Duy_Nhat.txt"));
        arrayMusic.add(new Music("ms32","Chỉ Còn Lại Tình Yêu","Bùi Anh Tuấn",R.drawable.ms32_chi_con_lai_tinh_yeu,R.raw.ms32_chi_con_lai_tinh_yeu,"ms32_Chi_Con_Lai_Tinh_Yeu.txt"));
        arrayMusic.add(new Music("ms33","Hẹn Một Mai","Bùi Anh Tuấn",R.drawable.ms33_hen_mot_mai,R.raw.ms33_hen_mot_mai,"ms33_Hen_Mot_Mai.txt"));
        arrayMusic.add(new Music("ms34","Phố Không Mùa","Bùi Anh Tuấn",R.drawable.ms34_pho_khong_mua,R.raw.ms34_pho_khong_mua,"ms34_Pho_Khong_Mua.txt"));
        arrayMusic.add(new Music("ms35","Đã Từng","Búi Anh Tuấn",R.drawable.ms35_da_tung,R.raw.ms35_da_tung,"ms35_Da_Tung.txt"));
        arrayMusic.add(new Music("ms36","Bí Mật Không Tên","Bùi Anh Tuấn" ,R.drawable.ms36_bi_mat_khong_ten,R.raw.ms36_bi_mat_khong_ten,"ms36_Bi_Mat_Khong_Ten.txt"));
        arrayMusic.add(new Music("ms37","Lăng Yên Dưới Vực Sâu","Bùi Anh Tuấn",R.drawable.ms37_lang_yen_duoi_vuc_sau,R.raw.ms37_lang_yen_duoi_vuc_sau,"ms37_Lang_Yen_Duoi_Vuc_Sau.txt"));
        arrayMusic.add(new Music("ms38","Chia Tay Trong Mưa", "Bùi Anh Tuấn",R.drawable.ms38_chia_tay_trong_mua,R.raw.ms38_chia_tay_trong_mua,"ms38_Chia_Tay_Trong_Mua.txt"));
        arrayMusic.add(new Music("ms39","Ngày Khác Lạ","Đen Vâu",R.drawable.ms39_ngay_khac_la,R.raw.ms39_ngay_khac_la,"ms39_ngay_khac_la.txt"));
        arrayMusic.add(new Music("ms40","Cho Tôi Lang Thang","Đen Vâu",R.drawable.ms40_cho_toi_lang_thang,R.raw.ms40_cho_toi_lang_thang,"ms40_cho_toi_lang_thang.txt"));
        arrayMusic.add(new Music("ms41","Ta Vắng Nàng","Đên Vâu",R.drawable.ms41_ta_va_nang,R.raw.ms41_ta_va_nang,"ms41_ta_va_nang.txt"));
        arrayMusic.add(new Music("ms42","Ngày Lang Thang","Đen Vâu",R.drawable.ms42_ngay_lang_thang,R.raw.ms42_ngay_lang_thang,"ms42_ngay_lang_thang.txt"));
        arrayMusic.add(new Music("ms43","Anh Đếch Cần Gì Nhiều Ngoài Em" ,"Đen Vâu",R.drawable.ms43_anh_dech_can_gi_nhieu_ngoai_em,R.raw.ms43_anh_dech_can_gi_nhieu_ngoai_em,"ms43_anh_dech_can_gi_nhieu_ngoai_em.txt"));
        arrayMusic.add(new Music("ms44","Lối Nhỏ","Đen Vâu",R.drawable.ms44_loi_nho,R.raw.ms44_loi_nho,"ms44_loi_nho.txt"));
        arrayMusic.add(new Music("ms47","Sunshine Alone","Binz",R.drawable.ms47_shunshine_alone,R.raw.ms47_shunshine_alone,"ms47_shunshine_alone.txt"));
        arrayMusic.add(new Music("ms48","Rời Xa","Binz",R.drawable.ms48_roi_xa,R.raw.ms48_roi_xa,"ms48_roi_xa.txt"));
        arrayMusic.add(new Music("ms49","Krazy","Binz",R.drawable.ms49_krazy,R.raw.ms49_krazy,"ms49_krazy.txt"));
        arrayMusic.add(new Music("ms50","Sofa","Binz",R.drawable.ms50_sofa,R.raw.ms50_sofa,"ms50_sofa.txt"));
        arrayMusic.add(new Music("ms51","Với Lấy","Binz",R.drawable.ms51_voi_lay,R.raw.ms51_voi_lay,"ms51_voi_lay.txt"));
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
                if(book.getId().equals(music.getId())){
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
