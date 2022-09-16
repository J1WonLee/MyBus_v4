package com.example.mybus.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(primaryKeys = {"lfb_id", "lfb_busId"} , tableName="Local_Fav_Stop_Bus")
public class LocalFavStopBus implements Serializable {
    @NonNull
    public String lfb_id;

    public Date lfb_order;
    @NonNull
    public String lfb_busId;

    public String lfb_busName;

    // 구간 순번으로 버스 도착 알림 시 필요함
    public String lfb_sectOrd = "-1";

    @Ignore
    public String stId;

    public LocalFavStopBus() {
    }

    public LocalFavStopBus(@NonNull String lfb_id, Date lfb_order, @NonNull String lfb_busId, String lfb_busName, String lfb_sectOrd) {
        this.lfb_id = lfb_id;
        this.lfb_order = lfb_order;
        this.lfb_busId = lfb_busId;
        this.lfb_busName = lfb_busName;
        this.lfb_sectOrd = lfb_sectOrd;
    }

    public LocalFavStopBus(String lfb_id, Date lfb_order, String lfb_busId, String lfb_busName) {
        this.lfb_id = lfb_id;
        this.lfb_order = lfb_order;
        this.lfb_busId = lfb_busId;
        this.lfb_busName = lfb_busName;
    }

    public String getLfb_id() {
        return lfb_id;
    }

    public void setLfb_id(String lfb_id) {
        this.lfb_id = lfb_id;
    }

    public Date getLfb_order() {
        return lfb_order;
    }

    public void setLfb_order(Date lfb_order) {
        this.lfb_order = lfb_order;
    }

    public String getLfb_busId() {
        return lfb_busId;
    }

    public void setLfb_busId(String lfb_busId) {
        this.lfb_busId = lfb_busId;
    }

    public String getLfb_busName() {
        return lfb_busName;
    }

    public void setLfb_busName(String lfb_busName) {
        this.lfb_busName = lfb_busName;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getLfb_sectOrd() {
        return lfb_sectOrd;
    }

    public void setLfb_sectOrd(String lfb_sectOrd) {
        this.lfb_sectOrd = lfb_sectOrd;
    }
}
