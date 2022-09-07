package com.example.mybus.searchDetail;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusPosList;
import com.example.mybus.apisearch.itemList.StationByRouteList;
import com.example.mybus.databinding.BusRouteDetailItemBinding;

import java.util.List;

public class BusRouteDetailAdapter extends RecyclerView.Adapter<BusRouteDetailAdapter.BusRouteDetailViewHolder>{
    private SearchDetailAdapter.OnItemClickListener mListener;
    private List<StationByRouteList> stationByRouteList;
    List<BusPosList> busPosList;

    @NonNull
    @Override
    public BusRouteDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       BusRouteDetailItemBinding binding = BusRouteDetailItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
       return new BusRouteDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BusRouteDetailViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        setStationList(holder, position);
        setBusPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        if (stationByRouteList!=null){
            return stationByRouteList.size();
        }else{
            return -1;
        }
    }

    public void updateRouteStationInfo(List<StationByRouteList> stationByRouteLists){
        this.stationByRouteList = stationByRouteLists;
        notifyDataSetChanged();
    }

    public void updateRoutePosInfo(List<BusPosList> busPosLists){
        this.busPosList = busPosLists;
        for (BusPosList lists : busPosList){
            notifyItemChanged(Integer.valueOf(lists.getSectOrd()));
        }
    }

    // 정류장 명, 정류장 번호 와 같이 텍스트를 채운다.
    public void setStationList(BusRouteDetailViewHolder holder, int position){
        if (stationByRouteList != null){
            if (position == 0){
//                holder.binding.busRouteLineFirst.setVisibility(View.GONE);
            }else if (position == stationByRouteList.size()){
                holder.binding.busRouteLineThird.setVisibility(View.GONE);
            }
            if (stationByRouteList.get(position).getTransYn().equals("Y")){
                Log.d("kkang", "busroutedetailAdapter stestationlist getTransYn" + stationByRouteList.get(position).getStationNm());
                holder.binding.busRouteLineSecond.setImageResource(R.drawable.ic_baseline_refresh_24);
            }
            holder.binding.busRouteStopName.setText(stationByRouteList.get(position).getStationNm());
//            holder.binding.busRouteStopId.setText("첫차 시간 : " + stationByRouteList.get(position).getBeginTm());
            holder.binding.busRouteStopId.setText("구간 ID : " + stationByRouteList.get(position).getSection());
        }
    }

    public void setBusPosition(BusRouteDetailViewHolder holder, int position){
        if (busPosList != null){
            for (BusPosList lists : busPosList){
                if (position == Integer.valueOf(lists.getSectOrd())){
                    if (lists.getStopFlag().equals("1")){
                        holder.binding.busSecondImage.setVisibility(View.VISIBLE);
                    }else if (lists.getStopFlag().equals("0")){
                        if (lists.getSectionId().equals(stationByRouteList.get(position).getSection()))  holder.binding.busFirstImage.setVisibility(View.VISIBLE);
                        else    holder.binding.busThirdImage.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View v , int position);
    }

    public void setOnItemClickListener(SearchDetailAdapter.OnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
    }




    class BusRouteDetailViewHolder extends RecyclerView.ViewHolder {
        BusRouteDetailItemBinding binding;
        public BusRouteDetailViewHolder(BusRouteDetailItemBinding binding) {
            super(binding.getRoot());
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
