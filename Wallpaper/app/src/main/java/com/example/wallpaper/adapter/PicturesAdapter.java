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

public class PicturesAdapter extends RecyclerView.Adapter<PicturesAdapter.PicturesViewHolder> {

    private Context context;
    private List<String> nameList = new ArrayList<>();


    public PicturesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public PicturesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pictures, parent, false);
        return new PicturesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PicturesViewHolder holder, int position) {
        Glide.with(context).load(nameList.get(position))
                .placeholder(R.drawable.ic_landscape_black)
                .into(holder.imgPicture);

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, ViewWallpaper.class);
            i.putExtra("current_img", position);
            Common.LIST_PICTURE = nameList;
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return nameList != null ? nameList.size() : 0;
    }

    public void setList(List<String> nameList) {
        this.nameList = nameList;
        notifyDataSetChanged();
    }


    public class PicturesViewHolder extends RecyclerView.ViewHolder {


        private ImageView imgPicture;

        public PicturesViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPicture = itemView.findViewById(R.id.img_picture);
        }
    }
}