package com.example.mybus.kakaoLogin;

public class KakaoLogin {
    private boolean isLogin = false;

    public boolean getIsLogin() {
        return isLogin;
    }
/*
    public boolean chkLogin(){
        UserApiClient.getInstance().me((user, error) -> {
            if (error != null){
                return false;
                // 로그인 안 된 상태
            }else{
                return true;
                // 로그인 된 상태
            }

        });
    }

 */
}
