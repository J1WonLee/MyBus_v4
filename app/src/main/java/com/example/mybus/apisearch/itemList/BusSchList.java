package com.example.mybus.apisearch.itemList;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.OffsetDateTime;

// 노선 번호 검색 시 받아올 리스트들.
// 검색창에서 노출시키는건 이름, 노선유형
@Entity(tableName = "BUS_SEARCH_LIST")
public class BusSchList implements Parcelable, Comparable<BusSchList> {

    protected BusSchList(Parcel in) {
        busRouteId = in.readString();
        busRouteNm = in.readString();
        routeType = in.readString();
        stStationNm = in.readString();
        edStationNm = in.readString();
        term = in.readString();
        lastBusYn = in.readString();
        lastBusTm = in.readString();
        firstBusTm = in.readString();
        lastLowTm = in.readString();
        firstLowTm = in.readString();
        corpNm = in.readString();
        stId = in.readString();
    }

    public BusSchList() {
    }

    public static final Creator<BusSchList> CREATOR = new Creator<BusSchList>() {
        @Override
        public BusSchList createFromParcel(Parcel in) {
            return new BusSchList(in);
        }

        @Override
        public BusSchList[] newArray(int size) {
            return new BusSchList[size];
        }
    };

    // 정렬
    @Override
    public int compareTo(BusSchList busSchList) {
        return busRouteId.compareTo(busSchList.busRouteId);
    }

    @NonNull
    @PrimaryKey
    @SerializedName("busRouteId")
    @Expose
    public String busRouteId;

    @SerializedName("busRouteNm")
    @Expose
    public String busRouteNm;
    @SerializedName("busRouteAbrv")
    @Expose
    public String busRouteAbrv;
    @SerializedName("length")
    @Expose
    public String length;
    @SerializedName("routeType")
    @Expose
    public String routeType;
    @SerializedName("stStationNm")
    @Expose
    public String stStationNm;
    @SerializedName("edStationNm")
    @Expose
    public String edStationNm;
    @SerializedName("term")
    @Expose
    public String term;
    @SerializedName("lastBusYn")
    @Expose
    public String lastBusYn;
    @SerializedName("lastBusTm")
    @Expose
    public String lastBusTm;
    @SerializedName("firstBusTm")
    @Expose
    public String firstBusTm;
    @SerializedName("lastLowTm")
    @Expose
    public String lastLowTm;
    @SerializedName("firstLowTm")
    @Expose
    public String firstLowTm;
    @SerializedName("corpNm")
    @Expose
    public String corpNm;

    private String stId;

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    @NonNull
    @ColumnInfo(name = "search_order", defaultValue = "2")
    public int order = 0;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getBusRouteId() {
        return busRouteId;
    }

    public void setBusRouteId(String busRouteId) {
        this.busRouteId = busRouteId;
    }

    public String getBusRouteNm() {
        return busRouteNm;
    }

    public void setBusRouteNm(String busRouteNm) {
        this.busRouteNm = busRouteNm;
    }

    public String getBusRouteAbrv() {
        return busRouteAbrv;
    }

    public void setBusRouteAbrv(String busRouteAbrv) {
        this.busRouteAbrv = busRouteAbrv;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getStStationNm() {
        return stStationNm;
    }

    public void setStStationNm(String stStationNm) {
        this.stStationNm = stStationNm;
    }

    public String getEdStationNm() {
        return edStationNm;
    }

    public void setEdStationNm(String edStationNm) {
        this.edStationNm = edStationNm;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getLastBusYn() {
        return lastBusYn;
    }

    public void setLastBusYn(String lastBusYn) {
        this.lastBusYn = lastBusYn;
    }

    public String getLastBusTm() {
        return lastBusTm;
    }

    public void setLastBusTm(String lastBusTm) {
        this.lastBusTm = lastBusTm;
    }

    public String getFirstBusTm() {
        return firstBusTm;
    }

    public void setFirstBusTm(String firstBusTm) {
        this.firstBusTm = firstBusTm;
    }

    public String getLastLowTm() {
        return lastLowTm;
    }

    public void setLastLowTm(String lastLowTm) {
        this.lastLowTm = lastLowTm;
    }

    public String getFirstLowTm() {
        return firstLowTm;
    }

    public void setFirstLowTm(String firstLowTm) {
        this.firstLowTm = firstLowTm;
    }

    public String getCorpNm() {
        return corpNm;
    }

    public void setCorpNm(String corpNm) {
        this.corpNm = corpNm;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(busRouteId);
        parcel.writeString(busRouteNm);
        parcel.writeString(routeType);
        parcel.writeString(stStationNm);
        parcel.writeString(edStationNm);
        parcel.writeString(term);
        parcel.writeString(lastBusYn);
        parcel.writeString(lastBusTm);
        parcel.writeString(firstBusTm);
        parcel.writeString(lastLowTm);
        parcel.writeString(firstLowTm);
        parcel.writeString(corpNm);
        parcel.writeString(stId);
    }

}
