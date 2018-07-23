package com.ddlite.android.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CacheUtils {

    private static final String TAG = "CacheUtils";
    private static final String FILE_CACHED_RESTAURANTS = "restaurants_list.txt";

    public static void putRestaurants(@NonNull Context context, String json) {
        if (context == null) {
            Log.e("TAG", "Context is killed, cannot cache the data");
            return;
        }
        try {
            File file = new File(context.getExternalFilesDir(null), FILE_CACHED_RESTAURANTS);
            if (file.exists()) {
                file.delete();
            }
            InputStream is = new ByteArrayInputStream(json.getBytes());
            OutputStream os = new FileOutputStream(file);
            byte[] data = new byte[is.available()];
            is.read(data);
            os.write(data);
            is.close();
            os.close();
            Log.e("TAG", "Cached data successfully to file at " + file.getAbsolutePath());
        } catch (IOException ie) {
            Log.e("TAG", "Failed to cache the data");
        }
    }

    public static String getRestaurants(@NonNull Context context) {
        if (context == null) {
            Log.e("TAG", "Context is null, cannot read cached data");
            return null;
        }
        try {
            File file = new File(context.getExternalFilesDir(null), FILE_CACHED_RESTAURANTS);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            Log.e("TAG", "Read the cached data successfully from file at " + file.getAbsolutePath());
            bufferedReader.close();
            return sb.toString();
        } catch (IOException ie) {
            Log.e("TAG", "Failed to cache the data");
            return null;
        }
    }
}
