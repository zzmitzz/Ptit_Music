package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Class.Category;
import com.example.musicapp.Data.LyricsSource;
import com.example.musicapp.R;

import java.io.IOException;
import java.util.List;

//public class LyricsAdapter extends RecyclerView.Adapter<LyricsAdapter.LyricsViewHolder> {
//
//    private Context mContext;
//    private List<Category> mListCategory;
//
//    public LyricsAdapter(Context mContext){
//        this.mContext = mContext;
//    }
//    public void setData(List<Category>list){
//        this.mListCategory = list;
//        notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public LyricsAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
//        return new CategoryViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
//        Category category = mListCategory.get(position);
//        if(category == null){
//            return;
//        }
//        holder.tvNameCategory.setText(category.getNameCategory());
//
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,RecyclerView.HORIZONTAL,false);
//        holder.rcvBook.setLayoutManager(linearLayoutManager);
//
//        BookAdapter bookAdapter = new BookAdapter();
//        bookAdapter.setData(mContext,category.getBooks());
//        holder.rcvBook.setAdapter(bookAdapter);
//    }
//
//    @Override
//    public int getItemCount() {
//        if(mListCategory != null){
//            return mListCategory.size();
//        }
//        return 0;
//    }
//
//    public class CategoryViewHolder extends RecyclerView.ViewHolder{
//        private TextView tvNameCategory;
//        private RecyclerView rcvBook;
//        public CategoryViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvNameCategory = itemView.findViewById(R.id.tv_name_category);
//            rcvBook = itemView.findViewById(R.id.rcv_book);
//        }
//    }
//}
//
public class LyricsAdapter extends  RecyclerView.Adapter<LyricsAdapter.LyricsViewHolder>{

    List<String> data;
    public LyricsAdapter(Context c, String fileName) throws IOException {
        LyricsSource a = new LyricsSource(c,fileName);
        data = a.arr;
    }

    @NonNull
    @Override
    public LyricsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyric_line,parent,false);
        return new LyricsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LyricsViewHolder holder, int position) {
        String a = data.get(position);
        holder.text.setText(a);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class LyricsViewHolder extends  RecyclerView.ViewHolder{
        private TextView text;
        public LyricsViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.Lyrics_text);
        }
    }
}