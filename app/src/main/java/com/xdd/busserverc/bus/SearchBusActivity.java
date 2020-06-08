package com.xdd.busserverc.bus;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xdd.busserverc.R;

public class SearchBusActivity extends AppCompatActivity {

    private EditText search_bus_position;
    private Button btn_select_bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bus);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("查找巴士");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        search_bus_position = findViewById(R.id.search_bus_position);
        btn_select_bus = findViewById(R.id.btn_select_bus);
        btn_select_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("position",search_bus_position.getText().toString());
                Intent intent = new Intent(SearchBusActivity.this,BusListActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

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
