package com.example.mybus.apisearch.msgBody;

import com.example.mybus.apisearch.itemList.BusPosList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

// 현재 버스의 위치 조회
public class BusPositionSearch {
    @SerializedName("itemList")
    private ArrayList<BusPosList> itemList;
    public ArrayList<BusPosList> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<BusPosList> itemList) {
        this.itemList = itemList;
    }
}
