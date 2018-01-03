package com.demo;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;
public class LoginActivity extends BaseActivity {
    private TextView tvNewAccount;
    private CardView cardViewLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvNewAccount=(TextView)findViewById(R.id.tvNewAccount);
        cardViewLogin=(CardView)findViewById(R.id.cardViewLogin);
        tvNewAccount.setOnClickListener(this);
        cardViewLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()){
            case R.id.tvNewAccount:
                startActivity(new Intent(LoginActivity.this,RegistrationActivity.class));
                break;
            case R.id.cardViewLogin:
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                break;
        }
    }
}
