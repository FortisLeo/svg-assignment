package com.example.assignmentsvg.presentation.activity;

import android.app.Application;
import android.text.style.AbsoluteSizeSpan;

import com.example.assignmentsvg.data.dao.DogImageDao;
import com.example.assignmentsvg.data.database.AppDatabase;
import com.example.assignmentsvg.domain.helpers.ImageCache;

public class AssignmentSVG extends Application {
    private DogImageDao dogImageDao;
    private static AssignmentSVG instance;


    @Override
    public void onCreate(){
        super.onCreate();
        ImageCache.initCache(this, 20, dogImageDao);

        AppDatabase appDatabase = AppDatabase.getInstance(this);
        dogImageDao = appDatabase.dogImageDao();
    }

    public static AssignmentSVG getInstance() {
        return instance;
    }

    public DogImageDao getDogImageDao() {
        return dogImageDao;
    }
}
