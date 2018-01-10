package com.demo.utils;

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


}
