package com.demo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.demo.MainActivity;
import com.demo.R;


/**
 * Created by root on 20/8/15.
 */
public class WorkEntryFragment extends BaseFragment {

    private TextView tv_office_job;
    private TextView tv_out_door_job;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_work_entry, null, false);
        tv_office_job = (TextView)v.findViewById(R.id.tv_office_job);
        tv_out_door_job = (TextView)v.findViewById(R.id.tv_out_door_job);

        ((MainActivity)getActivity()).setTitle("WORK ENTRY");

        tv_office_job.setOnClickListener(this);
        tv_out_door_job.setOnClickListener(this);
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
            case R.id.tv_office_job:
                Fragment officejobFragment = new OfficeJobFragment();
                displayView(officejobFragment);
                break;
            case R.id.tv_out_door_job:
                Fragment outdoorFragment = new OutdoorJboFragment();
                displayView(outdoorFragment);
                break;

        }
    }
}
