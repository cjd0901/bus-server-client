package com.xdd.busserverc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.domain.User;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText et_login_username,et_login_password;
    private TextView tv_forget_pwd;
    private Button btn_register;
    private Button btn_login;
    private User user;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("巴士服务系统");
        et_login_username = findViewById(R.id.et_login_username);
        et_login_password = findViewById(R.id.et_login_password);
        tv_forget_pwd = findViewById(R.id.forget_pwd);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        tv_forget_pwd.setOnClickListener(listener);
        user = new User();
        btn_login.setOnClickListener(listener);
        btn_register.setOnClickListener(listener);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(MainActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("user",response.getString("user"));
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this,ContainerActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.btn_login:
//                    intent = new Intent(MainActivity.this,ContainerActivity.class);
                    if("".equals(et_login_username.getText().toString().trim()) || "".equals(et_login_password.getText().toString().trim())){
                        Toast.makeText(MainActivity.this,"帐号或密码不能为空",Toast.LENGTH_SHORT).show();
                    }else {
                        user.setUsername(et_login_username.getText().toString());
                        user.setPassword(et_login_password.getText().toString());
                        new Thread(loginThread).start();
                    }
                    break;
                case R.id.btn_register:
                    intent = new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    break;
                case R.id.forget_pwd:
                    intent = new Intent(MainActivity.this,FpwdActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };
    private Thread loginThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String jsonUser = JSONObject.toJSONString(user);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonUser);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/user/login.do").post(requestBody).build();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
