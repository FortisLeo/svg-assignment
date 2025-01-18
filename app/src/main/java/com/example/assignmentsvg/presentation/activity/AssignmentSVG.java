package com.example.assignmentsvg.presentation.activity;

import android.app.Application;

import com.example.assignmentsvg.domain.helpers.ImageCache;

public class AssignmentSVG extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        ImageCache.initCache(this, 20);

    }
}
