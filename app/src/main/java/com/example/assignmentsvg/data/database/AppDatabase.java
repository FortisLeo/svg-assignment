package com.example.assignmentsvg.data.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.assignmentsvg.data.dao.DogImageDao;
import com.example.assignmentsvg.data.entity.DogImageEntity;

@Database(entities = {DogImageEntity.class}, version = 1)

public abstract class AppDatabase extends RoomDatabase {
    public abstract DogImageDao dogImageDao();

}
