package com.example.wallpaper.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wallpaper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment implements View.OnClickListener {


    private ImageView imgFb, imgGmail, imgChPlay, imgShare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        imgFb = view.findViewById(R.id.img_fb);
        imgGmail = view.findViewById(R.id.img_gmail);
        imgChPlay = view.findViewById(R.id.img_chplay);
        imgShare = view.findViewById(R.id.img_share);

        // Add handler
        imgFb.setOnClickListener(this);
        imgGmail.setOnClickListener(this);
        imgChPlay.setOnClickListener(this);
        imgShare.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == imgFb) {
            pageFaceBook();
        } else if (v == imgGmail) {
            sendEmail();
        } else if (v == imgChPlay) {
            shareApp();
        } else if (v == imgShare) {
            shareApp();
        }
    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);

        String email[] = {"buituan1999sn@gmail.com"};

        i.putExtra(Intent.EXTRA_EMAIL, email);
        i.putExtra(Intent.EXTRA_BCC, email);
        i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));

        i.setType("text/plain");

        i.putExtra(Intent.EXTRA_TEXT, "");

        startActivity(i);





    }

    private void shareApp() {
        Toast.makeText(getActivity(), "Apps này hiện chưa có trên CH Play", Toast.LENGTH_SHORT).show();
    }

    private void pageFaceBook() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://fb.com/tuananh99sn"));
        startActivity(intent);
    }


}
