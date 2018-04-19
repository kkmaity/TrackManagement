package com.demo.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.MainActivity;
import com.demo.R;
import com.demo.adapter.CommonAdapter;
import com.demo.adapter.OutDoorHistoryGridAdapter;
import com.demo.dialog.CommonDialog;
import com.demo.interfaces.OnRowClickListener;
import com.demo.model.CommonDialogModel;
import com.demo.model.OutDoorHistory;
import com.demo.network.KlHttpClient;
import com.demo.restservice.RestService;
import com.demo.services.LocationUpdateService;
import com.demo.utils.Constant;
import com.demo.utils.FileUtils;
import com.demo.utils.Imageutils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by root on 20/8/15.
 */
public class OutDoorWorkEntryOtherDetailsFragment extends BaseFragment implements LocationListener,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Imageutils.ImageAttachmentListener {

    private static EditText et_challan_number;
    private static EditText et_challan_date;
    private EditText et_hospital_name;
    private EditText et_doctor_name;
    private EditText et_invoice_number;
    private static EditText et_invoice_date;
    private EditText et_mode_of_transport;
    private EditText et_bike_list;
    private EditText et_description;
    private EditText et_expence;
    private ImageView ivPicture1;
    private ImageView ivPicture2;
    private ImageView ivPicture3;
    private ImageView ivPicture4;
    private ImageView ivPicture5;
    private TextView tv_start_work;
    private TextView tv_end_work;
    private TextView tv_start_date_time;
    private TextView tv_end_date_time;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private String lat, lng;
    private static final long INTERVAL = 1000 * 3;
    private static final long FASTEST_INTERVAL = 0;
    private boolean isAlreadyStarted = false;
    private boolean isButtonClicked = false;
    private int val = 0;
    private ListView gridAttendanceHis;
    private OutDoorHistoryGridAdapter outDoorHistoryGridAdapter;
    private ArrayList<OutDoorHistory> outDoorHistories = new ArrayList<>();
    private ArrayList<CommonDialogModel> commonDialogModels = new ArrayList<>();
    private JSONArray hostpitalListArr, doctorListArr, modeOfTranportArr, bileArr;
    private CommonAdapter adapter;
    private Imageutils imageutils;
    private int imageViewID;
    private Bitmap mBitmap1;
    private Bitmap mBitmap2;
    private Bitmap mBitmap3;
    private Bitmap mBitmap4;
    private Bitmap mBitmap5;
    private LinearLayout linBikelist;
    private View view;
    private String hospitalid, doctorid, transportid,bikeid;
    private Uri uri5;
    private Uri uri1;
    private Uri uri2;
    private Uri uri3;
    private Uri uri4;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_outdoor_work_entry_other_details, null, false);
        ((MainActivity) getActivity()).setTitle("Outdoor Work Entry");
        // et_challan_number = (EditText)v.findViewById(R.id.et_challan_number);
        // et_box_number = (EditText)v.findViewById(R.id.et_box_number);
        // et_description = (EditText)v.findViewById(R.id.et_description);
        imageutils =new Imageutils(getActivity(),this,true);
        et_challan_number = (EditText) v.findViewById(R.id.et_challan_number);
        linBikelist = (LinearLayout) v.findViewById(R.id.linBikelist);
        et_challan_date = (EditText) v.findViewById(R.id.et_challan_date);
        et_hospital_name = (EditText) v.findViewById(R.id.et_hospital_name);
        et_doctor_name = (EditText) v.findViewById(R.id.et_doctor_name);
        et_invoice_number = (EditText) v.findViewById(R.id.et_invoice_number);
        et_invoice_date = (EditText) v.findViewById(R.id.et_invoice_date);
        et_mode_of_transport = (EditText) v.findViewById(R.id.et_mode_of_transport);
        et_bike_list = (EditText) v.findViewById(R.id.et_bike_list);
        et_description = (EditText) v.findViewById(R.id.et_challan_number);
        linBikelist.setVisibility(View.GONE);
        et_expence = (EditText) v.findViewById(R.id.et_expence);
        ivPicture1 = (ImageView) v.findViewById(R.id.ivPicture1);
        ivPicture2 = (ImageView) v.findViewById(R.id.ivPicture2);
        ivPicture3 = (ImageView) v.findViewById(R.id.ivPicture3);
        ivPicture4 = (ImageView) v.findViewById(R.id.ivPicture4);
        ivPicture5 = (ImageView) v.findViewById(R.id.ivPicture5);

        ivPicture1.setOnClickListener(this);
        ivPicture2.setOnClickListener(this);
        ivPicture3.setOnClickListener(this);
        ivPicture4.setOnClickListener(this);
        ivPicture5.setOnClickListener(this);
        et_challan_date.setOnClickListener(this);
        et_hospital_name.setOnClickListener(this);
        et_doctor_name.setOnClickListener(this);
        et_invoice_number.setOnClickListener(this);
        et_mode_of_transport.setOnClickListener(this);
        et_bike_list.setOnClickListener(this);

        tv_start_work = (TextView) v.findViewById(R.id.tv_start_work);
        tv_end_work = (TextView) v.findViewById(R.id.tv_end_work);
        tv_start_date_time = (TextView) v.findViewById(R.id.tv_start_date_time);
        tv_end_date_time = (TextView) v.findViewById(R.id.tv_end_date_time);
        gridAttendanceHis = (ListView) v.findViewById(R.id.gridAttendanceHis);
        outDoorHistoryGridAdapter = new OutDoorHistoryGridAdapter(baseActivity, outDoorHistories);
        gridAttendanceHis.setAdapter(outDoorHistoryGridAdapter);
        tv_start_work.setOnClickListener(this);
        tv_end_work.setOnClickListener(this);
        ((MainActivity) getActivity()).setTitle(getArguments().getString("category_title"));
        getHistory();
       /* new HospitalListAsynctask().execute();
        new DoctorListAsynctask().execute();
        new ModeOfTranportListAsynctask().execute();*/

        return v;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start_work:
                if (!baseActivity.isNetworkConnected())
                    return;
                if (isValid()) {
                    LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        buildAlertMessageNoGps();
                    } else {
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
                if (!baseActivity.isNetworkConnected())
                    return;
                val = 2;
                isButtonClicked = true;

                getActivity().stopService(new Intent(getActivity(), LocationUpdateService.class));

                LocationManager manager1 = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if (!manager1.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                } else {
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


            case R.id.et_challan_date:

                DialogFragment newFragment1 = new ChallanDatePickerFragment();
                newFragment1.show(baseActivity.getSupportFragmentManager(), "datePicker");
                break;
            case R.id.et_hospital_name:
                view = et_hospital_name;
             getHospitalListAsyntask();
                break;
            case R.id.et_doctor_name:
                view = et_doctor_name;
                getDoctorListAsyntask();

                break;
            case R.id.et_invoice_date:
                DialogFragment newFragment2 = new InvoiceDatePickerFragment();
                newFragment2.show(baseActivity.getSupportFragmentManager(), "datePicker");
                break;
            case R.id.et_mode_of_transport:
                view = et_mode_of_transport;
                getModeOfTransportListAsyntask();
                break;
            case R.id.et_bike_list:
                view = et_bike_list;
                getBikeListAsyntask();
                break;
            case R.id.ivPicture1:
                imageViewID=1;
                imageutils.imagepicker(1);
                break;
            case R.id.ivPicture2:
                imageViewID=2;
                imageutils.imagepicker(1);
                break;
            case R.id.ivPicture3:
                imageViewID=3;
                imageutils.imagepicker(1);
                break;
            case R.id.ivPicture4:
                imageViewID=4;
                imageutils.imagepicker(1);
                break;
            case R.id.ivPicture5:
                imageViewID=5;
                imageutils.imagepicker(1);
                break;
        }
    }

    private void getHospitalListAsyntask() {
        if (baseActivity.isNetworkConnected()){

            new HospitalListAsynctask().execute();
        }
    }
    private void getDoctorListAsyntask() {

        if (baseActivity.isNetworkConnected()){

            new DoctorListAsynctask().execute();
        }
    }
    private void getModeOfTransportListAsyntask() {

        if (baseActivity.isNetworkConnected()){

            new ModeOfTranportListAsynctask().execute();
        }
    }

    private void getBikeListAsyntask() {

        if (baseActivity.isNetworkConnected()){

            new BikeListAsynctask().execute();
        }
    }
    //Description, expence, picture


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
        //((MainActivity) getActivity()).setTitle(getArguments().getString("category_title"));
    }


    public boolean isValid() {
        boolean flag = true;

        /*if (et_challan_number.getText().toString().trim().length() == 0) {
            et_challan_number.setError("Please fill challan number");
            flag = false;
        }*//*else if(et_box_number.getText().toString().trim().length() == 0){
            et_box_number.setError("Please fill box number");
            flag = false;
        }*/ /*else if (et_description.getText().toString().trim().length() == 0) {
            et_description.setError("Please fill description");
            flag = false;
        }*/
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
            if (isButtonClicked) {
                lat = String.valueOf(mCurrentLocation.getLatitude());
                lng = String.valueOf(mCurrentLocation.getLongitude());
                mGoogleApiClient.disconnect();
                if (val == 1) {
                    getAttendenceStart();
                } else if (val == 2) {
                    getAttendenceStop();
                }

            }


            isButtonClicked = false;


        }
    }

    private void getAttendenceStart() {
        if (getView() != null) {
            if (baseActivity.isNetworkConnected()) {
               // new StartAsynctask().execute();

                submitOutdorJobData(getArguments().getString("category_id"),et_challan_number.getText().toString(),et_challan_date.getText().toString(),hospitalid,doctorid,et_invoice_number.getText().toString(),et_invoice_date.getText().toString(),transportid,bikeid,et_expence.getText().toString(),lat, lng,mBitmap1,mBitmap2,mBitmap3,mBitmap4,mBitmap5);

            }
        }

    }

    private void getAttendenceStop() {

        if (getView() != null) {
            if (baseActivity.isNetworkConnected()) {
                new StopAsynctask().execute();

            }
        }

    }

    private void getHistory() {

        // if(getView()!=null){
        if (baseActivity.isNetworkConnected()) {
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
                // jsonObject.put("box_no", et_box_number.getText().toString().trim());
                jsonObject.put("description", et_description.getText().toString().trim());
                jsonObject.put("startLat", lat);
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

            if (json != null) {

                try {
                    if (json.getInt("ResponseCode") == 200) {
                        String already_started = json.getString("already_started");
                        if (already_started.equalsIgnoreCase("1")) {
                            Toast.makeText(baseActivity, "Work already started", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(baseActivity, "Work  started", Toast.LENGTH_LONG).show();
                        }


                        tv_start_date_time.setText(json.getJSONObject("ResponseData").getString("startTime"));
                        baseActivity.preference.setJobID(json.getJSONObject("ResponseData").getString("jobid"));

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
                jsonObject.put("endLat", lat);
                jsonObject.put("endLong", lng);
                Log.e("AttendenceStop ", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/outdoorworkStop.php", jsonObject);
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
                        isAlreadyStarted = false;
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


    public static class ChallanDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

            // Create a new instance of DatePickerDialog and return it
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            et_challan_date.setText(year + "-" + String.format("%02d", (month + 1)) + "-" + day);
            // Do something with the date chosen by the user
        }
    }

    public static class InvoiceDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

            // Create a new instance of DatePickerDialog and return it
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            et_invoice_date.setText(year + "-" + String.format("%02d", (month + 1)) + "-" + day);
            // Do something with the date chosen by the user
        }
    }


    public class HospitalListAsynctask extends AsyncTask<String, Void, JSONObject> {
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
                Log.e("HospitalListAsynctask", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/hospital_list.php", jsonObject);
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
                        hostpitalListArr = json.getJSONArray("ResponseData");
                        commonDialogModels.clear();
                        CommonDialogModel model;
                        for(int i=0;i<hostpitalListArr.length();i++){
                            model=  new CommonDialogModel();
                            model.setId(hostpitalListArr.getJSONObject(i).getString("hospital_id"));
                            model.setName(hostpitalListArr.getJSONObject(i).getString("hospital_name"));
                            commonDialogModels.add(model);

                        }

                       setValueInCommonDialog(commonDialogModels,et_hospital_name);


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setValueInCommonDialog(final ArrayList<CommonDialogModel> hostpitalListArr,final EditText et_name) {
        adapter=new CommonAdapter(baseActivity,hostpitalListArr);
        new CommonDialog(adapter,baseActivity, hostpitalListArr,view, new OnRowClickListener() {
            @Override
            public void onItemClick(int viewId,int position,View v) {
                et_name.setText(hostpitalListArr.get(position).getName());
                if (et_name.getText().toString().equalsIgnoreCase("Office Bike")){
                    linBikelist.setVisibility(View.VISIBLE);
                }else{
                    if(v == et_bike_list)
                        linBikelist.setVisibility(View.VISIBLE);
                    else
                        linBikelist.setVisibility(View.GONE);
                }
                if(v == et_hospital_name){
                    hospitalid = hostpitalListArr.get(position).getId();
                }else if(v == et_doctor_name){
                    doctorid = hostpitalListArr.get(position).getId();
                }else if(v == et_mode_of_transport){
                    transportid = hostpitalListArr.get(position).getId();
                }else if(v == et_bike_list){
                    bikeid = hostpitalListArr.get(position).getId();
                }


               /* if (et_name.getText().toString().equalsIgnoreCase("Office Bike")){
                    linBikelist.setVisibility(View.VISIBLE);
                }else
                    linBikelist.setVisibility(View.GONE);*/


            }
        }).show();
    }

    public class DoctorListAsynctask extends AsyncTask<String, Void, JSONObject> {
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
                Log.e("DoctorListAsynctask", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/doctor_list.php", jsonObject);
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
                        doctorListArr = json.getJSONArray("ResponseData");
                        commonDialogModels.clear();
                        CommonDialogModel model;
                        for(int i=0;i<doctorListArr.length();i++){
                            model=  new CommonDialogModel();
                            model.setId(doctorListArr.getJSONObject(i).getString("doctor_id"));
                            model.setName(doctorListArr.getJSONObject(i).getString("doctor_name"));
                            commonDialogModels.add(model);

                        }

                        setValueInCommonDialog(commonDialogModels,et_doctor_name);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public class ModeOfTranportListAsynctask extends AsyncTask<String, Void, JSONObject> {
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
                Log.e("HospitalListAsynctask", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/mode_of_transport.php", jsonObject);
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
                        modeOfTranportArr = json.getJSONArray("ResponseData");
                        commonDialogModels.clear();
                        CommonDialogModel model;
                        for(int i=0;i<modeOfTranportArr.length();i++){
                            model=  new CommonDialogModel();
                            model.setId(modeOfTranportArr.getJSONObject(i).getString("transport_id"));
                            model.setName(modeOfTranportArr.getJSONObject(i).getString("title"));
                           commonDialogModels.add(model);

                        }

                        setValueInCommonDialog(commonDialogModels,et_mode_of_transport);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class BikeListAsynctask extends AsyncTask<String, Void, JSONObject> {
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
                Log.e("DoctorListAsynctask", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/office_bike_list.php", jsonObject);
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
                        bileArr = json.getJSONArray("ResponseData");
                        commonDialogModels.clear();
                        CommonDialogModel model;
                        for(int i=0;i<bileArr.length();i++){
                            model=  new CommonDialogModel();
                            model.setId(bileArr.getJSONObject(i).getString("bike_id"));
                            model.setName(bileArr.getJSONObject(i).getString("bike_no"));
                            commonDialogModels.add(model);

                        }

                        setValueInCommonDialog(commonDialogModels,et_bike_list);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Fragment", "onRequestPermissionsResult: "+requestCode);
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        Bitmap bitmap = file;
        String file_name = filename;
        switch (imageViewID){
            case 1:
                ivPicture1.setImageBitmap(file);
                mBitmap1=file;
                uri1=uri;
               String select=FileUtils.getPath(baseActivity,uri);
                break;
            case 2:
                uri2=uri;
                ivPicture2.setImageBitmap(file);
                mBitmap2=file;
                break;
            case 3:
                uri3=uri;
                ivPicture3.setImageBitmap(file);
                mBitmap3=file;
                break;
            case 4:
                uri4=uri;
                ivPicture4.setImageBitmap(file);
                mBitmap4=file;
                break;
            case 5:
                uri5=uri;
                ivPicture5.setImageBitmap(file);
                mBitmap5=file;
                break;

        }


        String path =  Environment.getExternalStorageDirectory() + File.separator + "ImageAttach" + File.separator;
        imageutils.createImage(file,filename,path,false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d("Fragment", "onActivityResult: ");
        imageutils.onActivityResult(requestCode, resultCode, data);

    }    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "SMS");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Call"){
            Toast.makeText(baseActivity,"calling code",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="SMS"){
            Toast.makeText(baseActivity,"sending sms code",Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }


    private  void submitOutdorJobData(String job_category,
                                      String challan_no,
                                      String challan_date,
                                      String hospital_id,
                                      String doctor_id,
                                      String invoice_number,
                                      String invoice_date,
                                      String mode_of_transport,
                                      String office_bike_id,
                                      String expense,
                                      String startLat,
                                      String startLong,
                                      Bitmap picture1,
                                      Bitmap picture2,
                                      Bitmap picture3,
                                      Bitmap picture4,
                                      Bitmap picture5){



         challan_no = null == challan_no? "":challan_no;
         challan_date = null == challan_date? "":challan_date;
         hospital_id = null == hospital_id? "":hospital_id;
         doctor_id= null == doctor_id? "":doctor_id;
         invoice_number= null == invoice_number? "":invoice_number;
         invoice_date= null == invoice_date? "":invoice_date;
         mode_of_transport= null == mode_of_transport? "":mode_of_transport;
         office_bike_id= null == office_bike_id? "":office_bike_id;
         expense= null == expense? "":expense;
         startLat= null == startLat? "":startLat;
         startLong= null == startLong? "":startLong;
       /*  picture1= null == picture1? "":picture1;
         picture2= null == challan_no? "":challan_no;
         picture3= null == challan_no? "":challan_no;
         picture4= null == challan_no? "":challan_no;
         picture5 = null == challan_no? "":challan_no;*/

        RequestBody bodyApiKey = RequestBody.create(MultipartBody.FORM, Constant.API_KEY);
        RequestBody userIdBody = RequestBody.create(MultipartBody.FORM, baseActivity.preference.getUserId());
        RequestBody jobCategoryBody = RequestBody.create(MultipartBody.FORM, job_category);
        RequestBody challan_noBody = RequestBody.create(MultipartBody.FORM, challan_no);
        RequestBody challan_dateBody = RequestBody.create(MultipartBody.FORM, challan_date);
        RequestBody hospital_idBody = RequestBody.create(MultipartBody.FORM, hospital_id);
        RequestBody doctor_idBody = RequestBody.create(MultipartBody.FORM, doctor_id);
        RequestBody invoice_numberBody = RequestBody.create(MultipartBody.FORM, invoice_number);
        RequestBody invoice_dateBody = RequestBody.create(MultipartBody.FORM, invoice_date);
        RequestBody mode_of_transportBody = RequestBody.create(MultipartBody.FORM, mode_of_transport);
        RequestBody office_bike_idBody = RequestBody.create(MultipartBody.FORM, office_bike_id);
        RequestBody expenseBody = RequestBody.create(MultipartBody.FORM, expense);
        RequestBody startLatBody = RequestBody.create(MultipartBody.FORM, startLat);
        RequestBody startLongBody = RequestBody.create(MultipartBody.FORM, startLong);
        MultipartBody.Part body1=null,body2=null,body3=null,body4=null,body5 = null;
        if(picture1!=null){

            body1 = prepareFilePart("picture1",uri1);

            // RequestBody requestFile1 = RequestBody.create(MultipartBody.FORM, baseActivity.getImageFile(picture1));
            // body1 = MultipartBody.Part.createFormData("picture1", baseActivity.getImageFile(picture1).getName(), requestFile1);
        }

        if(picture2!=null){
            // RequestBody requestFile2 = RequestBody.create(MultipartBody.FORM, baseActivity.getImageFile(picture2));
            // body2 = MultipartBody.Part.createFormData("picture2", baseActivity.getImageFile(picture1).getName(), requestFile2);
            body2 = prepareFilePart("picture2", uri2);

        }
        if(picture3!=null){
            // RequestBody requestFile3 = RequestBody.create(MultipartBody.FORM, baseActivity.getImageFile(picture3));
            ///  body3 = MultipartBody.Part.createFormData("picture3", baseActivity.getImageFile(picture1).getName(), requestFile3);
            body3 = prepareFilePart("picture3", uri3);

        }

        if(picture4!=null){
            RequestBody requestFile4 = RequestBody.create(MultipartBody.FORM, baseActivity.getImageFile(picture4));
            // body4 = MultipartBody.Part.createFormData("picture4", baseActivity.getImageFile(picture1).getName(), requestFile4);
            body4 = prepareFilePart("picture4", uri4);

        }

        if(picture5!=null){
            RequestBody requestFile5 = RequestBody.create(MultipartBody.FORM, baseActivity.getImageFile(picture5));
            //  body5 = MultipartBody.Part.createFormData("picture5", baseActivity.getImageFile(picture1).getName(), requestFile5);
            body5 = prepareFilePart("picture5",uri5);
        }


        Call<ResponseBody> getDepartment = RestService.getInstance().restInterface.outdoorworkStart(bodyApiKey,userIdBody,
                jobCategoryBody,challan_noBody,challan_dateBody,hospital_idBody,doctor_idBody,invoice_numberBody,
                invoice_dateBody,mode_of_transportBody,office_bike_idBody,expenseBody,startLatBody,
                startLongBody,body1,body2,body3,body4,body5);
        getDepartment.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                baseActivity.dismissProgressDialog();

                try {
                    String res=response.body().string();
                    try {
                        JSONObject json=new JSONObject(res);
                        /*if (object.getInt("ResponseCode")==200){
                            JSONObject objects=object.getJSONObject("ResponseData");
                            String startTime = objects.getString("startTime");
                        }else
                            Toast.makeText(baseActivity,object.getString("message"),Toast.LENGTH_LONG).show();*/

                        try {
                            if (json.getInt("ResponseCode") == 200) {
                                String already_started = json.getString("already_started");
                                if (already_started.equalsIgnoreCase("1")) {
                                    Toast.makeText(baseActivity, "Work already started", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(baseActivity, "Work  started", Toast.LENGTH_LONG).show();
                                }


                                tv_start_date_time.setText(json.getJSONObject("ResponseData").getString("startTime"));
                                baseActivity.preference.setJobID(json.getJSONObject("ResponseData").getString("jobid"));

                                getActivity().startService(new Intent(getActivity(), LocationUpdateService.class));


                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                baseActivity.dismissProgressDialog();

            }
        });
    }
    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = FileUtils.getFile(baseActivity, fileUri);
        MediaType type = MediaType.parse(getMimeType(fileUri));
        RequestBody requestFile = RequestBody.create(type, file);
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }




    public String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = baseActivity.getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }
}
