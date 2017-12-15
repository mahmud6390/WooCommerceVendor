package com.mykaribe.vendor;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.mykaribe.vendor.controller.OrderGetController;
import com.mykaribe.vendor.listener.IOrderListUiCallback;
import com.mykaribe.vendor.model.Order;
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

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
