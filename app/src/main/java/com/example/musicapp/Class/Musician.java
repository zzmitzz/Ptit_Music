package com.example.musicapp.Class;

import java.io.Serializable;
public class Musician implements Serializable,Comparable<Musician> {

    private String name;
    private int imageId;
    private int imageBg;
    private boolean love;

    // Constructor để khởi tạo một đối tượng Musician với thông tin hình ảnh và tên
    public Musician(int imageId, int imageBg, String name) {
        this.imageId = imageId;
        this.imageBg = imageBg;
        this.name = name;
    }
    public boolean isLove() {
        return love;
    }
    public void setLove(boolean love) {
        this.love = love;
    }
    public int getImageBg() {
        return imageBg;
    }
    public void setImageBg(int imageBg) {
        this.imageBg = imageBg;
    }
    public int getImageId() {
        return imageId;
    }
    public String getName() {
        return name;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public int compareTo(Musician o) {
        return this.name.compareTo(o.name);
    }
}
