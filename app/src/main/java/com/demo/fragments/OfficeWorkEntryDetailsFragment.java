package com.demo.fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.demo.MainActivity;
import com.demo.R;
import com.demo.adapter.OutDoorHistoryGridAdapter;
import com.demo.model.OutDoorHistory;
import com.demo.network.KlHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OfficeWorkEntryDetailsFragment extends BaseFragment{

    private OutDoorHistoryGridAdapter outDoorHistoryGridAdapter;
    private ArrayList<OutDoorHistory> outDoorHistories = new ArrayList<>();
    private ListView gridAttendanceHis;
    private View tv_start_work;
    private View tv_end_work;
    private EditText etChallanNo;
    private EditText etBoxNo;
    private EditText etDesc;
    private TextView tv_start_date_time;
    private TextView tv_end_date_time;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_office_workentry_details, null, false);
        gridAttendanceHis = (ListView) v.findViewById(R.id.gridAttendanceHis);
        etChallanNo = (EditText) v.findViewById(R.id.etChallanNo);
        etBoxNo = (EditText) v.findViewById(R.id.etBoxNo);
        etDesc = (EditText) v.findViewById(R.id.etDesc);
        tv_start_work = (TextView)v.findViewById(R.id.tv_start_work);
        tv_end_work = (TextView)v.findViewById(R.id.tv_end_work);
        tv_start_date_time = (TextView)v.findViewById(R.id.tv_start_date_time);
        tv_end_date_time = (TextView)v.findViewById(R.id.tv_end_date_time);
        outDoorHistoryGridAdapter = new OutDoorHistoryGridAdapter(baseActivity, outDoorHistories);
        gridAttendanceHis.setAdapter(outDoorHistoryGridAdapter);
        tv_start_work.setOnClickListener(this);
        tv_end_work.setOnClickListener(this);
        ((MainActivity) getActivity()).setTitle(getArguments().getString("category_title"));
        getHistory();

        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
    private void getHistory() {

        // if(getView()!=null){
        if (baseActivity.isNetworkConnected()) {
            new HistoryAsynctask().execute();

        }
        // }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start_work:
                if (!baseActivity.isNetworkConnected())
                    return;
                if (isValid()) {
                    getAttendenceStart();
                }
                break;
            case R.id.tv_end_work:
                if (!baseActivity.isNetworkConnected())
                    return;
                getAttendenceStop();

                break;



        }
    }
    private void getAttendenceStop() {

        if (getView() != null) {
            if (baseActivity.isNetworkConnected()) {
                new StopAsynctask().execute();

            }
        }

    }
    private void getAttendenceStart() {

        if (getView() != null) {
            if (baseActivity.isNetworkConnected()) {
                new StartAsynctask().execute();

            }
        }

    }

    public class StartAsynctask extends AsyncTask<String, Void, JSONObject> {
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
                jsonObject.put("userid", baseActivity.preference.getUserId());
                jsonObject.put("job_category", getArguments().getString("category_id"));
                jsonObject.put("challan_no", etChallanNo.getText().toString().trim());
                 jsonObject.put("box_no", etBoxNo.getText().toString().trim());
                jsonObject.put("description", etDesc.getText().toString().trim());

                baseActivity.preference.setReq(jsonObject.toString());
                Log.e("SendTrackNotification", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/officejobStart.php", jsonObject);
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

            if (json != null) {

                try {
                    if (json.getInt("ResponseCode") == 200) {
                        String already_started = json.getString("already_started");
                        if (already_started.equalsIgnoreCase("1")) {
                            Toast.makeText(baseActivity, json.getString("message"), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(baseActivity, json.getString("message"), Toast.LENGTH_LONG).show();
                        }


                        tv_start_date_time.setText(json.getJSONObject("ResponseData").getString("startTime"));
                        baseActivity.preference.setJobID(json.getJSONObject("ResponseData").getString("jobid"));


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class StopAsynctask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                jsonObject.put("userid", baseActivity.preference.getUserId());
                Log.e("AttendenceStop ", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/officejobStop.php", jsonObject);
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

            if (json != null) {

                try {
                    if (json.getInt("ResponseCode") == 200) {
                        if (json.has("ResponseData")) {
                            tv_end_date_time.setText(json.getJSONObject("ResponseData").getString("stopTime"));
                            // getAttendenceHistory();
                        } else {
                            Toast.makeText(baseActivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    } else {
                        Toast.makeText(baseActivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }


    public class HistoryAsynctask extends AsyncTask<String, Void, JSONObject> {

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
                jsonObject.put("userid", baseActivity.preference.getUserId());

                Log.e("LeaveCount ", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/office_job_history.php", jsonObject);
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

            if (json != null) {



                try {
                    if (json.getInt("ResponseCode") == 200) {
                        if (json.has("ResponseData")) {

                            JSONArray jsonArray = json.getJSONArray("ResponseData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject c = jsonArray.getJSONObject(i);
                                String jobid = c.getString("jobid");
                                String category = c.getString("category");
                                String challan_no = c.getString("challan_no");
                                //  String box_no = c.getString("box_no");
                                String description = c.has("description")?c.getString("description"):"";
                                String startTime = c.getString("startTime");
                                String endTime = c.getString("endTime");
                                String job_status = c.getString("job_status");
                                if (category.equalsIgnoreCase(getArguments().getString("category_id"))) {
                                    outDoorHistories.add(new OutDoorHistory(jobid, category, challan_no, "", description, startTime, endTime, job_status));
                                }


                            }

                            outDoorHistoryGridAdapter.notifyDataSetChanged();

                        } else {
                            Toast.makeText(baseActivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }
    public boolean isValid() {
        boolean flag = true;

        if (etChallanNo.getText().toString().trim().length() == 0) {
            etChallanNo.setError("Please fill challan number");
            flag = false;
        }else if(etBoxNo.getText().toString().trim().length() == 0){
            etBoxNo.setError("Please fill box number");
            flag = false;
        }else if (etDesc.getText().toString().trim().length() == 0) {
            etDesc.setError("Please fill description");
            flag = false;
        }
        return flag;
    }
}
