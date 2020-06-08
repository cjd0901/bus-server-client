package com.xdd.busserverc;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.xdd.busserverc.ticketList.TicketListActivity;

import java.util.Calendar;
import java.util.Date;

public class BuyTicketActivity extends AppCompatActivity {

    private TextView date_picker;
    private EditText buy_ticket_to_station,buy_ticket_from_station;
    private Button btn_select_ticket;
    private String date;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int real_month;
    private DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("搜索车票");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        date_picker = findViewById(R.id.date_picker);
        btn_select_ticket = findViewById(R.id.btn_select_ticket);
        buy_ticket_from_station = findViewById(R.id.buy_ticket_from_station);
        buy_ticket_to_station = findViewById(R.id.buy_ticket_to_station);
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        real_month = mMonth + 1;
        date = mYear + "年" + real_month + "月" + mDay + "日";
        date_picker.setText(date);
        datePickerDialog =  new DatePickerDialog(BuyTicketActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mYear = year;
                mMonth = month;
                mDay = dayOfMonth;
                real_month = mMonth + 1;
                date = mYear + "年" + real_month + "月" + mDay + "日";
                date_picker.setText(date);
            }
        },mYear,mMonth,mDay);
        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(new Date().getTime());
        datePicker.setMaxDate(new Date().getTime() + 2592000000l);
        date_picker.setOnClickListener(date_listener);
        btn_select_ticket.setOnClickListener(select_ticket_listener);
    }

    private View.OnClickListener date_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePickerDialog.show();
        }
    };
    private View.OnClickListener select_ticket_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("fromStation",buy_ticket_from_station.getText().toString());
            bundle.putString("toStation",buy_ticket_to_station.getText().toString());
            bundle.putString("date",mYear + "-" + real_month + "-" + mDay);
            Intent intent = new Intent(BuyTicketActivity.this, TicketListActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

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
