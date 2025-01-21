package com.example.assignmentsvg.presentation.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.assignmentsvg.data.dao.DogImageDao;
import com.example.assignmentsvg.data.entity.DogImageEntity;
import com.example.assignmentsvg.data.model.DogImage;
import com.example.assignmentsvg.domain.api.DogAPI;
import com.example.assignmentsvg.domain.helpers.ImageCache;
import com.example.assignmentsvg.presentation.activity.AssignmentSVG;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GenerateDogsViewmodel extends ViewModel {

    private final MutableLiveData<Bitmap> dogImage = new MutableLiveData<>();


    public LiveData<Bitmap> getDogImage() {
        return dogImage;
    }


    public void fetchRandomDogImage(Context context) {

        DogAPI.DogApiService dogAPI = DogAPI.getInstance().create(DogAPI.DogApiService.class);
        Call<DogImage> dogImageCall = dogAPI.getRandomDogImage();

        dogImageCall.enqueue(new Callback<DogImage>() {
            @Override
            public void onResponse(Call<DogImage> call, Response<DogImage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String imageUrl = response.body().getMessage();
                    String cacheKey = imageUrl;
                    Log.d("TAG", imageUrl);

                    Bitmap cachedBitmap = ImageCache.getFromCache(cacheKey);
                    if (cachedBitmap != null) {
                        dogImage.setValue(cachedBitmap);
                    } else {
                        Glide.with(context)
                                .asBitmap()
                                .load(imageUrl)
                                .into(new CustomTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                        dogImage.setValue(resource);

                                        ImageCache.addToCache(context, cacheKey, imageUrl, AssignmentSVG.getInstance().getDogImageDao());

                                    }

                                    @Override
                                    public void onLoadCleared(Drawable placeholder) {

                                    }
                                });
                    }
                }
            }

            @Override
            public void onFailure(Call<DogImage> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());

            }
        });
    }
}
