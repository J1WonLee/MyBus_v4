package com.example.mybus.apisearch.msgBody;

import com.example.mybus.apisearch.itemList.BusStopList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BusRouteStopSearch {
    @SerializedName("MsgBody")
    public ArrayList<BusStopList> itemList;

    public ArrayList<BusStopList> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<BusStopList> itemList) {
        this.itemList = itemList;
    }
}
