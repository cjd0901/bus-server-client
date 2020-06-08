package com.xdd.busserverc.advice;

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
import com.xdd.busserverc.ContainerActivity;
import com.xdd.busserverc.R;
import com.xdd.busserverc.domain.Advice;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;

public class AddAdviceActivity extends AppCompatActivity {

    private EditText add_advice_content;
    private Button  add_advice_btn;
    private Advice advice;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advice);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("意见和反馈");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        add_advice_content = findViewById(R.id.add_advice_content);
        add_advice_btn = findViewById(R.id.add_advice_btn);
        advice = new Advice();
        add_advice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advice.setContent(add_advice_content.getText().toString());
                new Thread(httpThread).start();
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(AddAdviceActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    Intent intent = new Intent(AddAdviceActivity.this, ContainerActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
    }

    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String adviceJson = JSONObject.toJSONString(advice);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),adviceJson);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/advice/addAdvice.do").post(requestBody).build();
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
