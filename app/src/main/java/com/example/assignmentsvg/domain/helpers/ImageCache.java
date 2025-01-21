package com.example.assignmentsvg.domain.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.LruCache;

import androidx.lifecycle.LiveData;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.example.assignmentsvg.data.dao.DogImageDao;
import com.example.assignmentsvg.data.entity.DogImageEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class ImageCache {
    private static LruCache<String, Bitmap> memoryCache;
    private static final String TAG = "ImageCache";

    /**
     * Initialize LruCache and load cached file paths from Room DB
     */
    public static void initCache(Context context, int maxImages, DogImageDao dogImageDao) {
        if (memoryCache == null) {
            memoryCache = new LruCache<String, Bitmap>(maxImages) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount() / 1024; // Size in KB
                }
            };
            restoreCache(dogImageDao);
        }
    }

    /**
     * Add image to cache (Glide for caching and Room for file path storage)
     */
    public static void addToCache(Context context, String key, String imageUrl, DogImageDao dogImageDao) {
        if (getFromCache(key) == null) {
            Executors.newSingleThreadExecutor().execute(() -> {
                try {
                    // Get cached file path using Glide
                    FutureTarget<File> future = Glide.with(context)
                            .downloadOnly()
                            .load(imageUrl)
                            .submit();
                    File cachedFile = future.get();

                    if (cachedFile != null) {
                        String filePath = cachedFile.getAbsolutePath();
                        Log.d(TAG, "Image cached at: " + filePath);

                        // Save file path in Room DB
                        DogImageEntity entity = new DogImageEntity();
                        entity.key = key;
                        entity.filePath = filePath;
                        dogImageDao.insert(entity);

                        // Add image to memory cache
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        if (bitmap != null) {
                            memoryCache.put(key, bitmap);
                            Log.d(TAG, "Image added to memory cache: " + key);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error caching image: ", e);
                }
            });
        }
    }

    /**
     * Restore LruCache from Room DB
     */
    private static void restoreCache(DogImageDao dogImageDao) {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<DogImageEntity> cachedImages = dogImageDao.getAllDogCaches();
                for (DogImageEntity entity : cachedImages) {
                    File file = new File(entity.filePath);
                    if (file.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(entity.filePath);
                        if (bitmap != null) {
                            memoryCache.put(entity.key, bitmap);
                        }
                    }
                }
                Log.d(TAG, "Cache restored from Room DB.");
            } catch (Exception e) {
                Log.e(TAG, "Error restoring cache: ", e);
            }
        });
    }

    /**
     * Get image from LruCache
     */
    public static Bitmap getFromCache(String key) {
        return memoryCache.get(key);
    }

    /**
     * Clear memory cache and Room DB
     */
    public static void clearCache(DogImageDao dogImageDao) {
        if (memoryCache != null) {
            memoryCache.evictAll();
        }
        Executors.newSingleThreadExecutor().execute(dogImageDao::deleteAll);
        Log.d(TAG, "Cache cleared.");
    }

    public static List<Bitmap> getAllFromCache() {
        List<Bitmap> cachedImages = new ArrayList<>();

        if (memoryCache != null) {
            for (String key : memoryCache.snapshot().keySet()) {
                Bitmap bitmap = memoryCache.get(key);
                if (bitmap != null) {
                    cachedImages.add(bitmap);
                }
            }
        }
        return cachedImages;
    }
}
