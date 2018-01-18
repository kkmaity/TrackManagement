package com.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


import com.demo.adapter.NotificationRecyclerAdapter;
import com.demo.api.ApiAllNotification;
import com.demo.model.notification.NotificationMain;
import com.demo.model.notification.NotificationParam;
import com.demo.network.KlHttpClient;
import com.demo.restservice.OnApiResponseListener;
import com.demo.utils.Constant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NotificationActivity extends BaseActivity {
    private RecyclerView recyclerNotification;
    private Toolbar toolbarHome;
    private ImageView ivBackBtn;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        toolbarHome=(Toolbar)findViewById(R.id.toolbar);
        ivBackBtn=(ImageView)toolbarHome.findViewById(R.id.ivBack);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading...");


        recyclerNotification=(RecyclerView)findViewById(R.id.recyclerNotification);
        LinearLayoutManager horizontalLayoutManagaer4 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerNotification.setLayoutManager(horizontalLayoutManagaer4);

        ivBackBtn.setOnClickListener(this);
        getAllNotification();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.ivBack:
                startActivity(new Intent(NotificationActivity.this,MainActivity.class));
                finish();
                break;
        }
    }
   void getAllNotification(){
if (isNetworkConnected()){
    showProgressDialog();
    new ApiAllNotification(getParam(), new OnApiResponseListener() {
        @Override
        public <E> void onSuccess(E t) {
            dismissProgressDialog();
            NotificationMain main=(NotificationMain)t;
            if (main.getResponseCode()==200){
                NotificationRecyclerAdapter adapter=new NotificationRecyclerAdapter(NotificationActivity.this,main.getResponseData());
                recyclerNotification.setAdapter(adapter);
            }else
                Toast.makeText(NotificationActivity.this,main.getMessage(),Toast.LENGTH_LONG).show();


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

    private NotificationParam getParam() {
        NotificationParam param=new NotificationParam();
        param.setApiKey(Constant.API_KEY);
        return param;
    }
}
