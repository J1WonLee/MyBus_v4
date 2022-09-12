package com.example.mybus.mainadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.MainFavItemBinding;
import com.example.mybus.searchDetail.BusRouteDetailAdapter;
import com.example.mybus.vo.DataWithFavStopBus;

import java.util.List;

public class MainFavAdapter extends RecyclerView.Adapter<MainFavAdapter.MainFavViewHolder>{
    private List<DataWithFavStopBus> dataWithFavStopBusList;
    private List<StopUidSchList> stopUidSchList;
    private List<BusArrivalList> busArrivalList;
    private OnItemClickListener mListener;
    @NonNull
    @Override
    public MainFavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainFavItemBinding mainFavItemBinding = MainFavItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MainFavViewHolder(mainFavItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFavViewHolder holder, int position) {
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
    }

    public void setChildAdapter(MainFavViewHolder holder, int position){
        MainFavChildAdapter mainFavChildAdapter =  new MainFavChildAdapter(dataWithFavStopBusList.get(position).localFavStopBusList, stopUidSchList, busArrivalList);
        holder.mainFavItemBinding.nestedRv.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.mainFavItemBinding.nestedRv.setAdapter(mainFavChildAdapter);
    }


    public void updateDataWithFavStopBusList(List<DataWithFavStopBus> dataWithFavStopBusLists, List<StopUidSchList> stopUidSchLists){
        this.dataWithFavStopBusList = dataWithFavStopBusLists;
        this.stopUidSchList = stopUidSchLists;
        notifyDataSetChanged();
    }

    public void updateDataWithFavStopBusList(List<DataWithFavStopBus> dataWithFavStopBusLists, List<StopUidSchList> stopUidSchLists,List<BusArrivalList> busArrivalLists){
        this.dataWithFavStopBusList = dataWithFavStopBusLists;
        this.stopUidSchList = stopUidSchLists;
        this.busArrivalList = busArrivalLists;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onBtnClick(View v , int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
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
        }
    }
}
