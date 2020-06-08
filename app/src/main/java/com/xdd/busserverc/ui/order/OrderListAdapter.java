package com.xdd.busserverc.ui.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.R;
import com.xdd.busserverc.domain.Order;

import java.util.ArrayList;

public class OrderListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Order> orderList;
    private String passager;

    public OrderListAdapter(Context context,ArrayList<Order> list){
        inflater = LayoutInflater.from(context);
        orderList = list;
        SharedPreferences sharedPreferences = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        JSONObject json = JSONObject.parseObject(sharedPreferences.getString("user",""));
        passager = json.getString("realname");
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        private TextView order_from_station,order_to_station,order_id,order_departure_time,order_price,order_seat,order_passager;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.order_item,null);
            viewHolder = new ViewHolder();
            viewHolder.order_from_station = convertView.findViewById(R.id.order_from_station);
            viewHolder.order_to_station = convertView.findViewById(R.id.order_to_station);
            viewHolder.order_id = convertView.findViewById(R.id.order_id);
            viewHolder.order_departure_time = convertView.findViewById(R.id.order_departure_time);
            viewHolder.order_passager = convertView.findViewById(R.id.order_passager);
            viewHolder.order_price = convertView.findViewById(R.id.order_price);
            viewHolder.order_seat = convertView.findViewById(R.id.order_seat);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.order_from_station.setText(orderList.get(position).getFromStation());
        viewHolder.order_to_station.setText(orderList.get(position).getToStation());
        viewHolder.order_id.setText(orderList.get(position).getId()+"");
        viewHolder.order_departure_time.setText(orderList.get(position).getDepartureDate() + " " + orderList.get(position).getDepartureTime());
        viewHolder.order_passager.setText(passager);
        viewHolder.order_price.setText("¥"+orderList.get(position).getPrice());
        viewHolder.order_seat.setText(orderList.get(position).getSeat()+"");
        return convertView;
    }
}
