package com.ddlite.android.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RestaurantListData implements Parcelable {

    @SerializedName("name")
    public String title;
    @SerializedName("status")
    public String status;
    @SerializedName("average_rating")
    public String rating;
    @SerializedName("address")
    public RestaurantAddress address;
    @SerializedName("cover_img_url")
    public String imageUrl;
    @SerializedName("id")
    public String restaurantId;

    protected RestaurantListData(Parcel in) {
        title = in.readString();
        status = in.readString();
        rating = in.readString();
        address = in.readParcelable(RestaurantAddress.class.getClassLoader());
        imageUrl = in.readString();
        restaurantId = in.readString();
    }

    public static final Creator<RestaurantListData> CREATOR = new Creator<RestaurantListData>() {
        @Override
        public RestaurantListData createFromParcel(Parcel in) {
            return new RestaurantListData(in);
        }

        @Override
        public RestaurantListData[] newArray(int size) {
            return new RestaurantListData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(status);
        dest.writeString(rating);
        dest.writeParcelable(address, flags);
        dest.writeString(imageUrl);
        dest.writeString(restaurantId);
    }
}
