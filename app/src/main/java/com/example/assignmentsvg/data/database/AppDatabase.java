package com.example.assignmentsvg.data.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.assignmentsvg.data.dao.DogImageDao;
import com.example.assignmentsvg.data.entity.DogImageEntity;
import com.example.assignmentsvg.presentation.activity.AssignmentSVG;

@Database(entities = {DogImageEntity.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract DogImageDao dogImageDao();
    private static volatile AppDatabase INSTANCE;


    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .fallbackToDestructiveMigration() // Handle version migrations
                            .build();
                }
            }
        }
        return INSTANCE;
    }


}
