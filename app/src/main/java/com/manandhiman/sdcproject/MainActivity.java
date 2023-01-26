package com.manandhiman.sdcproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.intentfilter.androidpermissions.NotificationSettings;
import com.intentfilter.androidpermissions.PermissionManager;
import com.manandhiman.sdcproject.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Uri uri;
    StorageReference storageReference;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonSelectImage.setOnClickListener(v -> selectMedia("image/"));
        binding.buttonSelectVideo.setOnClickListener(v -> selectMedia("video/"));
        binding.buttonSelectAudio.setOnClickListener(v -> selectMedia("audio/"));

        binding.buttonUpload.setOnClickListener(v -> uploadMedia());
    }

    private void selectMedia(String mimeType) {
        int code = 0;

        if(mimeType.equals("image/")){
            binding.imageView.setVisibility(View.VISIBLE);
            binding.videoView.setVisibility(View.GONE);
            code =100;
        }else if(mimeType.equals("video/")){
            code = 101;
            binding.imageView.setVisibility(View.GONE);
            binding.videoView.setVisibility(View.VISIBLE);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(binding.videoView);
            binding.videoView.setMediaController(mediaController);
        }else if(mimeType.equals("audio/")){
            binding.imageView.setVisibility(View.GONE);
            binding.videoView.setVisibility(View.GONE);
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
                binding.imageView.setImageURI(uri);
            }
            else if(requestCode == 101){
                binding.videoView.setVideoURI(uri);
            }else if(requestCode == 102){
            }
        }
        binding.buttonUpload.setVisibility(View.VISIBLE);
    }

    private void uploadMedia() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        Date now = new Date();
        String fileName = format.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("images/"+fileName);

        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            uri = null;
            Toast.makeText(MainActivity.this,"File Uploaded Successfully", Toast.LENGTH_SHORT).show();
            hideAllViews();
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Failed to Upload",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideAllViews(){
        binding.buttonUpload.setVisibility(View.GONE);
        binding.videoView.setVisibility(View.GONE);
        binding.imageView.setVisibility(View.GONE);
    }

}