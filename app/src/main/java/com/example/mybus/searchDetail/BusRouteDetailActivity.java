package com.example.mybus.searchDetail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mybus.ActivityAnimate;
import com.example.mybus.MainActivity;
import com.example.mybus.R;
import com.example.mybus.apisearch.itemList.BusPosList;
import com.example.mybus.apisearch.itemList.BusSchList;
import com.example.mybus.apisearch.itemList.GBusLocationList;
import com.example.mybus.apisearch.itemList.GBusRouteList;
import com.example.mybus.apisearch.itemList.GBusRouteStationList;
import com.example.mybus.apisearch.itemList.RouteInfoList;
import com.example.mybus.apisearch.itemList.StationByRouteList;
import com.example.mybus.apisearch.itemList.StopSchList;
import com.example.mybus.databinding.ActivityBusRouteDetailBinding;
import com.example.mybus.menu.LoginActivity;
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
public class BusRouteDetailActivity extends AppCompatActivity implements ActivityAnimate {
    private ActivityBusRouteDetailBinding binding;
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Menu menu;
    private BusSchList busSchList;
    private RecyclerView recyclerView;
    private BusRouteDetailAdapter adapter;
    private BusRouteSearchDetailViewModel busRouteSearchDetailViewModel;
    private ImageView favImage;
    private ImageView dialogImage;
    private boolean isFavSaved = false;
    private List<StationByRouteList> stationByRouteList;
    private List<GBusRouteStationList> gBusRouteStationList;
    private Dialog dialog;
    private SharedPreferences sharedPreferences;
    private String loginId;
    private long mLastClickTime = 0L;
    private TextView emptyView;
    private String routeType = "-1";       //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBusRouteDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        busRouteSearchDetailViewModel = new ViewModelProvider(this).get(BusRouteSearchDetailViewModel.class);

        initView();
        initRecycler();
        getLoginId();
        setFabListener();
        getDataFromIntent();
        getIsFaved();
        setText();

    }

    public void setText(){
        binding.busName.setText(busSchList.getBusRouteNm());
        binding.busId.setText(busSchList.getBusRouteId());
    }

    public void initView(){
        favImage = binding.stopDetailAddFav;
        emptyView = binding.routeDetailNoResult;
        dialogImage = binding.showRouteInfoIcon;
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
//        collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
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
                    collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
//                    collapsingToolbarLayout.setBackgroundColor(getResources().getColor(R.color.yellow));
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
                if (getIntent().getAction() != null){
                    Intent intent = new Intent(BusRouteDetailActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                finishAfterTransition();
            }
        });

        favImage.setOnClickListener(view -> {
            if (stationByRouteList == null && gBusRouteStationList == null ){
                Toast.makeText(this, "?????? ?????? ??? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isFavSaved){
                busRouteSearchDetailViewModel.deleteLocalFav(busSchList.getBusRouteId());
                isFavSaved = false;
                favImage.setImageResource(R.drawable.ic_baseline_star_border_24);
                busRouteSearchDetailViewModel.deleteFbFab(busSchList.getBusRouteId(), loginId);
            }else{
                Long now = System.currentTimeMillis();
                Date date = new Date(now);
                LocalFav localFav = new LocalFav(busSchList.getBusRouteId(),"0" , "0" , busSchList.getBusRouteNm(), busSchList.getCorpNm(), 0, date, routeType);
                // routeType ?????????
                // ??? ???????????? ???????????? ?????? ??????
                busRouteSearchDetailViewModel.regitFav(localFav);
                isFavSaved = true;
                favImage.setImageResource(R.drawable.ic_baseline_star_24);
                busRouteSearchDetailViewModel.insertFbFav(localFav, loginId);
            }
        });

        dialogImage.setOnClickListener(view -> {
            initDialog();
        });
    }


    public void initRecycler(){
        recyclerView = binding.stopDetailRecycler;
        adapter = new BusRouteDetailAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        setRecyclerListener();
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
                if (stationByRouteList == null && gBusRouteStationList == null ){
                    Toast.makeText(this, "?????? ?????? ??? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                    break;
                }
                // ??? ???????????? ???????????? ?????? ??????
                if ( isFavSaved){
                    isFavSaved = !isFavSaved;
                    busRouteSearchDetailViewModel.deleteLocalFav(busSchList.getBusRouteId());
                    item.setIcon(R.drawable.ic_baseline_star_border_24);
                    favImage.setImageResource(R.drawable.ic_baseline_star_border_24);
                    busRouteSearchDetailViewModel.deleteFbFab(busSchList.getBusRouteId(), loginId);
                }else if (!isFavSaved){
                    isFavSaved = !isFavSaved;
                    Long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    LocalFav localFav = new LocalFav(busSchList.getBusRouteId(),"0", "0" , busSchList.getBusRouteNm(), busSchList.getCorpNm(), 0, date, routeType);
                    busRouteSearchDetailViewModel.regitFav(localFav);
                    item.setIcon(R.drawable.ic_baseline_star_24);
                    favImage.setImageResource(R.drawable.ic_baseline_star_24);
                    insertFbFav(localFav, loginId);
                }
                break;

            case R.id.action_home:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                exitAnimate();
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


    public void getDataFromIntent(){
        Bundle bundle = getIntent().getExtras();
        busSchList = bundle.getParcelable("busList");
        if (busSchList.getRouteType() != null){
            routeType = busSchList.getRouteType();
        }
        if (busSchList != null){
            if (busSchList.getBusRouteId().startsWith("1")){
                // ????????? ?????? ??? ??????
                setRouteStation();
            }else if(busSchList.getBusRouteId().startsWith("2")){
                // ????????? ????????? ??????
                setGbusRouteStation();
            }
        }
    }

    public void setRouteStation(){
        busRouteSearchDetailViewModel.getStationRouteList(busSchList.getBusRouteId());
        busRouteSearchDetailViewModel.stationRouteList.observe(this, new Observer<List<StationByRouteList>>() {
            @Override
            public void onChanged(List<StationByRouteList> stationByRouteLists) {
                if (stationByRouteLists != null){
                    stationByRouteList = stationByRouteLists;
                    adapter.updateRouteStationInfo(stationByRouteLists, busSchList.getStId());
                }else{
                    showEmptyView();
                }

            }
        });

        busRouteSearchDetailViewModel.getBusPositionList(busSchList.getBusRouteId());
        busRouteSearchDetailViewModel.busPosList.observe(this, new Observer<List<BusPosList>>() {
            @Override
            public void onChanged(List<BusPosList> busPosLists) {
                if (busPosLists != null){
                    adapter.updateRoutePosInfo(busPosLists);
                    routeType = busPosLists.get(0).getBusType();
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
//                    Log.d("BusRouteDetailActivity", "BusRouteDetail setGbusRouteStation gBusRouteStationLists : " +gBusRouteStationLists.size() );
                    gBusRouteStationList = gBusRouteStationLists;
                    adapter.updateGbusStationInfo(gBusRouteStationLists, busSchList.getStId());
                    if (busSchList.getCorpNm().equals("-1")){
                        busSchList.setCorpNm(gBusRouteStationList.get(0).getRegionName());
                    }
                }else{
                    showEmptyView();
                }
            }
        });

        busRouteSearchDetailViewModel.getGbusLocationList(busSchList.getBusRouteId());
        busRouteSearchDetailViewModel.gBusLocationList.observe(this, new Observer<List<GBusLocationList>>() {
            @Override
            public void onChanged(List<GBusLocationList> gBusLocationLists) {
//                Log.d("BusRouteDetailActivity", "BusRouteDetail setGbusRouteStation gBusLocationLists : " +gBusLocationLists.size() );
                if (gBusLocationLists != null){
                    adapter.updateGbusLocationList(gBusLocationLists);
                }
            }
        });
    }

    public void getIsFaved(){
        busRouteSearchDetailViewModel.getLocalFavIsSaved(busSchList.getBusRouteId());
        busRouteSearchDetailViewModel.isFavSaved.observe(this, new Observer<Integer>() {
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

    public void setRecyclerListener() {
        adapter.setOnItemClickListener(new BusRouteDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                StopSchList stopSchList = new StopSchList();
                stopSchList.setBusId(busSchList.getBusRouteId());
                Intent intent = new Intent(v.getContext(), StopDetailActivity.class);
                Bundle args = new Bundle();
                if (stationByRouteList != null && !(stationByRouteList.get(position).getArsId().equals(0))){
                    stopSchList.setNextDir(stationByRouteList.get(position).getDirection());
                    stopSchList.setArsId(stationByRouteList.get(position).getArsId());
                    stopSchList.setStId(stationByRouteList.get(position).getStation());
                    stopSchList.setStNm(stationByRouteList.get(position).getStationNm());
                    args.putParcelable("stopList", stopSchList);
                    intent.putExtras(args);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(BusRouteDetailActivity.this).toBundle());
//                    moveAnimate();
                }else if(gBusRouteStationList != null){
                    stopSchList.setStId(gBusRouteStationList.get(position).getStationId());
                    stopSchList.setStNm(gBusRouteStationList.get(position).getStationName());
                    if (position < gBusRouteStationList.size()-1)     stopSchList.setNextDir(gBusRouteStationList.get(position+1).getStationName());
                    else                                            stopSchList.setNextDir("??????");
                    args.putParcelable("stopList", stopSchList);
                    intent.putExtras(args);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(BusRouteDetailActivity.this).toBundle());
//                    moveAnimate();
                }
            }
        });
    }

    public void setFabListener(){
        floatingActionButton.setOnClickListener(view -> {
            if (SystemClock.elapsedRealtime() - mLastClickTime > 5000) {
                if (busSchList.getBusRouteId().startsWith("1")) {
                    busRouteSearchDetailViewModel.getBusPositionList(busSchList.getBusRouteId());
                } else if (busSchList.getBusRouteId().startsWith("2")) {
                    busRouteSearchDetailViewModel.getGbusLocationList(busSchList.getBusRouteId());
                }
            }else{
                Log.d("BusRouteDetailActivity", "not time yet");
            }
            mLastClickTime = SystemClock.elapsedRealtime();
        });
    }

    public void initDialog(){
        Log.d("BusRouteDetailActivity", "initDialog!");
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.route_detail_dialog);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(params);
        TextView wdays = dialog.findViewById(R.id.route_detail_term_contents);
        TextView term = dialog.findViewById(R.id.route_detail_term_contents);
        TextView firstLastSt = dialog.findViewById(R.id.route_detail_first_last_station_contents);
        // ????????? ?????? ????????? ?????? ???????????? ????????????.
        if (busSchList.getBusRouteId().startsWith("1")){
            busRouteSearchDetailViewModel.getRouteInfo(busSchList.getBusRouteId());
            busRouteSearchDetailViewModel.routeInfoList.observe(this, new Observer<List<RouteInfoList>>() {
                @Override
                public void onChanged(List<RouteInfoList> routeInfoLists) {
                    if (routeInfoLists != null){
                        try{
                            wdays.setText(routeInfoLists.get(0).getFirstBusTm().substring(8, 10) + ":" + routeInfoLists.get(0).getFirstBusTm().substring(10, 12) +
                                    " / " + routeInfoLists.get(0).getLastBusTm().substring(8, 10) + ":" + routeInfoLists.get(0).getLastBusTm().substring(10, 12) );
                            term.setText(routeInfoLists.get(0).getTerm() + "???");
                            firstLastSt.setText(routeInfoLists.get(0).getStStationNm() +"\n" +"<-> \n" + routeInfoLists.get(0).getEdStationNm());
                        }catch(Exception e){
                            wdays.setText("????????? ????????????");
                            term.setText("????????? ????????????");
                        }
                        dialog.show();
                    }
                }
            });
        }else if (busSchList.getBusRouteId().startsWith("2")){
            busRouteSearchDetailViewModel.getGbusRouteInfo(busSchList.getBusRouteId());
            busRouteSearchDetailViewModel.gBusRouteInfoList.observe(this, new Observer<List<GBusRouteList>>() {
                @Override
                public void onChanged(List<GBusRouteList> gBusRouteLists) {
                    if (gBusRouteLists != null){
                        try{
                            wdays.setText(gBusRouteLists.get(0).getUpFirstTime() + " / " + gBusRouteLists.get(0).getDownLastTime());
                            String termText = gBusRouteLists.get(0).getPeekAlloc() == null ? " " : gBusRouteLists.get(0).getPeekAlloc() + " ??? ";
                            termText += gBusRouteLists.get(0).getNpeekAlloc() == null ? " " : gBusRouteLists.get(0).getNpeekAlloc();
//                            term.setText(gBusRouteLists.get(0).getPeekAlloc() + "??? / " + gBusRouteLists.get(0).getNpeekAlloc()+"???");
                            firstLastSt.setText(gBusRouteLists.get(0).getStartStationName() +"\n" +"<-> \n" + gBusRouteLists.get(0).getEndStationName());
                            term.setText(termText);
                        }catch(Exception e){
                            wdays.setText("????????? ????????????");      term.setText("????????? ????????????");
                        }
                        dialog.show();
                        
                    }
                }
            });
        }
    }

    public void getLoginId(){
        sharedPreferences = getSharedPreferences(LoginActivity.sharedId, MODE_PRIVATE);
        loginId = sharedPreferences.getString("loginId", null);
    }

    public void insertFbFav(LocalFav  localFav,String loginId){
        busRouteSearchDetailViewModel.insertFbFav(localFav, loginId);
    }

    public void showEmptyView(){
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getAction() != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else{
            this.finishAfterTransition();
        }
    }

    @Override
    public void moveAnimate() {
        overridePendingTransition(R.anim.vertical_center, R.anim.none);
    }

    @Override
    public void exitAnimate() {
        overridePendingTransition(R.anim.none, R.anim.vertical_exit);
    }
}