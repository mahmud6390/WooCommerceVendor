package com.mykaribe.vendor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mykaribe.vendor.communication.PushListenerService;
import com.mykaribe.vendor.controller.OrderGetController;
import com.mykaribe.vendor.listener.IOrderListUiCallback;
import com.mykaribe.vendor.model.Order;
import com.mykaribe.vendor.utils.App;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.view.BarcodeScannerActivity;
import com.mykaribe.vendor.view.OrderListFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{


    private OrderListFragment orderListFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        ((FloatingActionButton) findViewById(R.id.fab)).setOnClickListener(this);
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
        orderListFragment=new OrderListFragment();
        toolbar.setTitle(R.string.order_list);
        getFragmentManager().beginTransaction().replace(R.id.content_view,orderListFragment).commit();
        setUpAmazonService();

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
                                InstanceID.getInstance(MainActivity.this).getToken(
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

            new AlertDialog.Builder(MainActivity.this)
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
                if(App.isCameraPermissionOk(MainActivity.this)){
                    startActivity(new Intent(MainActivity.this, BarcodeScannerActivity.class));
                }

                break;
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
