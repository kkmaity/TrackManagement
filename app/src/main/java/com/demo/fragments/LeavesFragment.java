package com.demo.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.Enum.AppMenu;
import com.demo.MainActivity;
import com.demo.R;
import com.demo.adapter.AttendanceGridAdapter;
import com.demo.adapter.LeaveGridAdapter;
import com.demo.api.ApiLeaveHistory;
import com.demo.model.leave.AppliedLeaveList;
import com.demo.model.leave.LeaveParam;
import com.demo.model.leave.ResponseDatum;
import com.demo.network.KlHttpClient;
import com.demo.restservice.OnApiResponseListener;
import com.demo.utils.Constant;
import com.savvi.rangedatepicker.CalendarPickerView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by root on 20/8/15.
 */
public class LeavesFragment extends BaseFragment {


    private LinearLayout linLeaveEmployee;
    private LinearLayout linLeaveAdmin;
    private TextView tv_normalLeave;
    private TextView tv_comp_leave;
    private TextView tv_normal_leave_count;
    private TextView tv_comp_leave_count;
    private ListView listLeaveHis;
    private LeaveGridAdapter adapter;
    private CalendarPickerView calendar;
    private List<Date> dates;
    private Date date;
    private String leaveType="normal";
    private String isCompOffApplicable="";
    private String normalLCount;
    private String compoffLCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_leaves, null, false);
        ((MainActivity)getActivity()).setTitle(AppMenu.LEAVES.name());
        linLeaveEmployee = (LinearLayout)v.findViewById(R.id.linLeaveEmployee);
        linLeaveAdmin = (LinearLayout)v.findViewById(R.id.linLeaveAdmin);
        tv_normalLeave = (TextView)v.findViewById(R.id.tv_normalLeave);
        tv_comp_leave = (TextView)v.findViewById(R.id.tv_comp_leave);
        tv_normal_leave_count = (TextView)v.findViewById(R.id.tv_normal_leave_count);
        tv_comp_leave_count = (TextView)v.findViewById(R.id.tv_comp_leave_count);
        listLeaveHis = (ListView)v.findViewById(R.id.listLeaveHis);
        tv_normalLeave.setOnClickListener(this);
        tv_comp_leave.setOnClickListener(this);
       /* if (baseActivity.preference.getIsAdmin()!=null&&baseActivity.preference.getIsAdmin().equalsIgnoreCase("1")){
            linLeaveEmployee.setVisibility(View.GONE);
            linLeaveAdmin.setVisibility(View.VISIBLE);
            // getEmpList();
        }else {*/
            linLeaveAdmin.setVisibility(View.GONE);
            linLeaveEmployee.setVisibility(View.VISIBLE);
            getLeaveCount();
            getLeaveHistory();


       // }
        return v;

    }

    private void getLeaveCount() {
        if(baseActivity.isNetworkConnected()){
            new LeaveCountAsynctask().execute();

        }

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.tv_normalLeave:
                if (normalLCount!=null&&!normalLCount.equalsIgnoreCase("0")){
                    leaveType="normal";
                    openDateDialog();
                }else
                    Toast.makeText(baseActivity,"No leave available",Toast.LENGTH_LONG).show();


                break;
            case R.id.tv_comp_leave:
                if (compoffLCount!=null&&!compoffLCount.equalsIgnoreCase("0")) {
                    if (isCompOffApplicable.equalsIgnoreCase("yes")) {
                        leaveType = "compoff";
                        openDateDialog();
                    } else
                        Toast.makeText(baseActivity, "Compoff leave not available", Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(baseActivity, "Compoff leave not available", Toast.LENGTH_LONG).show();


                break;
        }
    }

    private void getLeaveHistory() {
        if (baseActivity.isNetworkConnected()){
            baseActivity.showProgressDialog();
            new ApiLeaveHistory(getParam(), new OnApiResponseListener() {
                @Override
                public <E> void onSuccess(E t) {
                    baseActivity.dismissProgressDialog();
                    AppliedLeaveList main=(AppliedLeaveList)t;
                    if (main.getResponseCode()==200){
                        List<ResponseDatum> listH = main.getResponseData();
                        adapter=new LeaveGridAdapter(baseActivity,listH);
                        listLeaveHis.setAdapter(adapter);
                    }else
                        Toast.makeText(baseActivity,""+main.getMessage(),Toast.LENGTH_LONG).show();


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

    private LeaveParam getParam() {
        LeaveParam param=new LeaveParam();
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
    public class ApplyLeaveAsynctask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                jsonObject.put("userid", baseActivity.preference.getUserId());
                jsonObject.put("leavestart_date",  baseActivity.milisecondToDate(dates.get(0).getTime()));
                jsonObject.put("leave_end_date", baseActivity.milisecondToDate(dates.get(dates.size()-1).getTime()));
                jsonObject.put("description", "description");
                jsonObject.put("leave_type", leaveType);
                Log.e("LeaveApplied ", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/apply_leave.php", jsonObject);
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
                        if(json.has("ResponseData")){
                            //  tv_end_date_time.setText(json.getJSONObject("ResponseData").getString("stopTime"));
                            getLeaveHistory();
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
    public class LeaveCountAsynctask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                jsonObject.put("userid", baseActivity.preference.getUserId());

                Log.e("LeaveCount ", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/leavecounter.php", jsonObject);
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
                        if(json.has("ResponseData")){
                            normalLCount=json.getJSONObject("ResponseData").getString("normalLeave");
                            compoffLCount=json.getJSONObject("ResponseData").getString("compoffleave");
                            tv_normal_leave_count.setText("Normal Leave Count  "+normalLCount);
                            tv_comp_leave_count.setText("Compoff Leave Count  "+compoffLCount);
                            isCompOffApplicable=json.getJSONObject("ResponseData").getString("compoffleave_applicable");
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
    void openDateDialog(){
        final Dialog dialog=new Dialog(baseActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_date_range_dialog);
        dialog.setCancelable(false);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        TextView OK = (TextView) dialog.findViewById(R.id.OK);
        final Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 10);
        final Calendar lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -10);
        calendar = (CalendarPickerView) dialog.findViewById(R.id.calendar_view);
        calendar.init(lastYear.getTime(), nextYear.getTime()) //
                .inMode(CalendarPickerView.SelectionMode.MULTIPLE) //
                .withSelectedDate(new Date());

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String strdate = "2018-01-22";
        String strdate2 = "2018-01-26";

        try {
            Date newdate = dateformat.parse(strdate);
            Date newdate2 = dateformat.parse(strdate2);
            ArrayList<Date> arrayList = new ArrayList<>();
            arrayList.add(newdate);
            arrayList.add(newdate2);
            calendar.highlightDates(arrayList);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        //list.add(7);

        calendar.deactivateDates(list);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dates= calendar.getSelectedDates();
                dialog.dismiss();
                applyLeaveTask();
            }
        });

        dialog.show();
    }

    private void applyLeaveTask() {
        if(baseActivity.isNetworkConnected()){
            new ApplyLeaveAsynctask().execute();

        }
    }

}
