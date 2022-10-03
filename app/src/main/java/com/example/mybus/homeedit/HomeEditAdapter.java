package com.example.mybus.homeedit;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.databinding.HomeEditItemBinding;
import com.example.mybus.vo.DataWithFavStopBus;
import com.example.mybus.vo.LocalFav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class HomeEditAdapter extends RecyclerView.Adapter<HomeEditAdapter.HomeEditViewHolder> {
    public List<DataWithFavStopBus> dataWithFavStopBusList;
    private List<LocalFav> localFavList = new ArrayList<>();
    private List<LocalFav> deleteLocalFavList = new ArrayList<>();

    public HomeEditAdapter(List<DataWithFavStopBus> dataWithFavStopBusList) {
        this.dataWithFavStopBusList = dataWithFavStopBusList;
        setLocalFavList(dataWithFavStopBusList);
    }

    public void setLocalFavList(List<DataWithFavStopBus> dataWithFavStopBusList){
        if (dataWithFavStopBusList != null){
            for (int i =0; i< dataWithFavStopBusList.size(); i++){
                localFavList.add(dataWithFavStopBusList.get(i).localFav);
            }
        }

    }

    @NonNull
    @Override
    public HomeEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        HomeEditItemBinding homeEditItemBinding = HomeEditItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HomeEditViewHolder(homeEditItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeEditViewHolder holder, int position) {
        setTexts(holder, position);
    }

    @Override
    public int getItemCount() {
        if (dataWithFavStopBusList != null){
             return localFavList.size();
        }else{
            return 0;
        }
    }

    public void setTexts(HomeEditViewHolder holder, int position){
//        holder.homeEditItemBinding.listName.setText(dataWithFavStopBusList.get(position).localFav.getLf_name());
//        holder.homeEditItemBinding.homeEditDesc.setText(dataWithFavStopBusList.get(position).localFav.getLf_desc());

        holder.homeEditItemBinding.listName.setText(localFavList.get(position).getLf_name());
        holder.homeEditItemBinding.homeEditDesc.setText(localFavList.get(position).getLf_desc());
    }

//    @Override
//    public boolean onItemMove(int from_position, int to_position) {
//        DataWithFavStopBus dataWithFavStopBus = dataWithFavStopBusList.get(from_position);
//        dataWithFavStopBusList.remove(from_position);
//        dataWithFavStopBusList.add(to_position, dataWithFavStopBus);
//        notifyItemMoved(from_position, to_position);
//        Log.d("HomeEditAdapter" , "from_pos = " + from_position + "from date = " + dataWithFavStopBus.localFav.getLf_order() + " to_pos = " + to_position +" to date = " + dataWithFavStopBusList.get(to_position).localFav.getLf_order());
//        return true;
//    }
//
//    @Override
//    public void onItemSwipe(int position) {
//
//    }

    public void swapData(int from, int to){
//        Date date = dataWithFavStopBusList.get(to).localFav.getLf_order();
//        Log.d("HomeEditAdapter", "to date : " + date);
//        if (from < to){
//            date.setTime(date.getTime()+1000);
//        }else{
//            date.setTime(date.getTime()-1000);
//        }
//        Log.d("HomeEditAdapter", "after date : " + date);
//        dataWithFavStopBusList.get(from).localFav.setLf_order(date);
//        Collections.swap(dataWithFavStopBusList, from, to);
//        notifyItemMoved(from, to);

        Date date = localFavList.get(to).getLf_order();
        Date oldDate = localFavList.get(from).getLf_order();
        Log.d("HomeEditAdapter", "to date : " + date);
        if (from < to){
            date.setTime(date.getTime()-1000);
        }else{
            date.setTime(date.getTime()+1000);
        }
        Log.d("HomeEditAdapter", "after date : " + date);
        localFavList.get(from).setLf_order(date);
        localFavList.get(to).setLf_order(oldDate);
        Log.d("HomeEditAdapter", "from after time :::::::::::::: " + localFavList.get(from).getLf_order());
        Log.d("HomeEditAdapter", "to after time :::::::::::::: " + localFavList.get(to).getLf_order());
        Collections.swap(localFavList, from, to);
        notifyItemMoved(from, to);
    }

    public List<LocalFav> getRemoveList(){
        return deleteLocalFavList;
    }

    public void removeData(int position){
        deleteLocalFavList.add(localFavList.get(position));
        localFavList.remove(position);
        notifyItemRemoved(position);
    }

    public List<LocalFav> getLocalFav(){
        return localFavList;
    }

    class HomeEditViewHolder extends RecyclerView.ViewHolder{
        HomeEditItemBinding homeEditItemBinding;
        public HomeEditViewHolder(HomeEditItemBinding binding) {
            super(binding.getRoot());
            this.homeEditItemBinding = binding;
        }
    }
}
