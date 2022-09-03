package com.example.mybus.apisearch.itemList;


import androidx.annotation.NonNull;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;


// 버스 위치 조회 (id로 검색)
public class BusPosList {
    @SerializedName("sectOrd")
    public String sectOrd;
    @SerializedName("fullSectDist")
    public String fullSectDist;
    @SerializedName("sectDist")
    public String sectDist;
    @SerializedName("rtDist")
    public String rtDist;
    @SerializedName("stopFlag")
    public String stopFlag;
    @SerializedName("sectionId")
    public String sectionId;
    @SerializedName("dataTm")
    public String dataTm;
    @SerializedName("tmX")
    public Object tmX;
    @SerializedName("tmY")
    public Object tmY;
    @SerializedName("gpsX")
    public String gpsX;
    @SerializedName("gpsY")
    public String gpsY;
    @SerializedName("posX")
    public String posX;
    @SerializedName("posY")
    public String posY;
    @SerializedName("vehId")
    public String vehId;
    @SerializedName("plainNo")
    public String plainNo;
    @SerializedName("busType")
    public String busType;
    @SerializedName("lastStTm")
    public String lastStTm;
    @SerializedName("nextStTm")
    public String nextStTm;
    @SerializedName("nextStId")
    public String nextStId;
    @SerializedName("lastStnId")
    public String lastStnId;
    @SerializedName("trnstnid")
    public String trnstnid;
    @SerializedName("isrunyn")
    public String isrunyn;
    @SerializedName("islastyn")
    public String islastyn;
    @SerializedName("isFullFlag")
    public String isFullFlag;
    @SerializedName("congetion")
    public String congetion;

    @NonNull
    @PrimaryKey(autoGenerate = true)
    public int seq;

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getSectOrd() {
        return sectOrd;
    }

    public void setSectOrd(String sectOrd) {
        this.sectOrd = sectOrd;
    }

    public String getFullSectDist() {
        return fullSectDist;
    }

    public void setFullSectDist(String fullSectDist) {
        this.fullSectDist = fullSectDist;
    }

    public String getSectDist() {
        return sectDist;
    }

    public void setSectDist(String sectDist) {
        this.sectDist = sectDist;
    }

    public String getRtDist() {
        return rtDist;
    }

    public void setRtDist(String rtDist) {
        this.rtDist = rtDist;
    }

    public String getStopFlag() {
        return stopFlag;
    }

    public void setStopFlag(String stopFlag) {
        this.stopFlag = stopFlag;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getDataTm() {
        return dataTm;
    }

    public void setDataTm(String dataTm) {
        this.dataTm = dataTm;
    }

    public Object getTmX() {
        return tmX;
    }

    public void setTmX(Object tmX) {
        this.tmX = tmX;
    }

    public Object getTmY() {
        return tmY;
    }

    public void setTmY(Object tmY) {
        this.tmY = tmY;
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

    public String getVehId() {
        return vehId;
    }

    public void setVehId(String vehId) {
        this.vehId = vehId;
    }

    public String getPlainNo() {
        return plainNo;
    }

    public void setPlainNo(String plainNo) {
        this.plainNo = plainNo;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getLastStTm() {
        return lastStTm;
    }

    public void setLastStTm(String lastStTm) {
        this.lastStTm = lastStTm;
    }

    public String getNextStTm() {
        return nextStTm;
    }

    public void setNextStTm(String nextStTm) {
        this.nextStTm = nextStTm;
    }

    public String getNextStId() {
        return nextStId;
    }

    public void setNextStId(String nextStId) {
        this.nextStId = nextStId;
    }

    public String getLastStnId() {
        return lastStnId;
    }

    public void setLastStnId(String lastStnId) {
        this.lastStnId = lastStnId;
    }

    public String getTrnstnid() {
        return trnstnid;
    }

    public void setTrnstnid(String trnstnid) {
        this.trnstnid = trnstnid;
    }

    public String getIsrunyn() {
        return isrunyn;
    }

    public void setIsrunyn(String isrunyn) {
        this.isrunyn = isrunyn;
    }

    public String getIslastyn() {
        return islastyn;
    }

    public void setIslastyn(String islastyn) {
        this.islastyn = islastyn;
    }

    public String getIsFullFlag() {
        return isFullFlag;
    }

    public void setIsFullFlag(String isFullFlag) {
        this.isFullFlag = isFullFlag;
    }

    public String getCongetion() {
        return congetion;
    }

    public void setCongetion(String congetion) {
        this.congetion = congetion;
    }
}
