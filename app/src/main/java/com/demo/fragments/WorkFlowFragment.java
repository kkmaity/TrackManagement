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
public class WorkFlowFragment extends BaseFragment {

    private TextView tv_attendence;
    private TextView tv_work_entry;
    private TextView tv_leaves;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dashboard, null, false);
        tv_attendence = (TextView)v.findViewById(R.id.tv_attendence);
        tv_work_entry = (TextView)v.findViewById(R.id.tv_work_entry);
        tv_leaves = (TextView)v.findViewById(R.id.tv_leaves);

        tv_attendence.setOnClickListener(this);
        tv_work_entry.setOnClickListener(this);
        tv_leaves.setOnClickListener(this);
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
            case R.id.tv_attendence:
                ((MainActivity)getActivity()).onMenuItemSelect(AppMenu.ATTENDENCE);
                break;
            case R.id.tv_work_entry:
                ((MainActivity)getActivity()).onMenuItemSelect(AppMenu.WORk_ENTRY);
                break;
            case R.id.tv_leaves:
                ((MainActivity)getActivity()).onMenuItemSelect(AppMenu.LEAVES);
                break;
        }
    }
}
