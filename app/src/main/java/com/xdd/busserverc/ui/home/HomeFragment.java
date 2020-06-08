package com.xdd.busserverc.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.AddAdminActivity;
import com.xdd.busserverc.AddTicketActivity;
import com.xdd.busserverc.BuyTicketActivity;
import com.xdd.busserverc.ModifyPwdActivity;
import com.xdd.busserverc.R;
import com.xdd.busserverc.advice.AddAdviceActivity;
import com.xdd.busserverc.advice.AdviceListActivity;
import com.xdd.busserverc.bus.AddBusActivity;
import com.xdd.busserverc.bus.BusOrderActivity;
import com.xdd.busserverc.bus.SearchBusActivity;
import com.xdd.busserverc.domain.User;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private Button buyTicket,modify_password,rent_bus,support_advice,add_ticket,view_advice,add_admin,add_rent_bus,rent_order;
    private LinearLayout admin_service;
    private TextView tv_admin;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        buyTicket = root.findViewById(R.id.buyTicket);
        modify_password = root.findViewById(R.id.modify_password);
        rent_bus = root.findViewById(R.id.rent_bus);
        support_advice = root.findViewById(R.id.support_advice);
        add_ticket = root.findViewById(R.id.add_ticket);
        view_advice = root.findViewById(R.id.view_advice);
        add_admin = root.findViewById(R.id.add_admin);
        add_rent_bus = root.findViewById(R.id.add_rent_bus);
        admin_service = root.findViewById(R.id.admin_service);
        tv_admin = root.findViewById(R.id.tv_admin);
        rent_order = root.findViewById(R.id.rent_order);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        User user = JSONObject.parseObject(sharedPreferences.getString("user",""),User.class);
        if(user.getRoleId() != 0){
            tv_admin.setVisibility(View.INVISIBLE);
            admin_service.setVisibility(View.INVISIBLE);
        }
        buyTicket.setOnClickListener(btn_listener);
        modify_password.setOnClickListener(btn_listener);
        add_ticket.setOnClickListener(btn_listener);
        add_admin.setOnClickListener(btn_listener);
        support_advice.setOnClickListener(btn_listener);
        view_advice.setOnClickListener(btn_listener);
        add_rent_bus.setOnClickListener(btn_listener);
        rent_bus.setOnClickListener(btn_listener);
        rent_order.setOnClickListener(btn_listener);
        return root;
    }
    private View.OnClickListener btn_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.buyTicket:
                    intent = new Intent(getActivity(), BuyTicketActivity.class);
                    break;
                case R.id.modify_password:
                    intent = new Intent(getActivity(), ModifyPwdActivity.class);
                    break;
                case R.id.add_ticket:
                    intent = new Intent(getActivity(), AddTicketActivity.class);
                    break;
                case R.id.add_admin:
                    intent = new Intent(getActivity(), AddAdminActivity.class);
                    break;
                case R.id.support_advice:
                    intent = new Intent(getActivity(), AddAdviceActivity.class);
                    break;
                case R.id.view_advice:
                    intent = new Intent(getActivity(), AdviceListActivity.class);
                    break;
                case  R.id.add_rent_bus:
                    intent = new Intent(getActivity(), AddBusActivity.class);
                    break;
                case R.id.rent_bus:
                    intent = new Intent(getActivity(), SearchBusActivity.class);
                    break;
                case R.id.rent_order:
                    intent = new Intent(getActivity(), BusOrderActivity.class);
                    break;
            }
            startActivity(intent);
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}