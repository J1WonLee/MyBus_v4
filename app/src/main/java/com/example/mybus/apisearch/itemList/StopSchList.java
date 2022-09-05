package com.example.mybus.apisearch.itemList;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "STOP_SEARCH_LIST")
public class StopSchList implements Comparable<StopSchList>, Parcelable {

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

    public StopSchList() {
    }

    protected StopSchList(Parcel in) {
        stId = in.readString();
        stNm = in.readString();
        arsId = in.readString();
        nextDir = in.readString();
    }

    public static final Creator<StopSchList> CREATOR = new Creator<StopSchList>() {
        @Override
        public StopSchList createFromParcel(Parcel in) {
            return new StopSchList(in);
        }

        @Override
        public StopSchList[] newArray(int size) {
            return new StopSchList[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }
    
    // protected 생성자와 요소들 순서 맞춰줘야함
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(stId);
        parcel.writeString(stNm);
        parcel.writeString(arsId);
        parcel.writeString(nextDir);

    }
}
