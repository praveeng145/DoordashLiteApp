package com.ddlite.android.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RestaurantAddress implements Parcelable {

    @SerializedName("printable_address")
    public String fullAddress;
    @SerializedName("city")
    public String city;
    @SerializedName("state")
    public String state;
    @SerializedName("street")
    public String street;
    @SerializedName("lat")
    public float lat;
    @SerializedName("lng")
    public float lng;

    protected RestaurantAddress(Parcel in) {
        fullAddress = in.readString();
        city = in.readString();
        state = in.readString();
        street = in.readString();
        lat = in.readFloat();
        lng = in.readFloat();
    }

    public static final Creator<RestaurantAddress> CREATOR = new Creator<RestaurantAddress>() {
        @Override
        public RestaurantAddress createFromParcel(Parcel in) {
            return new RestaurantAddress(in);
        }

        @Override
        public RestaurantAddress[] newArray(int size) {
            return new RestaurantAddress[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullAddress);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(street);
        dest.writeFloat(lat);
        dest.writeFloat(lng);
    }
}
