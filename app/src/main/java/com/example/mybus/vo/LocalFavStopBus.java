package com.example.mybus.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(primaryKeys = {"lfb_id", "lfb_busId"} , tableName="Local_Fav_Stop_Bus")
public class LocalFavStopBus {
    @NonNull
    public String lfb_id;

    public Date lfb_order;
    @NonNull
    public String lfb_busId;

    public String lfb_busName;


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
}
