package com.demo;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.api.ApiLogin;
import com.demo.model.login.ApiLoginParam;
import com.demo.model.login.LoginMain;
import com.demo.network.KlHttpClient;
import com.demo.restservice.OnApiResponseListener;
import com.demo.services.LocationUpdateService;
import com.demo.utils.Constant;

import org.json.JSONObject;

public class PrivacyPolicyActivity extends BaseActivity {


    private TextView tv_body;
    private  Toolbar toolbar;

;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privecy_policy);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_body=(TextView)findViewById(R.id.tv_body);
        new PrivecyPolicyAsynctask().execute();

    }



    public class PrivecyPolicyAsynctask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                jsonObject.put("pageid", "1");
                 Log.e("SendTrackNotification", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/cms.php", jsonObject);
                return json;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            dismissProgressDialog();

            if(json!=null) {

                try {
                    toolbar.setTitle(json.getJSONObject("ResponseData").getString("pageTitle"));
                    tv_body.setText(json.getJSONObject("ResponseData").getString("pageContent"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        }
    }


}
