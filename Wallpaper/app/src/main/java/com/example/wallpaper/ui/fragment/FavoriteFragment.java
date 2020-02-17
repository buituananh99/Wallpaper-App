package com.example.wallpaper.ui.fragment;


import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.wallpaper.R;
import com.example.wallpaper.adapter.FavoriteAdapter;
import com.example.wallpaper.database.Favorite;
import com.example.wallpaper.database.FavoriteRoomDatabase;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    private LinearLayout linearNoFavorite;
    private RecyclerView rcyFavorite;
    private FavoriteRoomDatabase database;

    private List<String> favoriteList;

    private FavoriteAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        initView(view);

        database = FavoriteRoomDatabase.getInstance(getActivity());

        new LoadFavorite().execute();
        return view;
    }

    private void initView(View view) {
        rcyFavorite = view.findViewById(R.id.rcy_favorite);
        linearNoFavorite = view.findViewById(R.id.layout_no_favorite);

        rcyFavorite.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        rcyFavorite.setLayoutManager(gridLayoutManager);
        adapter = new FavoriteAdapter(getActivity());
        rcyFavorite.setAdapter(adapter);
    }

    class LoadFavorite extends AsyncTask<Void, List<String>, List<String> >{

        @Override
        protected List<String> doInBackground(Void... voids) {
            return database.dao().getAllFavorite();
        }

        @Override
        protected void onPostExecute(List<String> listFavorite) {
            super.onPostExecute(listFavorite);
            favoriteList = listFavorite;
            if(listFavorite.size() > 0){
                linearNoFavorite.setVisibility(View.GONE);
                adapter.setList(favoriteList);

            }else{
                rcyFavorite.setVisibility(View.GONE);
                linearNoFavorite.setVisibility(View.VISIBLE);
            }
        }
    }

}
