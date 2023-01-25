package com.manandhiman.sdcproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button buttonSelectImage, buttonSelectVideo, buttonSelectAudio, buttonUpload;
    ImageView imageView;
    VideoView videoView;

    Uri uri;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSelectImage = findViewById(R.id.buttonSelectImage);
        buttonSelectVideo = findViewById(R.id.buttonSelectVideo);
        buttonSelectAudio = findViewById(R.id.buttonSelectAudio);
        buttonUpload = findViewById(R.id.buttonUpload);

        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);

        buttonSelectImage.setOnClickListener(v -> selectMedia("image/"));
        buttonSelectVideo.setOnClickListener(v -> selectMedia("video/"));
        buttonSelectAudio.setOnClickListener(v -> selectMedia("audio/"));

        buttonUpload.setOnClickListener(v -> uploadMedia());

    }

    private void selectMedia(String mimeType) {
        int code = 0;
        if(mimeType.equals("image/")){
            imageView.setVisibility(View.VISIBLE);
            videoView.setVisibility(View.GONE);
            code =100;
        }else if(mimeType.equals("video/")){
            code = 101;
            imageView.setVisibility(View.GONE);
            videoView.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
        }else if(mimeType.equals("audio/")){
            code = 102;
        }

        Intent intent = new Intent();
        intent.setType(mimeType).setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(data !=null && data.getData() != null){
            uri = data.getData();
            if(requestCode == 100){
                imageView.setImageURI(uri);
            }
            else if(requestCode == 101){
                videoView.setVideoURI(uri);
            }

        }
        buttonUpload.setVisibility(View.VISIBLE);
        Log.v("APP", String.valueOf(uri));
        Log.v("APP", String.valueOf(data));
    }

    private void uploadMedia() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date now = new Date();
        String fileName = format.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);

        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            uri = null;
            Toast.makeText(MainActivity.this,"File Uploaded Successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();
            }
        });

    }




}