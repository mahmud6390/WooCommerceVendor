package com.mykaribe.vendor.view;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mykaribe.vendor.R;

/**
 * Created by USER on 21/12/2017.
 */
public class HomeFragment extends Fragment implements View.OnClickListener{
    private CardView cardViewBarcode,cardViewOrderList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.home_fragment, container, false);
        cardViewBarcode=(CardView)view.findViewById(R.id.card_view_barcode);
        cardViewOrderList=(CardView)view.findViewById(R.id.card_view_order_list);
        cardViewBarcode.setOnClickListener(this);
        cardViewOrderList.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_view_barcode:
                startActivity(new Intent(getActivity(), BarcodeScannerActivity.class));
                break;
            case R.id.card_view_order_list:
                if(getActivity()!=null && getActivity() instanceof HomeActivity){
                    HomeActivity homeActivity=(HomeActivity)getActivity();
                    homeActivity.setOrderListFragment();
                }
                break;
        }
    }
}
