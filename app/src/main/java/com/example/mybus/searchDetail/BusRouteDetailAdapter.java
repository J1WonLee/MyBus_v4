package com.example.mybus.searchDetail;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusPosList;
import com.example.mybus.apisearch.itemList.GBusLocationList;
import com.example.mybus.apisearch.itemList.GBusRouteStationList;
import com.example.mybus.apisearch.itemList.StationByRouteList;
import com.example.mybus.databinding.BusRouteDetailItemBinding;

import java.util.ArrayList;
import java.util.List;

public class BusRouteDetailAdapter extends RecyclerView.Adapter<BusRouteDetailAdapter.BusRouteDetailViewHolder>{
    private List<GBusRouteStationList> gBusRouteStation;
    private List<GBusLocationList> gBusLocationList;
    private OnItemClickListener mListener;
    private List<StationByRouteList> stationByRouteList;
    private List<BusPosList> busPosList;
    private String stId = null;

    @NonNull
    @Override
    public BusRouteDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       BusRouteDetailItemBinding binding = BusRouteDetailItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
       return new BusRouteDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BusRouteDetailViewHolder holder, int position) {
        Log.d("BusRouteDetailAdapter", "onBindViewHolder called!");
        holder.setIsRecyclable(false);
        if (stationByRouteList != null){
            Log.d("BusRouteDetailAdapter", "busroutedetalAdapter onBindViewHolder if state");
            setStationList(holder, position);
            setBusPosition(holder, position);
            setBackgroundColor(holder, position);
        }else if (gBusLocationList != null){
            Log.d("BusRouteDetailAdapter", "busroutedetalAdapter onBindViewHolder else state");
            setGbusStationList(holder, position);
            setGbusLocation(holder, position);
            setGBusBackgroundColor(holder, position);
        }

    }

    @Override
    public int getItemCount() {
        if (stationByRouteList!=null){
            return stationByRouteList.size();
        }else if (gBusRouteStation != null){
            return gBusRouteStation.size();
        }else{
            return -1;
        }

    }

    // 서울시 정류장 정보
    public void updateRouteStationInfo(List<StationByRouteList> stationByRouteLists ,  @Nullable String stId){
        this.stationByRouteList = stationByRouteLists;
        if (stId != null)       this.stId = stId;
        this.notifyDataSetChanged();
    }

    // 서울시 버스 위치 정보
    public void updateRoutePosInfo(List<BusPosList> busPosLists){
        this.busPosList = busPosLists;
        for (BusPosList lists : busPosList){
            notifyItemChanged(Integer.valueOf(lists.getSectOrd()));
        }
    }

    // 경기도 정류장 정보
    public void updateGbusStationInfo(List<GBusRouteStationList> gBusRouteStationLists, @Nullable String stId){
//        Log.d("kkang", "busroutedetailadapter updateGbusStationInfo called");
        this.gBusRouteStation = gBusRouteStationLists;
        if (stId != null)       this.stId = stId;
        Log.d("BusRouteDetailAdapter" , " busroutedetailadapter size of updateGbusStationInfo gBusRouteStationList size  :  " + gBusRouteStation.size());
        notifyDataSetChanged();
    }

    // 경기도 버스 위치 정보
    public void updateGbusLocationList(List<GBusLocationList> gBusLocationLists){
        Log.d("kkang", "busroutedetailadapter updateGbusLocationList called");
        this.gBusLocationList = gBusLocationLists;
        for (GBusLocationList lists : gBusLocationList){
            notifyItemChanged(lists.getStationSeq());
        }
    }




    // 정류장 명, 정류장 번호 와 같이 텍스트를 채운다.
    public void setStationList(BusRouteDetailViewHolder holder, int position){
        if (stationByRouteList != null){
            Log.d("kkang" , "busroutedetailAdapter stestationlist  stationByRouteList ");
            if (position == 0){
                holder.binding.busRouteLineFirst.setVisibility(View.GONE);
            }else if (position == stationByRouteList.size()){
                holder.binding.busRouteLineThird.setVisibility(View.GONE);
            }
            if (stationByRouteList.get(position).getTransYn().equals("Y")){
                Log.d("kkang", "busroutedetailAdapter stestationlist getTransYn" + stationByRouteList.get(position).getStationNm());
                holder.binding.busRouteLineSecond.setImageResource(R.drawable.ic_baseline_refresh_24);
            }
            holder.binding.busRouteStopName.setText(stationByRouteList.get(position).getStationNm());

            holder.binding.busRouteStopId.setText("정류장 ID : " + stationByRouteList.get(position).getStation());
        }
    }

    // 버스의 현재 위치를 보여준다.
    public void setBusPosition(BusRouteDetailViewHolder holder, int position){
        if (busPosList != null){
            for (BusPosList lists : busPosList){
                if (position == Integer.valueOf(lists.getSectOrd())){
                    if (lists.getStopFlag().equals("1")){
                        holder.binding.busSecondImage.setVisibility(View.VISIBLE);
                    }else if (lists.getStopFlag().equals("0")){
                        if (lists.getNextStId().equals(stationByRouteList.get(Integer.valueOf(lists.getSectOrd())).getStation())){
                            holder.binding.busFirstImage.setVisibility(View.VISIBLE);
                        }else{
                            holder.binding.busThirdImage.setVisibility(View.VISIBLE);
                        }
//                        holder.binding.busRouteFirstName.setText(lists.getNextStId());
                    }
                }
            }
        }
    }

    public void setGbusStationList(BusRouteDetailViewHolder holder, int position){
        if (position == 0){
            holder.binding.busRouteLineFirst.setVisibility(View.GONE);
        }else if (position == gBusLocationList.size()-1){
            holder.binding.busRouteLineThird.setVisibility(View.GONE);
        }
        if (gBusRouteStation.get(position).getTurnYn().equals("y")){
            holder.binding.busRouteLineSecond.setImageResource(R.drawable.ic_baseline_refresh_24);
        }
        holder.binding.busRouteStopName.setText(gBusRouteStation.get(position).getStationName());
        holder.binding.busRouteStopId.setText("첫차 시간 : " + gBusRouteStation.get(position).getStationId());


    }

    public void setGbusLocation(BusRouteDetailViewHolder holder, int position){
        if (gBusLocationList != null){
            for (GBusLocationList gbusLists : gBusLocationList){
                if (gbusLists.getStationId().equals(gBusRouteStation.get(position).getStationId())){
                    // 버스가 도착한 상태 일 경우
                    holder.binding.busSecondImage.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    public void setBackgroundColor(BusRouteDetailViewHolder holder , int position){
        if (stId != null && stationByRouteList.get(position).getStation().equals(this.stId)){
            Log.d("BusRouteDetailVH", "setBackgroundColor if state called");
            holder.binding.busRouteStopsList.setBackgroundColor(R.color.yellow);
        }
    }

    @SuppressLint("ResourceAsColor")
    public void setGBusBackgroundColor(BusRouteDetailViewHolder holder , int position){
        if (stId != null && gBusRouteStation.get(position).getStationId().equals(this.stId)){
            holder.binding.busRouteStopsList.setBackgroundColor(R.color.yellow);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v , int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
    }




    class BusRouteDetailViewHolder extends RecyclerView.ViewHolder {
        BusRouteDetailItemBinding binding;
        public BusRouteDetailViewHolder(BusRouteDetailItemBinding binding) {
            super(binding.getRoot());
//            Log.d("kkang" , "::::::::: BusRouteDetailViewHolder ");
            this.binding = binding;

            binding.busRouteItemWrap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && mListener != null){
                        mListener.onItemClick(view, pos);
                    }
                }
            });
        }
    }
}
