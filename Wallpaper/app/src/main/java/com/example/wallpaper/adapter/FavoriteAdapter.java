package com.example.wallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.wallpaper.R;
import com.example.wallpaper.common.Common;
import com.example.wallpaper.ui.activity.ViewWallpaper;

import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private Context context;
    private List<String> favoriteList = new ArrayList<>();

    public FavoriteAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Glide.with(context).load(favoriteList.get(position))
                .placeholder(R.drawable.ic_landscape_black)
                .into(holder.imgFavorite);

        holder.itemView.setOnClickListener(v -> {
            Common.LIST_PICTURE = favoriteList;
            Intent i = new Intent(context, ViewWallpaper.class);
            i.putExtra("current_img", position);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteList != null ? favoriteList.size() : 0;
    }

    public void setList(List<String> StringList) {
        this.favoriteList = StringList;
        notifyDataSetChanged();
    }


    public class FavoriteViewHolder extends RecyclerView.ViewHolder {


        private ImageView imgFavorite;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);

            imgFavorite = itemView.findViewById(R.id.img_favorite);

        }
    }
}