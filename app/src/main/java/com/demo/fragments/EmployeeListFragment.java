package com.demo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.demo.MainActivity;
import com.demo.R;
import com.demo.adapter.EmpRecyclerViewAdapter;
import com.demo.adapter.OutDoorJobGridAdapter;
import com.demo.api.ApiEmpList;
import com.demo.interfaces.ItemClickListner;
import com.demo.model.WorkEntry;
import com.demo.model.emplist.ApiEmpListParam;
import com.demo.model.emplist.EmpListMain;
import com.demo.restservice.OnApiResponseListener;
import com.demo.utils.Constant;

import java.util.ArrayList;

/**
 * Created by root on 25/1/18.
 */

public class EmployeeListFragment extends BaseFragment {

    private GridView list;
    private OutDoorJobGridAdapter officeGridAdapter;
    private ArrayList<WorkEntry> workEntries = new ArrayList<>();
    private RecyclerView recyclerEmpList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_emp_list, null, false);
        recyclerEmpList = (RecyclerView)v.findViewById(R.id.recyclerEmpList);
        ((MainActivity)getActivity()).setTitle("Employee List");
        LinearLayoutManager layoutManager = new LinearLayoutManager(baseActivity, LinearLayoutManager.VERTICAL, false);
        recyclerEmpList.setLayoutManager(layoutManager);
        recyclerEmpList.setItemAnimator(new DefaultItemAnimator());
        recyclerEmpList.hasFixedSize();
        getEmpList();
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

                                Fragment fragment = new OutDoorJobHistoryFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("emp_id",main.getResponseData().get(position).getUserid());
                                fragment.setArguments(bundle);
                                displayView(fragment);
                               /* Intent intent=new Intent(baseActivity,MapsActivity.class);
                                intent.putExtra("startlat",main.getResponseData().get(position).getStartLat());
                                intent.putExtra("startlong",main.getResponseData().get(position).getStartLong());
                                intent.putExtra("endlat",main.getResponseData().get(position).getEndLat());
                                intent.putExtra("endlong",main.getResponseData().get(position).getEndLong());

                                startActivity(intent);*/


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

}