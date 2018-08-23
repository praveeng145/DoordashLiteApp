package com.ddlite.android.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.ddlite.android.R;

public class RestaurantListActivityV2 extends FragmentActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ddl_restaurant_list_v2);

        bottomNavigationView = findViewById(R.id.bottom_navigationview);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.food:
                        Toast.makeText(RestaurantListActivityV2.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.orders:
                        Toast.makeText(RestaurantListActivityV2.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drinks:
                        Toast.makeText(RestaurantListActivityV2.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }
}
