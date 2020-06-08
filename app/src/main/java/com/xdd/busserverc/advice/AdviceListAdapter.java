package com.xdd.busserverc.advice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.xdd.busserverc.R;
import com.xdd.busserverc.domain.Advice;

import java.util.ArrayList;

public class AdviceListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private ArrayList<Advice> adviceList;

    public AdviceListAdapter(Context context, ArrayList<Advice> list){
        inflater = LayoutInflater.from(context);
        adviceList = list;
    }

    @Override
    public int getCount() {
        return adviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return adviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        private TextView advice_time,advice_content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
            convertView = inflater.inflate(R.layout.advice_item,null);
            viewHolder.advice_content = convertView.findViewById(R.id.advice_content);
            viewHolder.advice_time = convertView.findViewById(R.id.advice_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.advice_content.setText(adviceList.get(position).getContent());
        viewHolder.advice_time.setText(adviceList.get(position).getCreateTime());
        return convertView;
    }
}
