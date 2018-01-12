package com.demo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.TextView;

import com.demo.Enum.AppMenu;
import com.demo.MainActivity;
import com.demo.R;
import com.demo.adapter.AttendanceGridAdapter;
import com.demo.api.ApiAttendenceHistory;
import com.demo.api.ApiAttendenceStart;
import com.demo.api.ApiAttendenceStop;
import com.demo.api.ApiLogin;
import com.demo.model.attendence_history.ApiAttendenceHistoryParam;
import com.demo.model.attendence_history.AttendenceHistoryMain;
import com.demo.model.login.ApiLoginParam;
import com.demo.model.login.LoginMain;
import com.demo.model.start_attendence.ApiAttendenceStartParam;
import com.demo.model.start_attendence.AttendenceStartMain;
import com.demo.model.stop_attendence.ApiAttendenceStopParam;
import com.demo.model.stop_attendence.AttendenceStopMain;
import com.demo.restservice.OnApiResponseListener;
import com.demo.services.LocationUpdateService;
import com.demo.utils.Constant;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.common.api.Status;

import java.util.Timer;


/**
 * Created by root on 20/8/15.
 */
public class AttendenceFragment extends BaseFragment implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private TextView tv_start_work;
    private TextView tv_end_work;
    private TextView tv_end_date_time,tv_start_date_time;


    private GridView gridAttendanceHis;
    private AttendanceGridAdapter adapter;


    private static final String TAG = "LocationService";
    private static final long INTERVAL = 1000 * 3;
    private static final long FASTEST_INTERVAL = 0;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private String lat,lng;
    private boolean isStartButtonClick = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_attendence, null, false);
        ((MainActivity)getActivity()).setTitle(AppMenu.ATTENDENCE.name());
        tv_start_work = (TextView)v.findViewById(R.id.tv_start_work);
        tv_end_work = (TextView)v.findViewById(R.id.tv_end_work);
        tv_start_date_time = (TextView)v.findViewById(R.id.tv_start_date_time);
        tv_end_date_time = (TextView)v.findViewById(R.id.tv_end_date_time);
        gridAttendanceHis = (GridView)v.findViewById(R.id.gridAttendanceHis);
        tv_start_work.setOnClickListener(this);
        tv_end_work.setOnClickListener(this);
        getAttendenceHistory();




        return v;

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
        param.setUserid("1");
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
                isStartButtonClick = true;
                LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }else{
                    createLocationRequest();
                    mGoogleApiClient = new GoogleApiClient.Builder(baseActivity)
                            .addApi(LocationServices.API)
                            .addConnectionCallbacks(this)
                            .addOnConnectionFailedListener(this)
                            .build();
                    mGoogleApiClient.connect();
                }




                break;
            case R.id.tv_end_work:
                isStartButtonClick = false;
                LocationManager manager1 = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

                if ( !manager1.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }else{
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
            baseActivity.showProgressDialog();
            new ApiAttendenceStart(getAttendenceStartParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    baseActivity.dismissProgressDialog();
                    AttendenceStartMain main=(AttendenceStartMain)t;
                    if(main.getResponseCode()==200){
                        mGoogleApiClient.disconnect();

                        LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

                        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                            buildAlertMessageNoGps();
                        }else{
                            getActivity().startService(new Intent(getActivity(),LocationUpdateService.class));
                        }
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

    private ApiAttendenceStartParam getAttendenceStartParam() {
        ApiAttendenceStartParam map=new ApiAttendenceStartParam();
        map.setApiKey(Constant.API_KEY);
        map.setUserid(baseActivity.preference.getUserId());
        map.setStartLat(lat);
        map.setStartLong(lng);
        return map;
    }



    private void getAttendenceStop() {
        if(baseActivity.isNetworkConnected()){
            baseActivity.showProgressDialog();
            new ApiAttendenceStop(getAttendenceStopParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    baseActivity.dismissProgressDialog();
                    AttendenceStopMain main=(AttendenceStopMain)t;
                    if(main.getResponseCode()==200){
                        mGoogleApiClient.disconnect();
                        getActivity().stopService(new Intent(getActivity(),LocationUpdateService.class));
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





    private ApiAttendenceStopParam getAttendenceStopParam() {
        ApiAttendenceStopParam map=new ApiAttendenceStopParam();
        map.setApiKey(Constant.API_KEY);
        map.setUserid(baseActivity.preference.getUserId());
        map.setStartLat("");
        map.setStartLong("");
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
        updateUI();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    private void updateUI() {

        if (null != mCurrentLocation) {
             lat = String.valueOf(mCurrentLocation.getLatitude());
             lng = String.valueOf(mCurrentLocation.getLongitude());

             if(isStartButtonClick){
                 getAttendenceStart();
             }else{
                 getAttendenceStop();
             }



        }
    }
}
