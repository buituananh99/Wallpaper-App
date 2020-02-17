package com.example.wallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.wallpaper.Interface.ItemOnClickListener;
import com.example.wallpaper.ui.fragment.AboutFragment;
import com.example.wallpaper.ui.fragment.ColorsFragment;
import com.example.wallpaper.ui.fragment.FavoriteFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new ColorsFragment());

        initView();
        addHandler();
    }

    private void addHandler() {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    private void initView() {
       bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_color:
                loadFragment(new ColorsFragment());
                break;
            case R.id.action_favorite:
                loadFragment(new FavoriteFragment());
                break;
            case R.id.action_about:
                loadFragment(new AboutFragment());
                break;
        }
        return false;
    }

}
