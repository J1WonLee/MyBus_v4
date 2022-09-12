package com.example.mybus.mainadapter;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.MainFavBuslistItemBinding;
import com.example.mybus.searchDetail.SearchDetailAdapter;
import com.example.mybus.vo.LocalFavStopBus;

import java.util.List;

public class MainFavChildAdapter extends RecyclerView.Adapter<MainFavChildAdapter.MainFavChildViewHolder> {
    private List<LocalFavStopBus> localFavStopBusList;
    private List<StopUidSchList> stopUidSchList;
    private List<BusArrivalList> busArrivalList;
    private CountDownTimer countDownTimer;
    private ChileOnItemClickListener mListener;

    public MainFavChildAdapter(List<LocalFavStopBus> localFavStopBusList, List<StopUidSchList> stopUidSchLists, List<BusArrivalList> busArrivalLists) {
        this.localFavStopBusList = localFavStopBusList;
        this.stopUidSchList = stopUidSchLists;
        this.busArrivalList = busArrivalLists;
        Log.d("MainFavChildAdapter", "size of list : " + localFavStopBusList.size());
    }

    @NonNull
    @Override
    public MainFavChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainFavBuslistItemBinding favBuslistItemBinding = MainFavBuslistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MainFavChildViewHolder(favBuslistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFavChildViewHolder holder, int position) {
        // 서울시 정류장인 경우
        if (localFavStopBusList.get(position).getLfb_id().length()<=6){
            setTexts(holder, position);
        }else{
            // 경기도 정류장인 경우
            gBusSetTexts(holder, position);
        }

    }

    @Override
    public int getItemCount() {
        if (localFavStopBusList != null){
            return localFavStopBusList.size();
        }else{
            return 0;
        }
    }

    public void setTexts(MainFavChildViewHolder holder, int position){
        holder.favBuslistItemBinding.busName.setText(localFavStopBusList.get(position).lfb_busName);
        for (StopUidSchList lists : stopUidSchList){
            if (lists.getArsId().equals(localFavStopBusList.get(position).getLfb_id())){
                if (lists.getBusRouteId().equals(localFavStopBusList.get(position).getLfb_busId())){
                    setRemainTime(holder, lists.getArrmsg1(), 1);
                    setRemainTime(holder, lists.getArrmsgSec2(), 2);
                }
            }
        }
    }

    public void gBusSetTexts(MainFavChildViewHolder holder, int position){
        holder.favBuslistItemBinding.busName.setText(localFavStopBusList.get(position).lfb_busName);
        for (BusArrivalList lists : busArrivalList){
            if (lists.getStationId().equals(localFavStopBusList.get(position).getLfb_id())){
                if (lists.getRouteId().equals(localFavStopBusList.get(position).getLfb_busId())){
                    gBusSetRemainTime(holder, lists.getPredictTime1(), 1);
                    gBusSetRemainTime(holder, lists.getPredictTime2(), 2);
                }
            }
        }
    }


    public void setRemainTime(MainFavChildViewHolder holder, String time, int flag){
        try{
            long conversionTime = 0;
            String remainStops = time.substring(time.indexOf('[')+1, time.indexOf(']'));
            String getMinute = time.substring(0, time.indexOf("분"));
            String getSeconds = time.substring(time.indexOf("분")+1, time.indexOf("초"));

            conversionTime = Long.valueOf(getMinute) * 60 * 1000 + Long.valueOf(getSeconds) * 1000;

            countDownTimer = new CountDownTimer(conversionTime, 3000) {
                // fab 통해서 주기적으로 새로고침 시켜주기
                @Override
                public void onTick(long milliUntilFinished) {
                    long getMin = milliUntilFinished - (milliUntilFinished / (60 * 60 * 1000));
                    String min = String.valueOf(getMin / (60 * 1000));      // 몫
                    String second = String.valueOf((getMin % (60 * 1000)) / 1000);

                    if (flag == 1){
                        holder.favBuslistItemBinding.firstRemainTime.setText(min +" 분 " + second +" 초 " + remainStops);
                    }else{
                        holder.favBuslistItemBinding.secondRemainTime.setText(min +" 분 " + second +" 초 " + remainStops);
                    }

                }

                @Override
                public void onFinish() {
                    if (flag == 1){
                        holder.favBuslistItemBinding.firstRemainTime.setText("시간 초과");
                    }else{
                        holder.favBuslistItemBinding.secondRemainTime.setText("시간 초과");
                    }

                }
            }.start();

        }catch(Exception e){
            Log.d("MainFavChildAdapter", "Exception in searchdetailadapter setremaintime method msg : " + e.getMessage());
            if (flag == 1){
                holder.favBuslistItemBinding.firstRemainTime.setText(time);
            }else {
                holder.favBuslistItemBinding.secondRemainTime.setText(time);
            }
        }
    }

    public void gBusSetRemainTime(MainFavChildViewHolder holder, String time, int flag){
        try{
            long conversionTime = 0;
            String getMinute = time;
            int getSeconds = 0;

            conversionTime = Long.valueOf(getMinute) * 60 * 1000 + (getSeconds * 1000);

            countDownTimer = new CountDownTimer(conversionTime, 3000) {
                @Override
                public void onTick(long milliUntilFinished) {
                    long getMin = milliUntilFinished - (milliUntilFinished / (60 * 60 * 1000));
                    String min = String.valueOf(getMin / (60 * 1000));      // 몫
                    String second = String.valueOf((getMin % (60 * 1000)) / 1000);

                    if (flag == 1){
                        holder.favBuslistItemBinding.firstRemainTime.setText(min +" 분 " + second +" 초 ");
                    }else{
                        holder.favBuslistItemBinding.secondRemainTime.setText(min +" 분 " + second +" 초 ");
                    }
                }

                @Override
                public void onFinish() {
                    if (flag == 1){
                        holder.favBuslistItemBinding.firstRemainTime.setText("시간 초과");
                    }else{
                        holder.favBuslistItemBinding.secondRemainTime.setText("시간 초과");
                    }
                }
            }.start();
        }catch(Exception e){
            Log.d("kkang", "Exception in searchdetailadapter gbussetRemainTime method msg : " + e.getMessage());
            if (flag == 1){
                holder.favBuslistItemBinding.firstRemainTime.setText("도착 정보가 없습니다");
            }else {
                holder.favBuslistItemBinding.secondRemainTime.setText("도착 정보가 없습니다");
            }
        }
    }

    public interface ChileOnItemClickListener{
        void onBtnClick(View v , int position);
    }

    public void setOnItemClickListener(ChileOnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
    }


    class MainFavChildViewHolder extends RecyclerView.ViewHolder{
        MainFavBuslistItemBinding favBuslistItemBinding;
        public MainFavChildViewHolder(MainFavBuslistItemBinding binding) {
            super(binding.getRoot());
            favBuslistItemBinding = binding;
        }
    }
}
