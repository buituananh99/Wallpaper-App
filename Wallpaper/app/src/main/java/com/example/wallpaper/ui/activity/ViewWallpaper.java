package com.example.wallpaper.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.wallpaper.R;
import com.example.wallpaper.adapter.ViewPagerAdapter;
import com.example.wallpaper.common.Common;
import com.example.wallpaper.database.Favorite;
import com.example.wallpaper.database.FavoriteRoomDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dmax.dialog.SpotsDialog;

public class ViewWallpaper extends AppCompatActivity implements View.OnClickListener {


    private ViewPager viewPager;
    private RelativeLayout rotLayout;
    private FloatingActionButton fabDownload, fabSet, fabFavorite;

    private ViewPagerAdapter adapter;

    AlertDialog alertDialog;

    static final int PERMISSION_REQUEST_CODE = 1000;
    private static final int NUM_OF_THREADS = 4;

    ExecutorService executorService = Executors.newFixedThreadPool(NUM_OF_THREADS);

    FavoriteRoomDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_wallpaper);

        initView();

        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        Common.WIDTH = point.x;
        Common.HEIGHT = point.y;

        database = FavoriteRoomDatabase.getInstance(this);


        addViewPager();

        addFavorite();
    }

    private void addFavorite() {
        executorService.execute(() -> {
            String currentLinkImage = Common.LIST_PICTURE.get(viewPager.getCurrentItem());
            Favorite favorite = database.dao().isExist(currentLinkImage);
            if (favorite != null) {
                fabFavorite.setImageResource(R.drawable.ic_favorite_yes);
            } else {
                fabFavorite.setImageResource(R.drawable.ic_favorite_no);
            }
        });
    }

    private void initView() {
        viewPager = findViewById(R.id.view_pager);
        rotLayout = findViewById(R.id.rot_layout);
        fabDownload = findViewById(R.id.fab_download);
        fabFavorite = findViewById(R.id.fab_favorite);
        fabSet = findViewById(R.id.fab_set);

        // add handler
        fabDownload.setOnClickListener(this);
        fabFavorite.setOnClickListener(this);
        fabSet.setOnClickListener(this);
    }

    private void addViewPager() {
        if (getIntent() == null) return;

        int pos = getIntent().getIntExtra("current_img", 0);
        adapter = new ViewPagerAdapter(this, Common.LIST_PICTURE);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);
        adapter.notifyDataSetChanged();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addFavorite();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        if (v == fabDownload) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                downloadBitmap();
            }
        } else if (v == fabSet) {
            setWallpagerToScreen();
        } else if (v == fabFavorite) {
            updateFavorite();
        }
    }

    private void updateFavorite() {
        executorService.execute(() -> {
            String currentLinkImage = Common.LIST_PICTURE.get(viewPager.getCurrentItem());
            Favorite favorite = database.dao().isExist(currentLinkImage);
            if (favorite == null) {
                // TODO: Insert favorite
                fabFavorite.setImageResource(R.drawable.ic_favorite_yes);
                database.dao().insertFavorite(new Favorite(currentLinkImage, "" + System.currentTimeMillis()));
            } else {
                fabFavorite.setImageResource(R.drawable.ic_favorite_no);
                database.dao().deleteFavorite(currentLinkImage);

            }
        });
    }

    private void setWallpagerToScreen() {
        alertDialog = new SpotsDialog(this);
        alertDialog.setTitle("Please wait...");
        alertDialog.show();

        Glide.with(this)
                .asBitmap()
                .load(Common.LIST_PICTURE.get(viewPager.getCurrentItem()))
                .override(Common.WIDTH, Common.HEIGHT)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(ViewWallpaper.this);
                        try {
                            wallpaperManager.setBitmap(resource);
                            alertDialog.dismiss();
                            Snackbar.make(rotLayout, "Wallpaper set background success", Snackbar.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void downloadBitmap() {
        alertDialog = new SpotsDialog(this);
        alertDialog.setTitle("Please wait...");
        alertDialog.show();
        Glide.with(this)
                .asBitmap()
                .load(Common.LIST_PICTURE.get(viewPager.getCurrentItem()))
                .override(Common.WIDTH, Common.HEIGHT)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        saveBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private void saveBitmap(Bitmap resource) {
        String fileName = UUID.randomUUID() + ".jpg";
        String path = Environment.getExternalStorageDirectory().toString();
        File folder = new File(path + "/" + getString(R.string.app_name));
        folder.mkdir();

        File file = new File(folder, fileName);

        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            resource.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
            alertDialog.dismiss();
            Snackbar.make(rotLayout, "Download successful", Snackbar.LENGTH_LONG).show();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
