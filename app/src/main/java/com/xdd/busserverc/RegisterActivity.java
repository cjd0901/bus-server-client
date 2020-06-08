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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.domain.User;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_username,et_password,et_keyword,et_realname,et_idcard,et_phonenum,et_email;
    private Button btn_register;
    private User user;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("注册账号");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_keyword = findViewById(R.id.et_keyword);
        et_realname = findViewById(R.id.et_realname);
        et_idcard = findViewById(R.id.et_idcard);
        et_phonenum = findViewById(R.id.et_phonenum);
        et_email = findViewById(R.id.et_email);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(listener);
        user = new User();
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject json = (JSONObject) msg.obj;
                String status = (String) json.get("status");
                String message = (String) json.get("message");
                Toast.makeText(RegisterActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            user.setUsername(et_username.getText().toString());
            user.setPassword(et_password.getText().toString());
            user.setKeyword(et_keyword.getText().toString());
            user.setRealname(et_realname.getText().toString());
            user.setIdcard(et_idcard.getText().toString());
            user.setPhoneNum(et_phonenum.getText().toString());
            user.setEmail(et_email.getText().toString());
            new Thread(registerThread).start();
        }
    };
    private Thread registerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String jsonUser = JSONObject.toJSONString(user);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonUser);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/user/addUser.do").post(requestBody).build();
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
