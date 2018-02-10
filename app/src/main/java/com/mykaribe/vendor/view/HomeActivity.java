package com.mykaribe.vendor.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.internal.NavigationMenuView;
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
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.mykaribe.vendor.R;
import com.mykaribe.vendor.communication.PushListenerService;
import com.mykaribe.vendor.utils.App;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.PreferenceHelper;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{


    private Toolbar toolbar;
    private View headerView;
    private ProfileImageView profileImageView;
    private TextView nameTxt,emailTxt;

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
        headerView=navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);

        profileImageView=(ProfileImageView)headerView.findViewById(R.id.imageView);
        nameTxt=(TextView)headerView.findViewById(R.id.profile_name) ;
        emailTxt=(TextView)headerView.findViewById(R.id.profile_email) ;
//
//        profileImageView.setOnClickListener(this);

        setNavigationView();
        setProfileImage(PreferenceHelper.getString(Constant.VENDOR_IMAGE,""));
        setHomePageFragment();
        setUpAmazonService();

    }
    private void setNavigationView(){
        nameTxt.setText(PreferenceHelper.getString(Constant.VENDOR_NAME,""));
        emailTxt.setText(PreferenceHelper.getString(Constant.VENDOR_EMAIL,""));
    }
    private void setProfileImage(String url){
        ViewTarget viewTarget = new ViewTarget<ProfileImageView, GlideDrawable>(profileImageView){
            @Override
            public void onLoadStarted(Drawable placeholder) {
            }

            @Override
            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                profileImageView.setImage(errorDrawable.getCurrent());
            }

            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                profileImageView.setImage(resource.getCurrent());
            }
        };
        Glide.with(App.getContext())
                .load(url)
                .placeholder(R.drawable.app_icon)
                .error(R.drawable.app_icon)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(viewTarget);
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
                if(App.isCameraPermissionOk(HomeActivity.this)){
                    startActivity(new Intent(HomeActivity.this, BarcodeScannerActivity.class));
                }
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
                if(App.isCameraPermissionOk(HomeActivity.this)){
                    startActivity(new Intent(HomeActivity.this, BarcodeScannerActivity.class));
                }
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
