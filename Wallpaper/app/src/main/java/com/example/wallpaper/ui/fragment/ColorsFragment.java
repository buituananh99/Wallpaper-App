package com.example.wallpaper.ui.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.Toast;

import com.example.wallpaper.Interface.ItemOnClickListener;
import com.example.wallpaper.R;
import com.example.wallpaper.adapter.ColorsAdapter;
import com.example.wallpaper.adapter.PicturesAdapter;
import com.example.wallpaper.model.Colors;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ColorsFragment extends Fragment {

    private static final String TAG = "ColorsFragment";

    private RecyclerView rcyColors;
    private RecyclerView rcyPictures;

    private List<Colors> colorsList;
    private List<String> picturesList;

    private ColorsAdapter colorsAdapter;
    private PicturesAdapter picturesAdapter;

    private DatabaseReference refColors;
    private DatabaseReference refPictures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_colors, container, false);

        initView(view);
        getColors();


        return view;
    }


    private void initView(View view) {
        rcyColors = view.findViewById(R.id.rcy_colors);
        rcyPictures = view.findViewById(R.id.rcy_pictures);

        rcyColors.setHasFixedSize(true);
        rcyPictures.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        rcyColors.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rcyPictures.setLayoutManager(gridLayoutManager);


        picturesAdapter = new PicturesAdapter(getActivity());

        rcyPictures.setAdapter(picturesAdapter);
    }


    private void getColors() {
        colorsList = new ArrayList<>();
        refColors = FirebaseDatabase.getInstance().getReference("Colors");
        refColors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                colorsList.clear();
                for (DataSnapshot color : dataSnapshot.getChildren()) {
                    Colors colors = color.getValue(Colors.class);
                    colorsList.add(colors);
                }
                getPicturesByNameColor("random");

                colorsAdapter = new ColorsAdapter(getActivity(), colorsList, nameColor -> {
                    getPicturesByNameColor(nameColor);
                });

                rcyColors.setAdapter(colorsAdapter);
                colorsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }




    void getPicturesByNameColor(String color) {
        picturesList = new ArrayList<>();
        refPictures = FirebaseDatabase.getInstance().getReference("Wallpaper/" + color + "/");
        refPictures.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                picturesList.clear();
                for (DataSnapshot picture : dataSnapshot.getChildren()) {
                    picturesList.add(picture.getValue(String.class));
                }
                picturesAdapter.setList(picturesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
