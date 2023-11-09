package com.example.musicapp.Class;

import com.example.musicapp.Function.NlpUtils;

public class Music implements Comparable<Music>{

    private String id;
    private String tenNhac;
    private String caSi;
    private int hinhNen;
    private int sourceMp3;
    private boolean love;
    private String fileSource;

    // Constructor (Hàm khởi tạo)
    public Music(String id,String tenNhac, String caSi, int hinhNen, int sourceMp3, String fileSource)  {
        this.id = id;
        this.tenNhac = tenNhac;
        this.caSi = caSi;
        this.hinhNen = hinhNen;
        this.sourceMp3 = sourceMp3;
        this.fileSource = fileSource;
    }

    public boolean isLove() {
        return love;
    }
    public void setLove(boolean love) {
        this.love = love;
    }
    public void setFileSource(String fileSource) {
        this.fileSource = fileSource;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getTenNhac() {
        return tenNhac;
    }
    public void setTenNhac(String tenNhac) {
        this.tenNhac = tenNhac;
    }
    public String getCaSi() {
        return caSi;
    }
    public void setCaSi(String caSi) {
        this.caSi = caSi;
    }
    public int getHinhNen() {
        return hinhNen;
    }
    public void setHinhNen(int hinhNen) {
        this.hinhNen = hinhNen;
    }
    public int getSourceMp3() {
        return sourceMp3;
    }
    public void setSourceMp3(int sourceMp3) {
        this.sourceMp3 = sourceMp3;
    }
    @Override
    public int compareTo(Music music) {
        // Sử dụng NlpUtils.removeAccent để loại bỏ dấu và thực hiện so sánh theo tên không dấu
        return NlpUtils.removeAccent(this.getTenNhac()).compareTo(NlpUtils.removeAccent(music.getTenNhac()));
    }

    public String getFileSource() {
        return fileSource;
    }
}
