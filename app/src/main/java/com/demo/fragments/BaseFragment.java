package com.demo.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RadioGroup;


import com.demo.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;


/**
 * Created by root on 21/7/15.
 */
public class BaseFragment extends Fragment implements View.OnClickListener, TextWatcher,RadioGroup.OnCheckedChangeListener  {
    public SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    protected BaseActivity baseActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseActivity = (BaseActivity) context;

    }

    @Override
    public void onDetach() {
        baseActivity = null;
        super.onDetach();
    }

    @Override
    public void onClick(View view) {

    }

    public void displayView(Fragment fragment) {
        ((BaseFragment) getParentFragment()).displayView(fragment);
    }

    public void goBack() {
        ((BaseActivity) getActivity()).onBackPressed();
    }

    public void clearSatck() {
        ((BaseFragment) getParentFragment()).clearSatck();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }
}
