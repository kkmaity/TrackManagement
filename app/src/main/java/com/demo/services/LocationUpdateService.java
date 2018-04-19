package com.demo.services;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.demo.network.KlHttpClient;
import com.demo.preferences.Preference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

public class LocationUpdateService extends Service  implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "LocationService";
    private static final long INTERVAL = 1000 * 3;
    private static final long FASTEST_INTERVAL = 0;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private Preference preference;

    @Override
    public void onCreate() {
        preference = new Preference(getApplicationContext());
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       // Toast.makeText(this, "onStartCommand", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Connected .....................");
        }else{
            Log.d(TAG, "not connected .....................");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed .............");
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
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            String lat = String.valueOf(mCurrentLocation.getLatitude());
            String lng = String.valueOf(mCurrentLocation.getLongitude());


            if (preference.getJobID()!=null&&!preference.getJobID().isEmpty())
          //  new SendTrackNotification().execute(lat,lng);
            Log.d(TAG, "UI update LAt  ............."+lat);
        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Location onDestroy ..............: ");
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }



    public class SendTrackNotification extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                //jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                //jsonObject.put("userID", MyApplication.myApplication.preference.getUserId());

              /*  jsonObject.put("userLat",params[0]);
                jsonObject.put("userLong", params[1]);*/
                jsonObject.put("userLat",params[0]);
                jsonObject.put("userLong", params[1]);
                jsonObject.put("userid",preference.getUserId());
                jsonObject.put("jobid",preference.getJobID());
                jsonObject.put("ApiKey","0a2b8d7f9243305f2a4700e1870f673a");

                Log.e("SendTrackNotification", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/addlocation.php", jsonObject);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}