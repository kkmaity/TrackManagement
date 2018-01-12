package com.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 3/1/18.
 */

public class Constant {
    public static  String BASE_URL=getBaseURL();
    public static  String API_KEY="0a2b8d7f9243305f2a4700e1870f673a";
    public enum ENVIRONMENT{
        DEVELOPMENT, STAGING, PRODUCTION
    }

    public static  final ENVIRONMENT environment = ENVIRONMENT.DEVELOPMENT;

    private static String getBaseURL() {
        if (environment == ENVIRONMENT.PRODUCTION) {
            return "https://api.github.com/";
        } else if (environment == ENVIRONMENT.STAGING) {
            return "https://api.github.com/";
        } else if (environment == ENVIRONMENT.DEVELOPMENT) {
            return "http://173.214.180.212/";
        }
        return null;

    }

    public static Long getMillisecond(String datetime){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try
        {
            Date date = simpleDateFormat.parse(datetime);

            System.out.println("date : "+simpleDateFormat.format(date));
           return date.getTime();
        }
        catch (ParseException ex)
        {
            System.out.println("Exception "+ex);
        }
        return null;
    }

    public static String convertSecondsToHMmSs(long seconds) {
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        return String.format("%d:%02d:%02d", h,m,s);
    }


}
