package com.example.musicapp.Adapter;
//Adapter của ListView trong SearchFragment

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.Activity.MainActivity;
import com.example.musicapp.Activity.PlayMusicActivity;
import com.example.musicapp.Data.MusicData;
import com.example.musicapp.Fragment.PlayMusicFragment;
import com.example.musicapp.Class.Music;
import com.example.musicapp.R;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder>{
    private Context context;
    private List<Music> arrayMusic;
    private ClickIcon clickIcon;
    public interface ClickIcon{
        void updateIcon(Music music);
    }
    public MusicAdapter(ClickIcon clickIcon){
        this.clickIcon = clickIcon;
    }
    public void setData(Context context, List<Music>list){
        this.context = context;
        this.arrayMusic = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music,parent,false);
        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        final Music music = arrayMusic.get(position);
        if (music == null){
            return;
        }
        holder.tvMusic.setText(music.getTenNhac());
        holder.tvMusician.setText(music.getCaSi());
        holder.musicImg.setImageResource(music.getHinhNen());

        if (music.isLove()){
            holder.iconFav.setImageResource(R.drawable.ic_favorite);
        }
        else{
            holder.iconFav.setImageResource(R.drawable.ic_favorite_border);
        }

        holder.iconFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickIcon.updateIcon(music);
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickGoToDetail(music);
            }
        });
    }

    private void onClickGoToDetail(Music music) {
        Intent it = new Intent(context , PlayMusicActivity.class);
        List<Music>list = MusicData.getArrayMusic();
        if(context.equals(MainActivity.getContext())){
            PlayMusicFragment.setArrayMusic(list);
            it.putExtra("position", MusicData.getPosition(music.getId(),list) + "");
            context.startActivity(it);
        }
        else{
            list = MusicData.musicianList(music.getCaSi());
            PlayMusicFragment.setArrayMusic(list);
            it.putExtra("position",MusicData.getPosition(music.getId(),list) + "");
            context.startActivity(it);
        }


    }

    @Override
    public int getItemCount() {
        if (arrayMusic != null){
            return arrayMusic.size();
        }
        return 0;
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{
        private RelativeLayout relativeLayout;
        private TextView tvMusic,tvMusician;
        private ImageView musicImg;
        private ImageButton iconFav;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMusic = itemView.findViewById(R.id.musicName);
            tvMusician = itemView.findViewById(R.id.musicianName);
            musicImg = itemView.findViewById(R.id.musicImage);
            iconFav = itemView.findViewById(R.id.ic_fav);
            relativeLayout = itemView.findViewById(R.id.music_layout_line);
        }
    }

}
