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
import com.xdd.busserverc.domain.User;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;

public class FpwdActivity extends AppCompatActivity {

    private EditText et_fpwd_username,et_fpwd_keyword,et_fpwd_repassword;
    private Button btn_fpwd_modify;
    private User user;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fpwd);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("忘记密码");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        et_fpwd_username = findViewById(R.id.et_fpwd_username);
        et_fpwd_keyword = findViewById(R.id.et_fpwd_keyword);
        et_fpwd_repassword = findViewById(R.id.et_fpwd_repassword);
        btn_fpwd_modify = findViewById(R.id.btn_fpwd_modify);
        btn_fpwd_modify.setOnClickListener(listener);
        user = new User();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject json = (JSONObject) msg.obj;
                String status = (String) json.get("status");
                String message = (String) json.get("message");
                Toast.makeText(FpwdActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    Intent intent = new Intent(FpwdActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            user.setUsername(et_fpwd_username.getText().toString());
            user.setKeyword(et_fpwd_keyword.getText().toString());
            user.setPassword(et_fpwd_repassword.getText().toString());
            new Thread(httpThread).start();
        }
    };
    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String jsonUser = JSONObject.toJSONString(user);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonUser);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/user/modifyPassword.do").post(requestBody).build();
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
