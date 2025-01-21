package com.example.assignmentsvg.presentation.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.assignmentsvg.domain.helpers.ImageCache;
import com.example.assignmentsvg.presentation.activity.AssignmentSVG;

import java.util.List;

public class RecentlyGeneratedDogsViewModel extends ViewModel {

    private MutableLiveData<List<Bitmap>> dogImagesLive= new MutableLiveData<>();

    public void fetchCachedImages() {
        List<Bitmap> cachedImages = ImageCache.getAllFromCache();
        dogImagesLive.setValue(cachedImages);
    }

    public void clearCache(Context context) {
        ImageCache.clearCache(AssignmentSVG.getInstance().getDogImageDao());
        fetchCachedImages();
    }

    public LiveData<List<Bitmap>> getDogImagesLiveData() {
        return dogImagesLive;
    }
}
