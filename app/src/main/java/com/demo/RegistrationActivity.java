package com.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class RegistrationActivity extends BaseActivity {

    private EditText et_name;
    private EditText et_email;
    private EditText et_mobile;
    private EditText et_password;
    private TextView tv_register;

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
                break;
        }
    }


}
