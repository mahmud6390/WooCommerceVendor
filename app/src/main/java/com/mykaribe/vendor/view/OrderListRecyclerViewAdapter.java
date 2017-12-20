package com.mykaribe.vendor.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mykaribe.vendor.R;
import com.mykaribe.vendor.model.Order;
import com.mykaribe.vendor.model.Shipping;
import com.mykaribe.vendor.utils.Logger;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by USER on 16/12/2017.
 */
public class OrderListRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER=1;
    private static final int VIEW_TYPE_CONTENT=2;
    private static final String TAG ="OrderListRecyclerViewAdapter" ;
    private Context context;
    private List<Order> mOrderList;
    public OrderListRecyclerViewAdapter(Context context){
        this.context=context;
        mOrderList=new ArrayList<>();
    }
     public void setAdapterData(List<Order> orderList){
         mOrderList.clear();
         mOrderList.add(0,new Order());
         mOrderList.addAll(orderList);
     }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case VIEW_TYPE_HEADER:
                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_header, parent, false));
            case VIEW_TYPE_CONTENT:
                return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_content, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case VIEW_TYPE_HEADER:
                break;
            case VIEW_TYPE_CONTENT:
                ContentViewHolder viewHolder=(ContentViewHolder)holder;
                if(position % 2 ==0){
                    viewHolder.itemView.setBackgroundResource(R.color.selected_color);
                }else{
                    viewHolder.itemView.setBackgroundResource(R.color.white);
                }
                Order order=mOrderList.get(position);
                String orderName=getOrderText(order.getId()+"",order.getBilling().getFirst_name()+" "+order.getBilling().getLast_name(),order.getBilling().getEmail());
                Logger.debugLog(TAG,"OnbindView>>orderName:"+orderName);
                viewHolder.orderName.setText(Html.fromHtml(orderName));
               // viewHolder.shipTo.setText(Html.fromHtml(getShipTo(order.getShipping(),order.getShipping_lines().getMethod_title())));
                try{
                    viewHolder.date.setText(getDate(order.getDate_created()));
                }catch (Exception e){

                }
                viewHolder.total.setText(Html.fromHtml(getTotal(order.getCurrency(),order.getTotal(),order.getPayment_method_title())));
                viewHolder.status.setText(order.getStatus());
                break;
        }

    }
    private String getOrderText(String orderNo,String name,String email){
        return String.format(context.getString(R.string.oder_name),"#"+orderNo,name,email);

    }
    private String getShipTo(Shipping shipping,String methodTitle){
        String name=shipping.getFirst_name()+" "+shipping.getLast_name()+","+shipping.getCompany()+","+shipping.getAddress_1()+","
                +shipping.getCity()+","+shipping.getState()+","+shipping.getPostcode()+","+shipping.getCountry();
        return String.format(context.getString(R.string.ship_to),name,methodTitle);

    }
    private String getTotal(String currency,String total,String method){
        return String.format(context.getString(R.string.total),currency+""+total,method);
    }
    private String getDate(String dateWithTimeZone) throws Exception{
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date=formatter.parse(dateWithTimeZone);
        SimpleDateFormat df = new SimpleDateFormat("MMM dd,yyyy");
        return df.format(date);

    }

    @Override
    public int getItemViewType(int position) {
        return position==0?VIEW_TYPE_HEADER:VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
    private final class ContentViewHolder extends RecyclerView.ViewHolder{

        private TextView orderName,date,total,status;

        public ContentViewHolder(View itemView) {
            super(itemView);
            orderName=(TextView)itemView.findViewById(R.id.text_view_order_name);
            date=(TextView)itemView.findViewById(R.id.text_view_date);
            total=(TextView)itemView.findViewById(R.id.text_view_total);
            status=(TextView)itemView.findViewById(R.id.text_view_status);

        }
    }

    private final class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
