package com.demo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.demo.adapter.OutDoorJobGridAdapter;
import com.demo.model.OutDoorHistory;
import com.demo.network.KlHttpClient;
import com.demo.services.LocationUpdateService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by root on 20/8/15.
 */
public class OutDoorWorkEntryDetailsFragment extends BaseFragment implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private EditText et_challan_number;
    private EditText et_box_number;
    private EditText et_description;
    private TextView tv_start_work;
    private TextView tv_end_work;
    private TextView tv_start_date_time;
    private TextView tv_end_date_time;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private String lat,lng;
    private static final long INTERVAL = 1000 * 3;
    private static final long FASTEST_INTERVAL = 0;
    private boolean isAlreadyStarted=false;
    private boolean isButtonClicked = false;
    private int val = 0;
    private ListView gridAttendanceHis;
    private OutDoorHistoryGridAdapter outDoorHistoryGridAdapter;
    private ArrayList<OutDoorHistory> outDoorHistories = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_outdoor_work_entry_details, null, false);
        et_challan_number = (EditText)v.findViewById(R.id.et_challan_number);
        et_box_number = (EditText)v.findViewById(R.id.et_box_number);
        et_description = (EditText)v.findViewById(R.id.et_description);
        tv_start_work = (TextView)v.findViewById(R.id.tv_start_work);
        tv_end_work = (TextView)v.findViewById(R.id.tv_end_work);
        tv_start_date_time = (TextView)v.findViewById(R.id.tv_start_date_time);
        tv_end_date_time = (TextView)v.findViewById(R.id.tv_end_date_time);
        gridAttendanceHis = (ListView)v.findViewById(R.id.gridAttendanceHis);
        outDoorHistoryGridAdapter = new OutDoorHistoryGridAdapter(baseActivity,outDoorHistories);
        gridAttendanceHis.setAdapter(outDoorHistoryGridAdapter);
        tv_start_work.setOnClickListener(this);
        tv_end_work.setOnClickListener(this);
        getHistory();

        return v;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_start_work:
                if(isValid()){
                    LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

                    if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                    }else{
                        baseActivity.showProgressDialog();
                        isButtonClicked = true;
                        val = 1;
                        createLocationRequest();
                        mGoogleApiClient = new GoogleApiClient.Builder(baseActivity)
                                .addApi(LocationServices.API)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .build();
                        mGoogleApiClient.connect();
                    }
                }
                break;
                case R.id.tv_end_work:

                    val = 2;
                    isButtonClicked = true;

                    getActivity().stopService(new Intent(getActivity(),LocationUpdateService.class));

                    LocationManager manager1 = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

                    if ( !manager1.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                        buildAlertMessageNoGps();
                    }else{
                        baseActivity.showProgressDialog();
                        createLocationRequest();
                        mGoogleApiClient = new GoogleApiClient.Builder(baseActivity)
                                .addApi(LocationServices.API)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .build();
                        mGoogleApiClient.connect();


                    }
                break;
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        ((MainActivity)getActivity()).setTitle(getArguments().getString("category_title"));
    }


    public boolean isValid(){
        boolean flag = true;

        if(et_challan_number.getText().toString().trim().length() == 0 ){
            et_challan_number.setError("Please fill challan number");
            flag = false;
        }else if(et_box_number.getText().toString().trim().length() == 0){
            et_box_number.setError("Please fill box number");
            flag = false;
        }else if(et_description.getText().toString().trim().length() == 0){
            et_description.setError("Please fill description");
            flag = false;
        }
        return flag;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (mGoogleApiClient.isConnected()) {
            PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        attendenceStart();
    }

    private void attendenceStart() {

        if (null != mCurrentLocation) {
            if(isButtonClicked){
                lat = String.valueOf(mCurrentLocation.getLatitude());
                lng = String.valueOf(mCurrentLocation.getLongitude());
                mGoogleApiClient.disconnect();
                if(val == 1){
                    getAttendenceStart();
                }else if(val == 2){
                    getAttendenceStop();
                }

            }


            isButtonClicked = false;


        }
    }

    private void getAttendenceStart() {
        if(getView()!=null){
            if(baseActivity.isNetworkConnected()){
                new StartAsynctask().execute();

            }
        }

    }
    private void getAttendenceStop() {

        if(getView()!=null){
            if(baseActivity.isNetworkConnected()){
                new StopAsynctask().execute();

            }
        }

    }

    private void getHistory() {

       // if(getView()!=null){
            if(baseActivity.isNetworkConnected()){
                new HistoryAsynctask().execute();

            }
       // }

    }


    public class StartAsynctask extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                jsonObject.put("userid", baseActivity.preference.getUserId());
                jsonObject.put("job_category", getArguments().getString("category_id"));
                jsonObject.put("challan_no", et_challan_number.getText().toString().trim());
                jsonObject.put("box_no", et_box_number.getText().toString().trim());
                jsonObject.put("description", et_description.getText().toString().trim());
                jsonObject.put("startLat",lat);
                jsonObject.put("startLong", lng);
                baseActivity.preference.setReq(jsonObject.toString());
                Log.e("SendTrackNotification", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/outdoorjobStart.php", jsonObject);
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
                       String already_started= json.getString("already_started");
                        if (already_started.equalsIgnoreCase("1")){
                            Toast.makeText(baseActivity, "Work already started", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(baseActivity, "Work  started", Toast.LENGTH_LONG).show();
                        }



                            tv_start_date_time.setText(json.getJSONObject("ResponseData").getString("startTime"));

                             getActivity().startService(new Intent(getActivity(), LocationUpdateService.class));




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
                jsonObject.put("endLat",lat);
                jsonObject.put("endLong", lng);
                Log.e("AttendenceStop ", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/outdoorjobStop.php", jsonObject);
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
                        isAlreadyStarted=false;
                        if(json.has("ResponseData")){
                            tv_end_date_time.setText(json.getJSONObject("ResponseData").getString("stopTime"));
                           // getAttendenceHistory();
                        }else{
                            Toast.makeText(baseActivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    }else{
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
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/outdoor_job_history.php", jsonObject);
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

               /* "jobid": "3",
                        "category": "1",
                        "challan_no": "98765425",
                        "box_no": "abcd2456hty",
                        "description": "Dummy Text Description",
                        "startTime": "2018-01-20 00:46:36",
                        "endTime": "2018-01-20 00:48:09",
                        "job_status": "no"*/

                try {
                    if (json.getInt("ResponseCode") == 200) {
                        if(json.has("ResponseData")){

                            JSONArray jsonArray = json.getJSONArray("ResponseData");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject c = jsonArray.getJSONObject(i);
                                String jobid = c.getString("jobid");
                                String category = c.getString("category");
                                String challan_no = c.getString("challan_no");
                                String box_no = c.getString("box_no");
                                String description = c.getString("description");
                                String startTime = c.getString("startTime");
                                String endTime = c.getString("endTime");
                                String job_status = c.getString("job_status");
                                if(category.equalsIgnoreCase(getArguments().getString("category_id"))){
                                    outDoorHistories.add(new OutDoorHistory(jobid,category,challan_no,box_no,description,startTime,endTime,job_status));
                                }


                            }

                            outDoorHistoryGridAdapter.notifyDataSetChanged();

                        }else{
                            Toast.makeText(baseActivity, json.getString("message"), Toast.LENGTH_SHORT).show();
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        }
    }
}
