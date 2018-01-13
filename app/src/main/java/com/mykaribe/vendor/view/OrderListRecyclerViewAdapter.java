package com.mykaribe.vendor.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mykaribe.vendor.R;
import com.mykaribe.vendor.model.Order;
import com.mykaribe.vendor.model.Shipping;
import com.mykaribe.vendor.utils.Logger;
import com.mykaribe.vendor.utils.OrderStatus;

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
        // mOrderList.add(0,new Order());
         mOrderList.addAll(orderList);
     }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        switch (viewType){
//            case VIEW_TYPE_HEADER:
//                return new HeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_header, parent, false));
//            case VIEW_TYPE_CONTENT:
//                return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_content, parent, false));
//        }
        return new ContentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case VIEW_TYPE_HEADER:
                break;
            case VIEW_TYPE_CONTENT:
                ContentViewHolder viewHolder=(ContentViewHolder)holder;
                Order order=mOrderList.get(position);
                viewHolder.textViewOrderNumber.setText(Html.fromHtml(orderNumber(order.getId()+"")));
                viewHolder.textViewName.setText(order.getBilling().getFirst_name()+" "+order.getBilling().getLast_name()+"");
                viewHolder.textViewEmail.setText(order.getBilling().getEmail());
                //String orderName=getOrderText(order.getId()+"",order.getBilling().getFirst_name()+" "+order.getBilling().getLast_name(),order.getBilling().getEmail());
               // Logger.debugLog(TAG,"OnbindView>>orderName:"+orderName);
               // viewHolder.orderName.setText(Html.fromHtml(orderName));
               // viewHolder.shipTo.setText(Html.fromHtml(getShipTo(order.getShipping(),order.getShipping_lines().getMethod_title())));
                try{
                    String formatedDate=getDate(order.getDate_created());
                    String[] dateStr=getYearMonthDate(formatedDate);
                    viewHolder.textViewYear.setText(dateStr[0]);
                    viewHolder.textViewMonth.setText(dateStr[1]);
                    viewHolder.textViewDate.setText(dateStr[2]);
                }catch (Exception e){

                }
                if(order.getCurrency().equalsIgnoreCase("USD")){
                    viewHolder.textViewPrice.setText("$"+order.getTotal());
                }else{
                    viewHolder.textViewPrice.setText(order.getCurrency()+""+order.getTotal());
                }

                //viewHolder.total.setText(Html.fromHtml(getTotal(order.getCurrency(),order.getTotal(),order.getPayment_method_title())));
                viewHolder.textViewStatus.setText(order.getStatus());
                 if(order.getStatus().equalsIgnoreCase(OrderStatus.COMPLETED.toString())){
                     viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));//context.getColor(R.color.colorPrimary));
                     viewHolder.relativeLayoutBar.setBackgroundColor(ContextCompat.getColor(context, R.color.completed_cell));
                }
                else if(order.getStatus().equalsIgnoreCase(OrderStatus.ON_HOLD.toString())){
                     viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                     viewHolder.relativeLayoutBar.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                }else{
                     viewHolder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.black));
                     viewHolder.relativeLayoutBar.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                 }
                break;
        }

    }
    private String orderNumber(String orderNo){
        return String.format(context.getString(R.string.order_number),"#"+orderNo);

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
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");
        return df.format(date);

    }
    private String[] getYearMonthDate(String formatedDate){
        String[] strings;
        strings=formatedDate.split("-");
        return strings;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_CONTENT;
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
    private final class ContentViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewYear,textViewMonth,textViewDate,textViewOrderNumber,textViewName,textViewEmail;
        private TextView textViewPrice,textViewStatus;
        private RelativeLayout relativeLayoutBar;

        public ContentViewHolder(View itemView) {
            super(itemView);
            relativeLayoutBar=(RelativeLayout)itemView.findViewById(R.id.list_item_bar) ;
            textViewYear=(TextView)itemView.findViewById(R.id.year_txt);
            textViewMonth=(TextView)itemView.findViewById(R.id.month_txt);
            textViewDate=(TextView)itemView.findViewById(R.id.date_txt);
            textViewOrderNumber=(TextView)itemView.findViewById(R.id.order_number_txt);
            textViewName=(TextView)itemView.findViewById(R.id.name_txt);
            textViewEmail=(TextView)itemView.findViewById(R.id.email_txt);
            textViewPrice=(TextView)itemView.findViewById(R.id.price_txt);
            textViewStatus=(TextView)itemView.findViewById(R.id.status_txt);

        }
    }

    private final class HeaderViewHolder extends RecyclerView.ViewHolder{

        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
