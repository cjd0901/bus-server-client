package com.xdd.busserverc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.domain.Order;
import com.xdd.busserverc.domain.Ticket;
import com.xdd.busserverc.domain.User;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;

public class TicketDetailActivity extends AppCompatActivity {

    private TextView ticket_detail_from_station,
            ticket_detail_to_station,ticket_detail_departure_time,
            ticket_detail_price,ticket_detail_passager,ticket_detail_rest,
            ticket_detail_service_price,ticket_detail_bus_spec;
    private Button ticket_detail_purchase_ticket;
    private Ticket ticket;
    private Order order;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_detail);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("车票详情");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ticket_detail_from_station = findViewById(R.id.ticket_detail_from_station);
        ticket_detail_to_station = findViewById(R.id.ticket_detail_to_station);
        ticket_detail_departure_time = findViewById(R.id.ticket_detail_departure_time);
        ticket_detail_price = findViewById(R.id.ticket_detail_price);
        ticket_detail_passager = findViewById(R.id.ticket_detail_passager);
        ticket_detail_rest = findViewById(R.id.ticket_detail_rest);
        ticket_detail_service_price = findViewById(R.id.ticket_detail_service_price);
        ticket_detail_bus_spec = findViewById(R.id.ticket_detail_bus_spec);
        ticket_detail_purchase_ticket = findViewById(R.id.ticket_detail_purchase_ticket);
        order = new Order();
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String user = preferences.getString("user","");
        User u = JSONObject.parseObject(user,User.class);
        Bundle bundle = getIntent().getExtras();
        String t = bundle.getString("ticket");
        ticket = JSONObject.parseObject(t,Ticket.class);
        ticket_detail_from_station.setText(ticket.getFromStation());
        ticket_detail_to_station.setText(ticket.getToStation());
        ticket_detail_departure_time.setText(ticket.getDepartureDate() + " " + ticket.getDepartureTime());
        ticket_detail_price.setText("¥"+ticket.getPrice());
        ticket_detail_passager.setText(u.getRealname());
        ticket_detail_rest.setText(ticket.getRestTicket()+"张");
        ticket_detail_service_price.setText("¥"+ticket.getServicePrice());
        ticket_detail_bus_spec.setText(ticket.getBusSpec()+"座");
        ticket_detail_purchase_ticket.setOnClickListener(listener);
        order.setFromStation(ticket.getFromStation());
        order.setToStation(ticket.getToStation());
        order.setDepartureDate(ticket.getDepartureDate());
        order.setDepartureTime(ticket.getDepartureTime());
        order.setUserId(u.getId());
        order.setTicketId(ticket.getId());
        order.setPrice(ticket.getPrice());
        order.setServicePrice(ticket.getServicePrice());
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(TicketDetailActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    Intent intent = new Intent(TicketDetailActivity.this,ContainerActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AlertDialog alertDialog = new AlertDialog.Builder(TicketDetailActivity.this)
                    .setTitle("购买车票")
                    .setMessage("你确定购买吗？")
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
            alertDialog.show();
        }
    };

    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String jsonOrder = JSONObject.toJSONString(order);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonOrder);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/ticket/buyTicket.do").post(requestBody).build();
                Response response = http.newCall(request).execute();
                JSONObject json = JSONObject.parseObject(response.body().string());
                Message msg = new Message();
                msg.obj = json;
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
