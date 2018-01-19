package com.demo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.R;
import com.demo.adapter.CancelLeaveGridAdapter;
import com.demo.api.ApiLeaveHistory;
import com.demo.interfaces.ItemClickListner;
import com.demo.model.leave.AppliedLeaveList;
import com.demo.model.leave.LeaveParam;
import com.demo.model.leave.ResponseDatum;
import com.demo.restservice.OnApiResponseListener;
import com.demo.utils.Constant;

import java.util.List;

public class CancelLeaveFragment extends BaseFragment {
    private TextView tv_body;
    private CancelLeaveGridAdapter adapter;
    private ListView listLeaveHis;
    private String cancelLeaveId="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cancel_leave, null, false);
     //   tv_body=(TextView)v.findViewById(R.id.tv_body);
        listLeaveHis = (ListView)v.findViewById(R.id.listLeaveHis);
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
                       setAdapterValue(listH);
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

    private void setAdapterValue(final List<ResponseDatum> listH) {
        adapter=new CancelLeaveGridAdapter(baseActivity, listH, new ItemClickListner() {
            @Override
            public void onItemClick(Object viewID, int position) {
                cancelLeaveId=listH.get(position).getLeaveID();


            }
        });
        listLeaveHis.setAdapter(adapter);
    }

    private LeaveParam getParam() {
        LeaveParam param=new LeaveParam();
        param.setApiKey(Constant.API_KEY);
        param.setUserid(baseActivity.preference.getUserId());
        return param;
    }


}
