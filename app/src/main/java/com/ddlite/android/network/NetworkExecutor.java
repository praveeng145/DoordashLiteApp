package com.ddlite.android.network;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;

class NetworkExecutor {

    private static final String TAG = "NetworkExecutor";
    static NetworkExecutor networkExecutorInstance;

    private OkHttpClient okHttpClient;

    private NetworkExecutor() {
        okHttpClient = new OkHttpClient.Builder().build();
    }

    private synchronized static void createInstance() {
        if (networkExecutorInstance == null) {
            networkExecutorInstance = new NetworkExecutor();
        }
    }

    static NetworkExecutor getInstance() {
        if (networkExecutorInstance == null) {
            createInstance();
        }
        return networkExecutorInstance;
    }

    void executeAsyncGet(@NonNull Uri url, @Nullable Map<String, String> headers, @Nullable Callback callback) {
        Log.d(TAG, "Executing request : " + url.toString());
        Headers okHeaders = Headers.of(headers);
        Request request = new Request.Builder().headers(okHeaders).url(url.toString()).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

}
