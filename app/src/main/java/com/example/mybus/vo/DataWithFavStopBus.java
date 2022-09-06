package com.example.mybus.vo;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DataWithFavStopBus {
    @Embedded public LocalFav localFav;
    @Relation(
            parentColumn = "lf_id",
            entityColumn = "lfb_id"
    )
    public List<LocalFavStopBus> localFavStopBusList;
}
