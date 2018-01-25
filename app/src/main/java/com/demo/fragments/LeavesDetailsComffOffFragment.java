package com.demo.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.MainActivity;
import com.demo.R;
import com.demo.adapter.LeaveGridAdapter;
import com.demo.api.ApiLeaveHistory;
import com.demo.model.leave.AppliedLeaveList;
import com.demo.model.leave.LeaveParam;
import com.demo.model.leave.ResponseDatum;
import com.demo.network.KlHttpClient;
import com.demo.restservice.OnApiResponseListener;
import com.demo.utils.Constant;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;


/**
 * Created by root on 20/8/15.
 */
public class LeavesDetailsComffOffFragment extends BaseFragment{


    /*private LinearLayout linLeaveEmployee;
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
    private String compoffLCount;*/



    private static TextView tv_start,tv_end;
    private LinearLayout linLeaveEmployee;
    private LinearLayout linLeaveAdmin;
    private ListView listLeaveHis;
    private LeaveGridAdapter adapter;
    private String leaveType="normal";
    private int leaveCount = 0;
    private EditText tv_description;
    private TextView tv_apply;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_leaves_details, null, false);

        if(getArguments().getString("type").equals("normal")){
            ((MainActivity)getActivity()).setTitle("Normal Leave");
            leaveType="normal";
        }else if(getArguments().getString("type").equals("comp")){
            ((MainActivity)getActivity()).setTitle("Comp Off Leave");
            leaveType = "comp_off";
        }

        leaveCount = getArguments().getInt("count");

        tv_start = (TextView)v.findViewById(R.id.tv_start);
        tv_end = (TextView)v.findViewById(R.id.tv_end);
        linLeaveEmployee = (LinearLayout)v.findViewById(R.id.linLeaveEmployee);
        linLeaveAdmin = (LinearLayout)v.findViewById(R.id.linLeaveAdmin);
        listLeaveHis = (ListView)v.findViewById(R.id.listLeaveHis);
        tv_description = (EditText)v.findViewById(R.id.tv_description);
        tv_apply = (TextView)v.findViewById(R.id.tv_apply);

      /*  linLeaveEmployee = (LinearLayout)v.findViewById(R.id.linLeaveEmployee);
        linLeaveAdmin = (LinearLayout)v.findViewById(R.id.linLeaveAdmin);
        tv_normalLeave = (TextView)v.findViewById(R.id.tv_normalLeave);
        tv_comp_leave = (TextView)v.findViewById(R.id.tv_comp_leave);
        tv_normal_leave_count = (TextView)v.findViewById(R.id.tv_normal_leave_count);
        tv_comp_leave_count = (TextView)v.findViewById(R.id.tv_comp_leave_count);
        listLeaveHis = (ListView)v.findViewById(R.id.listLeaveHis);
        tv_normalLeave.setOnClickListener(this);
        tv_comp_leave.setOnClickListener(this);

            linLeaveAdmin.setVisibility(View.GONE);
            linLeaveEmployee.setVisibility(View.VISIBLE);

            getLeaveHistory();

*/
        tv_start.setOnClickListener(this);
        tv_end.setOnClickListener(this);
        tv_apply.setOnClickListener(this);
        getLeaveHistory();
        return v;

    }

   /* private void getLeaveCount() {
        if(baseActivity.isNetworkConnected()){
            new LeaveCountAsynctask().execute();

        }

    }*/

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.tv_start:
               // if (leaveCount>0){
                    DialogFragment newFragment = new StartDatePickerFragment();
                    newFragment.show(baseActivity.getSupportFragmentManager(), "datePicker");
                    //openDateDialog();
               /* }else
                    Toast.makeText(baseActivity,"No leave available",Toast.LENGTH_LONG).show();*/


                break;
            case R.id.tv_end:
               // if (leaveCount>0) {

                    String startDate = tv_start.getText().toString();

                    if(startDate.equalsIgnoreCase("select")){
                        Toast.makeText(baseActivity, "Select start date", Toast.LENGTH_SHORT).show();
                    }else{
                        DialogFragment newFragment1 = new EndDatePickerFragment();
                        newFragment1.show(baseActivity.getSupportFragmentManager(), "datePicker");
                    }

                   /* if (isCompOffApplicable.equalsIgnoreCase("yes")) {
                        leaveType = "compoff";
                        openDateDialog();
                    } else
                        Toast.makeText(baseActivity, "Compoff leave not available", Toast.LENGTH_LONG).show();*/
               // }else
                   // Toast.makeText(baseActivity, "Compoff leave not available", Toast.LENGTH_LONG).show();


                break;

            case R.id.tv_apply:

                if(isvalid()){
                    if(baseActivity.isNetworkConnected()){

                        String startDate1 = tv_start.getText().toString();
                        String enddate1 = tv_end.getText().toString();

                        if(leaveType.equalsIgnoreCase("normal")){

                            int diff = (int) Constant.getDayDiff(startDate1 , enddate1);
                            if(diff>leaveCount){
                                new AlertDialog.Builder(baseActivity)
                                        .setTitle("Alert")
                                        .setMessage("Paid Leave "+leaveCount +"\n"+"Un-Paid Leave "+(diff-leaveCount))
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                new ApplyLeaveAsynctask().execute();

                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();

                                            }
                                        })
                                        .show();
                            }else{
                                new ApplyLeaveAsynctask().execute();
                            }

                        }



                        if(leaveType.equalsIgnoreCase("comp_off")) {
                            int diff = (int) Constant.getDayDiff(enddate1, startDate1);
                            if (diff > leaveCount) {
                                new AlertDialog.Builder(baseActivity)
                                        .setTitle("Alert")
                                        .setMessage("Your leave balance only " + leaveCount + " Please reduce end date")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();


                                            }
                                        })

                                        .show();
                            }else{
                                new ApplyLeaveAsynctask().execute();
                            }
                        }






                    }
                }
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
                        List<ResponseDatum> listHComp = new ArrayList<>();

                        for(int i=0; i<listH.size(); i++){
                            if(!listH.get(i).getLeaveType().equalsIgnoreCase("normal")){
                                listHComp.add(listH.get(i));
                            }
                        }
                        adapter=new LeaveGridAdapter(baseActivity,listHComp);
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
                jsonObject.put("leavestart_date",  tv_start.getText().toString());
                jsonObject.put("leave_end_date",tv_end.getText().toString());
                jsonObject.put("description", tv_description.getText().toString().trim());
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
                            Toast.makeText(baseActivity, "Leave applied successfully", Toast.LENGTH_LONG).show();
                            tv_end.setText("Select");
                            tv_start.setText("Select");
                            tv_description.setText("");
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
    /*public class LeaveCountAsynctask extends AsyncTask<String, Void, JSONObject> {
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
    }*/
 /*   void openDateDialog(){
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
    }*/

    private void applyLeaveTask() {
        if(baseActivity.isNetworkConnected()){
            new ApplyLeaveAsynctask().execute();

        }
    }




    public static class StartDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

            // Create a new instance of DatePickerDialog and return it
            return datePickerDialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            tv_start.setText(year+"-"+String.format("%02d", (month+1))+"-"+day);
            // Do something with the date chosen by the user
        }
    }



    public static class EndDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {




        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker


            try {
                String st[] = tv_start.getText().toString().trim().split(Pattern.quote("-"));


                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                c.setTime(sdf.parse(tv_start.getText().toString()));
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

                // Create a new instance of DatePickerDialog and return it
                return datePickerDialog;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;

        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            tv_end.setText(year+"-"+String.format("%02d", (month+1))+"-"+day);
            // Do something with the date chosen by the user
        }
    }

    private boolean isvalid(){

        boolean flag = true;
        String startDate = tv_start.getText().toString();
        String enddate = tv_end.getText().toString();

        if(startDate.equalsIgnoreCase("select")){
            flag = false;
            Toast.makeText(baseActivity, "Select start date", Toast.LENGTH_SHORT).show();
        }else if(enddate.equalsIgnoreCase("select")){
            flag = false;
            Toast.makeText(baseActivity, "Select end date", Toast.LENGTH_SHORT).show();
        }else if(tv_description.getText().toString().trim().length() == 0){
            flag = false;
            Toast.makeText(baseActivity, "please enter Description", Toast.LENGTH_SHORT).show();
        }


        return flag;
    }

}
