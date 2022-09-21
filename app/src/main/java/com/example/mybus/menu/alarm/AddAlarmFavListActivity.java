package com.example.mybus.menu.alarm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mybus.databinding.ActivityAddAlarmFavListBinding;
import com.example.mybus.viewmodel.AddAlarmListViewModel;
import com.example.mybus.vo.DataWithFavStopBus;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddAlarmFavListActivity extends AppCompatActivity {
    private ActivityAddAlarmFavListBinding binding;
    private AddAlarmListViewModel addAlarmListViewModel;
    private List<DataWithFavStopBus> dataWithFavStopBusList;
    private RecyclerView recyclerView;
    private AddAlarmFavListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddAlarmFavListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initRecycler();
        addAlarmListViewModel = new ViewModelProvider(this).get(AddAlarmListViewModel.class);
        getFavLists();

    }

    public void initRecycler(){
        recyclerView = binding.addAlarmRecycler;
        adapter = new AddAlarmFavListAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    public void getFavLists(){
        addAlarmListViewModel.getFavStopBus();
        addAlarmListViewModel.localFavStopBusLists.observe(this, new Observer<List<DataWithFavStopBus>>() {
            @Override
            public void onChanged(List<DataWithFavStopBus> dataWithFavStopBuses) {
                if (dataWithFavStopBuses != null){
                    dataWithFavStopBusList = dataWithFavStopBuses;
                    adapter.updateDataWithFavStopBusList(dataWithFavStopBusList);
                }
            }
        });
    }
}