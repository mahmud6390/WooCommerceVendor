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
import com.mykaribe.vendor.utils.App;
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

    }

    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


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
