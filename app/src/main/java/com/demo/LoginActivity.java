package com.demo;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.api.ApiLogin;
import com.demo.model.login.ApiLoginParam;
import com.demo.model.login.LoginMain;
import com.demo.restservice.OnApiResponseListener;
import com.demo.utils.Constant;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends BaseActivity {
    private TextView tvNewAccount;
    private CardView cardViewLogin;
    private EditText    et_userid;
    private EditText    et_password;
    private TextView tv_login;
    private String deviceToken;

    ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        deviceToken = FirebaseInstanceId.getInstance().getToken();
        if (deviceToken!=null){
            preference.setDeviceToken(deviceToken);
        }
        tvNewAccount=(TextView)findViewById(R.id.tvNewAccount);
        cardViewLogin=(CardView)findViewById(R.id.cardViewLogin);
        et_userid=(EditText)findViewById(R.id.et_userid);
        et_password=(EditText)findViewById(R.id.et_password);
        tv_login=(TextView)findViewById(R.id.tv_login);
        tvNewAccount.setOnClickListener(this);
        tv_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tvNewAccount:
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                break;
            case R.id.tv_login:
                login();
                break;
        }
    }


    public void login(){
        if(isValid()){
            getLogin();
        }
    }

    public boolean isValid(){
        boolean flag = true;
        if(et_userid.getText().toString().trim().length() ==0 ){
            flag = false;
            et_userid.setError("Please enter user id");
        }else if(et_password.getText().toString().trim().length() ==0 ){
            flag = false;
            et_password.setError("Please enter password");
        }
        return  flag;
    }

    private void getLogin() {
        if(isNetworkConnected()){
            showProgressDialog();
            new ApiLogin(getParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    LoginMain main=(LoginMain)t;
                    if(main.getResponseCode()==200){
                        preference.setUserId(main.getResponseData().getUserid().toString());
                        preference.setName(main.getResponseData().getName().toString());
                        preference.setEmail(main.getResponseData().getEmail().toString());
                        preference.setPhone(main.getResponseData().getPhone().toString());
                        preference.setIsAdmin(main.getResponseData().getIsAdmin().toString());
                        preference.setUserStatus("yes");
                        callNewScreen();
                    }else {
                        Toast.makeText(LoginActivity.this,""+main.getMessage(),Toast.LENGTH_LONG).show();

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





    private ApiLoginParam getParam() {
        ApiLoginParam map=new ApiLoginParam();
        map.setApiKey(Constant.API_KEY);
        map.setUsername(et_userid.getText().toString().trim());
        map.setPassword(et_password.getText().toString().trim());
        map.setDeviceToken(deviceToken);
        return map;
    }
    private void callNewScreen() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}
