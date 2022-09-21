package com.example.mybus.menu.alarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.databinding.AddalarmFavBuslistItemBinding;
import com.example.mybus.mainadapter.MainFavChildAdapter;
import com.example.mybus.searchDetail.BusRouteDetailActivity;
import com.example.mybus.vo.LocalFavStopBus;

import java.util.List;

public class AddAlarmFavListChildAdapter extends  RecyclerView.Adapter<AddAlarmFavListChildAdapter.AddAlarmChildViewHolder>{
    public List<LocalFavStopBus> localFavStopBusList;
    private String stNm;

    public AddAlarmFavListChildAdapter(List<LocalFavStopBus> localFavStopBusList, String stNm) {
        this.localFavStopBusList = localFavStopBusList;
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
        BusSchList busLists = new BusSchList();

        // 도착 알람 추가
        holder.addalarmFavBuslistItemBinding.busAddAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busLists.setStId(localFavStopBusList.get(position).getStId());
                busLists.setBusRouteId(localFavStopBusList.get(position).getLfb_busId());
                busLists.setBusRouteNm(localFavStopBusList.get(position).getLfb_busName());
                busLists.setCorpNm(localFavStopBusList.get(position).getLfb_sectOrd());
                busLists.setStStationNm(stNm);
                Intent alarmIntent = new Intent(holder.itemView.getContext(), AddAlarmActivity.class);
                args.putParcelable("busList", busLists);
                alarmIntent.putExtras(args);
                holder.itemView.getContext().startActivity(alarmIntent);
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
