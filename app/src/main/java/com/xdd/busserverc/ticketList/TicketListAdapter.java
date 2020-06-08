package com.xdd.busserverc.ticketList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xdd.busserverc.R;
import com.xdd.busserverc.domain.Ticket;

import java.util.ArrayList;

public class TicketListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private ArrayList<Ticket> ticketList;

    public TicketListAdapter(Context context, ArrayList<Ticket> list){
        mInflater = LayoutInflater.from(context);
        ticketList = list;
    }

    @Override
    public int getCount() {
        return ticketList.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        private TextView ticket_item_time,ticket_item_from_station,ticket_item_to_station,ticket_price,ticket_rest;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder= null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.ticket_item,null);
            viewHolder = new ViewHolder();
            viewHolder.ticket_item_time = convertView.findViewById(R.id.ticket_item_time);
            viewHolder.ticket_item_from_station = convertView.findViewById(R.id.ticket_item_from_station);
            viewHolder.ticket_item_to_station = convertView.findViewById(R.id.ticket_item_to_station);
            viewHolder.ticket_price = convertView.findViewById(R.id.ticket_price);
            viewHolder.ticket_rest = convertView.findViewById(R.id.ticket_rest);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ticket_item_time.setText(ticketList.get(position).getDepartureTime());
        viewHolder.ticket_item_from_station.setText(ticketList.get(position).getFromStation());
        viewHolder.ticket_item_to_station.setText(ticketList.get(position).getToStation());
        viewHolder.ticket_price.setText("¥"+ticketList.get(position).getPrice());
        viewHolder.ticket_rest.setText(ticketList.get(position).getRestTicket()+"张");
        return convertView;
    }
}
