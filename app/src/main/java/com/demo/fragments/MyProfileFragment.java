package com.demo.fragments;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.demo.R;

/**
 * Created by root on 19/1/18.
 */

public class MyProfileFragment extends BaseFragment {
    private EditText et_name;
    private EditText et_email;
    private EditText et_mobile;
    private EditText et_password;
    private TextView tv_register;
    ImageView profile_image;
    private CardView cardUpload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_myprofile, null, false);
        profile_image = (ImageView)v.findViewById(R.id.profile_image);
        cardUpload = (CardView)v.findViewById(R.id.cardUpload);
        et_name = (EditText)v.findViewById(R.id.et_name);
        et_email = (EditText)v.findViewById(R.id.et_email);
        et_mobile = (EditText)v.findViewById(R.id.et_mobile);
        et_password = (EditText)v.findViewById(R.id.et_password);
        if (baseActivity.preference.getProfImage()!=null&&baseActivity.preference.getProfImage().length()>0){
            Glide.with(this).load(baseActivity.preference.getProfImage()).into(profile_image);

        }
        et_name.setText(baseActivity.preference.getName());
        et_email.setText(baseActivity.preference.getEmail());
        et_mobile.setText(baseActivity.preference.getPhone());
        et_password.setText("1111");
        return v;
    }


}
