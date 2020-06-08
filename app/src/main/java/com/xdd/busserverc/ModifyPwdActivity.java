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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.domain.User;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;

public class ModifyPwdActivity extends AppCompatActivity {

    private EditText modify_password_ed;
    private Button confirm_modify_password;
    private User user;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pwd);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("修改密码");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        modify_password_ed = findViewById(R.id.modify_password_ed);
        confirm_modify_password = findViewById(R.id.confirm_modify_password);
        SharedPreferences sharedPreferences = getSharedPreferences("user",MODE_PRIVATE);
        user = JSONObject.parseObject(sharedPreferences.getString("user",""),User.class);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(ModifyPwdActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    AlertDialog alertDialog = new AlertDialog.Builder(ModifyPwdActivity.this)
                            .setTitle("提示")
                            .setMessage("你的密码已经修改,请重新登陆。")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();
                                    Intent intent = new Intent(ModifyPwdActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            }).create();
                    alertDialog.show();
                }
                return false;
            }
        });
        confirm_modify_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setPassword(modify_password_ed.getText().toString());
                AlertDialog alertDialog = new AlertDialog.Builder(ModifyPwdActivity.this)
                        .setTitle("修改密码")
                        .setMessage("你确定修改密码吗？")
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
                        }).create();
                alertDialog.show();
            }
        });
    }

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
