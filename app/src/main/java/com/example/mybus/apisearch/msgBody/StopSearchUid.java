package com.example.mybus.apisearch.msgBody;

import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class StopSearchUid {
    @SerializedName("itemList")
    List<StopUidSchList> itemLists = new ArrayList<>();

    public List<StopUidSchList> getItemLists() {
        return itemLists;
    }

    public void setItemLists(List<StopUidSchList> itemLists) {
        this.itemLists = itemLists;
    }
}
