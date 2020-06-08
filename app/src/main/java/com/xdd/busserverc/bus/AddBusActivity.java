package com.xdd.busserverc.bus;

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
import com.xdd.busserverc.R;
import com.xdd.busserverc.domain.Bus;
import com.xdd.busserverc.util.HttpUtil;

import java.io.IOException;

public class AddBusActivity extends AppCompatActivity {

    private EditText add_bus_spec,add_bus_price,add_bus_position,add_bus_license_plate;
    private Button add_bus_btn;
    private Bus bus;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("添加车辆");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        add_bus_spec = findViewById(R.id.add_bus_spec);
        add_bus_price = findViewById(R.id.add_bus_price);
        add_bus_position = findViewById(R.id.add_bus_position);
        add_bus_license_plate = findViewById(R.id.add_bus_license_plate);
        add_bus_btn = findViewById(R.id.add_bus_btn);
        bus = new Bus();
        add_bus_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bus.setBusSpec(add_bus_spec.getText().toString());
                bus.setPrice(Double.parseDouble(add_bus_price.getText().toString()));
                bus.setPosition(add_bus_position.getText().toString());
                bus.setLicensePlate(add_bus_license_plate.getText().toString());
                new Thread(httpThread).start();
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                JSONObject response = (JSONObject) msg.obj;
                String status = response.getString("status");
                String message = response.getString("message");
                Toast.makeText(AddBusActivity.this,message,Toast.LENGTH_SHORT).show();
                if ("2".equals(status)){
                    add_bus_spec.setText("");
                    add_bus_price.setText("");
                    add_bus_position.setText("");
                    add_bus_license_plate.setText("");
                }
                return false;
            }
        });
    }

    private Thread httpThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                String jsonBus = JSONObject.toJSONString(bus);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"),jsonBus);
                OkHttpClient http = new OkHttpClient();
                Request request = new Request.Builder().url(HttpUtil.url + "/bus/addBus.do").post(requestBody).build();
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
