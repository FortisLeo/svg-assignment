package com.example.assignmentsvg.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "dog_image")

public class DogImageEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String key;
    public String encodedBitmap;
}
