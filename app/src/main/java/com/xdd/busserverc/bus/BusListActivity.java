package com.xdd.busserverc.bus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BusListActivity extends AppCompatActivity {

    private ListView bus_list;
    private String position;
    private Handler handler,handler2;
    private Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_list);
        Bundle bundle = getIntent().getExtras();
        position = bundle.getString("position");
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("查询" + position + "的出租车辆");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        bus_list = findViewById(R.id.bus_list);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                ArrayList<Bus> busList = (ArrayList<Bus>) msg.obj;
                bus_list.setAdapter(new BusListAdapter(BusListActivity.this,busList));
                return false;
            }
        });
        handler2 = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(BusListActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    Intent intent = new Intent(BusListActivity.this, ContainerActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        bus_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bus = (Bus) parent.getItemAtPosition(position);
                SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
                JSONObject json = JSONObject.parseObject(sharedPreferences.getString("user",""));
                bus.setUserId(Integer.parseInt(json.getString("id")));
                AlertDialog alertDialog = new AlertDialog.Builder(BusListActivity.this)
                        .setTitle("租车提醒")
                        .setMessage("您是否确定租赁 " + bus.getBusSpec())
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
                FormBody formBody = new FormBody.Builder().add("position",position).build();
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/bus/selectBusByPosition.do").post(formBody).build();
                Response response = http.newCall(request).execute();
                List<Bus> list = JSONObject.parseArray(response.body().string(), Bus.class);
                Message msg = new Message();
                msg.obj = list;
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
                String jsonBus = JSONObject.toJSONString(bus);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonBus);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/bus/rentBus.do").post(requestBody).build();
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
