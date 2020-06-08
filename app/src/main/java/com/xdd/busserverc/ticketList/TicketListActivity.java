package com.xdd.busserverc.ticketList;

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
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.R;
import com.xdd.busserverc.TicketDetailActivity;
import com.xdd.busserverc.domain.Ticket;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TicketListActivity extends AppCompatActivity {

    private ListView ticket_list;
    private Ticket ticket;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_list);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("车票列表");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ticket_list = findViewById(R.id.ticket_list);
        ticket = new Ticket();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                ArrayList<Ticket> list = (ArrayList<Ticket>) msg.obj;
                ticket_list.setAdapter(new TicketListAdapter(TicketListActivity.this,list));
                ticket_list.setOnItemClickListener(item_listener);
                return false;
            }
        });
        Bundle bundle = getIntent().getExtras();
        ticket.setFromStation(bundle.getString("fromStation"));
        ticket.setToStation(bundle.getString("toStation"));
        ticket.setDepartureDate(bundle.getString("date"));
        new Thread(httpThread).start();
    }
    private AdapterView.OnItemClickListener item_listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(TicketListActivity.this, TicketDetailActivity.class);
            Ticket ticket = (Ticket) parent.getItemAtPosition(position);
            Bundle bundle = new Bundle();
            bundle.putString("ticket",JSONObject.toJSONString(ticket));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String ticketJson = JSONObject.toJSONString(ticket);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),ticketJson);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/ticket/selectTicket.do").post(requestBody).build();
                Response response = http.newCall(request).execute();
                List<Ticket> obj = JSONObject.parseArray(response.body().string(),Ticket.class);
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
