package com.manandhiman.sdcproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder>  {

    private final Context context;
    private final ArrayList<Post> postsArrayList;


    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        final Post post = postsArrayList.get(position);
        
        holder.textView.setText(post.getNote());

        holder.itemView.setOnClickListener(v -> downloadfile(post));
        
    }

    private void downloadfile(Post post) {
        Log.v("down",post.getUrl());
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference()
                .child("media/")
                .child(post.getUrl());

        storageRef.getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Log.v("Down", uri.toString());
                    openMediaInWebView(uri);
                });
    }

    private void openMediaInWebView(Uri uri) {
        Intent i = new Intent(context, WebViewActivty.class);
        i.putExtra("uri",uri);
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        
        public ViewHolder(@NonNull View view){
            super(view);
            textView = view.findViewById(R.id.textView2);
        }
    }
    public PostsAdapter(Context context, ArrayList<Post> postsArrayList){
        this.context = context;
        this.postsArrayList = postsArrayList;
    }
}
