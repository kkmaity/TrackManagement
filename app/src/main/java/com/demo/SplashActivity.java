package com.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.demo.api.ApiAppConfig;
import com.demo.model.AppConfigMain;
import com.demo.model.AppConfigParam;
import com.demo.restservice.OnApiResponseListener;
import com.demo.utils.Constant;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 3/1/18.
 */

public class SplashActivity extends BaseActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        gatAppName();



    }

    private void gatAppName() {
        if(isNetworkConnected()){
            showProgressDialog();
            new ApiAppConfig(getParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    AppConfigMain main=(AppConfigMain)t;
                    if(main.getResponseCode()==200){
                        TextView tv=(TextView) findViewById(R.id.tvAppName);
                        tv.setText(main.getResponseData().getAppName());
                        callNewScreen();

                    }


                }

                @Override
                public <E> void onError(E t) {
                    dismissProgressDialog();
                }

                @Override
                public void onError() {
                    dismissProgressDialog();
                }
            });
        }


    }

    private void callNewScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (preference.getUserId()!=null&&preference.getUserId().length()>0){
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 3000);
    }

    private AppConfigParam getParam() {
        AppConfigParam map=new AppConfigParam();
        map.setApiKey(Constant.API_KEY);
        return map;
    }
}
