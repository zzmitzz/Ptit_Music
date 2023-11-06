package com.example.musicapp.Data;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.InputStreamReader;

public class LyricsSource {
    public List<String> arr;
    String nameFile;
    Context context;
    public LyricsSource(Context context,String a) throws IOException {
        this.context = context;
        this.nameFile = a;
        arr = getLyrics();
    }
    private List<String> getLyrics() throws IOException {
        List<String> res = new ArrayList<String>();
        AssetManager am = context.getAssets();
        BufferedReader reader = new BufferedReader(new InputStreamReader(am.open(nameFile)));
        Scanner sc = new Scanner(reader);
        while(sc.hasNextLine()){
            res.add(sc.nextLine());
        }
        return res;
    }
}
