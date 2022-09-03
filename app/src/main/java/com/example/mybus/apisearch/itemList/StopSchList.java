package com.example.mybus.apisearch.itemList;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "STOP_SEARCH_LIST")
public class StopSchList implements Comparable<StopSchList>{

    @NonNull
    @PrimaryKey
    @SerializedName("stId")
    public String stId;

    @SerializedName("stNm")
    public String stNm;
    @SerializedName("tmX")
    public String tmX;
    @SerializedName("tmY")
    public String tmY;
    @SerializedName("posX")
    public String posX;
    @SerializedName("posY")
    public String posY;
    @SerializedName("arsId")
    public String arsId;

    @NonNull
    @ColumnInfo(name = "search_order", defaultValue = "2")
    public int order=0;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }


    @Override
    public int compareTo(StopSchList stopSchList) {
        return stId.compareTo(stopSchList.getStId());
    }

    // 다음 진행 방향
    public String nextDir;

    public String getNextDir() {
        return nextDir;
    }

    public void setNextDir(String nextDir) {
        this.nextDir = nextDir;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getStNm() {
        return stNm;
    }

    public void setStNm(String stNm) {
        this.stNm = stNm;
    }

    public String getTmX() {
        return tmX;
    }

    public void setTmX(String tmX) {
        this.tmX = tmX;
    }

    public String getTmY() {
        return tmY;
    }

    public void setTmY(String tmY) {
        this.tmY = tmY;
    }

    public String getPosX() {
        return posX;
    }

    public void setPosX(String posX) {
        this.posX = posX;
    }

    public String getPosY() {
        return posY;
    }

    public void setPosY(String posY) {
        this.posY = posY;
    }

    public String getArsId() {
        return arsId;
    }

    public void setArsId(String arsId) {
        this.arsId = arsId;
    }


}
