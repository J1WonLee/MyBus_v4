package com.example.mybus.menu.alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.AddalarmFavItemBinding;
import com.example.mybus.mainadapter.MainFavAdapter;
import com.example.mybus.mainadapter.MainFavChildAdapter;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.SchAlarmInfo;

import java.util.List;

public class AddAlarmFavListAdapter extends RecyclerView.Adapter<AddAlarmFavListAdapter.AddAlarmFavListViewHolder> {
    private List<DataWithFavStopBus> dataWithFavStopBusList;
    private AddAlarmFavListChildAdapter childAdapter;
    private SchAlarmInfo schAlarmInfo;

    @NonNull
    @Override
    public AddAlarmFavListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AddalarmFavItemBinding binding = AddalarmFavItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AddAlarmFavListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AddAlarmFavListViewHolder holder, int position) {
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

    public void setChildAdapter(AddAlarmFavListViewHolder holder , int position){
        if (schAlarmInfo !=null){
            childAdapter = new AddAlarmFavListChildAdapter(dataWithFavStopBusList.get(position).localFavStopBusList, schAlarmInfo, dataWithFavStopBusList.get(position).localFav.getLf_name());
        }else{
            childAdapter =  new AddAlarmFavListChildAdapter(dataWithFavStopBusList.get(position).localFavStopBusList, dataWithFavStopBusList.get(position).localFav.getLf_name());
        }
        holder.addalarmFavItemBinding.nestedRv.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.addalarmFavItemBinding.nestedRv.setAdapter(childAdapter);
    }

    public void setTitleContents(AddAlarmFavListViewHolder holder, int position){
        if (dataWithFavStopBusList.get(position).localFavStopBusList.size() > 0){
            holder.addalarmFavItemBinding.favStopBusName.setText(dataWithFavStopBusList.get(position).localFav.getLf_name());
        }
    }

    public void updateDataWithFavStopBusList(List<DataWithFavStopBus> dataWithFavStopBusLists){
        this.dataWithFavStopBusList = dataWithFavStopBusLists;
        notifyDataSetChanged();
    }

    public void updateDataWithFavStopBusList(List<DataWithFavStopBus> dataWithFavStopBusLists, SchAlarmInfo schAlarmInfo){
        this.dataWithFavStopBusList = dataWithFavStopBusLists;
        this.schAlarmInfo = schAlarmInfo;
        notifyDataSetChanged();
    }

    class AddAlarmFavListViewHolder extends RecyclerView.ViewHolder{
        AddalarmFavItemBinding addalarmFavItemBinding;

        public AddAlarmFavListViewHolder(AddalarmFavItemBinding binding) {
            super(binding.getRoot());
            addalarmFavItemBinding = binding;
        }
    }
}
