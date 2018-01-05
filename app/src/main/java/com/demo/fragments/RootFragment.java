package com.demo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.demo.Enum.AppMenu;
import com.demo.MainActivity;
import com.demo.R;


/**
 * Created by root on 21/7/15.
 */
public class RootFragment extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_root, null, false);

        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_root);
        if (fragment == null) {
            shitchFragment(AppMenu.valueOf(getArguments().getString("appMenu")));
        }
        return v;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        baseActivity.getSupportActionBar().setTitle("Home");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    public void displayView(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fl_root, fragment).addToBackStack(null).commit();

        }
    }

    public void goBack() {

                ((MainActivity) getActivity()).onBackPressed();
    }

    public void shitchFragment(AppMenu appMenu){
        switch (appMenu){
            case HOME:
                getChildFragmentManager().beginTransaction().add(R.id.fl_root, new DashboardFragment()).commit();
                break;
            case ATTENDENCE:
                getChildFragmentManager().beginTransaction().add(R.id.fl_root, new AttendenceFragment()).commit();
                break;
            case WORk_ENTRY:
                getChildFragmentManager().beginTransaction().add(R.id.fl_root, new WorkEntryFragment()).commit();
                break;
            case LEAVES:
                getChildFragmentManager().beginTransaction().add(R.id.fl_root, new LeavesFragment()).commit();
                break;

        }
    }

    public void clearSatck(){
        FragmentManager fm = getChildFragmentManager();
        int count = fm.getBackStackEntryCount();
        for(int i = 0; i < count; ++i) {
            fm.popBackStackImmediate();
        }
            Fragment fragment = getChildFragmentManager().findFragmentById(R.id.fl_root);
            getChildFragmentManager().beginTransaction().add(R.id.fl_root, new DashboardFragment()).commit();

    }



}
