package com.xdd.busserverc.ui.mine;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.xdd.busserverc.MainActivity;
import com.xdd.busserverc.ModifyInformationActivity;
import com.xdd.busserverc.R;
import com.xdd.busserverc.domain.User;

public class MineFragment extends Fragment {

    private MineViewModel mViewModel;
    private TextView tv_mine_nickname,tv_mine_gender,tv_mine_phoneNum,tv_mine_email,tv_mine_realname,tv_mine_idcard;
    private Button modify_information,mine_logout;

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        modify_information = view.findViewById(R.id.mine_modify_information);
        tv_mine_nickname = view.findViewById(R.id.tv_mine_nickname);
        tv_mine_gender = view.findViewById(R.id.tv_mine_gender);
        tv_mine_phoneNum = view.findViewById(R.id.tv_mine_phoneNum);
        tv_mine_email = view.findViewById(R.id.tv_mine_email);
        tv_mine_realname = view.findViewById(R.id.tv_mine_realname);
        tv_mine_idcard = view.findViewById(R.id.tv_mine_idcard);
        mine_logout = view.findViewById(R.id.mine_logout);
        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String user = preferences.getString("user","");
        User u = JSONObject.parseObject(user,User.class);
        tv_mine_nickname.setText(u.getNickname());
        if(u.getGender() == 1){
            tv_mine_gender.setText("男");
        }else {
            tv_mine_gender.setText("女");
        }
        tv_mine_phoneNum.setText(u.getPhoneNum());
        tv_mine_email.setText(u.getEmail());
        tv_mine_realname.setText(u.getRealname());
        tv_mine_idcard.setText(u.getIdcard());
        modify_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyInformationActivity.class);
                startActivity(intent);
            }
        });
        mine_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MineViewModel.class);
        // TODO: Use the ViewModel
    }

}
