package com.example.wallpaper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.wallpaper.R;
import com.example.wallpaper.common.Common;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> listPicture;

    public ViewPagerAdapter(Context context, List<String> listPicture) {
        this.context = context;
        this.listPicture = listPicture;
    }

    @Override
    public int getCount() {
        return listPicture != null ? listPicture.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imgPicture = new ImageView(context);
        imgPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);


        Glide.with(context).load(listPicture.get(position))
                .override(Common.WIDTH, Common.HEIGHT)
                .placeholder(R.drawable.ic_landscape_black)
                .into(imgPicture);

        container.addView(imgPicture, 0);


        return imgPicture;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
