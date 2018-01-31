package com.demo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.demo.MainActivity;
import com.demo.R;


/**
 * Created by root on 20/8/15.
 */
public class OfficeWorkEntryDetailsFragment extends BaseFragment {


    private EditText et_challan_number;
    private EditText et_challan_date;
    private EditText et_hospital_name;
    private EditText et_doctor_name;
    private EditText et_invoice_number;
    private EditText et_invoice_date;
    private EditText et_mode_of_transport;
    private EditText et_bike_list;
    private EditText et_description;
    private EditText et_expence;
    private EditText et_picture;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_office_workentry_details, null, false);

        et_challan_number = (EditText)v.findViewById(R.id.et_challan_number);
        et_challan_date = (EditText)v.findViewById(R.id.et_challan_date);
        et_hospital_name = (EditText)v.findViewById(R.id.et_hospital_name);
        et_doctor_name = (EditText)v.findViewById(R.id.et_doctor_name);
        et_invoice_number = (EditText)v.findViewById(R.id.et_invoice_number);
        et_invoice_date = (EditText)v.findViewById(R.id.et_invoice_date);
        et_mode_of_transport = (EditText)v.findViewById(R.id.et_mode_of_transport);
        et_bike_list = (EditText)v.findViewById(R.id.et_bike_list);
        et_description = (EditText)v.findViewById(R.id.et_challan_number);
        et_expence = (EditText)v.findViewById(R.id.et_expence);
        et_picture = (EditText)v.findViewById(R.id.et_picture);

        et_challan_date.setOnClickListener(this);
        et_hospital_name.setOnClickListener(this);
        et_doctor_name.setOnClickListener(this);
        et_invoice_number.setOnClickListener(this);
        et_mode_of_transport.setOnClickListener(this);
        et_bike_list.setOnClickListener(this);


        return v;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        ((MainActivity)getActivity()).setTitle(getArguments().getString("category_title"));


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.et_challan_date:


                break;
            case R.id.et_hospital_name:
                break;
            case R.id.et_doctor_name:
                break;
            case R.id.et_invoice_number:
                break;
            case R.id.et_mode_of_transport:
                break;
            case R.id.et_bike_list:
                break;

        }
    }
}
