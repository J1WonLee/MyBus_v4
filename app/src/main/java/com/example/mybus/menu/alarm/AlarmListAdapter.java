package com.example.mybus.menu.alarm;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.R;
import com.example.mybus.databinding.AlarmlistItemBinding;
import com.example.mybus.mainadapter.MainFavAdapter;
import com.example.mybus.vo.SchAlarmInfo;

import java.util.Calendar;
import java.util.List;

public class AlarmListAdapter extends RecyclerView.Adapter<AlarmListAdapter.AlarmListViewHolder>{
    List<SchAlarmInfo> schAlarmInfoList;
    private OnItemClickListener mListener;

    @NonNull
    @Override
    public AlarmListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AlarmlistItemBinding binding = AlarmlistItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new AlarmListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmListViewHolder holder, int position) {
        setTexts(holder, position);
    }

    @Override
    public int getItemCount() {
        if (schAlarmInfoList != null){
            return schAlarmInfoList.size();
        }else{
            return -1;
        }
    }

    public void updateschAlarmInfoList(List<SchAlarmInfo> schAlarmInfoList){
        this.schAlarmInfoList = schAlarmInfoList;
        notifyDataSetChanged();
    }

    public void updatesSchAlarmOnOff(List<SchAlarmInfo> schAlarmInfoList, int position){
        this.schAlarmInfoList = schAlarmInfoList;
        notifyItemChanged(position);
    }

    public void setTexts(AlarmListViewHolder holder, int position){
        holder.binding.stopbusName.setText(schAlarmInfoList.get(position).getAlarm_stop_nm());
        holder.binding.routeName.setText(schAlarmInfoList.get(position).getAlarm_bus_nm());
        holder.binding.dates.setText(setDates(schAlarmInfoList.get(position).getWeeks()));
        holder.binding.busName.setText(setTimes(schAlarmInfoList.get(position).getAlarm_date()));
        if (schAlarmInfoList.get(position).isOn()){
            holder.binding.onOff.setChecked(true);
            holder.binding.dates.setTextColor(Color.rgb(0, 0, 0));
            holder.binding.busName.setTextColor(Color.rgb(0,0,0));
            holder.binding.editButton.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.black));
            holder.binding.delBtn.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.black));
            setTextColor(holder, position);
        }else{
            holder.binding.onOff.setChecked(false);
            holder.binding.stopbusName.setTextColor(Color.rgb(158, 158, 158));
            holder.binding.dates.setTextColor(Color.rgb(158, 158, 158));
            holder.binding.busName.setTextColor(Color.rgb(158,158,158));
            holder.binding.editButton.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.lightgray));
            holder.binding.delBtn.setColorFilter(holder.itemView.getContext().getResources().getColor(R.color.lightgray));
        }
    }

    public void setTextColor(AlarmListViewHolder holder, int position){
        if (schAlarmInfoList.get(position).getAlarm_id().startsWith("1")){
            holder.binding.stopbusName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.lightBlue));
        }else{
            holder.binding.stopbusName.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.lightGreen));
        }
    }

    public interface OnItemClickListener{
        void onScrollClick(CompoundButton v , int position, boolean b);
        void onDelBtnClick(View v, int position);
        void onEditBtnClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mListener = onItemClickListener;
    }


    public String setDates(String dates){
        String returnString="";
        String[] sliceDates = dates.split(",");
        for (int i=0; i< sliceDates.length; i++){
            switch(sliceDates[i]){
                case "2":
                    returnString += "월 ";
                    break;
                case "3":
                    returnString += "화 ";
                    break;
                case "4":
                    returnString += "수 ";
                    break;
                case "5":
                    returnString += "목 ";
                    break;
                case "6":
                    returnString += "금 ";
                    break;
                case "7":
                    returnString += "토 ";
                    break;
                case "1":
                    returnString += "일 ";
                    break;
                default:
                    break;
            }
        }
        return returnString;
    }

    public String setTimes(long times){
        String min="";
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(times);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        if (minute < 10){
            min = "0"+String.valueOf(minute);
        }else{
            min = String.valueOf(minute);
        }

        return String.valueOf(hour) + ":" + min;
    }

    class AlarmListViewHolder extends RecyclerView.ViewHolder{
        AlarmlistItemBinding binding;

        public AlarmListViewHolder(AlarmlistItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && mListener != null){
                        mListener.onScrollClick(compoundButton, pos, b);
                    }
                }
            });

            binding.delBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && mListener != null){
                        mListener.onDelBtnClick(view, pos);
                    }
                }
            });

            binding.editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION && mListener != null){
                        mListener.onEditBtnClick(view, pos);
                    }
                }
            });
        }
    }
}
