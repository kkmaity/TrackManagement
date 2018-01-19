package com.demo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.Enum.AppMenu;
import com.demo.MainActivity;
import com.demo.MapsActivity;
import com.demo.R;
import com.demo.adapter.AttendanceGridAdapter;
import com.demo.adapter.EmpRecyclerViewAdapter;
import com.demo.api.ApiAttendanceStatus;
import com.demo.api.ApiAttendenceHistory;
import com.demo.api.ApiAttendenceStart;
import com.demo.api.ApiAttendenceStop;
import com.demo.api.ApiEmpList;
import com.demo.interfaces.ItemClickListner;
import com.demo.model.attendanceStatus.ApiAttendanceStatusParam;
import com.demo.model.attendanceStatus.AttendanceStatusMain;
import com.demo.model.attendence_history.ApiAttendenceHistoryParam;
import com.demo.model.attendence_history.AttendenceHistoryMain;
import com.demo.model.emplist.ApiEmpListParam;
import com.demo.model.emplist.EmpListMain;
import com.demo.model.start_attendence.ApiAttendenceStartParam;
import com.demo.model.start_attendence.AttendenceStartMain;
import com.demo.model.stop_attendence.ApiAttendenceStopParam;
import com.demo.model.stop_attendence.AttendenceStopMain;
import com.demo.network.KlHttpClient;
import com.demo.restservice.OnApiResponseListener;
import com.demo.services.LocationUpdateService;
import com.demo.utils.Constant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

public class AttendenceFragment extends BaseFragment implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private TextView tv_start_work;
    private TextView tv_end_work;
    private TextView tv_end_date_time,tv_start_date_time;
    private ListView gridAttendanceHis;
    private AttendanceGridAdapter adapter;
    private static final String TAG = "LocationService";
    private static final long INTERVAL = 1000 * 3;
    private static final long FASTEST_INTERVAL = 0;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private String lat,lng;
    private Long attendenceStartTimeStamp;
    private boolean isStartButtonClick = true;
    private LinearLayout linAttendenceEmployee;
    private LinearLayout linAttendenceAdmin;
    private RecyclerView recyclerEmpList;

    private String latEnd;
    private String lngEnd;
    private boolean isAlreadyStarted=false;
    private boolean isButtonClicked = false;
    private int val = 0;
    private String startWorkDay="";
    private String already_started="";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_attendence, null, false);
        ((MainActivity)getActivity()).setTitle(AppMenu.ATTENDENCE.name());
        linAttendenceEmployee = (LinearLayout)v.findViewById(R.id.linAttendenceEmployee);
        linAttendenceAdmin = (LinearLayout)v.findViewById(R.id.linAttendenceAdmin);
        recyclerEmpList = (RecyclerView)v.findViewById(R.id.recyclerEmpList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false);
        recyclerEmpList.setLayoutManager(layoutManager);
        recyclerEmpList.setItemAnimator(new DefaultItemAnimator());
        recyclerEmpList.hasFixedSize();
        tv_start_work = (TextView)v.findViewById(R.id.tv_start_work);
        tv_end_work = (TextView)v.findViewById(R.id.tv_end_work);
        tv_start_date_time = (TextView)v.findViewById(R.id.tv_start_date_time);
        tv_end_date_time = (TextView)v.findViewById(R.id.tv_end_date_time);
        gridAttendanceHis = (ListView)v.findViewById(R.id.gridAttendanceHis);
        tv_start_work.setOnClickListener(this);
        tv_end_work.setOnClickListener(this);
        if (baseActivity.preference.getIsAdmin()!=null&&baseActivity.preference.getIsAdmin().equalsIgnoreCase("1")){
            linAttendenceEmployee.setVisibility(View.GONE);
            linAttendenceAdmin.setVisibility(View.VISIBLE);
            getEmpList();
        }else {
            linAttendenceAdmin.setVisibility(View.GONE);
            linAttendenceEmployee.setVisibility(View.VISIBLE);
            getAttendenceStatus();
            getAttendenceHistory();

        }

        return v;

    }

    private void getEmpList() {
        if (baseActivity.isNetworkConnected()){
            baseActivity.showProgressDialog();
            new ApiEmpList(getParamEmp(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    baseActivity.dismissProgressDialog();
                    final EmpListMain main=(EmpListMain)t;
                    if (main.getResponseCode()==200) {
                        EmpRecyclerViewAdapter adapter = new EmpRecyclerViewAdapter(baseActivity, main.getResponseData(), new ItemClickListner() {
                            @Override
                            public void onItemClick(Object viewID, int position) {
                                Intent intent=new Intent(baseActivity,MapsActivity.class);
                                intent.putExtra("startlat",main.getResponseData().get(position).getStartLat());
                                intent.putExtra("startlong",main.getResponseData().get(position).getStartLong());
                                intent.putExtra("endlat",main.getResponseData().get(position).getEndLat());
                                intent.putExtra("endlong",main.getResponseData().get(position).getEndLong());

                                startActivity(intent);


                            }
                        });
                        recyclerEmpList.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public <E> void onError(E t) {
                    baseActivity.dismissProgressDialog();
                }

                @Override
                public void onError() {
                    baseActivity.dismissProgressDialog();
                }
            });
        }

    }

    private ApiEmpListParam getParamEmp() {
        ApiEmpListParam param=new ApiEmpListParam();
        param.setApiKey(Constant.API_KEY);
        return param;
    }

    private void getAttendenceHistory() {
        if(baseActivity.isNetworkConnected()){
            baseActivity.showProgressDialog();
            new ApiAttendenceHistory(getParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    baseActivity.dismissProgressDialog();
                    AttendenceHistoryMain main=(AttendenceHistoryMain)t;
                    if (main.getResponseCode()==200){
                        adapter=new AttendanceGridAdapter(baseActivity,main.getResponseData());
                        gridAttendanceHis.setAdapter(adapter);

                    }


                }

                @Override
                public <E> void onError(E t) {
                    baseActivity.dismissProgressDialog();
                }

                @Override
                public void onError() {
                    baseActivity.dismissProgressDialog();
                }
            });
        }
    }

    private ApiAttendenceHistoryParam getParam() {
        ApiAttendenceHistoryParam param=new ApiAttendenceHistoryParam();
        param.setApiKey(Constant.API_KEY);
        param.setUserid(baseActivity.preference.getUserId());
        return param;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_start_work:

                    if (!isAlreadyStarted){
                        val = 1;
                        isButtonClicked = true;

                        LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

                        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
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


                    }else
                        Toast.makeText(baseActivity,"Work already started",Toast.LENGTH_LONG).show();


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

    @Override
    public void onResume() {
        super.onResume();

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



    private void getAttendenceStart() {
        if(baseActivity.isNetworkConnected()){
            new AttendenceStartAsynctask().execute();

        }
    }
    private void getAttendenceStop() {
        if(baseActivity.isNetworkConnected()){
            new AttendenceStopAsynctask().execute();

        }
    }


    private ApiAttendenceStartParam getAttendenceStartParam() {
        ApiAttendenceStartParam map=new ApiAttendenceStartParam();
        map.setApiKey(Constant.API_KEY);
        map.setUserid(baseActivity.preference.getUserId());
        map.setStartLat(lat);
        map.setStartLong(lng);
        return map;
    }



    private ApiAttendenceStopParam getAttendenceStopParam() {
        if (null != mCurrentLocation) {
            latEnd = String.valueOf(mCurrentLocation.getLatitude());
            lngEnd = String.valueOf(mCurrentLocation.getLongitude());
        }
        ApiAttendenceStopParam map=new ApiAttendenceStopParam();
        map.setApiKey(Constant.API_KEY);
        map.setUserid(baseActivity.preference.getUserId());
        map.setEndLat(latEnd);
        map.setEndLong(lngEnd);
        return map;
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

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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
    void getAttendenceStatus(){
        if (baseActivity.isNetworkConnected()){
            baseActivity.showProgressDialog();
            new ApiAttendanceStatus(getAttParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    baseActivity.dismissProgressDialog();
                    AttendanceStatusMain main=(AttendanceStatusMain)t;
                    if (main.getResponseCode()==200){
                        if (main.getResponseData().getIsUserWorkStarted().equalsIgnoreCase("true"))
                        tv_start_date_time.setText(main.getResponseData().getStartTime());
                        if (main.getResponseData().getIsUserWorkStarted().equalsIgnoreCase("true"))
                        isAlreadyStarted=true;
                    }

                }

                @Override
                public <E> void onError(E t) {
                    baseActivity.dismissProgressDialog();
                }

                @Override
                public void onError() {
                    baseActivity.dismissProgressDialog();
                }
            });
        }
    }

    private ApiAttendanceStatusParam getAttParam() {
        ApiAttendanceStatusParam param=new ApiAttendanceStatusParam();
        param.setApiKey(Constant.API_KEY);
        param.setUserid(baseActivity.preference.getUserId());
        return param;
    }


    public class AttendenceStartAsynctask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                jsonObject.put("userid", baseActivity.preference.getUserId());
                jsonObject.put("startLat",lat);
                jsonObject.put("startLong", lng);
                Log.e("SendTrackNotification", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/attendanceStart.php", jsonObject);
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
                                already_started= json.getString("already_started");
                                if (already_started.equalsIgnoreCase("1")){
                                    Toast.makeText(baseActivity, "Work already started", Toast.LENGTH_LONG).show();
                                }else {
                                    tv_start_date_time.setText(json.getJSONObject("ResponseData").getString("startTime"));
                                    String str = tv_start_date_time.getText().toString();
                                    String[] splited = str.split("\\s+");
                                    startWorkDay=splited[0];
                                    LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                        buildAlertMessageNoGps();
                                    } else {
                                        getActivity().startService(new Intent(getActivity(), LocationUpdateService.class));
                                    }
                                }

                        } else if (json.getInt("ResponseCode") == 400) {
                            Toast.makeText(baseActivity, "Work already started", Toast.LENGTH_LONG).show();

                        }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
        }
    }

    public class AttendenceStopAsynctask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                jsonObject.put("userid", baseActivity.preference.getUserId());
                jsonObject.put("endLat",lat);
                jsonObject.put("endLong", lng);
                Log.e("AttendenceStop ", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/attendanceStop.php", jsonObject);
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
                            getAttendenceHistory();
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
}
