package com.cookandroid.myloginsns;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

public class MainActivity extends AppCompatActivity {
    SessionCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("띵동 제주");

        callback=new SessionCallback(); //카카오 로그인버튼에 대한 콜백함수
        Session.getCurrentSession().addCallback(callback);
    }
    class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {

            UserManagement.requestMe(new MeResponseCallback() {
                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        //에러로 인한 로그인 실패
                        // finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    //로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                    //사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.

                    Log.e("UserProfile", userProfile.toString());
                    Log.e("UserProfile", userProfile.getId() + "");
                    Log.e("UserProfile", userProfile.getProfileImagePath() + "");
                    Log.e("UserProfile", String.valueOf(userProfile.getId()) + "");
                    Log.e("UserProfile", userProfile.getNickname() + "");

                    Intent intent=new Intent(MainActivity.this, Menu.class);
                    startActivity(intent);
                    finish();
                }
            });

        }
        // 세션 실패시
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
        }
    }

}
