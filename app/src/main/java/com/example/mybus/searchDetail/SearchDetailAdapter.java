package com.example.mybus.searchDetail;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.StopDetailItemBinding;
import com.example.mybus.search.StopSearchListAdapter;
import com.example.mybus.vo.LocalFavStopBus;

import java.util.Collections;
import java.util.List;

public class SearchDetailAdapter extends RecyclerView.Adapter<SearchDetailAdapter.SearchDetailViewHolder> {
    // 서울시 정류장 조회일 경우
    private List<StopUidSchList> sBusStopList;
    // 경기도 정류장 조회일 경우
    private List<BusArrivalList> gBusStopList;
    // 카운트 다운 
    private CountDownTimer countDownTimer;
    // 즐겨찾기 목록
    private List<LocalFavStopBus> localFavStopBusList;
    // 정류장 즐겨찾기 삭제 시 별 표시 해줄 플래그
    public boolean isFaved = false;
    public boolean isClicked = false;
    private String busId = null;
    // 클릭 리스너
    private OnItemClickListener mListener;
    public boolean isFirstClicked = false;
    public int firstClickPos = -1;
    @NonNull
    @Override
    public SearchDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StopDetailItemBinding stopDetailItemBinding = StopDetailItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchDetailViewHolder(stopDetailItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchDetailViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (sBusStopList != null){
            StopUidSchList stopUidSch = sBusStopList.get(position);
            if (position == 0){
                switch (stopUidSch.getRouteType()){
                    case "1":
                        // 공항 버스
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("공항 버스");
                        holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag2, 2);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        setBackGroundColor(holder, position);
                        setFavImage(holder, stopUidSch);
                        break;
                    case "2":
                        // 마을
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("마을 버스");
                        holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag2, 2);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        setBackGroundColor(holder, position);
                        break;
                    case "3":
                        // 간선
                        setFavImage(holder, stopUidSch);
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("간선 버스");
                        holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag2, 2);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        setBackGroundColor(holder, position);
                        break;
                    case "4":
                        // 지선
                        setFavImage(holder, stopUidSch);
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("지선 버스");
                        holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag2, 2);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "5":
                        // 순환
                        setFavImage(holder, stopUidSch);
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("순환 버스");
                        holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag2, 2);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "6":
                        // 광역
                        setFavImage(holder, stopUidSch);
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("광역 버스");
                        holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag2, 2);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "7":
                        // 인천
                        setFavImage(holder, stopUidSch);
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("인천 버스");
                        holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag2, 2);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "8":
                        // 경기
                        setFavImage(holder, stopUidSch);
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("경기 버스");
                        holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag2, 2);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    case "9":
                        // 폐지
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("폐지");
                        holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        holder.binding.firstRemainTime.setText("폐지");
                        holder.binding.secondRemainTime.setText("폐지");
                        holder.binding.firstRemainStop.setVisibility(View.INVISIBLE);
                        holder.binding.secondRemainStop.setVisibility(View.INVISIBLE);
                        break;
                    case "0":
                        // 공용
                        holder.binding.BusSort.setVisibility(View.VISIBLE);
                        holder.binding.BusSort.setText("공용");
                        holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
                        holder.binding.busRouteName.setText(stopUidSch.getRtNm());
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
                        setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag2, 2);
                        setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
                        setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
                        break;
                    default:
                        setBackGroundColor(holder, position);
                        break;
                }
            }else{
                setFavImage(holder, stopUidSch);
                setContents(holder, stopUidSch, sBusStopList.get(position-1));
                setBackGroundColor(holder, position);
            }
        }else if (gBusStopList != null){
            BusArrivalList busArrival = gBusStopList.get(position);
            holder.binding.busRouteName.setText(busArrival.getRouteNm());
            gBusSetRemainTime(holder, busArrival.getPredictTime1(), 1);
            gBusSetRemainTime(holder, busArrival.getPredictTime2(), 2);
            gBusSetRemainSeat(holder, busArrival.getRemainSeatCnt1(), busArrival.getLocationNo1(), 1);
            gBusSetRemainSeat(holder, busArrival.getRemainSeatCnt2(), busArrival.getLocationNo2(),2);
            setGbusBackGroundColor(holder, position);
            setGbusFavImage(holder, busArrival);
        }
    }

    public void setContents(SearchDetailViewHolder holder, StopUidSchList stopUidSch, StopUidSchList prevStopUidSch ){
        if (!setRouteTypeContents(stopUidSch.getRouteType(), prevStopUidSch.getRouteType())){
            // 새로운 버스 타입 등장한 경우
            holder.binding.BusSort.setVisibility(View.VISIBLE);
            holder.binding.BusSort.setText(setBusSort(stopUidSch.getRouteType()));
            holder.binding.BusSort.setBackgroundResource(R.color.lightgray);
            holder.binding.busRouteName.setText(stopUidSch.getRtNm());
            holder.binding.firstRemainTime.setText(stopUidSch.getArrmsgSec1());
            setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
            setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
            setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
            setRemainSeat(holder, stopUidSch.getArrmsgSec2(), stopUidSch.isFullFlag2, 2);
        }else{
            holder.binding.BusSort.setVisibility(View.GONE);
            holder.binding.busRouteName.setText(stopUidSch.getRtNm());
            setRemainTime(holder, stopUidSch.getArrmsgSec1(), 1);
            setRemainTime(holder, stopUidSch.getArrmsgSec2(), 2);
            setRemainSeat(holder, stopUidSch.getArrmsgSec1(), stopUidSch.isFullFlag1 , 1);
            setRemainSeat(holder, stopUidSch.getArrmsgSec2(), stopUidSch.isFullFlag2, 2);
        }
    }

    public void setRemainTime(SearchDetailViewHolder holder, String time, int flag){
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
                        holder.binding.firstRemainTime.setText(min +" 분 " + second +" 초 ");
                        holder.binding.firstRemainStop.setText(remainStops);
                    }else{
                        holder.binding.secondRemainTime.setText(min +" 분 " + second +" 초 ");
                        holder.binding.secondRemainStop.setText(remainStops);
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

        }catch(Exception e){
            Log.d("kkang", "Exception in searchdetailadapter setremaintime method msg : " + e.getMessage());
            if (flag == 1){
                holder.binding.firstRemainTime.setText(time);
            }else {
                holder.binding.secondRemainTime.setText(time);
            }


        }
    }

    public void gBusSetRemainTime(SearchDetailViewHolder holder, String time, int flag){
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
                        holder.binding.firstRemainTime.setText(min +" 분 " + second +" 초 ");
                    }else{
                        holder.binding.secondRemainTime.setText(min +" 분 " + second +" 초 ");
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
        }catch(Exception e){
            Log.d("SearchDetailViewHolder", "Exception in searchdetailadapter gbussetRemainTime method msg : " + e.getMessage());
            if (flag == 1){
                holder.binding.firstRemainTime.setText("도착 정보가 없습니다");
            }else {
                holder.binding.secondRemainTime.setText("도착 정보가 없습니다");
            }
        }
    }

    public String setFullFlag(String flag, String msg){
        if (msg.contains("종료")){
            return "종료";
        }else{
            if (flag.equals("1")){
                return "혼잡";
            }else{
                return "여유";
            }
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

    public void setRemainSeat(SearchDetailViewHolder holder, String time, String fullFlag , int flag){
        try{
            String remainStops = time.substring(time.indexOf('[')+1, time.indexOf(']'));
            String isFull = setFullFlag(fullFlag, time);
            if (flag == 1){
                holder.binding.firstRemainStop.setText(remainStops + " " + isFull);
            }else{
                holder.binding.secondRemainStop.setText(remainStops + " " + isFull);
            }
        }catch(Exception e){
            if (flag == 1){
                holder.binding.firstRemainStop.setText(" ");
            }else{
                holder.binding.secondRemainStop.setText(" ");
            }
        }



    }

    public void gBusSetRemainSeat(SearchDetailViewHolder holder, String seat, String stops , int flag){
        String remainStops = stops.equals("") ? " " : stops + "번째 전";
         if (flag == 1){
             if (seat.equals("-1")){
                 holder.binding.firstRemainStop.setText(remainStops);
             }else{
                 holder.binding.firstRemainStop.setText(remainStops + " " + seat);
             }
         }else{
             if (seat.equals("-1")){
                 holder.binding.secondRemainStop.setText(remainStops);
             }else{
                 holder.binding.secondRemainStop.setText(remainStops +" " + seat);
             }
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
//
//    // 서울 정류장
//    public void updateSBusStopList(List<StopUidSchList> lists, List<LocalFavStopBus> localFavStopBusList){
//        this.sBusStopList = lists;
//        this.localFavStopBusList = localFavStopBusList;
//        notifyDataSetChanged();
////        Log.d("kkang", "call updatesbusstoplist" + sBusStopList.size() +" ");
//    }

    public void updateFavListSbus(List<LocalFavStopBus> localFavStopBusList, List<StopUidSchList> lists){
        this.localFavStopBusList = localFavStopBusList;
        this.sBusStopList = lists;
        notifyDataSetChanged();
    }

    // 즐겨찾기 목록 및 경기도 버스장 리스트 초기화
    public void updateFavList(List<LocalFavStopBus> localFavStopBusList, List<BusArrivalList> lists){
        this.localFavStopBusList = localFavStopBusList;
        this.gBusStopList = lists;
        notifyDataSetChanged();
    }


    // 즐겨찾기 목록 초기화
    public void updateFavList(List<LocalFavStopBus> localFavStopBusList){
        this.localFavStopBusList = localFavStopBusList;
        notifyDataSetChanged();
    }


    // 서울 정류장
    public void updateSBusStopList(List<StopUidSchList> lists, @Nullable String busId){
        this.sBusStopList = lists;
        if (busId != null)  this.busId = busId;
        notifyDataSetChanged();
    }


    public void updateLists(List<StopUidSchList> lists){
        this.sBusStopList = lists;
        notifyDataSetChanged();
    }

    public void updateGbusLists(List<BusArrivalList> lists){
        this.gBusStopList = lists;
        Log.d("SearchDetailViewHolder", "updateGbusLists updated!");
        try{
            notifyDataSetChanged();
        }catch (Exception e){
            Log.d("SearchDetailViewHolder", "updateGbusLists updated error!" + e.getMessage());
        }
    }

    // 경기도 정류장
    public void updateGBusStopList(List<BusArrivalList> lists , @Nullable String busId){
        this.gBusStopList = lists;
        if (busId != null)          this.busId = busId;
        notifyDataSetChanged();
    }

    // 클릭 리스너
    public interface OnItemClickListener{
        void onItemClick(View v , int position);
        void onFabBtnClick(View v, int position);
        void onAlarmClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
    }

    class SearchDetailViewHolder extends RecyclerView.ViewHolder{
        StopDetailItemBinding binding;
        public SearchDetailViewHolder(@NonNull StopDetailItemBinding binding) {
            super(binding.getRoot());
            Log.d("SearchDetailViewHolder", "SearchDetailViewHolder called!");
            this.binding = binding;
            // 노선 상세보기 클릭
            binding.stopDetailListLayout.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && mListener != null){
                    mListener.onItemClick(view, pos);
                }
            });

            // 즐겨찾기 추가
            binding.addFav.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && mListener != null){
                    mListener.onFabBtnClick(view, pos);
                }
            });

            // 알람 추가 클릭 시
            binding.addAlarm.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && mListener != null){
                    mListener.onAlarmClick(view, pos);
                }
            });
        }
    }

    // 서울 정류장 버스 즐겨찾기 표시
    public void setFavImage(SearchDetailViewHolder holder, StopUidSchList stopUidSch) {
        if (localFavStopBusList != null && !isClicked) {
            for (LocalFavStopBus lsb : localFavStopBusList) {
                if (stopUidSch.getBusRouteId().equals(lsb.lfb_busId)) {
                    Log.d("SearchDetailViewHolder", "on searchdetailadapter set fav image");
                    stopUidSch.setFlag(true);
                    holder.binding.addFav.setImageResource(R.drawable.ic_baseline_star_24);
                }
            }
        }else if (localFavStopBusList != null && isClicked){
            if (stopUidSch.getFlag()){
                stopUidSch.setFlag(true);
                holder.binding.addFav.setImageResource(R.drawable.ic_baseline_star_24);
            }else{
                stopUidSch.setFlag(false);
                holder.binding.addFav.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
        }else{
            stopUidSch.setFlag(false);
        }
    }

    // 경기도 정류장 버스 즐겨찾기 표시
    public void setGbusFavImage(SearchDetailViewHolder holder, BusArrivalList busArrivalList) {
        Log.d("SearchDetailViewHolder", "busArrivalList is ::::::::::" + busArrivalList.isChkFlag() +" busnm :::::::::::" + busArrivalList.getRouteNm());
        if (localFavStopBusList != null && !isClicked) {
            for (LocalFavStopBus lsb : localFavStopBusList) {
                if (busArrivalList.getRouteId().equals(lsb.lfb_busId)) {
                    Log.d("SearchDetailViewHolder", "on searchdetailadapter set fav image");
                    busArrivalList.setChkFlag(true);
                    holder.binding.addFav.setImageResource(R.drawable.ic_baseline_star_24);
                }
            }
        }else if (localFavStopBusList != null && isClicked){
            if (busArrivalList.isChkFlag()){
                Log.d("SearchDetailViewHolder", "if state isChkFlag is ::::::::::" + busArrivalList.isChkFlag() +" busnm :::::::::::" + busArrivalList.getRouteNm());
                holder.binding.addFav.setImageResource(R.drawable.ic_baseline_star_24);
            }else{
                Log.d("SearchDetailViewHolder", " else state isChkFlag is ::::::::::" + busArrivalList.isChkFlag() +" busnm :::::::::::" + busArrivalList.getRouteNm());
                holder.binding.addFav.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
        }else{
            busArrivalList.setChkFlag(false);
        }
    }


    public void setBackGroundColor(SearchDetailViewHolder holder, int position){
        if (this.busId != null && sBusStopList.get(position).getBusRouteId().equals(busId)){
            holder.binding.stopDetailListLayout.setBackgroundResource(R.drawable.detail_list_item_deco);
        }
    }

    public void setGbusBackGroundColor(SearchDetailViewHolder holder, int position){
        if (this.busId != null && gBusStopList.get(position).getRouteId().equals(busId)){
            holder.binding.stopDetailListLayout.setBackgroundResource(R.drawable.detail_list_item_deco);
        }
    }

}
