package com.xdd.busserverc.ui.order;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.OrderDetailActivity;
import com.xdd.busserverc.R;
import com.xdd.busserverc.domain.Order;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {

    private OrderViewModel mViewModel;
    private ListView order_list;
    private Handler handler;

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_fragment, container, false);
        order_list = view.findViewById(R.id.order_list);
        new Thread(httpThread).start();
        return view;
    }
    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
            try {
                JSONObject json = JSONObject.parseObject(sharedPreferences.getString("user",""));
                String userId = json.getString("id");
                FormBody formBody = new FormBody.Builder().add("userId",userId).build();
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/order/selectOrder.do").post(formBody).build();
                Response response = http.newCall(request).execute();
                List<Order> list = JSONObject.parseArray(response.body().string(),Order.class);
                Message msg = new Message();
                msg.obj = list;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrderViewModel.class);
        // TODO: Use the ViewModel
        mViewModel.getOrderList().observe(this, new Observer<ArrayList<Order>>() {
            @Override
            public void onChanged(ArrayList<Order> orders) {
                order_list.setAdapter(new OrderListAdapter(getActivity(),orders));
            }
        });
        order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                Order order = (Order) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("order",JSONObject.toJSONString(order));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                ArrayList<Order> list = (ArrayList<Order>) msg.obj;
                mViewModel.getOrderList().postValue(list);
                return false;
            }
        });
    }

}
