package com.fashion.forlempopoli.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.fashion.forlempopoli.Adapter.NotificationAdapter;
import com.fashion.forlempopoli.Entity.request.NotificationRequest;
import com.fashion.forlempopoli.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationPanelActivity extends AppCompatActivity {

    RecyclerView notification_rV;
    NotificationAdapter notificationAdapter;

    List<NotificationRequest> notificationRequests;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout ll_Data_Found,ll_NoData_Found;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_panel);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        notification_rV = findViewById(R.id.notification_rV);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        ll_Data_Found = findViewById(R.id.ll_Data_Found);
        ll_NoData_Found = findViewById(R.id.ll_NoData_Found);

        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Notifications");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        layoutManager = new LinearLayoutManager(this);

        notification_rV.setLayoutManager(layoutManager);

        notificationRequests = new ArrayList<>();

        //Adding Data into ArrayList
        notificationRequests.add(new NotificationRequest("Notification 1"));
        notificationRequests.add(new NotificationRequest("Notification 1"));
        notificationRequests.add(new NotificationRequest("Notification 1"));
        notificationRequests.add(new NotificationRequest("Notification 1"));
        notificationRequests.add(new NotificationRequest("Notification 1"));
        notificationRequests.add(new NotificationRequest("Notification 1"));
        notificationRequests.add(new NotificationRequest("Notification 1"));
        notificationRequests.add(new NotificationRequest("Notification 1"));

        refreshItems();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
//                if (!connectivity.isConnected(Applied_leaves.this)) {
//                    Custom_Toast.warning(Applied_leaves.this, getString(R.string.no_internet_error));
//                    onItemsLoadComplete();
//                }
//                else {
                    refreshItems();
//                }
            }
        });

    }

    void refreshItems() {
        // Load items
        // ...

        notificationAdapter = new NotificationAdapter(getApplicationContext(),notificationRequests);
        notification_rV.setAdapter(notificationAdapter);
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                ll_Data_Found.setVisibility(View.VISIBLE);
                ll_NoData_Found.setVisibility(View.GONE);
                Log.e("data",notificationRequests.toString());

                handler.postDelayed(this, 3000);
            }
        };handler.postDelayed(r, 3000);
        /*if (!connectivity.isConnected(Applied_leaves.this)) {
            Custom_Toast.warning(Applied_leaves.this, getString(R.string.no_internet_error));
        }
        else {
            new fetchAppliedLeaves().execute("");
        }*/
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // Stop refresh animation

        swipeRefreshLayout.setRefreshing(false);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}