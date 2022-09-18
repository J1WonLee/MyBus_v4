package com.example.mybus.mainadapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.StopRouteList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.MainFavItemBinding;
import com.example.mybus.menu.HomeEditActivity;
import com.example.mybus.searchDetail.BusRouteDetailAdapter;
import com.example.mybus.vo.DataWithFavStopBus;

import java.util.List;

public class MainFavAdapter extends RecyclerView.Adapter<MainFavAdapter.MainFavViewHolder>{
    private List<DataWithFavStopBus> dataWithFavStopBusList;
    private List<StopUidSchList> stopUidSchList;
    private List<BusArrivalList> busArrivalList;
    private OnItemClickListener mListener;
    public MainFavChildAdapter mainFavChildAdapter;
    @NonNull
    @Override
    public MainFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainFavItemBinding mainFavItemBinding = MainFavItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MainFavViewHolder(mainFavItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFavViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        setTitleContents(holder, position);
        setChildAdapter(holder, position);
    }

    @Override
    public int getItemCount() {
        if (dataWithFavStopBusList != null){
            return dataWithFavStopBusList.size();
        }else{
            return -1;
        }
    }

    public void setTitleContents(MainFavViewHolder holder, int position){
        holder.mainFavItemBinding.favStopBusName.setText(dataWithFavStopBusList.get(position).localFav.getLf_name());
        if (dataWithFavStopBusList.get(position).localFav.getLf_isBus() == 0){
            holder.mainFavItemBinding.stopBusListBtn.setVisibility(View.GONE);
        }
    }

    public void setChildAdapter(MainFavViewHolder holder, int position){
        mainFavChildAdapter =  new MainFavChildAdapter(dataWithFavStopBusList.get(position).localFavStopBusList, stopUidSchList, busArrivalList);
        holder.mainFavItemBinding.nestedRv.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.mainFavItemBinding.nestedRv.setAdapter(mainFavChildAdapter);
    }


    public void updateDataWithFavStopBusList(List<DataWithFavStopBus> dataWithFavStopBusLists, List<StopUidSchList> stopUidSchLists){
        this.dataWithFavStopBusList = dataWithFavStopBusLists;
        this.stopUidSchList = stopUidSchLists;
        notifyDataSetChanged();
    }

    public void updateDataWithGBusFavStopBusList(List<DataWithFavStopBus> dataWithFavStopBusLists, List<BusArrivalList> busArrivalLists){
        this.dataWithFavStopBusList = dataWithFavStopBusLists;
        this.busArrivalList = busArrivalLists;
        notifyDataSetChanged();
    }

    public void updateDataWithFavStopBus(List<DataWithFavStopBus> dataWithFavStopBusLists){
        this.dataWithFavStopBusList = dataWithFavStopBusLists;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onBtnClick(View v , int position);
        void onTitleClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
    }

    public String getStopBusOrd(StopRouteList stopRouteList){
        for(StopUidSchList lists : stopUidSchList){
            if (lists.getBusRouteId().equals(stopRouteList.getBusRouteId())){
                Log.d("MainFavAdapter", "storder ::::::::" + lists.getStaOrd());
                return lists.getStaOrd();
            }
        }
        return "-1";
    }

    class MainFavViewHolder extends RecyclerView.ViewHolder{
        MainFavItemBinding mainFavItemBinding;

        public MainFavViewHolder(MainFavItemBinding binding) {
            super(binding.getRoot());
            mainFavItemBinding = binding;

            mainFavItemBinding.stopBusListBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && mListener != null){
                        mListener.onBtnClick(view, pos);
                    }
                }
            });

            mainFavItemBinding.favStopBusName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && mListener != null){
                        mListener.onTitleClick(view, pos);
                    }
                }
            });
        }
    }
}
