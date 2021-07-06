package com.hj.homecleanproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hj.homecleanproject.customInterface.ImageViewClickListener;
import com.hj.homecleanproject.customInterface.ImageViewLongClickListener;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyViewHolder> {

    private ArrayList<Bitmap> myPhoto = new ArrayList<>();

    private ImageViewLongClickListener listener;
    private ImageViewClickListener imageViewClickListener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_my_picture,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageBitmap(myPhoto.get(position));
        holder.imageView.setOnLongClickListener(v ->{
            if(listener != null){
                listener.OnImageViewLongClickListener(position);
            }
            return false;
        });
        holder.imageView.setOnClickListener(v ->{
            if(imageViewClickListener != null){
                imageViewClickListener.adapterToFragment(null,position,holder.imageView);
            }
        });
    }

    public Bitmap get(int position){
        return myPhoto.get(position);
    }

    @Override
    public int getItemCount() {
        return myPhoto.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.galleryImageView);
        }
    }

    public void addItem(Bitmap bitmap){
        myPhoto.add(bitmap);
    }

    public void removeItem(int position){
        myPhoto.remove(position);
    }

    public void setOnImageViewLongClickListener(ImageViewLongClickListener listener){
        this.listener = listener;
    }
    public void setOnImageViewClickListener(ImageViewClickListener imageViewClickListener){
        this.imageViewClickListener = imageViewClickListener;
    }
}
