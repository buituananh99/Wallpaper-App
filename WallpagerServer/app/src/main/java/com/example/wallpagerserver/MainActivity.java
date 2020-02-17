package com.example.wallpagerserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.wallpagerserver.model.Colors;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "MainActivity";


    private Button btnChooseFile, btnUploadFile;
    private ImageView imgWallpaper;
    private MaterialSpinner spinnerCategory;

    Uri filePath;

    String colorCategorySelected = "random";
    List<String> categoryList = new ArrayList<>();

    FirebaseStorage storage;
    StorageReference storageReference;
    
    DatabaseReference refColors;

    private int PERMISSION_REQUEST = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initFirebase();
        loadColorsCategorySpinner();
    }

    private void initFirebase() {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    private void loadColorsCategorySpinner() {

        categoryList.add("Choose Colors Category");
        spinnerCategory.setItems(categoryList);

        refColors = FirebaseDatabase.getInstance().getReference("Colors");
        refColors.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot color : dataSnapshot.getChildren()) {
                    Colors colors = color.getValue(Colors.class);
                    categoryList.add(colors.getName());
                }
                spinnerCategory.setItems(categoryList);

                spinnerCategory.setOnItemSelectedListener((view, position, id, item) -> {
                    colorCategorySelected = categoryList.get(position);
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Error " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

    }

    private void initView() {
        imgWallpaper = findViewById(R.id.img_wallpaper);
        spinnerCategory = findViewById(R.id.spinner_category);
        btnChooseFile = findViewById(R.id.btn_choose_file);
        btnUploadFile = findViewById(R.id.btn_upload_file);

        // Add handler
        btnChooseFile.setOnClickListener(this);
        btnUploadFile.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnChooseFile) {
            chooseFile();
        } else if (v == btnUploadFile) {

            if (spinnerCategory.getSelectedIndex() == 0) {
                Toast.makeText(this, "Please select Color category", Toast.LENGTH_SHORT).show();
                return;
            }

            upload();
        }
    }

    private void upload() {

        AlertDialog alertDialog = new SpotsDialog(this);
        alertDialog.show();
        alertDialog.setMessage("Uploading");

        StorageReference reference = storageReference.child(new StringBuilder("wallpaper/" + colorCategorySelected + "/")
                .append(UUID.randomUUID()).toString());

        reference.putFile(filePath).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }
            return reference.getDownloadUrl();
        }).addOnFailureListener(e -> {
            alertDialog.dismiss();
            Toast.makeText(this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri uri = task.getResult();
                saveUrlToDB(colorCategorySelected, uri.toString());
                alertDialog.dismiss();
            }
        });


    }

    private void saveUrlToDB(String colorCategorySelected, String link) {
        FirebaseDatabase
                .getInstance()
                .getReference("Wallpaper/" + colorCategorySelected)
                .push()
                .setValue(link).addOnCompleteListener(task -> {

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private void chooseFile() {
        Dexter.withActivity(MainActivity.this).withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Image :"), PERMISSION_REQUEST);
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                Toast.makeText(MainActivity.this, "asddasd", Toast.LENGTH_SHORT).show();

            }
        }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PERMISSION_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgWallpaper.setImageBitmap(bitmap);
                btnUploadFile.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
