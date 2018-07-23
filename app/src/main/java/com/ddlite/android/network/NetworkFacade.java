package com.ddlite.android.network;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static com.ddlite.android.network.NetworkFacade.NetworkError.NETWORK_ERROR;

@WorkerThread
public class NetworkFacade {

    public interface Callback {
        void success(NetworkResponse response);
        void failure(NetworkError error);
    }

    public static void getJsonAsync(@NonNull Uri url, @Nullable Map<String, String> headers, @Nullable final Callback callback) {
        okhttp3.Callback okCallback = new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.failure(new NetworkError(NETWORK_ERROR, "Network Error"));
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    int responseCode = response.code();
                    if (responseCode >= 200 && responseCode <= 299) { // allow response code 2XX
                        callback.success(NetworkResponse.parse(response));
                    } else {
                        callback.failure(new NetworkError(responseCode, "HTTP Error"));
                    }
                }
            }
        };
        if (headers == null) {
            headers = new HashMap<>();
        }
        NetworkExecutor.getInstance().executeAsyncGet(url, headers, okCallback);
    }


    static class AbstractNetworkResponse {

    }

    /**
     * Encapsulates the Network errors, Parsing errors and HttpErrors.
     * errorCode = 100 - 500 are reserved for the standard HTTP errors.
     * errorCode = 600 is used for all the network error
     * errorCode = 700 is used for all the IO errors
     *
     */
    public static class NetworkError extends AbstractNetworkResponse {

        static int NETWORK_ERROR = 600;

        int errorCode;
        String errorMessage;

        NetworkError(int code, String msg) {
            errorCode = code;
            errorMessage = msg;
        }
    }

    /**
     * Encapsulates the Network response.
     * Have helper methods to get the response in String and JSON formats
     */
    public static class NetworkResponse extends AbstractNetworkResponse {
        String response;
        int responseCode;

        static NetworkResponse parse(Response response) throws IOException {
            return new NetworkResponse(1, response.body().string());
        }

        NetworkResponse(int responseCode, String response) {
            this.responseCode = responseCode;
            this.response = response;
        }

        public JSONObject asJSON() throws JSONException {
            return new JSONObject(response);
        }

        public String asString() {
            return response;
        }
    }
}
