package com.demo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.demo.Enum.AppMenu;
import com.demo.MainActivity;
import com.demo.R;


/**
 * Created by root on 20/8/15.
 */
public class OutdoorJboFragment extends BaseFragment {

    private TextView tv_delevery;
    private TextView tv_pickup;
    private TextView tv_ot_assit;
    private TextView tv_bill_submission;
    private TextView tv_payment_collection;
    private TextView tv_doctor_meet;
    private TextView tv_others;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_out_door_job, null, false);
        tv_delevery  = (TextView)v.findViewById(R.id.tv_delevery);
        tv_pickup  = (TextView)v.findViewById(R.id.tv_pickup);
        tv_ot_assit  = (TextView)v.findViewById(R.id.tv_ot_assit);
        tv_bill_submission  = (TextView)v.findViewById(R.id.tv_bill_submission);
        tv_payment_collection  = (TextView)v.findViewById(R.id.tv_payment_collection);
        tv_doctor_meet  = (TextView)v.findViewById(R.id.tv_doctor_meet);
        tv_others  = (TextView)v.findViewById(R.id.tv_others);

        tv_delevery.setOnClickListener(this);
        tv_pickup.setOnClickListener(this);
        tv_ot_assit.setOnClickListener(this);
        tv_bill_submission.setOnClickListener(this);
        tv_payment_collection.setOnClickListener(this);
        tv_doctor_meet.setOnClickListener(this);
        tv_others.setOnClickListener(this);

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
            case R.id.tv_delevery:
                break;
            case R.id.tv_pickup:
                break;
            case R.id.tv_ot_assit:
                break;
            case R.id.tv_bill_submission:
                break;
            case R.id.tv_payment_collection:
                break;
            case R.id.tv_doctor_meet:
                break;
            case R.id.tv_others:
                break;
        }
    }
}
