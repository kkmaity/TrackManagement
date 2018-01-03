package com.demo.restservice;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

public class RestService {

    private static RestService restService;
    public RestInterface restInterface;

    public RestService() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
       // if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(httpLoggingInterceptor);
       // }

        restInterface = new Retrofit.Builder()
                .baseUrl(RestInterface.BASE_URL)
                .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(RestInterface.class);

        //currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
    }

    public static RestService getInstance() {
        if (restService == null) {
            restService = new RestService();
        }

        return restService;
    }

    OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
            .addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {

                    Request authorisedRequest = chain.request().newBuilder().build();
                           // .header("user", "admin").header("password", "123456").build();
                          //  .header("Authorization", "Basic "+ Constant.BASIC_AUTH_CREDIENTIAL).build();
                    return chain.proceed(authorisedRequest);
                }}).build();
}
