package com.example.mybus.vo;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "SCH_ALARM", indices = {@Index(value={"alarm_id", "alarm_busId"}
        , unique = true)})
public class SchAlarmInfo implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int alarm_seqId;

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


    protected SchAlarmInfo(Parcel in) {
        alarm_id = in.readString();
        alarm_busId = in.readString();
        alarm_stop_nm = in.readString();
        alarm_bus_nm = in.readString();
        weeks = in.readString();
        alarm_date = in.readLong();
        stOrder = in.readString();
        isOn = in.readByte() != 0;
        alarm_seqId = in.readInt();
    }

    public static final Creator<SchAlarmInfo> CREATOR = new Creator<SchAlarmInfo>() {
        @Override
        public SchAlarmInfo createFromParcel(Parcel in) {
            return new SchAlarmInfo(in);
        }

        @Override
        public SchAlarmInfo[] newArray(int size) {
            return new SchAlarmInfo[size];
        }
    };

    public int getAlarm_seqId() {
        return alarm_seqId;
    }

    public void setAlarm_seqId(int alarm_seqId) {
        this.alarm_seqId = alarm_seqId;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(alarm_id);
        parcel.writeString(alarm_busId);
        parcel.writeString(alarm_stop_nm);
        parcel.writeString(alarm_bus_nm);
        parcel.writeString(weeks);
        parcel.writeLong(alarm_date);
        parcel.writeString(stOrder);
        parcel.writeBoolean(isOn);
        parcel.writeInt(alarm_seqId);
    }
}
