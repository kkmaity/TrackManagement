package com.demo.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.demo.model.attendence_history.ApiAttendenceHistoryParam;
import com.demo.model.attendence_history.AttendenceHistoryMain;
import com.demo.restservice.OnApiResponseListener;
import com.demo.services.LocationUpdateService;
import com.demo.utils.Constant;

import java.util.Timer;


/**
 * Created by root on 20/8/15.
 */
public class AttendenceFragment extends BaseFragment {

    private TextView tv_start_work;
    private TextView tv_end_work;
    private TextView tv_end_date_time,tv_start_date_time;
    private Timer timer;
    long time;
    CountDownTimer countDownTimer=null;
    private GridView gridAttendanceHis;
    private AttendanceGridAdapter adapter;

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
            /* *//* String currentTime = baseActivity.getTimeStamp();
                tv_start_work.setText(currentTime);*//*

                //countDownView.start();
                baseActivity.preference.setStartTime(baseActivity.getTimeStamp());
                if(countDownTimer!=null){
                    countDownTimer.cancel();
                    countDownTimer=null;
                }
                TimerTask();









*/


                 LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    buildAlertMessageNoGps();
                }else{
                    getActivity().startService(new Intent(getActivity(),LocationUpdateService.class));
                }


                break;
            case R.id.tv_end_work:
                getActivity().stopService(new Intent(getActivity(),LocationUpdateService.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(baseActivity.preference.getStartTime().length()>0){
            String sTime = baseActivity.preference.getStartTime();
            String nowTime = baseActivity.getTimeStamp();
            time=Long.parseLong(nowTime)- Long.parseLong(sTime);
            TimerTask1(time);
        }*/
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
    public void TimerTask() {
         time = Long.parseLong(baseActivity.getTimeStamp());


        countDownTimer = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                try {
                    tv_start_date_time.setText(baseActivity.getTimer(time-millisUntilFinished));

                }catch (Exception e){

                }

            }

            public void onFinish() {
                tv_end_date_time.setText("done!");
            }
        }.start();
    }

    public void TimerTask1(final Long time1) {
        time = Long.parseLong(baseActivity.getTimeStamp());


         countDownTimer = new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {
                try {
                    tv_start_date_time.setText(baseActivity.getTimer((time+time1)-millisUntilFinished));

                }catch (Exception e){

                }

            }

            public void onFinish() {
                tv_end_date_time.setText("done!");
            }
        }.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(countDownTimer!=null){
            countDownTimer.cancel();
            countDownTimer=null;
        }
    }
}
