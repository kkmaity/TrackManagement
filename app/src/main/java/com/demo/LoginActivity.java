package com.demo;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {
    private TextView tvNewAccount;
    private CardView cardViewLogin;
    private EditText    et_userid;
    private EditText    et_password;
    private TextView tv_login;

;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
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
        String user = et_userid.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if(user.equals("admin") && password.equals("password")){
            startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtra("user", "admin"));
            finish();
        }else if(user.equals("user1") && password.equals("password")){
            startActivity(new Intent(LoginActivity.this,MainActivity.class).putExtra("user", "user1"));
            finish();
        }else{
            Toast.makeText(getApplicationContext(), "Please enter valid username and password", Toast.LENGTH_LONG).show();
        }
    }
}
