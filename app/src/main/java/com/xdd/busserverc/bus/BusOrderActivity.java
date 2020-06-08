package com.xdd.busserverc.bus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.ContainerActivity;
import com.xdd.busserverc.R;
import com.xdd.busserverc.domain.Bus;
import com.xdd.busserverc.domain.User;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusOrderActivity extends AppCompatActivity {

    private ListView bus_order_list;
    private Handler handler,handler2;
    private Bus bus;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_order);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("车辆租赁订单");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        bus_order_list = findViewById(R.id.bus_order_list);
        user = JSONObject.parseObject(sharedPreferences.getString("user",""),User.class);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                ArrayList<Bus> busList = (ArrayList<Bus>) msg.obj;
                bus_order_list.setAdapter(new BusOrderAdapter(BusOrderActivity.this,busList));
                return false;
            }
        });
        handler2 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(BusOrderActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    Intent intent = new Intent(BusOrderActivity.this, ContainerActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        bus_order_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bus = (Bus) parent.getItemAtPosition(position);
                AlertDialog alertDialog = new AlertDialog.Builder(BusOrderActivity.this)
                        .setTitle("取消订单")
                        .setMessage("您是否确定取消该订单")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(httpThread2).start();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
        new Thread(httpThread).start();
    }

    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                FormBody formBody = new FormBody.Builder().add("userId",user.getId()+"").build();
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/bus/selectBus.do").post(formBody).build();
                Response response = http.newCall(request).execute();
                List<Bus> obj = JSONObject.parseArray(response.body().string(),Bus.class);
                Message msg = new Message();
                msg.obj = obj;
                handler.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    private Thread httpThread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                FormBody formBody = new FormBody.Builder().add("busId",bus.getId()+"").build();
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/bus/returnBus.do").post(formBody).build();
                Response response = http.newCall(request).execute();
                JSONObject obj = JSONObject.parseObject(response.body().string());
                Message msg = new Message();
                msg.obj = obj;
                handler2.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    });

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
