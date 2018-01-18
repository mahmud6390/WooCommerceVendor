package com.mykaribe.vendor.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mykaribe.vendor.R;
import com.mykaribe.vendor.communication.PushListenerService;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.PreferenceHelper;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        ((FloatingActionButton) findViewById(R.id.fab)).setVisibility(View.GONE);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);

//        profileImageView=(ProfileImageView)navigationView.findViewById(R.id.imageView);
//        name=(TextView)navigationView.findViewById(R.id.profile_name) ;
//
//        profileImageView.setOnClickListener(this);


        setHomePageFragment();
        setUpAmazonService();

    }
    public void setOrderListFragment(){
        toolbar.setTitle(R.string.order_list);
        getFragmentManager().beginTransaction().replace(R.id.content_view,new OrderListFragment()).commit();
    }
    public void setHomePageFragment(){
        toolbar.setTitle(R.string.home_page);
        getFragmentManager().beginTransaction().replace(R.id.content_view,new HomeFragment()).commit();
    }
    public static PinpointManager pinpointManager;
    private void setUpAmazonService(){
        AWSMobileClient.getInstance().initialize(this).execute();
        if (pinpointManager == null) {
            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    getApplicationContext(),
                    AWSMobileClient.getInstance().getCredentialsProvider(),
                    AWSMobileClient.getInstance().getConfiguration());

            pinpointManager = new PinpointManager(pinpointConfig);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String deviceToken =
                                InstanceID.getInstance(HomeActivity.this).getToken(
                                        Constant.FIREBASE_SENDER_ID,
                                        GoogleCloudMessaging.INSTANCE_ID_SCOPE);
                        Logger.debugLog("DEVICE_TOKEN","device token:"+ deviceToken);
                        pinpointManager.getNotificationClient()
                                .registerGCMDeviceToken(deviceToken);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    protected void onPause() {
        super.onPause();

        // unregister notification receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register notification receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver,
                new IntentFilter(PushListenerService.ACTION_PUSH_NOTIFICATION));
    }

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.debugLog("notificationReceiver", "Received notification from local broadcast. Display it in a dialog.");

            Bundle data = intent.getBundleExtra(PushListenerService.INTENT_SNS_NOTIFICATION_DATA);
            String message = PushListenerService.getMessage(data);

            new AlertDialog.Builder(HomeActivity.this)
                    .setTitle("Push notification")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                startActivity(new Intent(HomeActivity.this, BarcodeScannerActivity.class));
                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.home:
                setHomePageFragment();
                break;
            case R.id.scan_barcode:
                startActivity(new Intent(this, BarcodeScannerActivity.class));
                break;
            case  R.id.order_list:
                setOrderListFragment();
                break;
            case R.id.logout:
                PreferenceHelper.putInt(Constant.VENDOR_ID,0);
                startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                HomeActivity.this.finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
