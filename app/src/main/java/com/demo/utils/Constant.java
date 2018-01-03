package com.demo.utils;

/**
 * Created by root on 3/1/18.
 */

public class Constant {
    public static  String BASE_URL=getBaseURL();
    public enum ENVIRONMENT{
        DEVELOPMENT, STAGING, PRODUCTION
    }

    public static  final ENVIRONMENT environment = ENVIRONMENT.STAGING;

    private static String getBaseURL() {
        if (environment == ENVIRONMENT.PRODUCTION) {
            return "https://api.github.com/";
        } else if (environment == ENVIRONMENT.STAGING) {
            return "https://api.github.com/";
        } else if (environment == ENVIRONMENT.DEVELOPMENT) {
            return "https://google.com/";
        }
        return null;

    }


}
