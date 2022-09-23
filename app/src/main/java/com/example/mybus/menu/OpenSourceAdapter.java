package com.example.mybus.menu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybus.databinding.OpensourceItemBinding;

import java.util.ArrayList;
import java.util.List;

public class OpenSourceAdapter extends RecyclerView.Adapter<OpenSourceAdapter.OpenSourceViewHolder>{
    List<String> openSourceList;


    @NonNull
    @Override
    public OpenSourceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OpensourceItemBinding binding = OpensourceItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new OpenSourceViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OpenSourceViewHolder holder, int position) {
        setTexts(holder, position);
    }

    @Override
    public int getItemCount() {
        if (openSourceList != null){
            return openSourceList.size();
        }else{
            return 0;
        }
    }

    public void setTexts(OpenSourceViewHolder holder, int position){
        holder.binding.opensourceName.setText(openSourceList.get(position).substring(0, openSourceList.get(position).indexOf(',')));
        holder.binding.opensourceLink.setText(openSourceList.get(position).substring(openSourceList.get(position).indexOf(',')+1).trim());
    }

    public void updateopenSourceLists( List<String> openSourceLists){
        this.openSourceList = openSourceLists;
        notifyDataSetChanged();
    }

    class OpenSourceViewHolder extends RecyclerView.ViewHolder{
        OpensourceItemBinding binding;

        public OpenSourceViewHolder(OpensourceItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
