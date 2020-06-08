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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.domain.User;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;

public class ModifyInformationActivity extends AppCompatActivity {

    private EditText modify_information_nickname,modify_information_phoneNum,modify_information_email;
    private RadioGroup modify_information_gender;
    private RadioButton rb_gender_1,rb_gender_0;
    private Button mine_confirm_modify;
    private Handler handler;
    private User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_information);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("修改信息");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        modify_information_nickname = findViewById(R.id.modify_information_nickname);
        modify_information_gender = findViewById(R.id.modify_information_gender);
        modify_information_phoneNum = findViewById(R.id.modify_information_phoneNum);
        modify_information_email = findViewById(R.id.modify_information_email);
        rb_gender_1 = findViewById(R.id.rb_gender_1);
        rb_gender_0 = findViewById(R.id.rb_gender_0);
        mine_confirm_modify = findViewById(R.id.mine_confirm_modify);
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String user = preferences.getString("user","");
        u = JSONObject.parseObject(user,User.class);
        modify_information_nickname.setText(u.getNickname());
        if(u.getGender()==1){
            modify_information_gender.check(rb_gender_1.getId());
        }else {
            modify_information_gender.check(rb_gender_0.getId());
        }
        modify_information_phoneNum.setText(u.getPhoneNum());
        modify_information_email.setText(u.getEmail());
        mine_confirm_modify.setOnClickListener(listener);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(ModifyInformationActivity.this,message,Toast.LENGTH_SHORT).show();
                if("2".equals(status)){
                    SharedPreferences.Editor ed = preferences.edit();
                    ed.clear();
                    ed.putString("user",response.getString("user"));
                    ed.apply();
                    Intent intent = new Intent(ModifyInformationActivity.this,ContainerActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            u.setNickname(modify_information_nickname.getText().toString());
            u.setGender(rb_gender_1.isChecked() ? 1:0);
            u.setPhoneNum(modify_information_phoneNum.getText().toString());
            u.setEmail(modify_information_email.getText().toString());
            new Thread(httpThread).start();
        }
    };
    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String jsonUser = JSONObject.toJSONString(u);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonUser);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/user/modifyInformation.do").post(requestBody).build();
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
