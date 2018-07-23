package com.ddlite.android.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddlite.android.R;
import com.ddlite.android.data.RestaurantListData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.RestaurantDataItemViewHolder> {

    public interface RestaurantListAdapterListener {
        void loadMoreItems();
        void onItemClick(RestaurantListData data);
    }

    private int numItems;
    private List<RestaurantListData> restaurantListData;
    private RestaurantListAdapterListener listAdapterListener;

    private final static int VIEW_TYPE_ROW = 1;
    private final static int VIEW_TYPE_LOAD_MORE = 2;
    private final static String TAG = "RestaurantListAdapter";

    public RestaurantListAdapter() {
        restaurantListData = new ArrayList<>();
        numItems = 0;
    }

    public void setRestaurantListAdapterListener(RestaurantListAdapterListener listener) {
        listAdapterListener = listener;
    }

    @NonNull
    @Override
    public RestaurantDataItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_ROW:
                return new RowItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.ddl_layout_list_item, parent, false));
            default:
            case VIEW_TYPE_LOAD_MORE:
                return new LoadMore(LayoutInflater.from(parent.getContext()).inflate(R.layout.ddl_layout_load_more, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantDataItemViewHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return numItems; // an additional row to show loading more items
    }

    @Override
    public int getItemViewType(int position) {
        return (position == (numItems - 1)) ? VIEW_TYPE_LOAD_MORE : VIEW_TYPE_ROW;
    }

    public void setRestaurantDataList(List<RestaurantListData> restaurantList) {
        this.restaurantListData = restaurantList;
        numItems = restaurantListData.size() + 1;
        Log.d(TAG, "Num of restaurants in the list : " + (numItems-1));
    }

    public void addRestaurantDataList(List<RestaurantListData> restaurantList) {
        this.restaurantListData.addAll(restaurantList);
        numItems = restaurantListData.size() + 1;
        Log.d(TAG, "Num of restaurants in the list : " + (numItems-1));
    }

    public void clear() {
        this.restaurantListData.clear();
    }

    public List<RestaurantListData> getRestaurantDataList() {
        return restaurantListData;
    }

    abstract class RestaurantDataItemViewHolder extends RecyclerView.ViewHolder {

        public RestaurantDataItemViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData(int position);
    }

    class RowItem extends RestaurantDataItemViewHolder {

        ImageView restaurantImage;
        TextView restaurantTitle;
        TextView restaurantAddress;
        TextView restaurantRatings;
        TextView restaurantStatus;

        public RowItem(View itemView) {
            super(itemView);
            restaurantImage = itemView.findViewById(R.id.restaurant_img);
            restaurantTitle = itemView.findViewById(R.id.restaurant_title);
            restaurantAddress = itemView.findViewById(R.id.restaurant_address);
            restaurantRatings = itemView.findViewById(R.id.restaurant_ratings);
            restaurantStatus = itemView.findViewById(R.id.restaurant_status);
            itemView.setOnClickListener(v -> listAdapterListener.onItemClick(getRestaurantDataList().get(getAdapterPosition())));
        }

        @Override
        public void bindData(int position) {
            RestaurantListData data = getRestaurantDataList().get(position);
            restaurantTitle.setText(data.title);
            restaurantAddress.setText(data.address.fullAddress);
            restaurantRatings.setText("Rating : " + data.rating);
            restaurantStatus.setText(data.status);
            Picasso.get().load(data.imageUrl).error(R.drawable.ic_doordash).into(restaurantImage);
        }
    }

    class LoadMore extends RestaurantDataItemViewHolder {

        public LoadMore(View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(int position) {
            listAdapterListener.loadMoreItems();
        }
    }
}
