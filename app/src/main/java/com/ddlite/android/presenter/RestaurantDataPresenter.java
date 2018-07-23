package com.ddlite.android.presenter;

import android.net.Uri;

import com.ddlite.android.activities.RestaurantInfoActivity;
import com.ddlite.android.data.RestaurantData;
import com.ddlite.android.network.NetworkFacade;
import com.google.gson.GsonBuilder;

public class RestaurantDataPresenter extends BasePresenter {

    RestaurantInfoActivity restaurantInfoActivity;

    public RestaurantDataPresenter(RestaurantInfoActivity activity) {
        restaurantInfoActivity = activity;
    }

    public void getRestaurantInfo(String restaurantId) {
        Uri uri = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY).path(PATH + restaurantId)
                .build();
        NetworkFacade.getJsonAsync(uri, null, new NetworkFacade.Callback() {

            @Override
            public void success(NetworkFacade.NetworkResponse response) {
                RestaurantData data = new GsonBuilder().create().fromJson(response.asString(), RestaurantData.class);
                updateData(data);
                hideProgress();
            }

            @Override
            public void failure(NetworkFacade.NetworkError error) {
                hideProgress();
            }
        });
        showProgress();
    }

    private void showProgress() {
        restaurantInfoActivity.runOnUiThread(() -> restaurantInfoActivity.showProgressBar());
    }

    private void hideProgress() {
        restaurantInfoActivity.runOnUiThread(() -> restaurantInfoActivity.dismissProgressBar());
    }

    private void updateData(final RestaurantData data) {
        restaurantInfoActivity.runOnUiThread(() -> {
                    restaurantInfoActivity.setRestaurantName(data.name);
                    if (data.description != null) {
                        restaurantInfoActivity.setRestaurantDescription(data.description);
                    }
                    /*if (data.cusineTypes != null) {
                        restaurantInfoActivity.setRestaurantCusine(Arrays.toString(data.cusineTypes));
                    }*/
                    if (data.address != null) {
                        restaurantInfoActivity.setRestaurantAddress(data.address.fullAddress);
                    }
                    if (data.phoneNumber != null) {
                        restaurantInfoActivity.setRestaurantPhone(data.phoneNumber);
                    }
                    if (data.status != null) {
                        restaurantInfoActivity.setRestaurantStatus(data.status);
                    }
                    if (data.imageUrl != null) {
                        restaurantInfoActivity.setRestaurantImage(data.imageUrl);
                    }
                }
        );
    }
}
