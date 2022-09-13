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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusArrivalList;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.apisearch.itemList.StopUidSchList;
import com.example.mybus.databinding.ActivityStopDetailBinding;
import com.example.mybus.search.SearchActivity;
import com.example.mybus.viewmodel.SearchDetailViewModel;
import com.example.mybus.vo.LocalFav;
import com.example.mybus.vo.LocalFavStopBus;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
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
    private List<StopUidSchList> stopUidSchList;
    private List<BusArrivalList> gbusBusLocationList = new ArrayList<>();
    private List<LocalFavStopBus> localFavStopBusList = new ArrayList<>();
    private SearchDetailViewModel searchDetailViewModel;
    private String keyword;
    private RecyclerView recyclerView;
    private SearchDetailAdapter searchDetailAdapter;
    private FloatingActionButton floatingActionButton;
    private ImageView favImage;
    private boolean isFavSaved = false;
    private TextView emptyview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStopDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        searchDetailViewModel = new ViewModelProvider(this).get(SearchDetailViewModel.class);

        initRecycler();
        getDataFromIntent();
        getFavStopBusList();
        initView();
        setFabListener();
        getIsFaved();
        setText();
    }

    public void initView(){
        emptyview = binding.stopDetailNoResult;
        favImage = binding.stopDetailAddFav;
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

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StopDetailActivity.this, SearchActivity.class);
                startActivity(intent);
                finish();
            }
        });


        favImage.setOnClickListener(view -> {
            String lfid=null;
            if (stopSchList.getStId().startsWith("1")){
                lfid = stopSchList.getArsId();
            }else if (stopSchList.getStId().startsWith("2")){
                lfid = stopSchList.getStId();
            }

            if (isFavSaved){
                searchDetailViewModel.deleteLocalFav(stopSchList.getArsId());
                isFavSaved = false;
                favImage.setImageResource(R.drawable.ic_baseline_star_border_24);
                searchDetailViewModel.deleteFbFabInStopDetail(lfid, "001234");
            }else{
                Long now = System.currentTimeMillis();
                Date date = new Date(now);
                LocalFav localFav = new LocalFav(lfid ,stopSchList.getStId()
                        , "0"
                        ,stopSchList.getStNm(), stopSchList.getNextDir(), 1, date);
                searchDetailViewModel.regitFav(localFav);
                isFavSaved = true;
                favImage.setImageResource(R.drawable.ic_baseline_star_24);
                searchDetailViewModel.insertFbFav(localFav, "001234");
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_add_fav:
                String lfid=null;
                Log.d("kkang", "stopdetail activity , actionaddfav clicked");
                Long now = System.currentTimeMillis();
                Date date = new Date(now);
                if (stopSchList.getStId().startsWith("1")){
                    lfid = stopSchList.getArsId();
                }else if (stopSchList.getStId().startsWith("2")){
                    lfid = stopSchList.getStId();
                }
                if (lfid!=null && isFavSaved){
                    isFavSaved = !isFavSaved;
                    searchDetailViewModel.deleteLocalFav(lfid);
                    item.setIcon(R.drawable.ic_baseline_star_border_24);
                    favImage.setImageResource(R.drawable.ic_baseline_star_border_24);
                    // 삭제
                    searchDetailViewModel.deleteFbFabInStopDetail(lfid, "001234");

                }else if (lfid != null && !isFavSaved){
                    isFavSaved = !isFavSaved;
                    LocalFav localFav = new LocalFav(lfid
                            , stopSchList.getStId()
                            , stopUidSchList.get(0).getStaOrd()
                            , stopSchList.getStNm(), stopSchList.getNextDir(), 1, date);
                    searchDetailViewModel.regitFav(localFav);
                    item.setIcon(R.drawable.ic_baseline_star_24);
                    favImage.setImageResource(R.drawable.ic_baseline_star_24);
                    // 추가
                    searchDetailViewModel.insertFbFav(localFav, "001234");
                }
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
        if (isFavSaved)     item.setIcon(R.drawable.ic_baseline_star_24);
        else                item.setIcon(R.drawable.ic_baseline_star_border_24);
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
        setRecyclerListener();
    }

    public void getSeoulBusArivalList(String keyword){
        searchDetailViewModel.getSeoulStopUidSchResult(keyword);
        searchDetailViewModel.stopUidSchList.observe(this, new Observer<List<StopUidSchList>>() {
            @Override
            public void onChanged(List<StopUidSchList> stopUidSchLists) {
                if (stopUidSchLists != null){
                    stopUidSchList = stopUidSchLists;
                    Log.d("kkang", "size is : " + stopUidSchList.size());
                    searchDetailAdapter.updateSBusStopList(stopUidSchList, stopSchList.getBusId());
                }else{
                    Log.d("kkang", "StopDetailActivity stopUidSchList is null!");
                    recyclerView.setVisibility(View.GONE);
                    emptyview.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void getFavStopBusList(){
        if (stopSchList.getStId().startsWith("1")){
            // 서울시 인 경우
            searchDetailViewModel.getFavStopBusList(stopSchList.getArsId());
        }else{
            // 경기도인 경우
            searchDetailViewModel.getFavStopBusList(stopSchList.getStId());
        }
       searchDetailViewModel.localFabStopBusList.observe(this, new Observer<List<LocalFavStopBus>>() {
           @Override
           public void onChanged(List<LocalFavStopBus> localFavStopBusList) {
               if (localFavStopBusList != null){
                   Log.d("kkang", "getFavStopBusList!");
                   searchDetailAdapter.updateFavList(localFavStopBusList);
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
                    searchDetailAdapter.updateGBusStopList(gbusBusLocationList, stopSchList.getBusId());
                }else{
                    Log.d("kkang", "StopDetailActivity busArrivalLists is null!");
                    recyclerView.setVisibility(View.GONE);
                    emptyview.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void setFabListener(){
        floatingActionButton.setOnClickListener(view -> {
            if (stopSchList.getStId().startsWith("1")){
                Log.d("kkang", "setfABlISTENER CASE 1");
                keyword = stopSchList.getArsId();
                getSeoulBusArivalList(keyword);
            }else if (stopSchList.getStId().startsWith("2")){
                Log.d("kkang", "setfABlISTENER CASE 2");
                keyword = stopSchList.getStId();
                getGbusArivalList(keyword);
            }
        });
    }

    public void setRecyclerListener(){
        searchDetailAdapter.setOnItemClickListener(new SearchDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 버스 상세보기로 이동
                Intent intent = new Intent(v.getContext(), BusRouteDetailActivity.class);
                Bundle args = new Bundle();
                BusSchList busLists = new BusSchList();
                busLists.setStId(stopSchList.getStId());
                if (stopUidSchList != null){
                    busLists.setBusRouteId(stopUidSchList.get(position).getBusRouteId());
                    busLists.setBusRouteNm(stopUidSchList.get(position).getRtNm());
                    busLists.setCorpNm(stopUidSchList.get(position).getRouteType());
                }else if(gbusBusLocationList != null){
                    busLists.setBusRouteId(gbusBusLocationList.get(position).getRouteId());
                    busLists.setBusRouteNm(gbusBusLocationList.get(position).getRouteNm());
                    busLists.setCorpNm(gbusBusLocationList.get(position).getFlag());
                }
                args.putParcelable("busList", busLists);
                intent.putExtras(args);
                startActivity(intent);
            }

            @Override
            public void onFabBtnClick(View v, int position) {
                Long now = System.currentTimeMillis();
                Date date = new Date(now);
                if (stopUidSchList != null){
                    LocalFav localFav = new LocalFav(stopUidSchList.get(position).getArsId(), stopUidSchList.get(position).getStId()
                            , stopUidSchList.get(position).getStaOrd()
                            , stopSchList.getStNm(), stopSchList.getNextDir(), 1,date);
                    LocalFavStopBus localFavStopBus = new LocalFavStopBus(localFav.getLf_id()
                            , date
                            , stopUidSchList.get(position).getBusRouteId()
                    , stopUidSchList.get(position).getRtNm());

                    searchDetailViewModel.regitFavList(localFav, localFavStopBus);
                    searchDetailViewModel.getFavStopBusList(stopSchList.getArsId());
                    if (stopUidSchList.get(position).getFlag()) {
                        stopUidSchList.get(position).setFlag(false);
                        // 삭제
                        searchDetailViewModel.deleteFbStopFav(localFav.getLf_id(), localFavStopBus.getLfb_busId(), "001234");
                    } else {
                        stopUidSchList.get(position).setFlag(true);
                        // 추가
                        searchDetailViewModel.insertFbStopFav(localFav, localFavStopBus,"001234");
                    }
                    searchDetailAdapter.notifyItemChanged(position);
                    searchDetailAdapter.isClicked = true;
                    searchDetailAdapter.updateLists(stopUidSchList,localFavStopBusList );

                }else if (gbusBusLocationList != null){
                    LocalFav localFav = new LocalFav(gbusBusLocationList.get(position).getStationId()
                            , gbusBusLocationList.get(position).getStationId()
                            , gbusBusLocationList.get(position).getStaOrder()
                            , stopSchList.getStNm(), stopSchList.getNextDir(), 1,date);
                    LocalFavStopBus localFavStopBus = new LocalFavStopBus(localFav.getLf_id()
                            , date
                            , gbusBusLocationList.get(position).getRouteId()
                            , gbusBusLocationList.get(position).getRouteNm());

                    searchDetailViewModel.regitFavList(localFav, localFavStopBus);
                    searchDetailViewModel.getFavStopBusList(stopSchList.getStId());
                    if (gbusBusLocationList.get(position).isChkFlag()) {
                        gbusBusLocationList.get(position).setChkFlag(false);
                        // 삭제
                        searchDetailViewModel.deleteFbStopFav(localFav.getLf_id(), localFavStopBus.getLfb_busId(), "001234");
                    } else {
                        gbusBusLocationList.get(position).setChkFlag(true);
                        // 추가
                        searchDetailViewModel.insertFbStopFav(localFav, localFavStopBus,"001234");
                    }
                    searchDetailAdapter.notifyItemChanged(position);
                    searchDetailAdapter.isClicked = true;
                    searchDetailAdapter.updateGbusLists(gbusBusLocationList,localFavStopBusList );
                }
            }
        });
    }

    public void getIsFaved(){
        if (stopSchList.getStId().startsWith("1")){
            searchDetailViewModel.getLocalFavIsSaved(stopSchList.getArsId());
        }else if (stopSchList.getStId().startsWith("2")){
            searchDetailViewModel.getLocalFavIsSaved(stopSchList.getStId());
        }
        searchDetailViewModel.isFavSaved.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer > 0 ){
                    isFavSaved = true;
                    favImage.setImageResource(R.drawable.ic_baseline_star_24);
                }else{
                    isFavSaved = false;
                    favImage.setImageResource(R.drawable.ic_baseline_star_border_24);
                }

            }
        });
    }

    public void setText(){
        binding.stopName.setText(stopSchList.getStNm());
        binding.stopArsId.setText(stopSchList.getStId());
        binding.stopDirection.setText(stopSchList.getNextDir());
    }
}