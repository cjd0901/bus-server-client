package com.xdd.busserverc.bus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdd.busserverc.R;
import com.xdd.busserverc.domain.Bus;

import java.util.ArrayList;

public class BusListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Bus> busList;

    public BusListAdapter(Context context, ArrayList<Bus> list){
        inflater = LayoutInflater.from(context);
        busList = list;
    }

    @Override
    public int getCount() {
        return busList.size();
    }

    @Override
    public Object getItem(int position) {
        return busList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder{
        private TextView bus_item_bus_spec,bus_item_price,bus_item_license_plate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.bus_list_item,null);
            viewHolder = new ViewHolder();
            viewHolder.bus_item_bus_spec = convertView.findViewById(R.id.bus_item_bus_spec);
            viewHolder.bus_item_price = convertView.findViewById(R.id.bus_item_price);
            viewHolder.bus_item_license_plate = convertView.findViewById(R.id.bus_item_license_plate);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.bus_item_bus_spec.setText(busList.get(position).getBusSpec());
        viewHolder.bus_item_license_plate.setText(busList.get(position).getLicensePlate());
        viewHolder.bus_item_price.setText(busList.get(position).getPrice() + "/天");
        return convertView;
    }
}
