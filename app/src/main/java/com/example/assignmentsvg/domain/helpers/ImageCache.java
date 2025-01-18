package com.example.assignmentsvg.domain.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.util.LruCache;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ImageCache {
    private static LruCache<String, Bitmap> memoryCache;
    private static final String PREF_NAME = "ImageCachePreferences";
    private static final String CACHE_KEY = "ImageCache";
    public static void initCache(Context context, int maxImages) {
        if (memoryCache == null) {
            memoryCache = new LruCache<String, Bitmap>(maxImages) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return 1;
                }
            };
            restoreCache(context);

        }



    }

    public static void addToCache(Context context, String key, Bitmap bitmap) {
        if (getFromCache(key) == null) {
            memoryCache.put(key, bitmap);
            saveCacheToPreferences(context);

        }
    }

    private static void saveCacheToPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        List<String> encodedImages = new ArrayList<>();
        for (String key : memoryCache.snapshot().keySet()) {
            Bitmap bitmap = memoryCache.get(key);
            if (bitmap != null) {
                encodedImages.add(key + ":" + encodeBitmapToBase64(bitmap));
            }
        }

        editor.putStringSet(CACHE_KEY, new HashSet<>(encodedImages));
        editor.apply();
    }

    private static void restoreCache(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<String> encodedImages = new ArrayList<>(sharedPreferences.getStringSet(CACHE_KEY, new HashSet<>()));

        for (String entry : encodedImages) {
            String[] splitEntry = entry.split(":", 3);
            if (splitEntry.length == 3) {
                String key = splitEntry[0] + ":" + splitEntry[1];
                Bitmap bitmap = decodeBase64ToBitmap(splitEntry[2]);
                if (bitmap != null) {
                    memoryCache.put(key, bitmap);
                }
            }
        }
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
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            sharedPreferences.edit().remove(CACHE_KEY).apply();
        }
    }
    public static List<Bitmap> getAll() {
        if (memoryCache != null) {
            List<Bitmap> cachedImages = new ArrayList<>();
            for (String key : memoryCache.snapshot().keySet()) {
                cachedImages.add(memoryCache.get(key));
            }
            return cachedImages;
        }
        return new ArrayList<>();
    }



}

