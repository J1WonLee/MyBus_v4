package com.example.mybus.vo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.List;

public class DataWithFavStopBus implements Serializable {
    @Embedded public LocalFav localFav;
    @Relation(
            parentColumn = "lf_id",
            entityColumn = "lfb_id"
    )
    public List<LocalFavStopBus> localFavStopBusList;
}
