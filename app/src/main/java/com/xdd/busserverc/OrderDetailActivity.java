package com.xdd.busserverc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.domain.Order;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;

public class OrderDetailActivity extends AppCompatActivity {

    private TextView order_detail_from_station,order_detail_to_station,order_detail_id,order_detail_departure_time,
                     order_detail_price,order_detail_seat,order_detail_passager,order_detail_service_price,order_detail_status;
    private Button order_detail_btn_refund;
    private Order order;
    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("订单详情");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        JSONObject json = JSONObject.parseObject(sharedPreferences.getString("user",""));
        String passager = json.getString("realname");
        order_detail_from_station = findViewById(R.id.order_detail_from_station);
        order_detail_to_station = findViewById(R.id.order_detail_to_station);
        order_detail_id = findViewById(R.id.order_detail_id);
        order_detail_departure_time = findViewById(R.id.order_detail_departure_time);
        order_detail_price = findViewById(R.id.order_detail_price);
        order_detail_seat = findViewById(R.id.order_detail_seat);
        order_detail_passager = findViewById(R.id.order_detail_passager);
        order_detail_service_price = findViewById(R.id.order_detail_service_price);
        order_detail_status = findViewById(R.id.order_detail_status);
        order_detail_btn_refund = findViewById(R.id.order_detail_btn_refund);
        Bundle bundle = getIntent().getExtras();
        order = JSONObject.parseObject(bundle.getString("order"),Order.class);
        order_detail_from_station.setText(order.getFromStation());
        order_detail_to_station.setText(order.getToStation());
        order_detail_id.setText(order.getId()+"");
        order_detail_departure_time.setText(order.getDepartureDate() + " " + order.getDepartureTime());
        order_detail_price.setText("¥"+order.getPrice());
        order_detail_seat.setText(order.getSeat()+"");
        order_detail_passager.setText(passager);
        order_detail_service_price.setText("¥"+order.getServicePrice());
        switch (order.getStatus()){
            case 0:
                order_detail_status.setText("未支付");
                break;
            case 1:
                order_detail_status.setText("已支付");
                break;
            case 2:
                order_detail_status.setText("已退款");
                break;
        }
        order_detail_btn_refund.setOnClickListener(listener);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(OrderDetailActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    Intent intent = new Intent(OrderDetailActivity.this,ContainerActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog alertDialog = new AlertDialog.Builder(OrderDetailActivity.this)
                    .setTitle("退票")
                    .setMessage("你确定退票吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new Thread(httpThread).start();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            if(order.getStatus() == 2){
                Toast.makeText(OrderDetailActivity.this,"您已退款",Toast.LENGTH_SHORT).show();
            }else {
                alertDialog.show();
            }
        }
    };

    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String orderJson = JSONObject.toJSONString(order);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),orderJson);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/ticket/refund.do").post(requestBody).build();
                Response response = http.newCall(request).execute();
                JSONObject obj = JSONObject.parseObject(response.body().string());
                Message msg = new Message();
                msg.obj = obj;
                handler.sendMessage(msg);
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
