package com.xdd.busserverc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.domain.User;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;

public class AddAdminActivity extends AppCompatActivity {

    private EditText add_admin_username;
    private Button add_admin_btn;
    private User user;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("添加管理员");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        add_admin_username = findViewById(R.id.add_admin_username);
        add_admin_btn = findViewById(R.id.add_admin_btn);
        user = new User();
        add_admin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUsername(add_admin_username.getText().toString());
                new Thread(httpThread).start();
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(AddAdminActivity.this,message,Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            String jsonUser = JSONObject.toJSONString(user);
            try {
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonUser);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/user/addAdmin.do").post(requestBody).build();
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
