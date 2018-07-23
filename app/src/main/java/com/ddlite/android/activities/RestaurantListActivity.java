package com.ddlite.android.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.ddlite.android.R;
import com.ddlite.android.presenter.RestaurantListPresenter;
import com.ddlite.android.utils.LocationUtils;

public class RestaurantListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RestaurantListPresenter restaurantListPresenter;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    private boolean refreshList = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ddl_activity_restaurant_list);
        setupToolbar(findViewById(R.id.toolbar));

        restaurantListPresenter = new RestaurantListPresenter(this);

        RecyclerView recyclerView = findViewById(R.id.restaurantList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(restaurantListPresenter.getRestaurantListAdapter());
        DividerItemDecoration decoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            decoration.setDrawable(getResources().getDrawable(R.drawable.drawable_divider, getTheme()));
        } else {
            decoration.setDrawable(getResources().getDrawable(R.drawable.drawable_divider));
        }
        recyclerView.addItemDecoration(decoration);

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        refreshList = true;
    }

    private void setupToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refreshList) {
            restaurantListPresenter.loadData(LocationUtils.getLastKnownLocation());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        restaurantListPresenter.writeData();
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

    public void setSwipeRefresh(boolean refresh) {
        swipeRefreshLayout.setRefreshing(refresh);
    }

    public void showErrorDialog(String title) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(title);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public void launchRestaurantInfoActivity(String restaurantId) {
        refreshList = false;
        Intent intent = new Intent(this, RestaurantInfoActivity.class);
        intent.putExtra(RestaurantInfoActivity.INTENT_EXTRA_RESTAURANT_ID, restaurantId);
        startActivity(intent);
    }

    @Override
    public void onRefresh() {
        restaurantListPresenter.loadData(LocationUtils.getLastKnownLocation());
    }
}
