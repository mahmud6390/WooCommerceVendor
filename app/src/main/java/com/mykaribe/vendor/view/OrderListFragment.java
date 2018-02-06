package com.mykaribe.vendor.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mykaribe.vendor.R;
import com.mykaribe.vendor.controller.OrderGetController;
import com.mykaribe.vendor.controller.ProductIdController;
import com.mykaribe.vendor.listener.IOrderListUiCallback;
import com.mykaribe.vendor.listener.IProductIdListCallback;
import com.mykaribe.vendor.model.Order;
import com.mykaribe.vendor.utils.App;
import com.mykaribe.vendor.utils.Constant;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.PreferenceHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 16/12/2017.
 */
public class OrderListFragment extends Fragment implements View.OnClickListener,IProductIdListCallback {
    private TextView textViewNoOrder,textViewTotalRecord;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewOrder;
    private OrderListRecyclerViewAdapter adapter;
    private OrderGetController orderGetController;
    private FloatingActionButton floatingActionButton;
    private SwipeRefreshLayout swipeRefreshLayoutOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.view_order_fragment, container, false);
        textViewNoOrder=(TextView)view.findViewById(R.id.text_view_no_order);
        textViewTotalRecord=(TextView)view.findViewById(R.id.text_view_total_record);
        recyclerViewOrder=(RecyclerView)view.findViewById(R.id.recycle_view_order);
        showNoOrderRecord(false);
        progressBar=(ProgressBar)view.findViewById(R.id.progress_bar);
        floatingActionButton=(FloatingActionButton)view.findViewById(R.id.fab);
        swipeRefreshLayoutOrder=(SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_order);
        swipeRefreshLayoutOrder.setColorSchemeResources(
                R.color.colorAccent,
                R.color.colorAccent,
                R.color.colorAccent);
        swipeRefreshLayoutOrder.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    loadProductIdList();
                    swipeRefreshLayoutOrder.setRefreshing(false);

            }
        });
        floatingActionButton.setOnClickListener(this);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadProductIdList();
    }
    private void loadProductIdList(){

        progressBar.setVisibility(View.VISIBLE);
        int vendorId= PreferenceHelper.getInt(Constant.VENDOR_ID,0);
        String userName=PreferenceHelper.getString(Constant.USER_NAME,"");
        String password=PreferenceHelper.getString(Constant.PASSWORD,"");
        Logger.debugLog("App","onCreate>>>vendorId:"+vendorId+":userName:"+userName+":password:"+password);
        if(vendorId!=0 && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)){
            new ProductIdController().getProductIdList(userName,password,this);
        }
    }
    private void loadOrder(){

        orderGetController=new OrderGetController();
        orderGetController.getOrderList(new IOrderListUiCallback() {
            @Override
            public void onOrderListUpdate(List<Order> orders) {
                progressBar.setVisibility(View.GONE);
                showNoOrderRecord(true);
                notifyAdapter(orders);
            }

            @Override
            public void onOrderUpdate(Order order) {

            }

            @Override
            public void onOrderFailed() {
                showNoOrderRecord(true);

            }
        });
    }

    private void notifyAdapter(List<Order> orders){
        textViewTotalRecord.setText(orders.size()+" orders");
        if(orders.size()>0){
            textViewNoOrder.setVisibility(View.GONE);
            recyclerViewOrder.setVisibility(View.VISIBLE);
            if(adapter==null){
                adapter=new OrderListRecyclerViewAdapter(getActivity());
                adapter.setAdapterData(orders);
                recyclerViewOrder.setAdapter(adapter);
            }else{
                adapter.setAdapterData(orders);
                adapter.notifyDataSetChanged();
            }
        }else{
            textViewNoOrder.setVisibility(View.VISIBLE);
            recyclerViewOrder.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                if(App.isCameraPermissionOk(getActivity())){
                    startActivity(new Intent(getActivity(), BarcodeScannerActivity.class));
                }
                break;
        }
    }

    @Override
    public void onSuccessList(ArrayList<Integer> productId) {
        App.setProductIdList(productId);
        loadOrder();

    }
    private void showNoOrderRecord(boolean visibility){
        if(visibility){
            textViewNoOrder.setVisibility(View.VISIBLE);
            textViewTotalRecord.setVisibility(View.VISIBLE);
        }else{
            textViewNoOrder.setVisibility(View.INVISIBLE);
            textViewTotalRecord.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onFailed() {
        Toast.makeText(getActivity(), R.string.failed_product,Toast.LENGTH_SHORT).show();

    }
}
