package com.xdd.busserverc.advice;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.R;
import com.xdd.busserverc.domain.Advice;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdviceListActivity extends AppCompatActivity {

    private Handler handler;
    private ListView advice_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_list);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("反馈列表");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        advice_list = findViewById(R.id.advice_list);
        new Thread(httpThread).start();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                ArrayList<Advice> response = (ArrayList<Advice>) msg.obj;
                advice_list.setAdapter(new AdviceListAdapter(AdviceListActivity.this,response));
                return false;
            }
        });
    }

    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/advice/selectAdvice.do").build();
                Response response = http.newCall(request).execute();
                List<Advice> obj = JSONObject.parseArray(response.body().string(),Advice.class);
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
