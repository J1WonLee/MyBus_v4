package com.example.mybus.searchDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.ActivityStopDetailBinding;
import com.example.mybus.viewmodel.SearchDetailViewModel;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StopDetailActivity extends AppCompatActivity {
    private ActivityStopDetailBinding binding;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Menu menu;
    private StopSchList stopSchList;
    private List<StopUidSchList> stopUidSchList = new ArrayList<>();
    private List<BusArrivalList> gbusBusLocationList = new ArrayList<>();
    private SearchDetailViewModel searchDetailViewModel;
    private String keyword;
    private RecyclerView recyclerView;
    private SearchDetailAdapter searchDetailAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStopDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        searchDetailViewModel = new ViewModelProvider(this).get(SearchDetailViewModel.class);

        initView();
        initRecycler();
        getDataFromIntent();



        // 뷰모델 생성

//        searchDetailViewModel.getSeoulStopUidSchResult(keyword);
//        searchDetailViewModel.stopUidSchList.observe(this, new Observer<List<StopUidSchList>>() {
//            @Override
//            public void onChanged(List<StopUidSchList> stopUidSchLists) {
//                if (stopUidSchLists != null){
//                    stopUidSchList = stopUidSchLists;
////                    Log.d("kkang", "size is : " + stopUidSchList.size());
//                }else{
//                    Log.d("kkang", "StopDetailActivity stopUidSchList is null!");
//                }
//            }
//        });


//        searchDetailViewModel.getGBusStopUidSchResult(keyword);
//        searchDetailViewModel.gbusUidSchList.observe(this, new Observer<List<BusArrivalList>>() {
//            @Override
//            public void onChanged(List<BusArrivalList> busArrivalLists) {
//                if (busArrivalLists != null){
//                    gbusBusLocationList = busArrivalLists;
//                }else{
//                    Log.d("kkang", "StopDetailActivity busArrivalLists is null!");
//                }
//            }
//        });
    }



    public void initView(){
        toolbar = binding.stopDetailToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        collapsingToolbarLayout = binding.stopDetailCollapsing;
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingToolbar_TitleText);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbar_TitleText);

        collapsingToolbarLayout.setTitle("");

        appBarLayout = binding.stopAppbar;
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("정류장 이름");
                    showOption(R.id.action_add_fav);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    hideOption(R.id.action_add_fav);
                    isShow = false;
                }
            }
        });
    }

    // 정류장 정보를 받아온다.
    public void getDataFromIntent(){
        Bundle bundle = getIntent().getExtras();
        stopSchList = bundle.getParcelable("stopList");
//        Log.d("kkang", stopSchList.getStId() + " , <<id  " + stopSchList.getArsId() +", >> arsId");

        if (stopSchList.getStId().startsWith("1")){
            keyword = stopSchList.getArsId();
            getSeoulBusArivalList(keyword);
        }else if (stopSchList.getStId().startsWith("2")){
            keyword = stopSchList.getStId();
            getGbusArivalList(keyword);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_detail, menu);
        hideOption(R.id.action_add_fav);
        return true;
    }

    public void showOption(int id){
        MenuItem item = menu.findItem(id);
        item.setVisible(true);
    }

    public void hideOption(int id){
        MenuItem item = menu.findItem(id);
        item.setVisible(false);
    }

    public void initRecycler(){
        recyclerView = binding.stopDetailRecycler;
        searchDetailAdapter = new SearchDetailAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searchDetailAdapter);
    }

    public void getSeoulBusArivalList(String keyword){
        searchDetailViewModel.getSeoulStopUidSchResult(keyword);
        searchDetailViewModel.stopUidSchList.observe(this, new Observer<List<StopUidSchList>>() {
            @Override
            public void onChanged(List<StopUidSchList> stopUidSchLists) {
                if (stopUidSchLists != null){
                    stopUidSchList = stopUidSchLists;
//                    Log.d("kkang", "size is : " + stopUidSchList.size());
                    searchDetailAdapter.updateSBusStopList(stopUidSchList);
                }else{
                    Log.d("kkang", "StopDetailActivity stopUidSchList is null!");
                }
            }
        });
    }

    public void getGbusArivalList(String keyword){
        searchDetailViewModel.getGBusStopUidSchResult(keyword);
        searchDetailViewModel.gbusUidSchList.observe(this, new Observer<List<BusArrivalList>>() {
            @Override
            public void onChanged(List<BusArrivalList> busArrivalLists) {
                if (busArrivalLists != null){
                    gbusBusLocationList = busArrivalLists;
                    searchDetailAdapter.updateGBusStopList(gbusBusLocationList);
                }else{
                    Log.d("kkang", "StopDetailActivity busArrivalLists is null!");
                }
            }
        });
    }
}