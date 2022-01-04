package com.example.prectice_jay_image_upload;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class YouTubeActivity extends AppCompatActivity {


    Button btnSelect, btnUpload;
    TextView tvTitle;
    ImageView imageView;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);

        btnSelect = findViewById(R.id.btnSelect);
        btnUpload = findViewById(R.id.btnUpload);
        tvTitle = findViewById(R.id.tvTitle);
        imageView = findViewById(R.id.imgView);


        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectPhoto();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });

    }

    private void selectPhoto() {

        Intent intent = new Intent();
        intent.setType("images/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {


            imageUri = data.getData();
            imageView.setImageURI(imageUri);

        }
    }

    private void uploadPhoto() {

        progressDialog = new ProgressDialog(YouTubeActivity.this);
        progressDialog.setTitle("Uploading Files ...");
        progressDialog.show();

        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_mm_ss", Locale.CANADA);
                Date date = new Date();
                String filename = dateFormat.format(date);
                storageReference = FirebaseStorage.getInstance().getReference("images/" + filename);

                imageView.setImageURI(null);

                Toast.makeText(YouTubeActivity.this, "Image SuccessFully Uploaded", Toast.LENGTH_SHORT).show();


                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                Toast.makeText(YouTubeActivity.this, "UnsuccessFull", Toast.LENGTH_SHORT).show();


            }
        });
    }
}