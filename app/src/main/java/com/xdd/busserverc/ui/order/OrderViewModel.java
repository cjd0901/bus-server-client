package com.xdd.busserverc.ui.order;

import com.xdd.busserverc.domain.Order;

import java.util.ArrayList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OrderViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<ArrayList<Order>> orderList;

    public OrderViewModel(){
        orderList = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Order>> getOrderList() {
        return orderList;
    }
}
