package com.example.mybus.searchDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusPosList;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.GBusLocationList;
import com.example.mybus.apisearch.itemList.GBusRouteStationList;
import com.example.mybus.apisearch.itemList.StationByRouteList;
import com.example.mybus.databinding.ActivityBusRouteDetailBinding;
import com.example.mybus.search.SearchActivity;
import com.example.mybus.viewmodel.BusRouteSearchDetailViewModel;
import com.example.mybus.vo.LocalFav;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BusRouteDetailActivity extends AppCompatActivity {
    private ActivityBusRouteDetailBinding binding;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Menu menu;
    private BusSchList busSchList;
    private RecyclerView recyclerView;
    private BusRouteDetailAdapter adapter;
    private GBusRouteDetailAdapter gbusAdapter;
    private BusRouteSearchDetailViewModel busRouteSearchDetailViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusRouteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        busRouteSearchDetailViewModel = new ViewModelProvider(this).get(BusRouteSearchDetailViewModel.class);

        initView();
        initRecycler();
        getDataFromIntent();
        setText();

    }

    public void setText(){
        binding.busName.setText(busSchList.getBusRouteNm());
        binding.busCorp.setText(busSchList.getCorpNm());
        binding.busId.setText(busSchList.getBusRouteId());
    }

    public void initView(){
        toolbar = binding.stopDetailToolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        floatingActionButton = binding.refreshButton;

        collapsingToolbarLayout = binding.stopDetailCollapsing;
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsingToolbar_TitleText);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsingToolbar_TitleText);

        collapsingToolbarLayout.setTitle("");
        collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.green));
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
                    collapsingToolbarLayout.setTitle(busSchList.getBusRouteNm());
                    collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.green));
                    showOption(R.id.action_add_fav);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbarLayout.setTitle(" ");
                    hideOption(R.id.action_add_fav);
                    isShow = false;
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BusRouteDetailActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void initRecycler(){
        recyclerView = binding.stopDetailRecycler;
        adapter = new BusRouteDetailAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search_detail, menu);
        hideOption(R.id.action_add_fav);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_add_fav:
                Log.d("BusRouteDetailActivity", "stopdetail activity , actionaddfav clicked");
                Long now = System.currentTimeMillis();
                Date date = new Date(now);
                LocalFav localFav = new LocalFav(busSchList.getBusRouteId() , busSchList.getBusRouteNm(), busSchList.getCorpNm(), 0, date);
                // 뷰 모델에서 즐겨찾기 작업 추가
                busRouteSearchDetailViewModel.regitFav(localFav);
                break;

            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
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


    public void getDataFromIntent(){
        Bundle bundle = getIntent().getExtras();
        busSchList = bundle.getParcelable("busList");
        if (busSchList != null){
            if (busSchList.getBusRouteId().startsWith("1")){
                // 서울시 버스 인 경우
                setRouteStation();
            }else if(busSchList.getBusRouteId().startsWith("2")){
                // 경기도 버스인 경우
                setGbusRouteStation();
            }
        }else{
            // 정류장 상세보기에서 온 경우 routeId를 받아와야 함
        }
    }

    public void setRouteStation(){
        busRouteSearchDetailViewModel.getStationRouteList(busSchList.getBusRouteId());
        busRouteSearchDetailViewModel.stationRouteList.observe(this, new Observer<List<StationByRouteList>>() {
            @Override
            public void onChanged(List<StationByRouteList> stationByRouteLists) {
                if (stationByRouteLists != null){
                    adapter.updateRouteStationInfo(stationByRouteLists);
                }

            }
        });

        busRouteSearchDetailViewModel.getBusPositionList(busSchList.getBusRouteId());
        busRouteSearchDetailViewModel.busPosList.observe(this, new Observer<List<BusPosList>>() {
            @Override
            public void onChanged(List<BusPosList> busPosLists) {
                if (busPosLists != null){
                    adapter.updateRoutePosInfo(busPosLists);
                }

            }
        });
    }

    public void setGbusRouteStation(){
        busRouteSearchDetailViewModel.getGbusStopList(busSchList.getBusRouteId());
        busRouteSearchDetailViewModel.gBusStationList.observe(this, new Observer<List<GBusRouteStationList>>() {
            @Override
            public void onChanged(List<GBusRouteStationList> gBusRouteStationLists) {
                if (gBusRouteStationLists != null){
                    Log.d("BusRouteDetailActivity", "BusRouteDetail setGbusRouteStation gBusRouteStationLists : " +gBusRouteStationLists.size() );
                    adapter.updateGbusStationInfo(gBusRouteStationLists);
                }
            }
        });

        busRouteSearchDetailViewModel.getGbusLocationList(busSchList.getBusRouteId());
        busRouteSearchDetailViewModel.gBusLocationList.observe(this, new Observer<List<GBusLocationList>>() {
            @Override
            public void onChanged(List<GBusLocationList> gBusLocationLists) {
                Log.d("BusRouteDetailActivity", "BusRouteDetail setGbusRouteStation gBusLocationLists : " +gBusLocationLists.size() );
                if (gBusLocationLists != null){
                    adapter.updateGbusLocationList(gBusLocationLists);
                }
            }
        });
    }
}