package com.example.mybus.vo;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.Date;

@Entity(tableName = "SCH_ALARM", primaryKeys = {"alarm_id", "alarm_busId"})
public class SchAlarmInfo {
    @NonNull
    private String alarm_id;
    @NonNull
    private String alarm_busId;

    private String alarm_stop_nm;

    private String alarm_bus_nm;

    private String weeks;

    private long alarm_date;

    private String stOrder;

    private boolean isOn = true;

    public SchAlarmInfo(@NonNull String alarm_id, @NonNull String alarm_busId, String alarm_stop_nm, String alarm_bus_nm, String weeks, long alarm_date, String stOrder) {
        this.alarm_id = alarm_id;
        this.alarm_busId = alarm_busId;
        this.alarm_stop_nm = alarm_stop_nm;
        this.alarm_bus_nm = alarm_bus_nm;
        this.weeks = weeks;
        this.alarm_date = alarm_date;
        this.stOrder = stOrder;
    }

    public String getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(String alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getAlarm_busId() {
        return alarm_busId;
    }

    public void setAlarm_busId(String alarm_busId) {
        this.alarm_busId = alarm_busId;
    }

    public String getAlarm_stop_nm() {
        return alarm_stop_nm;
    }

    public void setAlarm_stop_nm(String alarm_stop_nm) {
        this.alarm_stop_nm = alarm_stop_nm;
    }

    public String getAlarm_bus_nm() {
        return alarm_bus_nm;
    }

    public void setAlarm_bus_nm(String alarm_bus_nm) {
        this.alarm_bus_nm = alarm_bus_nm;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public long getAlarm_date() {
        return alarm_date;
    }

    public void setAlarm_date(long alarm_date) {
        this.alarm_date = alarm_date;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public String getStOrder() {
        return stOrder;
    }

    public void setStOrder(String stOrder) {
        this.stOrder = stOrder;
    }
}
