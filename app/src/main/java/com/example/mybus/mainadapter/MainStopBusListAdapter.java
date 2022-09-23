package com.example.mybus.mainadapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.GBusStopRouteList;
import com.example.mybus.apisearch.itemList.StopRouteList;
import com.example.mybus.databinding.MainBusItemListBinding;
import com.example.mybus.vo.LocalFavStopBus;

import java.util.List;

public class MainStopBusListAdapter extends RecyclerView.Adapter<MainStopBusListAdapter.MainStopBusListViewHolder> {
    private List<StopRouteList> stopRouteList;
    private List<GBusStopRouteList> gBusStopRouteList;
    private List<LocalFavStopBus> localFavStopBusList;
    private OnItemClickListener mListener;
    public boolean isClicked = false;


    @NonNull
    @Override
    public MainStopBusListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainBusItemListBinding mainBusItemListBinding = MainBusItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MainStopBusListViewHolder(mainBusItemListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MainStopBusListViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        if (stopRouteList != null){
            setContexts(holder, position);
            chkIsFavSaved(holder, position);
        }else if (gBusStopRouteList != null){
            setGBusContexts(holder, position);
            chkGBusIsFavSaved(holder,position);
        }
    }

    @Override
    public int getItemCount() {
        if (stopRouteList != null){
            return stopRouteList.size();
        }else if (gBusStopRouteList != null){
            return gBusStopRouteList.size();
        }else{
            return -1;
        }
    }

    public void updateStopRouteList(List<StopRouteList> stopRouteLists, List<LocalFavStopBus> localFavStopBusLists){
        this.stopRouteList = stopRouteLists;
        this.gBusStopRouteList = null;
        this.localFavStopBusList = localFavStopBusLists;
        notifyDataSetChanged();
    }

    public void updateGBusStopRouteList(List<GBusStopRouteList> gBusStopRouteLists, List<LocalFavStopBus> localFavStopBusLists){
        this.gBusStopRouteList = gBusStopRouteLists;
        this.stopRouteList = null;
        this.localFavStopBusList = localFavStopBusLists;
        notifyDataSetChanged();
    }

    public void setContexts(MainStopBusListViewHolder holder, int position){
        holder.mainBusItemListBinding.busName.setText(stopRouteList.get(position).getBusRouteNm());
    }

    public void setGBusContexts(MainStopBusListViewHolder holder, int position){
        holder.mainBusItemListBinding.busName.setText(gBusStopRouteList.get(position).getRouteName());
    }

    public void chkIsFavSaved(MainStopBusListViewHolder holder, int position){
        // 초기 화면 설정
        if (localFavStopBusList != null && !isClicked){
            for (LocalFavStopBus favLists : localFavStopBusList){
                if (favLists.getLfb_busId().equals(stopRouteList.get(position).getBusRouteId())) {
                    stopRouteList.get(position).setFlag(true);
                    holder.mainBusItemListBinding.busFaved.setImageResource(R.drawable.ic_baseline_star_24);
                }
            }
        // 클릭 시 화면 설정
        }else if (localFavStopBusList != null && isClicked){
            if (stopRouteList.get(position).isFlag()){
                stopRouteList.get(position).setFlag(true);
                holder.mainBusItemListBinding.busFaved.setImageResource(R.drawable.ic_baseline_star_24);
            }else{
                stopRouteList.get(position).setFlag(false);
                Log.d("MainStopBusAdapter", "chkIsFavSaved star border.");
                holder.mainBusItemListBinding.busFaved.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
        }
    }

    public void chkGBusIsFavSaved(MainStopBusListViewHolder holder, int position){
        if (localFavStopBusList != null && !isClicked) {
            for (LocalFavStopBus favLists : localFavStopBusList) {
                if (favLists.getLfb_busId().equals(gBusStopRouteList.get(position).getRouteId())) {
                    gBusStopRouteList.get(position).setFlag(true);
                    holder.mainBusItemListBinding.busFaved.setImageResource(R.drawable.ic_baseline_star_24);
                }
            }
        }else if (localFavStopBusList != null && isClicked){
            if (gBusStopRouteList.get(position).isFlag()){
                gBusStopRouteList.get(position).setFlag(true);
                holder.mainBusItemListBinding.busFaved.setImageResource(R.drawable.ic_baseline_star_24);
            }else{
                gBusStopRouteList.get(position).setFlag(false);
                holder.mainBusItemListBinding.busFaved.setImageResource(R.drawable.ic_baseline_star_border_24);
            }
        }
    }

    public interface OnItemClickListener{
        void onBtnClick(View v , int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
    }

    class MainStopBusListViewHolder extends RecyclerView.ViewHolder {
        MainBusItemListBinding mainBusItemListBinding;

        public MainStopBusListViewHolder(MainBusItemListBinding binding) {
            super(binding.getRoot());
            Log.d("MainStopBusListAdapter", "viewholder called");
            mainBusItemListBinding = binding;
            // 알람으로 이동하는 버튼 리스너 필요
            mainBusItemListBinding.busFaved.setOnClickListener(new View.OnClickListener() {
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
