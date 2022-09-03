package com.example.mybus.apisearch.msgBody;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// 노선 번호 조회
public class BusRouteSearch implements Serializable {

    @SerializedName("itemList")
    @Expose
    public List<BusSchList> itemList = new ArrayList<>();

    public List<BusSchList> getItemList() {
        return itemList;
    }

    public void setItemList(List<BusSchList> itemList) {
        this.itemList = itemList;
    }
}
