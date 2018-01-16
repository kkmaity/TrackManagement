package com.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;


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
}
