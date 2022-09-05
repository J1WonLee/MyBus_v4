package com.example.mybus.searchDetail;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.StopDetailItemBinding;
import com.example.mybus.search.StopSearchListAdapter;

import java.util.Collections;
import java.util.List;

public class SearchDetailAdapter extends RecyclerView.Adapter<SearchDetailAdapter.SearchDetailViewHolder> {
    // 서울시 정류장 조회일 경우
    private List<StopUidSchList> sBusStopList;
    // 경기도 정류장 조회일 경우
    private List<BusArrivalList> gBusStopList;
    // 카운트 다운 
    private CountDownTimer countDownTimer;
    @NonNull
    @Override
    public SearchDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StopDetailItemBinding stopDetailItemBinding = StopDetailItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchDetailViewHolder(stopDetailItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchDetailViewHolder holder, int position) {
        if (sBusStopList != null){
            StopUidSchList stopUidSch = sBusStopList.get(position);
            if (position == 0){
                switch (stopUidSch.getRouteType()){
                    case "1":
                        // 공항 버스
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("공항 버스");
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainSeat.setText(stopUidSch.isFullFlag1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "2":
                        // 마을
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("마을 버스");
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainSeat.setText(stopUidSch.isFullFlag1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "3":
                        // 간선
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("간선 버스");
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainSeat.setText(stopUidSch.isFullFlag1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "4":
                        // 지선
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("지선 버스");
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainSeat.setText(stopUidSch.isFullFlag1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "5":
                        // 순환
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("순환 버스");
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainSeat.setText(stopUidSch.isFullFlag1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "6":
                        // 광역
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("광역 버스");
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainSeat.setText(stopUidSch.isFullFlag1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "7":
                        // 인천
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("인천 버스");
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainSeat.setText(stopUidSch.isFullFlag1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "8":
                        // 경기
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("경기 버스");
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainSeat.setText(stopUidSch.isFullFlag1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "9":
                        // 폐지
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("폐지");
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainSeat.setText(stopUidSch.isFullFlag1);
                        break;
                    case "0":
                        // 공용
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("공용");
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainSeat.setText(stopUidSch.isFullFlag1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                }
            }else{
                setContents(holder, stopUidSch, sBusStopList.get(position-1));
            }
        }else if (gBusStopList != null){
            BusArrivalList busArrival = gBusStopList.get(position);
            holder.binding.busRouteName.setText(busArrival.getRouteNm());
        }
    }

    public void setContents(SearchDetailViewHolder holder, StopUidSchList stopUidSch, StopUidSchList prevStopUidSch ){
        if (!setRouteTypeContents(stopUidSch.getRouteType(), prevStopUidSch.getRouteType())){
            // 새로운 버스 타입 등장한 경우
            holder.binding.BusSort.setVisibility(View.VISIBLE);
            holder.binding.BusSort.setText(setBusSort(stopUidSch.getRouteType()));
            holder.binding.busRouteName.setText(stopUidSch.getRtNm());
            setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
            setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
            holder.binding.firstRemainSeat.setText(setFullFlag(stopUidSch.getIsFullFlag1()));
            holder.binding.secondRemainSeat.setText(setFullFlag(stopUidSch.getIsFullFlag2()));
        }else{
            holder.binding.BusSort.setVisibility(View.GONE);
            holder.binding.busRouteName.setText(stopUidSch.getRtNm());
            setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
            setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
            holder.binding.firstRemainSeat.setText(setFullFlag(stopUidSch.getIsFullFlag1()));
            holder.binding.secondRemainSeat.setText(setFullFlag(stopUidSch.getIsFullFlag2()));
        }
    }

    public void setRemainTime(SearchDetailViewHolder holder, String time, int flag){
        if (!(time.contains("분") || time.contains("초"))){
            // 시간 설정x
            if (flag == 1){
                holder.binding.firstRemainTime.setText(time);
            }else{
                holder.binding.secondRemainTime.setText(time);
            }

        }else{
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
                        holder.binding.firstRemainTime.setText(min +" 분 " + second +" 초 " + remainStops);
                    }else{
                        holder.binding.secondRemainTime.setText(min +" 분 " + second +" 초 " + remainStops);
                    }

                }

                @Override
                public void onFinish() {
                    if (flag == 1){
                        holder.binding.firstRemainTime.setText("시간 초과");
                    }else{
                        holder.binding.firstRemainTime.setText("시간 초과");
                    }

                }
            }.start();
        }

    }

    public String setFullFlag(String flag){
        if (flag.equals("1")){
            return "만차";
        }else{
            return "여유";
        }
    }

    public String setBusSort(String type) {
        switch (type) {
            case "1":
                // 공항 버스
                return "공항 버스";
            case "2":
                // 마을
                return "마을 버스";
            case "3":
                // 간선
                return "간선 버스";
            case "4":
                // 지선
                return "지선 버스";
            case "5":
                // 순환
                return "순환 버스";
            case "6":
                // 광역
                return "광역 버스";
            case "7":
                // 인천
                return "인천 버스";
            case "8":
                // 경기
                return "경기 버스";
            case "9":
                // 폐지
                return "폐지";
            case "0":
                // 공용
                return "공용";
            default:
                return "폐지";
        }
    }

    public boolean setRouteTypeContents(String a, String b){
        if (a.equals(b)){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int getItemCount() {
        if (sBusStopList != null){
            return sBusStopList.size();
        }else if (gBusStopList != null){
            return gBusStopList.size();
        }else{
            return 0;
        }
    }

    // 서울 정류장
    public void updateSBusStopList(List<StopUidSchList> lists){
        this.sBusStopList = lists;
//        Log.d("kkang", "call updatesbusstoplist" + sBusStopList.size() +" ");
    }

    // 경기도 정류장
    public void updateGBusStopList(List<BusArrivalList> lists){
        this.gBusStopList = lists;
//        Collections.sort(gBusStopList);
    }

    class SearchDetailViewHolder extends RecyclerView.ViewHolder{
        StopDetailItemBinding binding;


        public SearchDetailViewHolder(@NonNull StopDetailItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
