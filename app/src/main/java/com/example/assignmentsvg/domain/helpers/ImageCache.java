package com.example.assignmentsvg.domain.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;

import androidx.room.Room;

import com.example.assignmentsvg.data.database.AppDatabase;
import com.example.assignmentsvg.data.entity.DogImageEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ImageCache {
    private static LruCache<String, Bitmap> memoryCache;
    private static AppDatabase database;

    public static void initCache(Context context, int maxImages) {
        if (memoryCache == null) {
            memoryCache = new LruCache<String, Bitmap>(maxImages) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return 1;
                }
            };
            database = Room.databaseBuilder(context, AppDatabase.class, "dog_image_db").build();
            restoreCache(context);
        }
    }

    public static void addToCache(Context context, String key, Bitmap bitmap) {
        if (getFromCache(key) == null) {
            memoryCache.put(key, bitmap);
            saveCacheToDatabase(context);
        }
    }

    private static void saveCacheToDatabase(Context context) {
        new Thread(() -> {
            List<DogImageEntity> imageCacheEntities = new ArrayList<>();
            for (String key : memoryCache.snapshot().keySet()) {
                Bitmap bitmap = memoryCache.get(key);
                if (bitmap != null) {
                    String encodedBitmap = encodeBitmapToBase64(bitmap);
                    DogImageEntity entity = new DogImageEntity();
                    entity.key = key;
                    entity.encodedBitmap = encodedBitmap;
                    imageCacheEntities.add(entity);
                }
            }
            database.dogImageDao().insertImageCache(imageCacheEntities.toArray(new DogImageEntity[0]));
        }).start();
    }

    private static void restoreCache(Context context) {
        new Thread(() -> {
            List<DogImageEntity> entities = database.dogImageDao().getAllDogCaches();
            for (DogImageEntity entity : entities) {
                Bitmap bitmap = decodeBase64ToBitmap(entity.encodedBitmap);
                if (bitmap != null) {
                    memoryCache.put(entity.key, bitmap);
                }
            }
        }).start();
    }

    private static String encodeBitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private static Bitmap decodeBase64ToBitmap(String encodedString) {
        try {
            byte[] decodedBytes = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeStream(new ByteArrayInputStream(decodedBytes));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getFromCache(String key) {
        return memoryCache.get(key);
    }

    public static void clearCache(Context context) {
        if (memoryCache != null) {
            memoryCache.evictAll();
            new Thread(() -> database.dogImageDao().clearCache()).start();
        }
    }

    public static List<Bitmap> getAll() {
        List<Bitmap> cachedImages = new ArrayList<>();
        if (memoryCache != null) {
            for (String key : memoryCache.snapshot().keySet()) {
                cachedImages.add(memoryCache.get(key));
            }
        }
        return cachedImages;
    }
}