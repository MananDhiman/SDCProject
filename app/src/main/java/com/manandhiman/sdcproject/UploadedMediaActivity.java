package com.manandhiman.sdcproject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.manandhiman.sdcproject.databinding.ActivityUploadedMediaBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UploadedMediaActivity extends AppCompatActivity {

    StorageReference storageReference = FirebaseStorage.getInstance().getReference("media/");
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notes");
    ActivityUploadedMediaBinding binding;
    public static ArrayList<Post> postsArrayList = new ArrayList<>();
    private PostsAdapter postsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadedMediaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        postsArrayList.clear();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //logExistingMedia();
        logExistingDBEntries();
    }

    private void logExistingDBEntries() {
        databaseReference = FirebaseDatabase.getInstance().getReference("notes");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
//                    Log.v("TAG Key",""+ childDataSnapshot.getKey());
//                    Log.v("TAG value",""+ childDataSnapshot.getValue());

                    Map<String, Object> map = (HashMap<String, Object>) childDataSnapshot.getValue();
                    if (map==null) return;

                    Post post = new Post();
                    post.setUrl(Objects.requireNonNull(map.get("url")).toString());
                    post.setNote(Objects.requireNonNull(map.get("note")).toString());
                    postsArrayList.add(post);
                }
                postsAdapter = new PostsAdapter(UploadedMediaActivity.this, postsArrayList);
                binding.recyclerView.setAdapter(postsAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void logExistingMedia() {
        storageReference.listAll().addOnSuccessListener(listResult -> {
            for(StorageReference prefix: listResult.getPrefixes()){
                Log.v("listresult prefix", String.valueOf(prefix));
            }
            for(StorageReference item: listResult.getItems()){
                Log.v("listresult item", String.valueOf(item));
            }
        })
                .addOnFailureListener(e -> Toast.makeText(UploadedMediaActivity.this, "Some Error Occured", Toast.LENGTH_SHORT).show());
    }


}
