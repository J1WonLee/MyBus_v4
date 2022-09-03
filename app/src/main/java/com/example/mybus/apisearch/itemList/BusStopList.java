package com.example.mybus.apisearch.itemList;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


public class BusStopList {
    @SerializedName("busRouteId")
    public String busRouteId;
    @SerializedName("busRouteNm")
    public String busRouteNm;
    @SerializedName("busRouteAbrv")
    public String busRouteAbrv;
    @SerializedName("seq")
    public String seq;
    @SerializedName("sesectionq")
    public String section;
    @SerializedName("station")
    public String station;
    @SerializedName("arsId")
    public String arsId;
    @SerializedName("stationNm")
    public String stationNm;
    @SerializedName("gpsX")
    public String gpsX;
    @SerializedName("gpsY")
    public String gpsY;
    @SerializedName("posX")
    public String posX;
    @SerializedName("posY")
    public String posY;
    @SerializedName("fullSectDist")
    public String fullSectDist;
    @SerializedName("direction")
    public String direction;
    @SerializedName("stationNo")
    public String stationNo;
    @SerializedName("routeType")
    public String routeType;
    @SerializedName("beginTm")
    public String beginTm;
    @SerializedName("lastTm")
    public String lastTm;
    @SerializedName("trnstnid")
    public String trnstnid;
    @SerializedName("sectSpd")
    public String sectSpd;
    @SerializedName("transYn")
    public String transYn;



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

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getArsId() {
        return arsId;
    }

    public void setArsId(String arsId) {
        this.arsId = arsId;
    }

    public String getStationNm() {
        return stationNm;
    }

    public void setStationNm(String stationNm) {
        this.stationNm = stationNm;
    }

    public String getGpsX() {
        return gpsX;
    }

    public void setGpsX(String gpsX) {
        this.gpsX = gpsX;
    }

    public String getGpsY() {
        return gpsY;
    }

    public void setGpsY(String gpsY) {
        this.gpsY = gpsY;
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

    public String getFullSectDist() {
        return fullSectDist;
    }

    public void setFullSectDist(String fullSectDist) {
        this.fullSectDist = fullSectDist;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getRouteType() {
        return routeType;
    }

    public void setRouteType(String routeType) {
        this.routeType = routeType;
    }

    public String getBeginTm() {
        return beginTm;
    }

    public void setBeginTm(String beginTm) {
        this.beginTm = beginTm;
    }

    public String getLastTm() {
        return lastTm;
    }

    public void setLastTm(String lastTm) {
        this.lastTm = lastTm;
    }

    public String getTrnstnid() {
        return trnstnid;
    }

    public void setTrnstnid(String trnstnid) {
        this.trnstnid = trnstnid;
    }

    public String getSectSpd() {
        return sectSpd;
    }

    public void setSectSpd(String sectSpd) {
        this.sectSpd = sectSpd;
    }

    public String getTransYn() {
        return transYn;
    }

    public void setTransYn(String transYn) {
        this.transYn = transYn;
    }
}
