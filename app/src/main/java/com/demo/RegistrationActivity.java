package com.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.demo.api.ApiAppConfig;
import com.demo.api.ApiRegistration;
import com.demo.model.AppConfigMain;
import com.demo.model.AppConfigParam;
import com.demo.model.registration.ApiRegistrationParam;
import com.demo.model.registration.RegistrationMain;
import com.demo.restservice.OnApiResponseListener;
import com.demo.utils.Constant;

import java.util.regex.Pattern;

public class RegistrationActivity extends BaseActivity {

    private EditText et_name;
    private EditText et_email;
    private EditText et_mobile;
    private EditText et_password;
    private TextView tv_register;
    public static final String EMAIL_PATTERN ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        et_name = (EditText)findViewById(R.id.et_name);
        et_email = (EditText)findViewById(R.id.et_email);
        et_mobile = (EditText)findViewById(R.id.et_mobile);
        et_password = (EditText)findViewById(R.id.et_password);
        tv_register = (TextView)findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_register:
                if(isvalid()){

                }
                break;
        }
    }

    public boolean isvalid(){

        boolean flag = true;
        if(et_name.getText().toString().trim().length() == 0){
            flag = false;
            et_name.setError("Plesae enter name");
        }else if(et_email.getText().toString().trim().length() == 0){
            flag = false;
            et_email.setError("Plesae enter email");
        }else if(et_mobile.getText().toString().trim().length() == 0){
            flag = false;
            et_mobile.setError("Plesae enter mobile");
        }else if(et_password.getText().toString().trim().length() == 0){
            flag = false;
            et_password.setError("Plesae enter password");
        }else if(!isvalidMailid(et_email.getText().toString().trim())){
            flag = false;
            et_password.setError("Plesae enter valid email");
        }else if(et_mobile.getText().toString().trim().length() != 10){
            flag = false;
            et_mobile.setError("Plesae enter 10 digit mobile");
        }else if(et_password.getText().toString().trim().length() < 6){
            flag = false;
            et_mobile.setError("Plesae enter min 6 length password");
        }
        return  flag;

    }


    private void getRegistration() {
        if(isNetworkConnected()){
            showProgressDialog();
            new ApiRegistration(getParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    dismissProgressDialog();
                    RegistrationMain main=(RegistrationMain)t;
                    if(main.getResponseCode()==200){
                       preference.setUserId(main.getResponseData().getUserid().toString());
                       preference.setName(main.getResponseData().getName().toString());
                       preference.setEmail(main.getResponseData().getEmail().toString());
                       preference.setPhone(main.getResponseData().getPhone().toString());
                       preference.setUserStatus("yes");
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



    public  boolean isvalidMailid(String mail) {
        return Pattern.compile(EMAIL_PATTERN).matcher(mail).matches();
    }

    private ApiRegistrationParam getParam() {
        ApiRegistrationParam map=new ApiRegistrationParam();
        map.setApiKey(Constant.API_KEY);
        map.setName(et_name.getText().toString().trim());
        map.setEmail(et_email.getText().toString().trim());
        map.setPassword(et_password.getText().toString().trim());
        map.setPhone(et_mobile.getText().toString().trim());
        map.setDeviceToken(Constant.API_KEY);
        return map;
    }
    private void callNewScreen() {
        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

}
