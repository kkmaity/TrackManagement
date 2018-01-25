package com.demo.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;

import com.demo.R;
import com.demo.adapter.OutDoorJobGridAdapter;
import com.demo.model.WorkEntry;
import com.demo.network.KlHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by root on 20/8/15.
 */
public class OutdoorJboFragment extends BaseFragment {

    private GridView list;
    private OutDoorJobGridAdapter officeGridAdapter;
    private ArrayList<WorkEntry> workEntries = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_out_door_job, null, false);
        list = (GridView) v.findViewById(R.id.list);
        officeGridAdapter = new OutDoorJobGridAdapter(this,baseActivity,workEntries);
        list.setAdapter(officeGridAdapter);
        getWorkEntrey();
        return v;

    }

    private void getWorkEntrey() {
        if(baseActivity.isNetworkConnected()){
            new WorkEntryAsynctask().execute();

        }
    }

    public void callFragment(Fragment fragment){
        displayView(fragment);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public class WorkEntryAsynctask extends AsyncTask<String, Void, JSONObject> {

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
                Log.e("LeaveApplied ", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/outdoor_job_category.php", jsonObject);
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
                    if (json.getInt("ResponseCode") == 200) {
                        workEntries.clear();
                        JSONArray jsonArray = json.getJSONArray("ResponseData");
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject c = jsonArray.getJSONObject(i);
                            String category_id = c.getString("category_id");
                            String category_title = c.getString("category_title");
                            workEntries.add(new WorkEntry(category_id, category_title));
                        }
                        officeGridAdapter.notifyDataSetChanged();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        }
    }
}


