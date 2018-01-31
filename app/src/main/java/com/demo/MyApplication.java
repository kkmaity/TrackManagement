package com.demo;

import android.app.Application;

import com.demo.preferences.Preference;

/**
 * Created by root on 21/1/18.
 */

public class MyApplication extends Application {

    public  static MyApplication myApplication;

    public Preference preference=null;
    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        preference =new Preference(getApplicationContext());
    }
}
