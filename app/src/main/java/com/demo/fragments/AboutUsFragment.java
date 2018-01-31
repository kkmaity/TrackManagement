package com.demo.fragments;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.demo.MainActivity;
import com.demo.R;
import com.demo.network.KlHttpClient;

import org.json.JSONObject;

public class AboutUsFragment extends BaseFragment {


    private TextView tv_body;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_privecy_policy, null, false);

        tv_body=(TextView)v.findViewById(R.id.tv_body);
        new PrivecyPolicyAsynctask().execute();
        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }




    public class PrivecyPolicyAsynctask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            baseActivity.showProgressDialog();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                jsonObject.put("pageid", "3");
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
            baseActivity.dismissProgressDialog();

            if(json!=null) {

                try {
                    ((MainActivity)getActivity()).setTitle(json.getJSONObject("ResponseData").getString("pageTitle"));
                    tv_body.setText(json.getJSONObject("ResponseData").getString("pageContent"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        }
    }


}
