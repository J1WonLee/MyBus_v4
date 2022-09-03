package com.example.mybus.apisearch.msgBody;

import com.example.mybus.apisearch.itemList.StopSchList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

// 정류장 검색
public class StopSearch {
    @SerializedName("itemList")
    List<StopSchList> itemList = new ArrayList<>();

    public List<StopSchList> getItemList() {
        return itemList;
    }

    public void setItemList(List<StopSchList> itemList) {
        this.itemList = itemList;
    }
}
