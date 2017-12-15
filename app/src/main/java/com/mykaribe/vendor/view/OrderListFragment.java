package com.mykaribe.vendor.view;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mykaribe.vendor.R;
import com.mykaribe.vendor.controller.OrderGetController;
import com.mykaribe.vendor.listener.IOrderListUiCallback;
import com.mykaribe.vendor.model.Order;

import java.util.List;

/**
 * Created by USER on 16/12/2017.
 */
public class OrderListFragment extends Fragment {
    private TextView textViewNoOrder,textViewTotalRecord;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewOrder;
    private OrderListRecyclerViewAdapter adapter;
    private OrderGetController orderGetController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.view_order_fragment, container, false);
        textViewNoOrder=(TextView)view.findViewById(R.id.text_view_no_order);
        textViewTotalRecord=(TextView)view.findViewById(R.id.text_view_total_record);
        recyclerViewOrder=(RecyclerView)view.findViewById(R.id.recycle_view_order);
        progressBar=(ProgressBar)view.findViewById(R.id.progress_bar);
        recyclerViewOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        orderGetController=new OrderGetController();
        progressBar.setVisibility(View.VISIBLE);
        orderGetController.getOrderList(new IOrderListUiCallback() {
            @Override
            public void onOrderListUpdate(List<Order> orders) {
                progressBar.setVisibility(View.GONE);
                notifyAdapter(orders);
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
}
