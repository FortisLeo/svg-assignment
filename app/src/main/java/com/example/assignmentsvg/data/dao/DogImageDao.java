package com.example.assignmentsvg.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.assignmentsvg.data.entity.DogImageEntity;

import java.util.List;

@Dao
public interface DogImageDao {
    @Insert
    void insertImageCache(DogImageEntity dogImageEntity);

    @Query("SELECT * FROM dog_image WHERE key = :key LIMIT 1")
    DogImageEntity getDogImageCacheByKey(String key);

    @Query("SELECT * FROM dog_image")
    List<DogImageEntity> getAllDogCaches();

    @Query("DELETE FROM dog_image")
    void clearCache();
}
