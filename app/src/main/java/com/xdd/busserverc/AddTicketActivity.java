package com.xdd.busserverc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.domain.Ticket;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;

public class AddTicketActivity extends AppCompatActivity {

    private EditText add_ticket_from_station,add_ticket_to_station,add_ticket_departure_date,add_ticket_departure_time,
            add_ticket_rest_ticket,add_ticket_price,add_ticket_bus_spec,add_ticket_service_price;
    private Button add_ticket_btn;
    private Ticket ticket;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("添加车票");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        add_ticket_from_station = findViewById(R.id.add_ticket_from_station);
        add_ticket_to_station = findViewById(R.id.add_ticket_to_station);
        add_ticket_departure_date = findViewById(R.id.add_ticket_departure_date);
        add_ticket_departure_time = findViewById(R.id.add_ticket_departure_time);
        add_ticket_rest_ticket = findViewById(R.id.add_ticket_rest_ticket);
        add_ticket_price = findViewById(R.id.add_ticket_price);
        add_ticket_bus_spec = findViewById(R.id.add_ticket_bus_spec);
        add_ticket_service_price = findViewById(R.id.add_ticket_service_price);
        add_ticket_btn = findViewById(R.id.add_ticket_btn);
        add_ticket_btn.setOnClickListener(listener);
        ticket = new Ticket();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(AddTicketActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    Intent intent = new Intent(AddTicketActivity.this,ContainerActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ticket.setFromStation(add_ticket_from_station.getText().toString());
            ticket.setToStation(add_ticket_to_station.getText().toString());
            ticket.setDepartureDate(add_ticket_departure_date.getText().toString());
            ticket.setDepartureTime(add_ticket_departure_time.getText().toString());
            ticket.setRestTicket(Integer.parseInt(add_ticket_rest_ticket.getText().toString()));
            ticket.setPrice(Double.parseDouble(add_ticket_price.getText().toString()));
            ticket.setBusSpec(Integer.parseInt(add_ticket_bus_spec.getText().toString()));
            ticket.setServicePrice(Double.parseDouble(add_ticket_service_price.getText().toString()));
            new Thread(httpThread).start();
        }
    };

    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String ticketJson = JSONObject.toJSONString(ticket);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),ticketJson);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/ticket/addTicket.do").post(requestBody).build();
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
