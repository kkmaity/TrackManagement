package com.demo.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.MainActivity;
import com.demo.R;
import com.demo.adapter.CancelLeaveGridAdapter;
import com.demo.api.ApiLeaveHistory;
import com.demo.interfaces.LeaveItemClickListner;
import com.demo.model.leave.AppliedLeaveList;
import com.demo.model.leave.CheckedResponseDatum;
import com.demo.model.leave.LeaveParam;
import com.demo.model.leave.ResponseDatum;
import com.demo.network.KlHttpClient;
import com.demo.restservice.OnApiResponseListener;
import com.demo.utils.Constant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CancelLeaveFragment extends BaseFragment implements LeaveItemClickListner{
    private TextView tv_body;
    private CancelLeaveGridAdapter adapter;
    private ListView listLeaveHis;
    private String cancelLeaveId="";
    private TextView tv_apply,tv_cancel;
    private List<CheckedResponseDatum> checkedResponseData = new ArrayList<>();
    private LinearLayout linCancal;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cancel_leave, null, false);
        linCancal=(LinearLayout)v.findViewById(R.id.linCancal);
        listLeaveHis = (ListView)v.findViewById(R.id.listLeaveHis);
        tv_apply = (TextView)v.findViewById(R.id.tv_apply);
        tv_cancel = (TextView)v.findViewById(R.id.tv_cancel);
        linCancal.setVisibility(View.GONE);
        tv_apply.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);
        adapter=new CancelLeaveGridAdapter(baseActivity, checkedResponseData, this);
        listLeaveHis.setAdapter(adapter);
        ((MainActivity)getActivity()).setTitle("Cancel Leave");
        //MAi.setTitle("Cancel Leave");
        //tv_body.setText("Cancel Leave");
      //  new PrivecyPolicyAsynctask().execute();
        getLeaveHistory();
        return v;

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
            case R.id.tv_apply:
                apply();
            break;
            case R.id.tv_cancel:
                cancel();
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
                        checkedResponseData.clear();
                        for(int i=0; i<listH.size(); i++){

                            checkedResponseData.add(new CheckedResponseDatum(listH.get(i),false));

                        }
                      adapter.notifyDataSetChanged();
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

    public void apply(){
        ArrayList<String> ids = new ArrayList<>();

        for(int i=0; i<checkedResponseData.size(); i++){
            if(checkedResponseData.get(i).isChecked()){
                ids.add(checkedResponseData.get(i).getResponseDatum().getLeaveID());
            }

        }
       String leaveids =  android.text.TextUtils.join(",", ids);

        if(leaveids.length()>0){
            if(baseActivity.isNetworkConnected()){
                new ApplyCalcelLeaveAsynctask().execute(leaveids);
            }
        }else{
            Toast.makeText(baseActivity, "No leave selected yet", Toast.LENGTH_LONG).show();
        }
    }

    public void cancel(){

        for(int i=0; i<checkedResponseData.size(); i++){
            checkedResponseData.get(i).setChecked(false);
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(int position) {
        boolean val = checkedResponseData.get(position).isChecked();
        if(val){
            checkedResponseData.get(position).setChecked(false);
        }else{
            checkedResponseData.get(position).setChecked(true);
        }
        adapter.notifyDataSetChanged();
    }



    public class ApplyCalcelLeaveAsynctask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("ApiKey", "0a2b8d7f9243305f2a4700e1870f673a");
                jsonObject.put("userid", baseActivity.preference.getUserId());
                jsonObject.put("leaveID",  params[0]);
                 Log.e("ApplyCalcelLeaveA", jsonObject.toString());
                JSONObject json = KlHttpClient.SendHttpPost("http://173.214.180.212/emp_track/api/attendanceCancel.php", jsonObject);
                return json;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
          //  baseActivity.dismissProgressDialog();

            if(json!=null) {

                try {
                    if (json.getInt("ResponseCode") == 200) {
                      //  if(json.has("ResponseData")){
                            Toast.makeText(baseActivity, json.getString("message"), Toast.LENGTH_LONG).show();

                            getLeaveHistory();
                       // }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




        }
    }
}
