package com.example.mybus.menu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mybus.MainActivity;
import com.example.mybus.databinding.ActivityLoginBinding;
import com.example.mybus.firebaserepo.FbRepository;
import com.example.mybus.roomrepo.BusRepository;
import com.example.mybus.vo.User;
import com.kakao.sdk.user.UserApiClient;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private boolean isLogin = false;        // 로그인 성공 여부
    @Inject
    public BusRepository repository;

    @Inject
    public FbRepository fbRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.nologin.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        binding.kakaoLogin.setOnClickListener(view -> {
            logIn();
        });

    }

    public void logIn(){
        if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(this)){
            // 카카오톡 설치되어 있을 경우에 카카오톡으로 로그인
            UserApiClient.getInstance().loginWithKakaoTalk(this, ((oAuthToken, error) -> {
                if (error != null){
                    // 로그인 실패
                    Toast.makeText(this, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show();
                }else{
                    // 로그인 성공
                    getInfo();
                }
                return null;
            }));

        }else{
            UserApiClient.getInstance().loginWithKakaoAccount(this, ((oAuthToken, error) -> {
                if (error != null){
                    Toast.makeText(this, "로그인에 실패하였습니다", Toast.LENGTH_SHORT).show();
                }else if (oAuthToken != null){
                    getInfo();
                }
                return null;
            }));

        }
    }

    // db에 회원 정보 저장 후 페이지 이동
    public void getInfo(){
        UserApiClient.getInstance().me((user, error) -> {
            if (error == null){
               //  Log.d("kkang" , "id : " + user.getId() +" name : " + user.getKakaoAccount().getProfile().getNickname() +  " thunbnail : " + user.getKakaoAccount().getProfile().getThumbnailImageUrl());
                repository.register(new User(user.getId().toString(), user.getKakaoAccount().getProfile().getNickname(), user.getKakaoAccount().getProfile().getThumbnailImageUrl()));
                fbRepository.insert(new User(user.getId().toString(), user.getKakaoAccount().getProfile().getNickname(), user.getKakaoAccount().getProfile().getThumbnailImageUrl()));
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
            return null;
        });
    }
}