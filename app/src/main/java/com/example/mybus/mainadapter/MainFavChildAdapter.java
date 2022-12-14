package com.example.mybus.mainadapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.ActivityAnimate;
import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.MainFavBuslistItemBinding;
import com.example.mybus.menu.alarm.AlarmArriveActivity;
import com.example.mybus.searchDetail.BusRouteDetailActivity;
import com.example.mybus.searchDetail.SearchDetailAdapter;
import com.example.mybus.vo.LocalFavStopBus;

import java.util.List;

public class MainFavChildAdapter extends RecyclerView.Adapter<MainFavChildAdapter.MainFavChildViewHolder>{
    public List<LocalFavStopBus> localFavStopBusList;
    public List<StopUidSchList> stopUidSchList;
    public List<BusArrivalList> busArrivalList;
    private CountDownTimer countDownTimer;
    public static ChildOnItemClickListener mListener;
    private String stNm;

    public MainFavChildAdapter(List<LocalFavStopBus> localFavStopBusList, List<StopUidSchList> stopUidSchLists, List<BusArrivalList> busArrivalLists, String stNm) {
        this.localFavStopBusList = localFavStopBusList;
        this.stopUidSchList = stopUidSchLists;
        this.busArrivalList = busArrivalLists;
        this.stNm = stNm;
//        Log.d("MainFavChildAdapter", "size of list : " + localFavStopBusList.size());
    }



    @NonNull
    @Override
    public MainFavChildViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainFavBuslistItemBinding favBuslistItemBinding = MainFavBuslistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MainFavChildViewHolder(favBuslistItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainFavChildViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        // ????????? ???????????? ??????
        if (localFavStopBusList.get(position).getLfb_id().length()<=5){
            setTexts(holder, position);
        }else{
            // ????????? ???????????? ??????
            gBusSetTexts(holder, position);
        }
        setListener(holder, position);

    }

    public void setListener( MainFavChildViewHolder holder, int position){
        Bundle args = new Bundle();
        BusSchList busLists = new BusSchList();
        holder.favBuslistItemBinding.busName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MainFavChildAdapter", localFavStopBusList.get(position).getLfb_busId());
                busLists.setStId(localFavStopBusList.get(position).getStId());
                busLists.setBusRouteId(localFavStopBusList.get(position).getLfb_busId());
                busLists.setBusRouteNm(localFavStopBusList.get(position).getLfb_busName());
                busLists.setCorpNm("-1");
                Intent intent = new Intent(holder.itemView.getContext(), BusRouteDetailActivity.class);
                intent.setAction("com.example.mybus.fromMain");
                args.putParcelable("busList", busLists);
                intent.putExtras(args);
                holder.itemView.getContext().startActivity(intent);
                ((Activity)holder.itemView.getContext()).overridePendingTransition(R.anim.vertical_center, R.anim.none);
            }
        });

        holder.favBuslistItemBinding.busRemainTimeWrap.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Log.d("MainFavChildAdapter", localFavStopBusList.get(position).getLfb_busId());
                busLists.setStId(localFavStopBusList.get(position).getStId());
                busLists.setBusRouteId(localFavStopBusList.get(position).getLfb_busId());
                busLists.setBusRouteNm(localFavStopBusList.get(position).getLfb_busName());
                busLists.setCorpNm("123");
                Intent intent = new Intent(holder.itemView.getContext(), BusRouteDetailActivity.class);
                intent.setAction("com.example.mybus.fromMain");
                args.putParcelable("busList", busLists);
                intent.putExtras(args);
                holder.itemView.getContext().startActivity(intent);
                ((Activity)holder.itemView.getContext()).overridePendingTransition(R.anim.vertical_center, R.anim.none);
            }
        });

        // ?????? ?????? ??????
        holder.favBuslistItemBinding.busAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busLists.setStId(localFavStopBusList.get(position).getStId());
                busLists.setBusRouteId(localFavStopBusList.get(position).getLfb_busId());
                busLists.setBusRouteNm(localFavStopBusList.get(position).getLfb_busName());
                busLists.setCorpNm(localFavStopBusList.get(position).getLfb_sectOrd());
                Intent alarmIntent = new Intent(holder.itemView.getContext(), AlarmArriveActivity.class);
                args.putParcelable("busList", busLists);
                alarmIntent.putExtras(args);
                holder.itemView.getContext().startActivity(alarmIntent);
                ((Activity)holder.itemView.getContext()).overridePendingTransition(R.anim.vertical_center, R.anim.none);
            }
        });
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
        if (stopUidSchList != null){
            for (StopUidSchList lists : stopUidSchList){
                if (lists.getArsId().equals(localFavStopBusList.get(position).getLfb_id())){
                    localFavStopBusList.get(position).setStId(lists.getStId());
                    if (lists.getBusRouteId().equals(localFavStopBusList.get(position).getLfb_busId())){
                        localFavStopBusList.get(position).setLfb_sectOrd(lists.getStaOrd());
                        setRemainTime(holder, lists.getArrmsg1(), 1);
                        setRemainTime(holder, lists.getArrmsgSec2(), 2);
                    }
                }
            }
        }

    }

    public void gBusSetTexts(MainFavChildViewHolder holder, int position){
        holder.favBuslistItemBinding.busName.setText(localFavStopBusList.get(position).lfb_busName);
        if (busArrivalList != null){
            for (BusArrivalList lists : busArrivalList){
                if (lists.getStationId().equals(localFavStopBusList.get(position).getLfb_id())){
                    localFavStopBusList.get(position).setStId(lists.getStationId());
                    if (lists.getRouteId().equals(localFavStopBusList.get(position).getLfb_busId())){
                        localFavStopBusList.get(position).setLfb_sectOrd(lists.getStaOrder());
                        gBusSetRemainTime(holder, lists.getPredictTime1(), lists.getLocationNo1(), 1);
                        gBusSetRemainTime(holder, lists.getPredictTime2(), lists.getLocationNo2(), 2);
                    }
                }
            }
        }
    }

    public void setRemainTime(MainFavChildViewHolder holder, String time, int flag){
        try{
            long conversionTime = 0;
            String remainStops = time.substring(time.indexOf('[')+1, time.indexOf(']'));
            String getMinute = time.substring(0, time.indexOf("???"));
            String getSeconds = time.substring(time.indexOf("???")+1, time.indexOf("???"));

            conversionTime = Long.valueOf(getMinute) * 60 * 1000 + Long.valueOf(getSeconds) * 1000;

            countDownTimer = new CountDownTimer(conversionTime, 3000) {
                // fab ????????? ??????????????? ???????????? ????????????
                @Override
                public void onTick(long milliUntilFinished) {
                    long getMin = milliUntilFinished - (milliUntilFinished / (60 * 60 * 1000));
                    String min = String.valueOf(getMin / (60 * 1000));      // ???
                    String second = String.valueOf((getMin % (60 * 1000)) / 1000);

                    if (flag == 1){
                        holder.favBuslistItemBinding.firstRemainTime.setText(min +" ??? " + second +" ??? " + remainStops);
                    }else{
                        holder.favBuslistItemBinding.secondRemainTime.setText(min +" ??? " + second +" ??? " + remainStops);
                    }

                }

                @Override
                public void onFinish() {
                    if (flag == 1){
                        holder.favBuslistItemBinding.firstRemainTime.setText("?????? ??????");
                    }else{
                        holder.favBuslistItemBinding.secondRemainTime.setText("?????? ??????");
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

    public void gBusSetRemainTime(MainFavChildViewHolder holder, String time, String stops, int flag){
        try{
            String remainStops = stops.equals("") ? "" : stops + "?????? ???";
            long conversionTime = 0;
            String getMinute = time;
            int getSeconds = 0;

            conversionTime = Long.valueOf(getMinute) * 60 * 1000 + (getSeconds * 1000);

            countDownTimer = new CountDownTimer(conversionTime, 3000) {
                @Override
                public void onTick(long milliUntilFinished) {
                    long getMin = milliUntilFinished - (milliUntilFinished / (60 * 60 * 1000));
                    String min = String.valueOf(getMin / (60 * 1000));      // ???
                    String second = String.valueOf((getMin % (60 * 1000)) / 1000);

                    if (flag == 1){
                        holder.favBuslistItemBinding.firstRemainTime.setText(min +" ??? " + second +" ??? " + " " + remainStops);
                    }else{
                        holder.favBuslistItemBinding.secondRemainTime.setText(min +" ??? " + second +" ??? " +  " " + remainStops);
                    }
                }

                @Override
                public void onFinish() {
                    if (flag == 1){
                        holder.favBuslistItemBinding.firstRemainTime.setText("?????? ??????");
                    }else{
                        holder.favBuslistItemBinding.secondRemainTime.setText("?????? ??????");
                    }
                }
            }.start();
        }catch(Exception e){
            Log.d("kkang", "Exception in searchdetailadapter gbussetRemainTime method msg : " + e.getMessage());
            if (flag == 1){
                holder.favBuslistItemBinding.firstRemainTime.setText("?????? ????????? ????????????");
            }else {
                holder.favBuslistItemBinding.secondRemainTime.setText("?????? ????????? ????????????");
            }
        }
    }

    public interface ChildOnItemClickListener{
        void onItemClick(View v , int position);
    }

    public void setOnItemClickListener(ChildOnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
    }


    class MainFavChildViewHolder extends RecyclerView.ViewHolder{
        MainFavBuslistItemBinding favBuslistItemBinding;
        public MainFavChildViewHolder(MainFavBuslistItemBinding binding) {
            super(binding.getRoot());
            favBuslistItemBinding = binding;

            favBuslistItemBinding.mainFavBuslistWrap.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && mListener != null){
                    mListener.onItemClick(view, pos);
                }
            });
        }
    }
}
