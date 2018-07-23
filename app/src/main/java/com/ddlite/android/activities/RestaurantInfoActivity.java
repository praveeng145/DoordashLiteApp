package com.ddlite.android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.ddlite.android.R;
import com.ddlite.android.presenter.RestaurantDataPresenter;
import com.squareup.picasso.Picasso;

public class RestaurantInfoActivity extends AppCompatActivity {

    public final static String INTENT_EXTRA_RESTAURANT_ID = "restaurant_id";

    private String restaurantId;
    private ProgressDialog progressDialog;
    private TextView restaurantName;
    private TextView restaurantDesc;
    private TextView restaurantCusine;
    private TextView restaurantPhone;
    private TextView restaurantAddress;
    private TextView restaurantStatus;
    private ImageView restaurantImage;

    private RestaurantDataPresenter restaurantDataPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ddl_activity_restaurant_info);

        Intent intent = getIntent();
        if (intent != null) {
            restaurantId = intent.getStringExtra(INTENT_EXTRA_RESTAURANT_ID);
            if (null == restaurantId || restaurantId.isEmpty()) {
                showErrorDialog("Unable to load Restaurant Info at the moment.");
                return;
            }
        } else {
            showErrorDialog("Unable to load Restaurant Info at the moment.");
            return;
        }

        restaurantName = findViewById(R.id.restaurant_value);
        restaurantDesc = findViewById(R.id.description_value);
        restaurantCusine = findViewById(R.id.cusine_value);
        restaurantPhone = findViewById(R.id.phone_value);
        restaurantAddress = findViewById(R.id.address_value);
        restaurantStatus = findViewById(R.id.status_value);
        restaurantImage = findViewById(R.id.restaurant_img);

        restaurantDataPresenter = new RestaurantDataPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        restaurantDataPresenter.getRestaurantInfo(restaurantId);
    }

    private void showErrorDialog(String title) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(title);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
    }

    public void showProgressBar() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Please wait while loading restaurants..");

        }
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void dismissProgressBar() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void setRestaurantName(String name) {
        restaurantName.setText(name);
    }

    public void setRestaurantAddress(String address) {
        restaurantAddress.setText(address);
    }

    public void setRestaurantPhone(String phone) {
        restaurantPhone.setText(phone);
    }

    public void setRestaurantStatus(String status) {
        restaurantStatus.setText(status);
    }

    public void setRestaurantCusine(String cusineType) {
        restaurantCusine.setText(cusineType);
    }

    public void setRestaurantDescription(String description) {
        restaurantDesc.setText(description);
    }

    public void setRestaurantImage(String url) {
        Picasso.get().load(url).error(R.drawable.ic_doordash).into(restaurantImage);
    }
}
