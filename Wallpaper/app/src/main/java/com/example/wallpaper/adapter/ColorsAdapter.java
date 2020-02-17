package com.example.wallpaper.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wallpaper.Interface.ItemOnClickListener;
import com.example.wallpaper.R;
import com.example.wallpaper.model.Colors;

import java.util.ArrayList;
import java.util.List;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder> {

    private Context context;
    private List<Colors> colorsList;
    private ItemOnClickListener itemOnClickListener;

    public ColorsAdapter(Context context, List<Colors> colorsList, ItemOnClickListener itemOnClickListener) {
        this.context = context;
        this.colorsList = colorsList;
        this.itemOnClickListener = itemOnClickListener;
    }

    @NonNull
    @Override
    public ColorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_colors, parent, false);
        return new ColorsViewHolder(view, itemOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorsViewHolder holder, int position) {
        if (colorsList.get(position).getName().equals("random")) {
            int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
                holder.btnItemColor.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.random_color));
            } else {
                holder.btnItemColor.setBackground(ContextCompat.getDrawable(context, R.drawable.random_color));
            }
        } else {
            RoundRectShape roundRectShape = new RoundRectShape(new float[]{
                    10, 10, 10, 10,
                    10, 10, 10, 10,
            }, null, null);

            ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
            shapeDrawable.getPaint().setColor(Color.parseColor(colorsList.get(position).getHex()));

            holder.btnItemColor.setBackground(shapeDrawable);

        }


    }

    @Override
    public int getItemCount() {
        return colorsList != null ? colorsList.size() : 0;
    }




    public class ColorsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        Button btnItemColor;
        ItemOnClickListener itemOnClickListener;

        public ColorsViewHolder(@NonNull View itemView, ItemOnClickListener itemOnClickListener) {
            super(itemView);

            btnItemColor = itemView.findViewById(R.id.btn_item_color);
            this.itemOnClickListener = itemOnClickListener;


            btnItemColor.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            itemOnClickListener.onclick(colorsList.get(getAdapterPosition()).getName());
        }


    }
}