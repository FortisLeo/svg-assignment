package com.example.assignmentsvg.presentation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Layer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmentsvg.R;

import java.util.List;

public class DogSliderAdapter extends RecyclerView.Adapter<DogSliderAdapter.ViewHolder> {
    private List<Bitmap> cachedImages;
    private final Context context;

    public DogSliderAdapter(List<Bitmap> cachedImages, Context context) {
        this.cachedImages = cachedImages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dog, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int reversePosition = cachedImages.size() - 1 - position;

        holder.ivDogImage.setImageBitmap(cachedImages.get(reversePosition));
    }
    public void updateData(List<Bitmap> newImages) {
        this.cachedImages = newImages;


        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cachedImages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivDogImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivDogImage = itemView.findViewById(R.id.ivDogImage);

        }
    }
}
