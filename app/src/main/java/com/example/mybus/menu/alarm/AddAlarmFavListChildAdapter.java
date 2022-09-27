package com.example.mybus.menu.alarm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.databinding.AddalarmFavBuslistItemBinding;
import com.example.mybus.mainadapter.MainFavChildAdapter;
import com.example.mybus.searchDetail.BusRouteDetailActivity;
import com.example.mybus.vo.LocalFavStopBus;
import com.example.mybus.vo.SchAlarmInfo;

import java.util.List;

public class AddAlarmFavListChildAdapter extends  RecyclerView.Adapter<AddAlarmFavListChildAdapter.AddAlarmChildViewHolder>{
    public List<LocalFavStopBus> localFavStopBusList;
    private SchAlarmInfo schAlarmInfo;
    private String stNm;

    public AddAlarmFavListChildAdapter(List<LocalFavStopBus> localFavStopBusList, String stNm) {
        this.localFavStopBusList = localFavStopBusList;
        this.stNm = stNm;
    }

    public AddAlarmFavListChildAdapter(List<LocalFavStopBus> localFavStopBusList, SchAlarmInfo schAlarmInfo, String stNm){
        this.localFavStopBusList = localFavStopBusList;
        this.schAlarmInfo = schAlarmInfo;
        this.stNm = stNm;
    }

    @NonNull
    @Override
    public AddAlarmChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddalarmFavBuslistItemBinding binding = AddalarmFavBuslistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AddAlarmChildViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddAlarmChildViewHolder holder, int position) {
        setText(holder, position);
        setListener(holder, position);
    }

    @Override
    public int getItemCount() {
        if (localFavStopBusList != null){
            return localFavStopBusList.size();
        }
        return 0;
    }

    public void setText(AddAlarmChildViewHolder holder, int position){
        holder.addalarmFavBuslistItemBinding.busName.setText(localFavStopBusList.get(position).lfb_busName);
    }

    public void setListener(AddAlarmChildViewHolder holder, int position){
        Bundle args = new Bundle();
        Bundle prevArgs = new Bundle();
        BusSchList busLists = new BusSchList();

        // 도착 알람 추가
        holder.addalarmFavBuslistItemBinding.busAddAlarm.setOnClickListener(new View.OnClickListener() {
            Intent alarmIntent;
            @Override
            public void onClick(View view) {
                if (schAlarmInfo != null){
                    prevArgs.putParcelable("prevUpdatedAlarm", schAlarmInfo);
                    schAlarmInfo.setAlarm_stop_nm(stNm);
                    schAlarmInfo.setAlarm_bus_nm(localFavStopBusList.get(position).getLfb_busName());
                    schAlarmInfo.setAlarm_id(localFavStopBusList.get(position).getStId());
                    schAlarmInfo.setAlarm_busId(localFavStopBusList.get(position).getLfb_busId());
                    schAlarmInfo.setStOrder(localFavStopBusList.get(position).getLfb_sectOrd());
                    alarmIntent = new Intent(holder.itemView.getContext(), UpdateAlarmActivity.class);
                    args.putParcelable("updateAlarm", schAlarmInfo);
                    alarmIntent.putExtras(args);
                    alarmIntent.putExtras(prevArgs);
                }else{
                    busLists.setStId(localFavStopBusList.get(position).getStId());
                    busLists.setBusRouteId(localFavStopBusList.get(position).getLfb_busId());
                    busLists.setBusRouteNm(localFavStopBusList.get(position).getLfb_busName());
                    busLists.setCorpNm(localFavStopBusList.get(position).getLfb_sectOrd());
                    busLists.setStStationNm(stNm);
                    alarmIntent = new Intent(holder.itemView.getContext(), AddAlarmActivity.class);
                    args.putParcelable("busList", busLists);
                    alarmIntent.putExtras(args);
                }
                holder.itemView.getContext().startActivity(alarmIntent);
                ((Activity) holder.itemView.getContext()).overridePendingTransition(R.anim.vertical_center, R.anim.none);
            }
        });
    }

    class AddAlarmChildViewHolder extends RecyclerView.ViewHolder{
        AddalarmFavBuslistItemBinding addalarmFavBuslistItemBinding;

        public AddAlarmChildViewHolder(AddalarmFavBuslistItemBinding binding) {
            super(binding.getRoot());
            addalarmFavBuslistItemBinding = binding;
        }
    }
}
