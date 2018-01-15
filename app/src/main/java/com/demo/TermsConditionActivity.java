package com.demo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.demo.network.KlHttpClient;

import org.json.JSONObject;

public class TermsConditionActivity extends BaseActivity {


    private TextView tv_body;
    private  Toolbar toolbar;

;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privecy_policy);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_body=(TextView)findViewById(R.id.tv_body);
        new TermsConditionAsynctask().execute();

    }



    public class TermsConditionAsynctask extends AsyncTask<String, Void, JSONObject> {

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
                jsonObject.put("pageid", "2");
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
