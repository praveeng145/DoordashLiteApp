package com.ddlite.android.presenter;

import android.location.Location;
import android.net.Uri;
import android.util.Log;

import com.ddlite.android.activities.RestaurantListActivity;
import com.ddlite.android.adapters.RestaurantListAdapter;
import com.ddlite.android.data.RestaurantListData;
import com.ddlite.android.network.NetworkFacade;
import com.ddlite.android.utils.CacheUtils;
import com.ddlite.android.utils.Utils;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestaurantListPresenter extends BasePresenter implements RestaurantListAdapter.RestaurantListAdapterListener {

    final static String Q_P_LIMIT = "limit";
    final static String Q_P_OFFSET = "offset";
    final static String Q_P_LAT = "lat";
    final static String Q_P_LANG = "lng";

    private final static String TAG = "RestaurantListPrsntr";
    private RestaurantListAdapter restaurantListAdapter;
    private int offset;

    private RestaurantListActivity restaurantListActivity;
    private Location location;

    public RestaurantListPresenter(RestaurantListActivity activity) {
        restaurantListActivity = activity;
        restaurantListAdapter = new RestaurantListAdapter();
        restaurantListAdapter.setRestaurantListAdapterListener(this);
    }

    public RestaurantListAdapter getRestaurantListAdapter() {
        return restaurantListAdapter;
    }

    public void loadData(Location location) {
        this.location = location;
        this.offset = 0;
        if (Utils.isNetworkAvailable(restaurantListActivity.getApplicationContext())) {
            getRestaurantList(location, offset);
            notifyState(PresenterState.NETWORK_REQUEST_START);
        } else {
            List<RestaurantListData> cachedData = readData();
            if (cachedData != null && !cachedData.isEmpty()) {
                restaurantListAdapter.setRestaurantDataList(cachedData);
                restaurantListActivity.runOnUiThread(() -> {
                    restaurantListAdapter.notifyDataSetChanged();
                    restaurantListActivity.showToast("No network connection. Showing cached data");
                });
            } else {
                restaurantListActivity.runOnUiThread(() -> {
                    restaurantListActivity.dismissProgressBar();
                    restaurantListActivity.setSwipeRefresh(false);
                    restaurantListActivity.showErrorDialog("There is no network connection and cached data. Sorry! can't do much");
                });
            }
        }
    }

    public void getRestaurantList(Location location, final int offset) {
        Uri uri = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY).path(PATH)
                .appendQueryParameter(Q_P_LANG, String.valueOf(location.getLongitude()))
                .appendQueryParameter(Q_P_LAT, String.valueOf(location.getLatitude()))
                .appendQueryParameter(Q_P_OFFSET, String.valueOf(offset))
                .appendQueryParameter(Q_P_LIMIT, String.valueOf(10))
                .build();
        NetworkFacade.getJsonAsync(uri, null, new NetworkFacade.Callback() {
            @Override
            public void success(NetworkFacade.NetworkResponse response) {
                try {
                    RestaurantListData[] data = new GsonBuilder().create().fromJson(response.asString(), RestaurantListData[].class);
                    if (offset == 0) {
                        restaurantListAdapter.setRestaurantDataList(new ArrayList(Arrays.asList(data)));
                    } else {
                        restaurantListAdapter.addRestaurantDataList(Arrays.asList(data));
                    }
                    notifyState(PresenterState.NETWORK_REQUEST_END);
                } catch (JsonSyntaxException je) {
                    Log.e(TAG, "Error while fetching data " + je.getMessage());
                    notifyState(PresenterState.NETWORK_ERROR);
                }
            }

            @Override
            public void failure(NetworkFacade.NetworkError error) {
                // TODO : handle failures
                notifyState(PresenterState.NETWORK_ERROR);
            }
        });
    }

    @Override
    public void loadMoreItems() {
        if (Utils.isNetworkAvailable(restaurantListActivity.getApplicationContext())) {
            getRestaurantList(this.location, ++offset);
        } else {
            restaurantListActivity.runOnUiThread(() -> restaurantListActivity.showToast("No Network connection. Cannot fetch more data."));
        }
    }

    @Override
    public void onItemClick(RestaurantListData data) {
        if (Utils.isNetworkAvailable(restaurantListActivity.getApplicationContext())) {
            restaurantListActivity.launchRestaurantInfoActivity(data.restaurantId);
        } else {
            restaurantListActivity.runOnUiThread(() -> restaurantListActivity.showToast("No Network connection. Cannot load restaurant info at the moment."));
        }
    }

    private void notifyState(PresenterState state) {
        switch (state) {
            case NETWORK_REQUEST_START:
                restaurantListActivity.runOnUiThread(() -> restaurantListActivity.showProgressBar());
                break;
            case NETWORK_REQUEST_END:
                restaurantListActivity.runOnUiThread(() -> {
                    restaurantListActivity.dismissProgressBar();
                    restaurantListActivity.setSwipeRefresh(false);
                    restaurantListAdapter.notifyDataSetChanged();
                    restaurantListActivity.showToast("Showing list of restaurants near Palo Alto");
                });
                break;
            case NETWORK_ERROR:
                restaurantListActivity.runOnUiThread(() -> {
                    restaurantListActivity.dismissProgressBar();
                    restaurantListActivity.setSwipeRefresh(false);
                    restaurantListActivity.showErrorDialog("Error while fetch data.");
                });
                break;
        }
    }

    public void writeData() {
        List<RestaurantListData> list = restaurantListAdapter.getRestaurantDataList();
        RestaurantListData[] array = new RestaurantListData[list.size()];
        CacheUtils.putRestaurants(restaurantListActivity, new GsonBuilder().create().toJson(list.toArray(array), RestaurantListData[].class));
    }

    public List<RestaurantListData> readData() {
        String cachedJson = CacheUtils.getRestaurants(restaurantListActivity);
        if (cachedJson != null && !cachedJson.isEmpty()) {
            try {
                RestaurantListData[] data = new GsonBuilder().create().fromJson(cachedJson, RestaurantListData[].class);
                return new ArrayList(Arrays.asList(data));
            } catch (JsonSyntaxException jse) {
                Log.e(TAG, "Error while parsing the cached data");
            }
        }
        return null;
    }
}
