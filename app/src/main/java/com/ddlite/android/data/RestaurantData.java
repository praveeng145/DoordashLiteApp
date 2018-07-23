package com.ddlite.android.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class RestaurantData implements Parcelable {

    @SerializedName("phone_number")
    public String phoneNumber;
    @SerializedName("tags")
    public String[] cusineTypes;
    @SerializedName("status")
    public String status;
    @SerializedName("description")
    public String description;
    @SerializedName("name")
    public String name;
    @SerializedName("address")
    public RestaurantAddress address;
    @SerializedName("cover_img_url")
    public String imageUrl;

    protected RestaurantData(Parcel in) {
        phoneNumber = in.readString();
        cusineTypes = in.createStringArray();
        status = in.readString();
        description = in.readString();
        name = in.readString();
        address = in.readParcelable(RestaurantAddress.class.getClassLoader());
        imageUrl = in.readString();
    }

    public static final Creator<RestaurantData> CREATOR = new Creator<RestaurantData>() {
        @Override
        public RestaurantData createFromParcel(Parcel in) {
            return new RestaurantData(in);
        }

        @Override
        public RestaurantData[] newArray(int size) {
            return new RestaurantData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phoneNumber);
        dest.writeStringArray(cusineTypes);
        dest.writeString(status);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeParcelable(address, flags);
        dest.writeString(imageUrl);
    }
}
